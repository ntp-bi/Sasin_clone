package com.ntp.sasin_be.services;

import com.ntp.sasin_be.dto.ShipmentDTO;
import com.ntp.sasin_be.enums.ShipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IShipmentService {
    Page<ShipmentDTO> getAllShipments(Pageable pageable);

    ShipmentDTO createShipment(ShipmentDTO shipmentDTO);

    ShipmentDTO getShipmentByOrderId(Long orderId);

    ShipmentDTO updateShipmentStatus(Long orderId, ShipmentStatus status);
}
