package com.wanted.cqrs.config;

import com.wanted.cqrs.common.response.ApiError;
import com.wanted.cqrs.common.response.ApiErrorEnum;
import com.wanted.cqrs.common.response.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.wanted_study.cqrs.apis")
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException mx) {
        Map<String, String> details = new HashMap<>();

        mx.getBindingResult().getAllErrors().forEach((error) -> {
           String field = ((FieldError) error).getField();
           details.put(field, error.getDefaultMessage());
        });

        return ResponseUtils.fail(ApiErrorEnum.INVALID_INPUT, details);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<?> handleRuntimeException(RuntimeException rx) {
        return ResponseUtils.fail(ApiErrorEnum.INTERNAL_ERROR);
    }
}
