package com.wanted.cqrs.apis.auth.domain;

import com.wanted.cqrs.common.auth.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserWithRoleDto {
    private final User user;
    private final Roles role;
}
