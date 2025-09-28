package com.ntp.sasin_be.dto;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long userId;
    private Long productId;
    private Integer quantity = 1;
}
