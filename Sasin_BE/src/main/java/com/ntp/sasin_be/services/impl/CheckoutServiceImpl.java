package com.ntp.sasin_be.services.impl;

import com.ntp.sasin_be.dto.CheckoutRequest;
import com.ntp.sasin_be.dto.OrderDTO;
import com.ntp.sasin_be.entities.Cart;
import com.ntp.sasin_be.entities.CartItem;
import com.ntp.sasin_be.entities.Order;
import com.ntp.sasin_be.entities.OrderItem;
import com.ntp.sasin_be.enums.CartStatus;
import com.ntp.sasin_be.enums.OrderStatus;
import com.ntp.sasin_be.enums.PaymentStatus;
import com.ntp.sasin_be.exceptions.ResourceNotFoundException;
import com.ntp.sasin_be.helper.GenerateOrderCode;
import com.ntp.sasin_be.mapper.OrderMapper;
import com.ntp.sasin_be.repositories.CartItemRepository;
import com.ntp.sasin_be.repositories.CartRepository;
import com.ntp.sasin_be.repositories.OrderItemRepository;
import com.ntp.sasin_be.repositories.OrderRepository;
import com.ntp.sasin_be.services.ICheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements ICheckoutService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO checkout(CheckoutRequest request) {
        Long userId = request.getUserId();

        Cart cart = cartRepository.findByUserIdAndStatus(request.getUserId(), CartStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active cart not found for user " + userId));

        List<CartItem> cartItems = cartItemRepository.getAllByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order();
        order.setCode(GenerateOrderCode.generateOrderCode());
        order.setUser(cart.getUser());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setTotalPrice(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItem.setNote(null);

            orderItems.add(orderItem);
            total = total.add(orderItem.getTotalPrice());
        }

        order.setItems(orderItems);
        order.setSubtotal(total);

        // shipping & discount - accept from request or default to ZERO
        BigDecimal shippingFee = request.getShippingFee() == null ? BigDecimal.ZERO : request.getShippingFee();
        BigDecimal discount = request.getDiscount() == null ? BigDecimal.ZERO : request.getDiscount();

        order.setShippingFee(shippingFee);
        order.setDiscount(discount);
        order.setTotalAmount(total.add(shippingFee).subtract(discount));

        order.setCustomerNote(request.getCustomerNote());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setDeliveryLatitude(request.getDeliveryLatitude());
        order.setDeliveryLongitude(request.getDeliveryLongitude());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // save order items (cascade may handle it but to be explicit)
        orderItemRepository.saveAll(orderItems);

        // mark cart checked out (next time a new cart is created)
        cart.setStatus(CartStatus.CHECKED_OUT);
        cartRepository.save(cart);

        return orderMapper.toOrderDTO(savedOrder);
    }
}
