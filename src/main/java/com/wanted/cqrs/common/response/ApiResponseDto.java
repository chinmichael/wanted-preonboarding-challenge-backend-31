package com.wanted.cqrs.common.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponseDto<T, E> {

    private final boolean success;

    private final T data;

    private final String message;

    private final ApiError<E> error;

    @Builder
    private ApiResponseDto(boolean success, T data, String message, ApiError<E> error) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.error = error;
    }
}
