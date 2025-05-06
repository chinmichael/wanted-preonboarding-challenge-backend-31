package com.wanted.cqrs.apis.seller.service;

import com.wanted.cqrs.apis.seller.domain.Seller;
import com.wanted.cqrs.apis.seller.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service("SellerService")
@Transactional
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;

    @Override
    public Optional<Seller> getSellerById(Long id) {
        return sellerRepository.findById(id);
    }
}
