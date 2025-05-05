package com.wanted.cqrs.apis.auth.domain.dto;

import com.wanted.cqrs.apis.auth.domain.User;
import com.wanted.cqrs.common.auth.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserWithRoleDto {
    private final User user;
    private final Roles role;
}
