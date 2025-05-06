package com.wanted.cqrs.apis.product.domain;

import com.wanted.cqrs.apis.category.domain.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    private Tag tag;
}
