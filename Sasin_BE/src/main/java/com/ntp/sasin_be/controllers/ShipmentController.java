package com.ntp.sasin_be.controllers;

import com.ntp.sasin_be.dto.ShipmentDTO;
import com.ntp.sasin_be.enums.ShipmentStatus;
import com.ntp.sasin_be.services.impl.ShipmentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ShipmentController {
    private final ShipmentServiceImpl shipmentService;

    @GetMapping("/admin/shipments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ShipmentDTO>> getAllShipments(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(shipmentService.getAllShipments(pageable));
    }

    @PostMapping("/admin/shipment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShipmentDTO> createShipment(@RequestBody ShipmentDTO shipmentDTO) {
        return ResponseEntity.ok(shipmentService.createShipment(shipmentDTO));
    }

    @GetMapping("/admin/shipment/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShipmentDTO> getShipmentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(shipmentService.getShipmentByOrderId(orderId));
    }

    @PutMapping("/admin/shipment/status/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ShipmentDTO updateStatus(@PathVariable Long orderId,
                                    @RequestParam ShipmentStatus status) {
        return shipmentService.updateShipmentStatus(orderId, status);
    }
}
