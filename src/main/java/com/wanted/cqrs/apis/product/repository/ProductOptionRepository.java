package com.wanted.cqrs.apis.product.repository;

import com.wanted.cqrs.apis.product.domain.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
}
