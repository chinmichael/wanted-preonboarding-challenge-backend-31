package com.wanted.cqrs.apis.auth.repository;

import com.wanted.cqrs.apis.auth.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface AuthRepository {
    Optional<User> searchUserByEmail(@Param("email") String email);
}
