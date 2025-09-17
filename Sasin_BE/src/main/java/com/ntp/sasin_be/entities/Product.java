package com.ntp.sasin_be.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku; // Stock Keeping Unit

    private String title;

    @Lob
    private String description;

    @Column(precision = 13, scale = 2)
    private BigDecimal price;

    @Column(precision = 13, scale = 2)
    private BigDecimal originalPrice;

    private Integer stock;

    private Boolean isNew = false;

    private Boolean isHot = false;

    private Boolean isAvailable = true;

    private String unit; // tô, phần, combo

    @Column(length = 1024)
    private String image01;

    @Column(length = 1024)
    private String image02;

    @Column(columnDefinition = "json")
    private String imagesJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchise_id", nullable = false)
    private RestaurantBranch franchise;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews;
}