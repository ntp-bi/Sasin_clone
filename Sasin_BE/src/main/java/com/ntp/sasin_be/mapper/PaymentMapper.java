package com.ntp.sasin_be.mapper;

import com.ntp.sasin_be.dto.PaymentDTO;
import com.ntp.sasin_be.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(source = "order.id", target = "orderId")
    PaymentDTO toDTO(Payment payment);

    List<PaymentDTO> toDTOs(List<Payment> payments);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "providerResponse", ignore = true)
    Payment toEntity(PaymentDTO dto);

    List<Payment> toEntities(List<PaymentDTO> dtos);
}