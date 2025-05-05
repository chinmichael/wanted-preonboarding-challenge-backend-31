package com.wanted.cqrs.apis.main.service;

import com.wanted.cqrs.apis.category.service.CategoryService;
import com.wanted.cqrs.apis.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("MainPageService")
@Transactional
@RequiredArgsConstructor
public class MainPageServiceImpl implements MainPageService {

    private final ProductService productService;
    private final CategoryService categoryService;
}
