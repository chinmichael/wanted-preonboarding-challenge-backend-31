package com.wanted.cqrs.apis.seller.repository;

import com.wanted.cqrs.apis.seller.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
