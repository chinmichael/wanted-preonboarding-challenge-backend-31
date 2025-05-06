package com.wanted.cqrs.apis.product.service;

import com.wanted.cqrs.apis.brand.service.BrandService;
import com.wanted.cqrs.apis.product.domain.Product;
import com.wanted.cqrs.apis.product.domain.ProductDetail;
import com.wanted.cqrs.apis.product.domain.dto.ProductSavReqDto;
import com.wanted.cqrs.apis.product.domain.dto.ProductSavResDto;
import com.wanted.cqrs.apis.product.repository.*;
import com.wanted.cqrs.apis.seller.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service("ProductService")
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductPriceRepository productPriceRepository;

    private final ProductOptionRepository productOptionRepository;
    private final ProductOptionGroupRepository productOptionGroupRepository;

    private final ProductTagRepository productTagRepository;
    private final ProductCategoryRepository productCategoryRepository;

    private final SellerService sellerService;
    private final BrandService brandService;

    @Override
    public ProductSavResDto save(ProductSavReqDto requestDto, UserDetails user) {

        // 제품

        if(requestDto.getId() != null) productRepository.findById(requestDto.getId()).orElseThrow(
                () -> new NoSuchElementException("존재 하지 않은 상품 입니다.")
        );

        Product saveProduct = productRepository.save(Product.builder()
                .id(requestDto.getId())
                .name(requestDto.getName())
                .slug(requestDto.getSlug())
                .shortDescription(requestDto.getShortDescription())
                .brand(brandService.searchBrandById(requestDto.getBrandId()).orElseThrow(
                        () -> new NoSuchElementException("존재 하지 않은 브랜드 입니다.")
                ))
                .build());

        // 제품 상세

        Optional<ProductDetail> detail = productDetailRepository.findByProductId(saveProduct.getId());
        ProductSavReqDto.Detail saveDetail = requestDto.getDetail() == null ? new ProductSavReqDto.Detail() : requestDto.getDetail();
        productDetailRepository.save(ProductDetail.builder()
                .id(detail.map(ProductDetail::getId).orElse(null))
                .product(saveProduct)
                .weight(saveDetail.getWeight())
                .dimensions(saveDetail.getDimensions())
                .materials(saveDetail.getMaterials())
                .countryOfOrigin(saveDetail.getCountryOfOrigin())
                .warrantyInfo(saveDetail.getWarrantyInfo())
                .careInstructions(saveDetail.getCareInstructions())
                .additionalInfo(saveDetail.getAdditionalInfo())
                .build());

        return null;
    }
}
