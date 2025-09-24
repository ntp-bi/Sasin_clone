package com.ntp.sasin_be.auth.dto;

import com.ntp.sasin_be.enums.Role;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsDTO {
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private boolean gender;
    private String image;
    private LocalDate dateOfBirth;
    private Role role;
}
