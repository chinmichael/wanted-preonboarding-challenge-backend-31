package com.wanted.cqrs.apis.auth.controller;

import com.wanted.cqrs.apis.auth.domain.dto.RequestLoginDto;
import com.wanted.cqrs.apis.auth.service.AuthService;
import com.wanted.cqrs.common.response.ApiErrorEnum;
import com.wanted.cqrs.common.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "인증 API", description = "기본 토큰 발급 관련 (최소사양)")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/api/auth"}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "로그인 (JWT Access Token 발급)", description = "기본 사양에 맞춰 이메일만 입력")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLoginDto request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        return ResponseUtils.success(token);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        Map<String, String> details = new HashMap<>();
        details.put("message", e.getMessage());
        return ResponseUtils.fail(ApiErrorEnum.UNAUTHORIZED, details);
    }
}
