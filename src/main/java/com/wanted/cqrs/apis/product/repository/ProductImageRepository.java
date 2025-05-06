package com.wanted.cqrs.apis.product.repository;

import com.wanted.cqrs.apis.product.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
