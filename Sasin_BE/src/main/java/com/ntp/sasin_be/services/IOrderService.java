package com.ntp.sasin_be.services;

import com.ntp.sasin_be.dto.OrderDTO;
import com.ntp.sasin_be.enums.OrderStatus;

import java.util.List;

public interface IOrderService {
    OrderDTO getOrderById(Long orderId);

    List<OrderDTO> listOrderByUserId(Long userId);

    List<OrderDTO> getAllOrders();

    OrderDTO updateStatus(Long orderId, OrderStatus orderStatus);
}