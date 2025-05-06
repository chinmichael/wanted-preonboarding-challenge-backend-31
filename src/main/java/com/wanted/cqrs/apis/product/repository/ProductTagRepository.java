package com.wanted.cqrs.apis.product.repository;

import com.wanted.cqrs.apis.product.domain.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {
}
