package com.ntp.sasin_be.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionDTO {
    private Long id;
    private String title;
    private String slug;
    private String description;
    private String bannerUrl;
    private boolean active;
}