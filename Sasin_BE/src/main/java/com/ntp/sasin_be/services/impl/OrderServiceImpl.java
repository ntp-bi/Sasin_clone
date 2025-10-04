package com.ntp.sasin_be.services.impl;

import com.ntp.sasin_be.dto.OrderDTO;
import com.ntp.sasin_be.entities.Order;
import com.ntp.sasin_be.enums.OrderStatus;
import com.ntp.sasin_be.exceptions.ResourceNotFoundException;
import com.ntp.sasin_be.mapper.OrderMapper;
import com.ntp.sasin_be.repositories.OrderRepository;
import com.ntp.sasin_be.services.IOrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found " + orderId));

        return orderMapper.toOrderDTO(order);
    }

    @Override
    public List<OrderDTO> listOrderByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toOrderDTO);
    }

    @Override
    @Transactional
    public OrderDTO updateStatus(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found " + orderId));

        order.setStatus(orderStatus);
        orderRepository.save(order);

        return orderMapper.toOrderDTO(order);
    }
}
