package com.ntp.sasin_be.dto;

import com.ntp.sasin_be.enums.PaymentMethod;
import com.ntp.sasin_be.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long id;
    private Long orderId;
    private PaymentMethod method;
    private PaymentStatus status;
    private String transactionCode;
    private BigDecimal amount;
}
