package com.wanted.cqrs.apis.product.domain;

import com.wanted.cqrs.apis.product.domain.jsonb.ProductDetailDimension;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "product_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    private BigDecimal weight;
    private String materials;
    private String countryOfOrigin;
    private String warrantyInfo;
    private String careInstructions;

    @JdbcTypeCode(SqlTypes.JSON)
    private ProductDetailDimension dimensions;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> additionalInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private Product product;
}
