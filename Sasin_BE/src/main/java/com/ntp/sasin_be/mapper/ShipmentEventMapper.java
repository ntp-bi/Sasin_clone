package com.ntp.sasin_be.mapper;

import com.ntp.sasin_be.dto.ShipmentEventDTO;
import com.ntp.sasin_be.entities.ShipmentEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShipmentEventMapper {
    @Mapping(source = "shipment.id", target = "shipmentId")
    ShipmentEventDTO toDTO(ShipmentEvent event);

    List<ShipmentEventDTO> toDTOs(List<ShipmentEvent> events);

    @Mapping(target = "shipment", ignore = true)
    ShipmentEvent toEntity(ShipmentEventDTO dto);

    List<ShipmentEvent> toEntities(List<ShipmentEventDTO> dtos);
}