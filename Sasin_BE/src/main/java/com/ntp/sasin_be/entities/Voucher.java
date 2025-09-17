package com.ntp.sasin_be.entities;

import com.ntp.sasin_be.enums.DiscountType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private Double discountValue;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private Double maxDiscountValue;

    private Double minOrderValue;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean active = true;
}
