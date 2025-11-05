package com.ntp.sasin_be.controllers;

import com.ntp.sasin_be.dto.MomoPaymentInitResponse;
import com.ntp.sasin_be.services.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final IPaymentService paymentService;

    @PostMapping("/momo/{orderId}")
    public ResponseEntity<MomoPaymentInitResponse> createMomoPayment(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.initiateMomoPayment(orderId));
    }

    // IPN endpoint for MoMo to call
    @PostMapping("/momo/ipn")
    public ResponseEntity<String> momoIpn(@RequestBody String body) {
        paymentService.handleMomoIpn(body);
        return ResponseEntity.ok("OK");
    }
}