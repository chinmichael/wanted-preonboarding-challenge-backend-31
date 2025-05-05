package com.wanted.cqrs.apis.auth.repository;

import com.wanted.cqrs.apis.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
