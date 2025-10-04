package com.ntp.sasin_be.controllers;

import com.ntp.sasin_be.dto.OrderDTO;
import com.ntp.sasin_be.enums.OrderStatus;
import com.ntp.sasin_be.services.impl.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<OrderDTO>> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @PutMapping("/admin/order/status/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDTO> updateStatus(@PathVariable Long orderId,
                                                 @RequestParam OrderStatus orderStatus) {
        return ResponseEntity.ok(orderService.updateStatus(orderId, orderStatus));
    }
}
