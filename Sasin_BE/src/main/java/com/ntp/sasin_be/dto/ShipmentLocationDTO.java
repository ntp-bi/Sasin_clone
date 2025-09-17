package com.ntp.sasin_be.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentLocationDTO {
    private Long id;
    private Long shipmentId;
    private Double lat;
    private Double lng;
    private String address;
    private LocalDateTime recordedAt;
}
