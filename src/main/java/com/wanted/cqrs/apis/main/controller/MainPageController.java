package com.wanted.cqrs.apis.main.controller;

import com.wanted.cqrs.apis.main.service.MainPageService;
import com.wanted.cqrs.common.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "메인 페이지 API", description = "메인 페이지 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/api/main"}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class MainPageController {

    private final MainPageService mainPageService;

    @Operation(summary = "메인 페이지 상품 및 카테고리 목록 조회", description = "")
    @GetMapping
    public ResponseEntity<?> searchList() {
        return ResponseUtils.success();
    }
}
