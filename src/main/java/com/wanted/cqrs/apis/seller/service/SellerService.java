package com.wanted.cqrs.apis.seller.service;

import com.wanted.cqrs.apis.seller.domain.Seller;

import java.util.Optional;

public interface SellerService {
    Optional<Seller> getSellerById(Long id);
}
