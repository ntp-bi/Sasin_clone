package com.ntp.sasin_be.controllers;

import com.ntp.sasin_be.dto.AddToCartRequest;
import com.ntp.sasin_be.dto.CartDTO;
import com.ntp.sasin_be.dto.UpdateCartItemRequest;
import com.ntp.sasin_be.services.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartServiceImpl cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getActiveCartForUser(userId));
    }

    @PostMapping
    public ResponseEntity<CartDTO> createCart(@RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(request.getUserId(), request.getProductId(), request.getQuantity()));
    }

    @PutMapping("/item")
    public ResponseEntity<CartDTO> updateCart(@RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateQuantity(request.getCartItemId(), request.getQuantity()));
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<CartDTO> deleteCartItem(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.removeCartItem(id));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
