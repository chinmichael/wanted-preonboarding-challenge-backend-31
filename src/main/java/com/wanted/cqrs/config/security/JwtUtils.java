package com.wanted.cqrs.config.security;

import com.wanted.cqrs.common.auth.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    private final int accessExpire;
    private final int refreshExpire;
    private final String secretKey;

    public JwtUtils(@Value("${com.wanted.cqrs.jwt.access-expire-milli:0}") final int accessExpire,
                    @Value("${com.wanted.cqrs.jwt.refresh-expire-milli:0}") final int refreshExpire,
                    @Value("${com.wanted.cqrs.jwt.secret-key:}") final String secretKey) {
        this.accessExpire = accessExpire;
        this.refreshExpire = refreshExpire;
        this.secretKey = secretKey;
    }

    /**
     * jwt 생성 (userId, role 기준 생성)
     * @param userId
     * @param roleArr
     * @return
     */
    public String createAccessJwt(String userId, String[] roleArr) {
        return this.accessTokenBuilderWithoutExpireSet(userId, roleArr)
                .setExpiration(new Date(System.currentTimeMillis() + accessExpire)) //만료일
                .compact();
    }

    /**
     * 토큰 셋 생성
     * @param userId
     * @param roleArr
     * @return
     */
    public TokenDto generateTokenSet(String userId, String[] roleArr) {
        Date accessExpireDate = new Date(System.currentTimeMillis() + accessExpire);

        String accessToken = this.accessTokenBuilderWithoutExpireSet(userId, roleArr)
                .setExpiration(accessExpireDate) //만료일
                .compact();
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpire))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenDto.builder()
                .accessExpiresIn(accessExpire)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private JwtBuilder accessTokenBuilderWithoutExpireSet(String userId, String[] roleArr) {
        //token Header 생성
        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        //token Claims 생성
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("roles", roleArr);

        return Jwts.builder().setHeader(headerMap)
                .setClaims(claims)
                .setSubject(userId) // 유효검증용 subject 설정
                .setIssuedAt(new Date(System.currentTimeMillis())) //생성일
                .signWith(SignatureAlgorithm.HS256, secretKey);
    }

    /**
     * jwt subject (userId) 조회
     * @param token
     * @return
     */
    public String getJwtSubject(String token) {
        if(StringUtils.hasText(token)) return this.getClaimFromToken(token, Claims::getSubject);
        else return null;
    }

    /**
     * jwt claims 내 roles 조회
     * @param token
     * @return
     */
    public String[] getJwtRoles(String token) {
        if(!StringUtils.hasText(token)) return new String[0];

        Map<String, Object> claims = this.getAllClaimsFromToken(token);
        String[] result = (String[]) claims.get("roles");
        return result;
    }

    /**
     * 토큰 만료여부 확인
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        final Date expiration = this.getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }
}
