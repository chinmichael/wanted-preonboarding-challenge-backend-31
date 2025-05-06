package com.wanted.cqrs.apis.product.service;

import com.wanted.cqrs.apis.product.domain.dto.ProductSavReqDto;
import com.wanted.cqrs.apis.product.domain.dto.ProductSavResDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface ProductService {
    ProductSavResDto save(ProductSavReqDto requestDto, UserDetails user);
}
