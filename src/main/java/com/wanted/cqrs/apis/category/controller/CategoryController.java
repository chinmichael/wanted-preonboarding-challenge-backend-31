package com.wanted.cqrs.apis.category.controller;

import com.wanted.cqrs.apis.category.service.CategoryService;
import com.wanted.cqrs.common.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "카테고리 API", description = "카테고리 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/api/categories"}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 목록 조회", description = "")
    @GetMapping
    public ResponseEntity<?> searchList() {
        return ResponseUtils.success();
    }

    @Operation(summary = "특정 카테고리 상품 목록 조회", description = "")
    @GetMapping("/{id}/products")
    public ResponseEntity<?> searchProductList(@PathVariable long id) {
        return ResponseUtils.success();
    }
}
