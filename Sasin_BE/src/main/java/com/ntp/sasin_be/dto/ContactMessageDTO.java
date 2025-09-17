package com.ntp.sasin_be.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactMessageDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Long restaurantId;
    private String subject;
    private String description;
    private boolean isRead;
}