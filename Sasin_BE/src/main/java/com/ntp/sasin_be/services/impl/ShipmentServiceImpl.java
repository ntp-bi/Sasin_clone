package com.ntp.sasin_be.services.impl;

import com.ntp.sasin_be.dto.ShipmentDTO;
import com.ntp.sasin_be.entities.Order;
import com.ntp.sasin_be.entities.Shipment;
import com.ntp.sasin_be.enums.ShipmentStatus;
import com.ntp.sasin_be.exceptions.ResourceNotFoundException;
import com.ntp.sasin_be.mapper.ShipmentMapper;
import com.ntp.sasin_be.repositories.OrderRepository;
import com.ntp.sasin_be.repositories.ShipmentRepository;
import com.ntp.sasin_be.services.IShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements IShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;
    private final ShipmentMapper shipmentMapper;

    @Override
    public Page<ShipmentDTO> getAllShipments(Pageable pageable) {
        return shipmentRepository.findAll(pageable).map(shipmentMapper::toDTO);
    }

    @Override
    public ShipmentDTO createShipment(ShipmentDTO shipmentDTO) {
        Shipment shipment = shipmentMapper.toEntity(shipmentDTO);

        if (shipmentDTO.getOrderId() != null) {
            Order order = orderRepository.findById(shipmentDTO.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + shipmentDTO.getOrderId()));
            shipment.setOrder(order);
        }

        if (shipment.getEvents() != null) {
            shipment.getEvents().forEach(event -> event.setShipment(shipment));
        }

        if (shipment.getLocations() != null) {
            shipment.getLocations().forEach(loc -> loc.setShipment(shipment));
        }

        Shipment saved = shipmentRepository.save(shipment);
        return shipmentMapper.toDTO(saved);
    }

    @Override
    public ShipmentDTO getShipmentByOrderId(Long orderId) {
        Shipment shipment = shipmentRepository.findByOrderId(orderId);

        if (shipment == null) {
            throw new ResourceNotFoundException("Shipment not found for orderId: " + orderId);
        }

        return shipmentMapper.toDTO(shipment);
    }

    @Override
    public ShipmentDTO updateShipmentStatus(Long orderId, ShipmentStatus status) {
        Shipment shipment = shipmentRepository.findByOrderId(orderId);
        if (shipment == null) {
            throw new ResourceNotFoundException("Shipment not found for orderId: " + orderId);
        }

        shipment.setStatus(status);

        if (status == ShipmentStatus.IN_TRANSIT) {
            shipment.setPickedUpAt(LocalDateTime.now());
        } else if (status == ShipmentStatus.DELIVERED) {
            shipment.setDeliveredAt(LocalDateTime.now());
        }

        return shipmentMapper.toDTO(shipmentRepository.save(shipment));
    }
}
