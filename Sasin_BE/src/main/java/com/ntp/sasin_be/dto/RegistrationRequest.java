package com.ntp.sasin_be.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean gender;
    private LocalDate dateOfBirth;
}
