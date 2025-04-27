package com.wanted.cqrs.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiError<T> {
    private final String code;
    private final String message;
    private final T details;

    @Builder
    private ApiError(ApiErrorEnum error, T details) {
        this.code = error.getCode();
        this.message = error.getMessage();
        this.details = details;
    }
}
