package com.wanted.cqrs.common.pagenation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class PageRequest {
    private long page;
    private long perPage;
}
