package com.ntp.sasin_be.repositories;

import com.ntp.sasin_be.entities.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Shipment findByOrderId(Long orderId);
}
