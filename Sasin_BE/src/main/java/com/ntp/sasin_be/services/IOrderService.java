package com.ntp.sasin_be.services;

import com.ntp.sasin_be.dto.OrderDTO;
import com.ntp.sasin_be.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    OrderDTO getOrderById(Long orderId);

    List<OrderDTO> listOrderByUserId(Long userId);

    Page<OrderDTO> getAllOrders(Pageable pageable);

    OrderDTO updateStatus(Long orderId, OrderStatus orderStatus);
}