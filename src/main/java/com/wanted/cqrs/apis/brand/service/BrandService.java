package com.wanted.cqrs.apis.brand.service;

import com.wanted.cqrs.apis.brand.domain.Brand;

import java.util.Optional;

public interface BrandService {
    Optional<Brand> searchBrandById(Long id);
}
