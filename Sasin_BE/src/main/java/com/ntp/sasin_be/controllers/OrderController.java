package com.ntp.sasin_be.controllers;

import com.ntp.sasin_be.dto.OrderDTO;
import com.ntp.sasin_be.enums.OrderStatus;
import com.ntp.sasin_be.services.impl.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    private final OrderServiceImpl orderService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping("/order/user/{userId}")
    public ResponseEntity<List<OrderDTO>> listOrderByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.listOrderByUserId(userId));
    }

    @GetMapping("/admin/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/admin/order/status/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDTO> updateStatus(@PathVariable Long orderId,
                                                 @RequestParam OrderStatus orderStatus) {
        return ResponseEntity.ok(orderService.updateStatus(orderId, orderStatus));
    }
}
