package com.wanted.cqrs.apis.product.controller;

import com.wanted.cqrs.apis.product.domain.dto.ProductSavReqDto;
import com.wanted.cqrs.apis.product.service.ProductService;
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

@Tag(name = "상품 관리 API", description = "상품 관리 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/api/products"}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

    @Operation(summary = "상품 등록", description = "")
    @PostMapping
    public ResponseEntity<?> register(@RequestBody ProductSavReqDto requestDto,
                                      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {



        return ResponseUtils.create();
    }

    @Operation(summary = "상품 목록 조회", description = "")
    @GetMapping
    public ResponseEntity<?> searchList() {
        return ResponseUtils.success();
    }

    @Operation(summary = "상품 상세 조회", description = "")
    @GetMapping("/{id}")
    public ResponseEntity<?> searchDetail(@PathVariable long id) {
        return ResponseUtils.success();
    }

    @Operation(summary = "상품 수정", description = "")
    @PutMapping("/{id}")
    public ResponseEntity<?> modify(@PathVariable long id,
                                    @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
        return ResponseUtils.success();
    }

    @Operation(summary = "상품 삭제", description = "soft delete")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable long id,
                                    @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
        return ResponseUtils.success();
    }

    @Operation(summary = "상품 옵션 추가", description = "")
    @PostMapping("/{id}/options")
    public ResponseEntity<?> optionsAdd(@PathVariable long id,
                                        @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
        return ResponseUtils.success();
    }

    @Operation(summary = "상품 수정 추가", description = "")
    @PutMapping("/{id}/options")
    public ResponseEntity<?> optionsModify(@PathVariable long id,
                                           @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
        return ResponseUtils.success();
    }

    @Operation(summary = "상품 옵션 삭제", description = "")
    @DeleteMapping("/{id}/options")
    public ResponseEntity<?> optionsRemove(@PathVariable long id,
                                           @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
        return ResponseUtils.success();
    }

    @Operation(summary = "상품 이미지 추가", description = "")
    @PostMapping("/{id}/images")
    public ResponseEntity<?> imagesAdd(@PathVariable long id,
                                       @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
        return ResponseUtils.success();
    }

    @Operation(summary = "상품 리뷰 조회", description = "")
    @GetMapping("/{id}/reviews")
    public ResponseEntity<?> reviewSearch(@PathVariable long id) {
        return ResponseUtils.success();
    }

    @Operation(summary = "상품 리뷰 작성", description = "")
    @PostMapping("/{id}/reviews")
    public ResponseEntity<?> reviewRegister(@PathVariable long id,
                                            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
        return ResponseUtils.success();
    }
}
