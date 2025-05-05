package com.wanted.cqrs.apis.auth.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "테스트 인증 dto")
public class RequestLoginDto {
    @Schema(description = "테스트 사용자 이메일, 1~5:seller", example = "jiwon.kim@example.com")
    private final String email;
    @Schema(description = "형식적으로 작성한 필드")
    private final String password;
}
