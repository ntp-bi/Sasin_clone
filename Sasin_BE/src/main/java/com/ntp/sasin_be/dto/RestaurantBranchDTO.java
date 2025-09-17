package com.ntp.sasin_be.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantBranchDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private Double lat;
    private Double lng;
}
