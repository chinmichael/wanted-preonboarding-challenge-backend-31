package com.wanted.cqrs.apis.review.controller;

import com.wanted.cqrs.apis.review.service.ReviewService;
import com.wanted.cqrs.common.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "리뷰 API", description = "리뷰 API, 각 상품별 조회 및 등록은 상품관리 API 참조")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/api/reviews"}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 수정", description = "")
    @PutMapping("/{id}")
    public ResponseEntity<?> reviewSearch(@PathVariable long id,
                                          @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
        return ResponseUtils.success();
    }

    @Operation(summary = "리뷰 삭제", description = "")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> reviewRegister(@PathVariable long id,
                                            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
        return ResponseUtils.success();
    }
}
