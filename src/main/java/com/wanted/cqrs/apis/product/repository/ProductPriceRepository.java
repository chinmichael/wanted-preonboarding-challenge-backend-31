package com.wanted.cqrs.apis.product.repository;

import com.wanted.cqrs.apis.product.domain.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
}
