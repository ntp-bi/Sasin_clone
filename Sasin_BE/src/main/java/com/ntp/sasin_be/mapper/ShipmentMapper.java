package com.ntp.sasin_be.mapper;

import com.ntp.sasin_be.dto.ShipmentDTO;
import com.ntp.sasin_be.entities.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ShipmentEventMapper.class, ShipmentLocationMapper.class})
public interface ShipmentMapper {
    @Mapping(source = "order.id", target = "orderId")
    ShipmentDTO toDTO(Shipment shipment);

    List<ShipmentDTO> toDTOs(List<Shipment> shipments);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "events", source = "events")
    @Mapping(target = "locations", source = "locations")
    Shipment toEntity(ShipmentDTO dto);

    List<Shipment> toEntities(List<ShipmentDTO> dtos);
}