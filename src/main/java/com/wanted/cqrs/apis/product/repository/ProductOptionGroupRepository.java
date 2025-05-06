package com.wanted.cqrs.apis.product.repository;

import com.wanted.cqrs.apis.product.domain.ProductOptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionGroupRepository extends JpaRepository<ProductOptionGroup, Long>  {
}
