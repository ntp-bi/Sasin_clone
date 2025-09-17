package com.ntp.sasin_be.entities;

import com.ntp.sasin_be.enums.PaymentMethod;
import com.ntp.sasin_be.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    private String transactionCode;

    @Column(columnDefinition = "json")
    private String providerResponse;

    @Column(precision = 13, scale = 2)
    private BigDecimal amount;

    private LocalDateTime paidAt;

    private LocalDateTime createdAt = LocalDateTime.now();
}