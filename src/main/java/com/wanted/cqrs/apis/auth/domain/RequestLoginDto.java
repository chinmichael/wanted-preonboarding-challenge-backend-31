package com.wanted.cqrs.apis.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestLoginDto {
    private final String email;
    private final String password;
}
