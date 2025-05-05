package com.wanted.cqrs.apis.review.repository;

import com.wanted.cqrs.apis.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
