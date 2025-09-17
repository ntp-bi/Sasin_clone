package com.ntp.sasin_be.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostDTO {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String salary;
    private boolean active;
}