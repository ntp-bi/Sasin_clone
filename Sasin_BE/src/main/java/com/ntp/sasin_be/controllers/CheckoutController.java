package com.ntp.sasin_be.controllers;

import com.ntp.sasin_be.dto.CheckoutRequest;
import com.ntp.sasin_be.dto.OrderDTO;
import com.ntp.sasin_be.services.CheckoutServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checkout")
public class CheckoutController {
    private final CheckoutServiceImpl checkoutService;

    @PostMapping
    public ResponseEntity<OrderDTO> checkout(@RequestBody CheckoutRequest checkoutRequest) {
        return ResponseEntity.ok(checkoutService.checkout(checkoutRequest));
    }
}
