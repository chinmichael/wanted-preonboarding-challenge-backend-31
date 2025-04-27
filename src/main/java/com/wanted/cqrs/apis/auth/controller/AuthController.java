package com.wanted.cqrs.apis.auth.controller;

import com.wanted.cqrs.apis.auth.domain.RequestLoginDto;
import com.wanted.cqrs.apis.auth.service.AuthService;
import com.wanted.cqrs.common.response.ApiErrorEnum;
import com.wanted.cqrs.common.response.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/api/auth"}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class AuthController {
    private final AuthService authService;

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
