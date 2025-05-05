package com.wanted.cqrs.apis.product.domain.dto;

import com.wanted.cqrs.apis.product.constant.ProductEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductSavReqDto {

    private String name;
    private String slug;
    private String shortDescription;
    private Long sellerId;
    private Long brandId;
    private ProductEnums.Status status;

    @Getter
    @AllArgsConstructor
    public static class Detail {

        private static class Dimension {
            private int width;
            private int height;
            private int depth;
        }

    }

    @Getter
    @AllArgsConstructor
    public static class Price {

    }

    @Getter
    @AllArgsConstructor
    public static class Category {

    }

    @Getter
    @AllArgsConstructor
    public static class OptionGroup {

        @Getter
        @AllArgsConstructor
        public static class Option {

        }
    }

    public static class Image {

    }
}
