package com.ntp.sasin_be.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String description;
}