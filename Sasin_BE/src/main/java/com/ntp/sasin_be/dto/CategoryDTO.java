package com.ntp.sasin_be.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
}
