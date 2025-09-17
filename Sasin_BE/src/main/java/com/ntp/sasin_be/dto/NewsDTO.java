package com.ntp.sasin_be.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsDTO {
    private Long id;
    private String title;
    private String slug;
    private String content;
    private String thumbnailUrl;
    private String author;
    private boolean published;
}
