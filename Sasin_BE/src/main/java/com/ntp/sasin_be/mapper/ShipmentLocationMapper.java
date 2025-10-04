package com.ntp.sasin_be.mapper;

import com.ntp.sasin_be.dto.ShipmentLocationDTO;
import com.ntp.sasin_be.entities.ShipmentLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShipmentLocationMapper {
    @Mapping(source = "shipment.id", target = "shipmentId")
    ShipmentLocationDTO toDTO(ShipmentLocation loc);

    List<ShipmentLocationDTO> toDTOs(List<ShipmentLocation> locs);

    @Mapping(target = "shipment", ignore = true)
    ShipmentLocation toEntity(ShipmentLocationDTO dto);

    List<ShipmentLocation> toEntities(List<ShipmentLocationDTO> dtos);
}