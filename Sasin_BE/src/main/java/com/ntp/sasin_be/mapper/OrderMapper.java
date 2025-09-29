package com.ntp.sasin_be.mapper;

import com.ntp.sasin_be.dto.OrderDTO;
import com.ntp.sasin_be.dto.OrderItemDTO;
import com.ntp.sasin_be.entities.Order;
import com.ntp.sasin_be.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderDTO toOrderDTO(Order order);

    List<OrderDTO> toOrderDTOs(List<Order> orders);

    @Mapping(target = "user", ignore = true)
    Order toOrderEntity(OrderDTO orderDTO);

    List<Order> toOrderEntities(List<OrderDTO> orderDTOs);

    @Mapping(source = "product.title", target = "productTitle")
    @Mapping(source = "product.id", target = "productId")
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);

    List<OrderItemDTO> toOrderItemDTOs(List<OrderItem> orderItems);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    OrderItem toOrderItemEntity(OrderItemDTO orderItemDTO);

    List<OrderItem> toOrderItemEntities(List<OrderItemDTO> orderItemDTOs);
}
