package com.ntp.sasin_be.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String slug;

    @Lob
    private String content;

    private String thumbnailUrl;

    private String author;

    private boolean published = false;

    private LocalDateTime publishedAt;
}
