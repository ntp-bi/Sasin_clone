package com.ntp.sasin_be.auth.dto;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserToken {
    private String token;
    private String refreshToken;
    private Date expiredAt;
}
