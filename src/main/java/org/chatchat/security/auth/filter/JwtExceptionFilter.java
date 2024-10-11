package org.chatchat.security.auth.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.chatchat.common.exception.ApiException;
import org.chatchat.common.exception.type.ErrorType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    private static final String JWT_TOKEN_EXCEPTION = "JwtTokenException";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (SecurityException e) {
            log.error("FilterException throw SecurityException Exception : {}", e.getMessage());
            request.setAttribute(JWT_TOKEN_EXCEPTION, ErrorType.NO_AUTHORIZATION_ERROR);
            filterChain.doFilter(request, response);
        } catch (MalformedJwtException e) {
            log.error("FilterException throw MalformedJwtException Exception : {}", e.getMessage());
            request.setAttribute(JWT_TOKEN_EXCEPTION, ErrorType.TOKEN_MALFORMED_ERROR);
            filterChain.doFilter(request, response);
        } catch (IllegalArgumentException e) {
            log.error("FilterException throw IllegalArgumentException Exception : {}", e.getMessage());
            request.setAttribute(JWT_TOKEN_EXCEPTION, ErrorType.TOKEN_TYPE_ERROR);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("FilterException throw ExpiredJwtException Exception : {}", e.getMessage());
            request.setAttribute(JWT_TOKEN_EXCEPTION, ErrorType.TOKEN_EXPIRED_ERROR);
            filterChain.doFilter(request, response);
        } catch (UnsupportedJwtException e) {
            log.error("FilterException throw UnsupportedJwtException Exception : {}", e.getMessage());
            request.setAttribute(JWT_TOKEN_EXCEPTION, ErrorType.TOKEN_UNSUPPORTED_ERROR);
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            log.error("FilterException throw JwtException Exception : {}", e.getMessage());
            request.setAttribute(JWT_TOKEN_EXCEPTION, ErrorType.TOKEN_UNKNOWN_ERROR);
            filterChain.doFilter(request, response);
        } catch (ApiException e) {
            log.error("FilterException throw Exception Exception : {}", e.getMessage());
            request.setAttribute(JWT_TOKEN_EXCEPTION, e.getErrorType());
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("FilterException throw Exception Exception : {}", e.getMessage());
            request.setAttribute(JWT_TOKEN_EXCEPTION,ErrorType.INTERNAL_SERVER_ERROR);
            filterChain.doFilter(request, response);
        }
    }
}