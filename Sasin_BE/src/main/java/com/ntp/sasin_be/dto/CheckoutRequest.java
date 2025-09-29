package com.ntp.sasin_be.dto;

import com.ntp.sasin_be.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequest {
    private Long userId;
    private String deliveryAddress;
    private Double deliveryLatitude;
    private Double deliveryLongitude;
    private String customerNote;
    private PaymentMethod paymentMethod;
    private BigDecimal shippingFee;
    private BigDecimal discount;
}
