package com.wanted.cqrs.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiErrorEnum {
    INVALID_INPUT(
            "INVALID_INPUT",
            "잘못된 입력 데이터",
            "입력 데이터가 유효하지 않습니다.",
            HttpStatus.BAD_REQUEST.value()),

    RESOURCE_NOT_FOUND(
            "RESOURCE_NOT_FOUND",
            "요청한 리소스를 찾을 수 없음",
            "요청한 리소스를 찾을 수 없습니다.",
            HttpStatus.NOT_FOUND.value()),

    UNAUTHORIZED(
            "UNAUTHORIZED",
            "인증되지 않은 요청",
            "인증이 필요합니다.",
            HttpStatus.UNAUTHORIZED.value()),

    FORBIDDEN(
            "FORBIDDEN",
            "권한이 없는 요청",
            "해당 작업을 수행할 권한이 없습니다.",
            HttpStatus.FORBIDDEN.value()),

    CONFLICT(
            "CONFLICT",
            "리소스 충돌 발생",
            "리소스 충돌이 발생했습니다.",
            HttpStatus.CONFLICT.value()),

    INTERNAL_ERROR(
            "INTERNAL_ERROR",
            "서버 내부 오류",
            "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.",
            HttpStatus.INTERNAL_SERVER_ERROR.value());

    private final String code;
    private final String description;
    private final String message;
    private final int httpStatus;
}
