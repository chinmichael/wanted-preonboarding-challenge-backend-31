package com.wanted.cqrs.apis.product.repository;

import com.wanted.cqrs.apis.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
}
