package com.ntp.sasin_be.services;

import com.ntp.sasin_be.dto.CartDTO;

public interface ICartService {
    CartDTO getActiveCartForUser(Long userId);

    CartDTO addToCart(Long userId, Long productId, Integer quantity);

    CartDTO updateQuantity(Long cartItemId, int quantity);

    CartDTO removeCartItem(Long cartItemId);

    void clearCart(Long userId);
}
