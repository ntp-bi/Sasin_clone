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
    private Double latitude;
    private Double longitude;
    private String address;
    private LocalDateTime recordedAt;
}
