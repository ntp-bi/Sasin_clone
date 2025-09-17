package com.ntp.sasin_be.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
