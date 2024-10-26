package org.chatchat.security.jwt.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import javax.crypto.SecretKey;

import lombok.RequiredArgsConstructor;
import org.chatchat.common.exception.UnauthorizedException;
import org.chatchat.user.domain.User;
import org.chatchat.user.service.UserQueryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.chatchat.common.exception.type.ErrorType.TOKEN_EXPIRED_ERROR;
import static org.chatchat.common.exception.type.ErrorType.TOKEN_MALFORMED_ERROR;

@Component
@RequiredArgsConstructor
public class JwtProvider {


    private final UserQueryService userQueryService;
    @Value("${security.jwt.token.access-secret-key}")
    private String key;
    @Value("${security.jwt.token.access-expire-length}")
    public int validityInMilliseconds;

    public String generateJwtToken(final String email) {
        Claims claims = createClaims(email);
        Date now = new Date();
        long expiredDate = calculateExpirationDate(now);
        SecretKey secretKey = generateKey();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(expiredDate))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT claims 생성
    private Claims createClaims(final String email) {
        return Jwts.claims().setSubject(String.valueOf(email));
    }

    // JWT 만료 시간 계산
    private long calculateExpirationDate(final Date now) {
        return now.getTime() + validityInMilliseconds;
    }

    // Key 생성
    private SecretKey generateKey() {
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰의 유효성 검사
    public void isValidToken(final String jwtToken) {
        try {
            SecretKey secretKey = generateKey();
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwtToken);

        } catch (ExpiredJwtException e) { // 어세스 토큰 만료
            throw new UnauthorizedException(TOKEN_EXPIRED_ERROR);
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            throw new UnauthorizedException(TOKEN_MALFORMED_ERROR, e.getMessage());
        }
    }

    // jwtToken 으로 Authentication 에 사용자 등록
    public void getAuthenticationFromToken(final String jwtToken) {
        User loginUser = getUserByToken(jwtToken);
        setContextHolder(jwtToken, loginUser);
    }

    // token 으로부터 유저 정보 확인
    private User getUserByToken(final String jwtToken) {
        String userEmail = getUserEmailFromToken(jwtToken);
        return userQueryService.findExistingUserByEmail(userEmail);
    }

    private void setContextHolder(String jwtToken, User loginUser) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, jwtToken, Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    // 토큰에서 user email 얻기
    public String getUserEmailFromToken(final String jwtToken) {
        SecretKey secretKey = generateKey();

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();

        return claims.getSubject();
    }
}
