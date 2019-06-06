package org.multimodule.api.security;

import java.time.Instant;
import java.util.Date;

import org.apache.log4j.Logger;
import org.multimodule.common.entity.CustomUserDetails;
import org.multimodule.common.exception.InvalidTokenRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

    private static final Logger logger = Logger.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private Long jwtExpirationInMs;

    @Value("${app.jwt.claims.refresh.name}")
    private String jwtClaimRefreshName;

    /**
     * principal object 에서 토큰을 생성 후, JWT에 새로 고친 토큰을 삽입함
     * 즉, 새로운 JWT를 만들 수 있게 함.
     */
    public String generateToken(CustomUserDetails customUserDetails) {
        Instant expiryDate = Instant.now().plusMillis(jwtExpirationInMs);
        return Jwts.builder()
                .setSubject(Long.toString(customUserDetails.getId()))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * principal object 에서 토큰을 생성 후, JWT에 새로 고친 토큰을 삽입함
     * 즉, 새로운 JWT를 만들 수 있게 함.
     */
    public String generateTokenFromUserId(Long userId) {
        Instant expiryDate = Instant.now().plusMillis(jwtExpirationInMs);
        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    /**
     * 토큰에 캡슐화된 사용자 ID를 반환함
     */
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    /**
     * 토큰의 유효성을 체크함. (서명 유효성, 토큰 유효기간 만료여부, 토큰 지원 여부)
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
            throw new InvalidTokenRequestException("JWT", authToken, "올바르지 않은 JWT 서명입니다.");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
            throw new InvalidTokenRequestException("JWT", authToken, "잘못된 형태의 JWT 토큰입니다.");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
            throw new InvalidTokenRequestException("JWT", authToken, "토큰이 만료되었습니다. 새로고침이 필요합니다.");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
            throw new InvalidTokenRequestException("JWT", authToken, "지원하지 않은 JWT 토큰입니다.");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
            throw new InvalidTokenRequestException("JWT", authToken, "잘못된 argument token");
        }
    }

    /**
     * Return the jwt expiration for the client so that they can execute
     * the refresh token logic appropriately
     */
    public Long getExpiryDuration() {
        return jwtExpirationInMs;
    }
}