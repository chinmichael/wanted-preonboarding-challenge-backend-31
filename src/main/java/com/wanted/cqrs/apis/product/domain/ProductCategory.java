package com.wanted.cqrs.apis.product.domain;

import com.wanted.cqrs.apis.category.domain.Category;
import com.wanted.cqrs.apis.category.domain.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_categoris")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    private Boolean isPrimary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
}
