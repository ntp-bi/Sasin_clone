package com.ntp.sasin_be.dto;

import com.ntp.sasin_be.enums.CartStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {
    private Long id;
    private Long userId;
    private BigDecimal totalPrice;
    private CartStatus status;
    private List<CartItemDTO> cartItems;
}
