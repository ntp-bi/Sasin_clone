package com.ntp.sasin_be.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String sku;
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private Boolean isNew;
    private Boolean isHot;
    private Boolean isAvailable;
    private String unit;
    private String image01;
    private String image02;
    private String imagesJson;

    private Long categoryId;
    private Long supplierId;
    private Long franchiseId;
}
