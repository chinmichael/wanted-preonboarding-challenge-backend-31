package com.wanted.cqrs.apis.product.repository;


import com.wanted.cqrs.apis.product.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
