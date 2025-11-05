package com.ntp.sasin_be.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "momo")
public class MomoConfig {
    private String partnerCode;
    private String accessKey;
    private String secretKey;
    private String requestUrl;
    private String returnUrl; // frontend redirect after payment
    private String notifyUrl; // IPN endpoint
    private String partnerName;
    private String storeId;
}