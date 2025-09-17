package com.ntp.sasin_be.dto;

import com.ntp.sasin_be.enums.OrderStatus;
import com.ntp.sasin_be.enums.PaymentMethod;
import com.ntp.sasin_be.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private String code;
    private Long userId;
    private BigDecimal subtotal;
    private BigDecimal shippingFee;
    private BigDecimal discount;
    private BigDecimal totalAmount;
    private String deliveryAddress;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private List<OrderItemDTO> items;
}
