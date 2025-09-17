package com.ntp.sasin_be.dto;

import com.ntp.sasin_be.enums.ShipmentStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentDTO {
    private Long id;
    private Long orderId;
    private String carrier;
    private String trackingCode;
    private ShipmentStatus status;
    private LocalDateTime estimatedDelivery;
    private List<ShipmentEventDTO> events;
    private List<ShipmentLocationDTO> locations;
}