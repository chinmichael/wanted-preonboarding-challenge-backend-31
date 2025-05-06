package com.wanted.cqrs.apis.product.domain;

import com.wanted.cqrs.apis.brand.domain.Brand;
import com.wanted.cqrs.apis.review.domain.Review;
import com.wanted.cqrs.apis.seller.domain.Seller;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    private String name;
    private String slug;
    private String shortDescription;
    private String fullDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    @OneToMany(fetch = FetchType.LAZY)
    private List<ProductImage> images;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToOne(fetch = FetchType.LAZY)
    private ProductDetail detail;

    @OneToOne(fetch = FetchType.LAZY)
    private ProductPrice price;
}
