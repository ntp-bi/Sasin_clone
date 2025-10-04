package com.ntp.sasin_be.repositories;

import com.ntp.sasin_be.entities.ShipmentEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentEventRepository extends JpaRepository<ShipmentEvent, Long> {
}
