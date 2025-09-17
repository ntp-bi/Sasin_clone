package com.ntp.sasin_be.entities;

import com.ntp.sasin_be.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipment_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Shipment shipment;

    private String statusText;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    private String location;

    private String note;

    private LocalDateTime eventTime = LocalDateTime.now();
}