package com.ntp.sasin_be.dto;

import com.ntp.sasin_be.enums.DiscountType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherDTO {
    private Long id;
    private String code;
    private Double discountValue;
    private DiscountType discountType;
    private Double maxDiscountValue;
    private Double minOrderValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
}
