package org.chatchat.chatmessage.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chatchat.common.exception.InternalServerException;
import org.chatchat.common.exception.NotFoundException;
import org.chatchat.common.exception.UnauthorizedException;
import org.chatchat.common.exception.type.ErrorType;
import org.chatchat.security.jwt.provider.JwtProvider;
import org.chatchat.user.domain.User;
import org.chatchat.user.service.UserQueryService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketInterceptor extends HttpSessionHandshakeInterceptor {

    private final JwtProvider jwtProvider;
    private final UserQueryService userQueryService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        try {
            String jwtToken = extractTokenFromCookie(request);
            if (jwtToken == null) {
                throw new UnauthorizedException(ErrorType.TOKEN_NOT_INCLUDED_ERROR);
            }

            jwtProvider.isValidToken(jwtToken);
            String userEmail = jwtProvider.getUserEmailFromToken(jwtToken);
            User user = userQueryService.findExistingUserByEmail(userEmail);

            if (user == null) {
                throw new NotFoundException(ErrorType.USER_NOT_FOUND_ERROR);
            }

            attributes.put("username", user.getUsername());
            return true;
        } catch (Exception e) {
            throw new InternalServerException(ErrorType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private String extractTokenFromCookie(ServerHttpRequest request) {
        MultiValueMap<String, String> headers = request.getHeaders();
        List<String> cookies = headers.get(HttpHeaders.COOKIE);

        if (cookies != null) {
            for (String cookie : cookies) {
                String[] cookiePairs = cookie.split("; ");
                for (String cookiePair : cookiePairs) {
                    String[] keyValue = cookiePair.split("=");
                    if ("jwtToken".equals(keyValue[0]) && keyValue.length > 1) {
                        return keyValue[1];
                    }
                }
            }
        }
        return null;
    }
}
