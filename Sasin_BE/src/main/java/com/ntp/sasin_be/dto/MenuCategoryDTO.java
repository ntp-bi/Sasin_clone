package com.ntp.sasin_be.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuCategoryDTO {
    private Long id;
    private String name;
    private String slug;
}