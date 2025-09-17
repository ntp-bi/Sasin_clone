package com.ntp.sasin_be.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "promotions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promotion extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String slug;

    @Lob
    private String description;

    private String bannerUrl;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean active = true;
}