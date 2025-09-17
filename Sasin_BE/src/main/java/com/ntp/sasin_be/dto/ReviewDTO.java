package com.ntp.sasin_be.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long productId;
    private String productTitle;
    private Integer rating;
    private String content;
    private String imageUrl;
    private boolean approved;
}