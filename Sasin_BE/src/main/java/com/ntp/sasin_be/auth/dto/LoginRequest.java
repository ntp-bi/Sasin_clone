package com.ntp.sasin_be.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank
    private String userName;

    @NotBlank
    private String password;
}
