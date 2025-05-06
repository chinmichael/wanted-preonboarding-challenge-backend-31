package com.wanted.cqrs.apis.product.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wanted.cqrs.apis.product.constant.ProductEnums;
import com.wanted.cqrs.apis.product.domain.jsonb.ProductDetailDimension;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ProductSavReqDto {

    @JsonIgnore private Long id;

    private String name;
    private String slug;
    private String shortDescription;
    private Long sellerId;
    private Long brandId;
    private ProductEnums.Status status;

    private Detail detail;
    private Price price;

    private List<Category> categories;
    private List<OptionGroup> optionGroups;
    private List<Image> images;
    private List<Long> tags;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {
        private Double weight;
        private ProductDetailDimension dimensions;
        private String materials;
        private String countryOfOrigin;
        private String warrantyInfo;
        private String careInstructions;
        private Map<String, Object> additionalInfo;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Price {
        private Integer basePrice;
        private Integer salePrice;
        private Integer costPrice;
        private String currency;
        private Integer taxRate;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private Long CategoryId;
        private boolean isPrimary;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionGroup {

        private String name;
        private int displayOrder;
        private List<Option> options;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Option {
            private String name;
            private Integer additionalPrice;
            private String sku;
            private Integer stock;
            private int displayOrder;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Image {
        private String url;
        private String altText;
        private boolean isPrimary;
        private int displayOrder;
        private Long optionId;
    }
}
