package org.chatchat.security.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.chatchat.common.exception.ApiException;
import org.chatchat.common.exception.InternalServerException;
import org.chatchat.security.jwt.provider.JwtProvider;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static org.chatchat.common.exception.type.ErrorType.INTERNAL_SERVER_ERROR;
import static org.chatchat.security.cookie.CookieUtil.getCookieValue;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final AuthenticationFailureHandler failureHandler;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (isPublicUri(requestURI)) {
            // Public uri 일 경우 검증 안함
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwtToken = getCookieValue(request);
            jwtProvider.isValidToken(jwtToken);
            jwtProvider.getAuthenticationFromToken(jwtToken);
        } catch (ApiException e) {
            failureHandler.onAuthenticationFailure(request, response,
                    new InsufficientAuthenticationException(e.getMessage(), e));
            return;
        } catch (Exception e) {
            throw new InternalServerException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private boolean isPublicUri(final String requestURI) {
        return
                requestURI.startsWith("/swagger-ui/**") ||
                        requestURI.startsWith("/health") ||
                        requestURI.startsWith("/v3/api-docs/**") ||
                        requestURI.startsWith("/favicon.ico") ||
                        requestURI.startsWith("/error") ||
                        requestURI.startsWith("/ws/**") ||
                        requestURI.startsWith("/api/auth/sign-up") ||
                        requestURI.startsWith("/api/auth/login");
    }
}

