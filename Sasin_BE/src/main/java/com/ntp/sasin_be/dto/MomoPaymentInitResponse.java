package com.ntp.sasin_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MomoPaymentInitResponse {
    private String payUrl;
    private String message;
    private Integer resultCode;
}