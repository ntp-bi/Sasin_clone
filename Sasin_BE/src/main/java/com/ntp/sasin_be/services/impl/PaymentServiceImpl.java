package com.ntp.sasin_be.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntp.sasin_be.config.MomoConfig;
import com.ntp.sasin_be.dto.MomoPaymentInitResponse;
import com.ntp.sasin_be.entities.Order;
import com.ntp.sasin_be.entities.Payment;
import com.ntp.sasin_be.enums.PaymentMethod;
import com.ntp.sasin_be.enums.PaymentStatus;
import com.ntp.sasin_be.exceptions.ResourceNotFoundException;
import com.ntp.sasin_be.repositories.OrderRepository;
import com.ntp.sasin_be.repositories.PaymentRepository;
import com.ntp.sasin_be.services.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements IPaymentService {

    private final MomoConfig momoConfig;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Transactional
    public MomoPaymentInitResponse initiateMomoPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        String requestId = UUID.randomUUID().toString();
        // orderInfo: hiển thị cho khách hàng
        String orderInfo = "Thanh toán đơn hàng " + order.getCode();

        // amount: MoMo yêu cầu String, không decimal
        String amount = order.getTotalAmount().setScale(0, RoundingMode.HALF_UP).toPlainString();

        // tạo orderId hợp lệ gửi sang MoMo
        String safeOrderId = buildSafeOrderId(order);

        String extraData = "";
        String redirectUrl = momoConfig.getReturnUrl();
        String ipnUrl = momoConfig.getNotifyUrl();
        String requestType = "captureWallet";

        // build rawSignature theo đúng thứ tự MoMo yêu cầu
        String rawSignature = "accessKey=" + momoConfig.getAccessKey()
                + "&amount=" + amount
                + "&extraData=" + extraData
                + "&ipnUrl=" + ipnUrl
                + "&orderId=" + safeOrderId
                + "&orderInfo=" + orderInfo
                + "&partnerCode=" + momoConfig.getPartnerCode()
                + "&redirectUrl=" + redirectUrl
                + "&requestId=" + requestId
                + "&requestType=" + requestType;

        String signature = hmacSHA256(rawSignature, momoConfig.getSecretKey());

        // body gửi MoMo
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("partnerCode", momoConfig.getPartnerCode());
        body.put("accessKey", momoConfig.getAccessKey());
        body.put("partnerName", momoConfig.getPartnerName() == null ? "Sasin" : momoConfig.getPartnerName());
        body.put("storeId", momoConfig.getStoreId() == null ? "SasinStore" : momoConfig.getStoreId());
        body.put("requestId", requestId);
        body.put("amount", amount);
        body.put("orderId", safeOrderId);
        body.put("orderInfo", orderInfo);
        body.put("redirectUrl", redirectUrl);
        body.put("ipnUrl", ipnUrl);
        body.put("lang", "vi");
        body.put("extraData", extraData);
        body.put("requestType", requestType);
        body.put("signature", signature);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> http = new HttpEntity<>(body, headers);

        String endpoint = momoConfig.getRequestUrl();
        ResponseEntity<String> resp = restTemplate.postForEntity(endpoint, http, String.class);

        try {
            JsonNode root = objectMapper.readTree(resp.getBody());
            int resultCode = root.path("resultCode").asInt();
            String payUrl = root.path("payUrl").asText(null);
            String message = root.path("message").asText(null);

            // lưu Payment
            Payment p = new Payment();
            p.setOrder(order);
            p.setMethod(PaymentMethod.MOMO);
            p.setStatus(PaymentStatus.PENDING);
            p.setTransactionCode(root.path("transId").asText(null));
            p.setProviderResponse(resp.getBody());
            p.setAmount(order.getTotalAmount());
            p.setCreatedAt(LocalDateTime.now());
            paymentRepository.save(p);

            return new MomoPaymentInitResponse(payUrl, message, resultCode);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse MoMo response: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public void handleMomoIpn(String rawBody) {
        try {
            JsonNode root = objectMapper.readTree(rawBody);
            String momoOrderId = root.path("orderId").asText(null);
            int resultCode = root.path("resultCode").asInt();
            String transId = root.path("transId").asText(null);

            if (momoOrderId == null) {
                throw new RuntimeException("MoMo IPN missing orderId");
            }

            // Extract DB orderId from momoOrderId (assume format: ORD-{id}-xxx)
            Long dbOrderId = null;
            Pattern p = Pattern.compile("^ORD-(\\d+).*");
            Matcher m = p.matcher(momoOrderId);
            if (m.find()) {
                try {
                    dbOrderId = Long.parseLong(m.group(1));
                } catch (NumberFormatException ignored) {
                }
            }

            // Find order by ID or fallback to code
            Order order;
            if (dbOrderId != null) {
                order = orderRepository.findById(dbOrderId)
                        .orElseThrow(() -> new ResourceNotFoundException("Order not found: "));
            } else {
                order = orderRepository.findByCode(momoOrderId)
                        .orElseThrow(() -> new ResourceNotFoundException("Order not found for momo orderId: " + momoOrderId));
            }

            // Handle Payment
            Payment payment = paymentRepository.findByTransactionCode(transId);
            if (payment == null) {
                payment = new Payment();
                payment.setOrder(order);
                payment.setMethod(PaymentMethod.MOMO);
                payment.setTransactionCode(transId);
                payment.setAmount(order.getTotalAmount());
            }

            payment.setProviderResponse(rawBody);
            payment.setPaidAt(LocalDateTime.now());

            if (resultCode == 0) {
                payment.setStatus(PaymentStatus.SUCCESS);
                order.setPaymentStatus(PaymentStatus.SUCCESS);
                order.setStatus(com.ntp.sasin_be.enums.OrderStatus.CONFIRMED);
            } else {
                payment.setStatus(PaymentStatus.FAILED);
                order.setPaymentStatus(PaymentStatus.FAILED);
            }

            paymentRepository.save(payment);
            orderRepository.save(order);

        } catch (Exception e) {
            throw new RuntimeException("Failed to handle MoMo IPN: " + e.getMessage(), e);
        }
    }


    // ===== helper =====
    private String buildSafeOrderId(Order order) {
        // dùng code trong DB + timestamp
        String base = order.getCode() + "-" + System.currentTimeMillis();
        String cleaned = base.replaceAll("[^0-9A-Za-z\\-_.:]", "");
        cleaned = cleaned.replaceAll("[-_.:]+", "-");
        cleaned = cleaned.replaceAll("^[^0-9A-Za-z]+", "").replaceAll("[^0-9A-Za-z]+$", "");
        if (cleaned.isEmpty()) {
            cleaned = "ORD" + order.getId();
        }
        return cleaned;
    }

    private String hmacSHA256(String data, String key) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to HMAC SHA256", e);
        }
    }
}