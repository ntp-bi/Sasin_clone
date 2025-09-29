package com.ntp.sasin_be.helper;

import java.time.LocalDateTime;
import java.util.UUID;

public class GenerateOrderCode {
    public static String generateOrderCode() {
        return "ORD-" + LocalDateTime.now()
                .toString()
                .replaceAll("[:.T]", "") + "-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}
