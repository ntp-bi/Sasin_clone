package com.ntp.sasin_be.entities;

import com.ntp.sasin_be.auth.entities.User;
import com.ntp.sasin_be.enums.OrderStatus;
import com.ntp.sasin_be.enums.PaymentMethod;
import com.ntp.sasin_be.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @Column(precision = 13, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 13, scale = 2)
    private BigDecimal shippingFee;

    @Column(precision = 13, scale = 2)
    private BigDecimal discount;

    @Column(precision = 13, scale = 2)
    private BigDecimal totalAmount;

    private String deliveryAddress;

    private Double deliveryLatitude;

    private Double deliveryLongitude;

    private String customerNote;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();
}
