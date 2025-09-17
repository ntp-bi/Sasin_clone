package com.ntp.sasin_be.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FranchiseRequestDTO {
    private Long id;
    private String ownerName;
    private String email;
    private String phone;
    private String location;
    private String note;
    private boolean approved;
}
