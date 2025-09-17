package com.ntp.sasin_be.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "restaurant_branches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantBranch extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String phone;

    private String email;

    private Double latitude;

    private Double longitude;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<ContactMessage> contactMessages;
}
