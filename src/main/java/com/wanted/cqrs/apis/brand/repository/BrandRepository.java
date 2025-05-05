package com.wanted.cqrs.apis.brand.repository;

import com.wanted.cqrs.apis.brand.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
