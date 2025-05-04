package com.wanted.cqrs.common.pagenation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.function.Supplier;

@Getter
public class PageResponseDto<T> {
    private final List<T> items;
    private final PageResponse pagination;

    public PageResponseDto(List<T> items, PageRequest pageRequest, long total) {
        this.items = items;

        this.pagination = PageResponse.builder()
                .currentPage(pageRequest.getPage())
                .perPage(pageRequest.getPerPage())
                .totalItems(total)
                .totalPages((long)Math.ceil((total * 1.0) / pageRequest.getPerPage()))
                .build();
    }

    public PageResponseDto(List<T> items, PageRequest pageRequest, Supplier<Long> totalSupplier) {
        this(items, pageRequest, totalSupplier.get());
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PageResponse {
        private long totalItems;
        private long totalPages;
        private long currentPage;
        private long perPage;
    }
}
