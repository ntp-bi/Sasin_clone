package com.ntp.sasin_be.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    private Long id;
    private Long productId;
    private String productTitle;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
