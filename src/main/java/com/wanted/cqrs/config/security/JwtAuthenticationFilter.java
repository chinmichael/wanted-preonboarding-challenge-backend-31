package com.wanted.cqrs.config.security;

import com.wanted.cqrs.apis.auth.domain.UserWithRoleDto;
import com.wanted.cqrs.apis.auth.service.AuthService;
import com.wanted.cqrs.common.response.ApiErrorEnum;
import com.wanted.cqrs.common.response.ResponseUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AuthService authService;

    public static final List<String> PERMIT_URLS = Collections.unmodifiableList(Arrays.asList(
            "/api/auth/login"
    ));

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        if(PERMIT_URLS.stream().anyMatch(url -> uri.startsWith(url))) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        Map<String, String> details = new HashMap<>();

        try {
            String accessToken = authHeader != null && authHeader.startsWith(BEARER_PREFIX) ? authHeader.substring(7) : null;
            if(accessToken != null) throw new IllegalAccessException("Invalid token");
            // 만료된 경우 token 파싱 중 expired ext
            if(!jwtUtils.isTokenExpired(accessToken)) {
                UserDetails userDetails = this.loadUserDetails(jwtUtils.getJwtSubject(accessToken));

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null ,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        } catch (IllegalAccessException ie) {
            details.put("message", ie.getMessage());
            ResponseUtils.printErrorResponse(response, ApiErrorEnum.UNAUTHORIZED, details);
        } catch (ExpiredJwtException ee) {
            details.put("message", "토큰이 만료되었습니다.");
            ResponseUtils.printErrorResponse(response, ApiErrorEnum.UNAUTHORIZED, details);
        }

        filterChain.doFilter(request, response);
    }

    private UserDetails loadUserDetails(String tokenSubject) throws IllegalAccessException { // 인증은 간단히 넘어가므로 기본 UserDetails 사용
        // 간단히 DB 조회만 처리, 해당 서비스에서 role은 임의로 부여 (User, Seller)
        UserWithRoleDto user = authService.searchUserWithRoleByEmail(tokenSubject);
        if(user == null || user.getUser() == null) throw new IllegalAccessException("사용자가 존재하지 않습니다.");

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));

        return new User(Long.toString(user.getUser().getId()), null, authorities);
    }
}
