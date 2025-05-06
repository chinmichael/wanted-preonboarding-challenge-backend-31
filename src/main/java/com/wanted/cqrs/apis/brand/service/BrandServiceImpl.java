package com.wanted.cqrs.apis.brand.service;

import com.wanted.cqrs.apis.brand.domain.Brand;
import com.wanted.cqrs.apis.brand.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service("BrandService")
@Transactional
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    public Optional<Brand> searchBrandById(Long id) {
        return brandRepository.findById(id);
    }
}
