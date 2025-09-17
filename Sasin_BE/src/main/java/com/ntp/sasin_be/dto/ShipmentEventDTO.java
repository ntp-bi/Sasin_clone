package com.ntp.sasin_be.dto;

import com.ntp.sasin_be.enums.ShipmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentEventDTO {
    private Long id;
    private Long shipmentId;
    private String statusText;
    private ShipmentStatus status;
    private String location;
    private String note;
    private LocalDateTime eventTime;
}
