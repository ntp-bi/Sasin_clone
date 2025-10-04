package com.ntp.sasin_be.repositories;

import com.ntp.sasin_be.entities.ShipmentLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentLocationRepository extends JpaRepository<ShipmentLocation, Long> {
}
