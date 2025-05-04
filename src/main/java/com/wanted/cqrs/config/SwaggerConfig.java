package com.wanted.cqrs.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "CQRS 시스템 설계/구축 챌린지 API 명세서",
                description = "WANTED CQRS 시스템 설계/구축 챌린지 API 명세서",
                version = "v1"))
@Configuration
public class SwaggerConfig {

    public static final String SWAGGER_AUTH_TYPE = "bearerAuth";
    public static final String AUTH_TYPE = "bearer";
    public static final String TOKEN_TYPE = "JWT";
    public static final String AUTH_HEADER = "Authorization";


    @Bean
    public OpenAPI openAPI(){
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme(AUTH_TYPE)
                .bearerFormat(TOKEN_TYPE)
                .in(SecurityScheme.In.HEADER)
                .name(AUTH_HEADER);

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(SWAGGER_AUTH_TYPE);

        return new OpenAPI()
                .components(new Components().addSecuritySchemes(SWAGGER_AUTH_TYPE, securityScheme))
                .security(Arrays.asList(securityRequirement));
    }

    @Bean
    public GroupedOpenApi authApiGroup() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/api/auth/**")
                .addOpenApiCustomizer(openApi ->
                        openApi.setInfo(new Info()
                                .title("인증처리 (토큰발급) API")
                                .description("")
                                .version("0.0.1")
                        )
                )
                .build();
    }

    @Bean
    public GroupedOpenApi productApiGroup() {
        return GroupedOpenApi.builder()
                .group("product")
                .pathsToMatch("/api/products/**")
                .addOpenApiCustomizer(openApi ->
                        openApi.setInfo(new Info()
                                .title("상품 관리 API")
                                .description("")
                                .version("0.0.1")
                        )
                )
                .build();
    }

    @Bean
    public GroupedOpenApi categoryApiGroup() {
        return GroupedOpenApi.builder()
                .group("category")
                .pathsToMatch("/api/categories/**")
                .addOpenApiCustomizer(openApi ->
                        openApi.setInfo(new Info()
                                .title("카테고리 API")
                                .description("")
                                .version("0.0.1")
                        )
                )
                .build();
    }

    @Bean
    public GroupedOpenApi mainPageApiGroup() {
        return GroupedOpenApi.builder()
                .group("main-page")
                .pathsToMatch("/api/main/**")
                .addOpenApiCustomizer(openApi ->
                        openApi.setInfo(new Info()
                                .title("메인 페이지 API")
                                .description("")
                                .version("0.0.1")
                        )
                )
                .build();
    }

    @Bean
    public GroupedOpenApi reviewApiGroup() {
        return GroupedOpenApi.builder()
                .group("review")
                .pathsToMatch("/api/reviews/**")
                .addOpenApiCustomizer(openApi ->
                        openApi.setInfo(new Info()
                                .title("리뷰 API")
                                .description("")
                                .version("0.0.1")
                        )
                )
                .build();
    }
}
