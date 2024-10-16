package org.chatchat.chatmessage.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.domain.MessageType;
import org.chatchat.common.exception.InternalServerException;
import org.chatchat.common.exception.NotFoundException;
import org.chatchat.common.exception.UnauthorizedException;
import org.chatchat.common.exception.type.ErrorType;
import org.chatchat.security.jwt.provider.JwtProvider;
import org.chatchat.user.domain.User;
import org.chatchat.user.service.UserQueryService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final UserQueryService userQueryService;
    private final JwtProvider jwtProvider;


    // 웹소켓 연결 시 호출되는 메서드
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("New WebSocket connection established.");
        log.info("event.getMessage(): {}", event.getMessage());
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap((Message<?>) event.getMessage().getHeaders().get("simpConnectMessage"));
        String jwtToken = headerAccessor.getFirstNativeHeader("Authorization");
        log.info("JWT Token 값: {}", jwtToken);
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);  // JWT 토큰 추출
            try {
                // 토큰의 유효성 검증
                jwtProvider.isValidToken(jwtToken);

                // JWT에서 사용자 정보 추출 및 인증 설정
                String userEmail = jwtProvider.getUserEmailFromToken(jwtToken);
                User user = userQueryService.findExistingUserByEmail(userEmail);

                if (user == null && user.getUsername() == null) {
                    throw new NotFoundException(ErrorType.USER_NOT_FOUND_ERROR);
                }
                // 사용자 정보 저장
                headerAccessor.getSessionAttributes().put("username", user.getUsername());
                log.info("Authenticated WebSocket connection for user: {}", user.getUsername());
            } catch (UnauthorizedException u) {
                SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create(SimpMessageType.DISCONNECT);
                accessor.setSessionId(headerAccessor.getSessionId());
                messagingTemplate.convertAndSend("/app/disconnect", "Invalid token, closing connection.");
                throw new UnauthorizedException(ErrorType.TOKEN_MALFORMED_ERROR, u.getMessage());
            } catch (Exception e) {
                throw new InternalServerException(ErrorType.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    // 웹소켓 연결 해제 시 호출되는 메서드
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        Long roomId = (Long) headerAccessor.getSessionAttributes().get("roomId");

        if (username == null) {
            throw new UnauthorizedException(ErrorType.NO_AUTHORIZATION_ERROR);
        }

        log.info("User Disconnected: {}", username);
        // 연결 해제 시 사용자 퇴장 메시지 전송
        ChatMessage chatMessage = ChatMessage.builder()
                .type(MessageType.LEAVE)
                .roomId(String.valueOf(roomId))
                .sender(username)
                .content(username + " has left the chat")
                .build();

        messagingTemplate.convertAndSend("/topic/public", chatMessage);
    }
}
