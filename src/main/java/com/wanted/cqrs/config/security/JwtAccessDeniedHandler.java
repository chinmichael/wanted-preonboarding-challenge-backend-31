package com.wanted.cqrs.config.security;

import com.wanted.cqrs.common.response.ApiErrorEnum;
import com.wanted.cqrs.common.response.ResponseUtils;
import com.wanted.cqrs.common.utils.Mappers;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseUtils.printErrorResponse(response, ApiErrorEnum.FORBIDDEN);
    }
}
