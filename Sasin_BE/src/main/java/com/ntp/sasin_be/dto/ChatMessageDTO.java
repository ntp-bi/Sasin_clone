package com.ntp.sasin_be.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    private Long id;
    private Long userId;
    private String sessionToken;
    private String sender;
    private String message;
    private String channel;
}