package com.wanted.cqrs.config.security;

import com.wanted.cqrs.apis.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
//public class SecurityConfig extends WebSecurityConfiguration {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final JwtUtils jwtUtils;
    private final AuthService authService;

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorizeRequests -> {
            authorizeRequests.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .requestMatchers("/api/auth/login").permitAll()
                    .anyRequest().permitAll();
        });

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.csrf(CsrfConfigurer::disable);
        http.sessionManagement(sessionManagement
                -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(exception -> {
           exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                   .accessDeniedHandler(jwtAccessDeniedHandler);
        });

        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtils, authService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("http://localhost");

        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
