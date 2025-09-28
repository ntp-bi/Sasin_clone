package com.ntp.sasin_be.dto;

import lombok.Data;

@Data
public class UpdateCartItemRequest {
    private Long cartItemId;
    private Integer quantity;
}
