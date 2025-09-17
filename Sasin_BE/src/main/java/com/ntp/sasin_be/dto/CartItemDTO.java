package com.ntp.sasin_be.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {
    private Long id;
    private Long productId;
    private String productTitle;
    private Integer quantity;
    private BigDecimal unitPrice;
}
