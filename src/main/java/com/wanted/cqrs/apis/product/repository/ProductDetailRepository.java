package com.wanted.cqrs.apis.product.repository;

import com.wanted.cqrs.apis.product.domain.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    Optional<ProductDetail> findByProductId(Long productId);
}
