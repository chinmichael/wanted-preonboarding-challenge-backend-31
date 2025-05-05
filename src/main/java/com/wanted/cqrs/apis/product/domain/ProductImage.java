package com.wanted.cqrs.apis.product.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    private String url;
    private String altText;
    private Boolean isPrimary;
    private Integer displayOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "option_id", nullable = true)
    private ProductOption option;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
}
