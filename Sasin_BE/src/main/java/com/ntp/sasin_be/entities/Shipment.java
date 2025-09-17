package com.ntp.sasin_be.entities;

import com.ntp.sasin_be.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String carrier;

    private String trackingCode;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status = ShipmentStatus.WAITING_PICKUP;

    private LocalDateTime estimatedDelivery;

    private LocalDateTime pickedUpAt;

    private LocalDateTime deliveredAt;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShipmentEvent> events;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShipmentLocation> locations;
}
