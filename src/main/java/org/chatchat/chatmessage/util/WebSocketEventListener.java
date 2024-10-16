package org.chatchat.chatmessage.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.domain.MessageType;
import org.chatchat.common.exception.InternalServerException;
import org.chatchat.common.exception.UnauthorizedException;
import org.chatchat.common.exception.type.ErrorType;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    // 웹소켓 연결 시 호출되는 메서드
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("New WebSocket connection established.");
        log.info("event.getMessage(): {}", event.getMessage());
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap((Message<?>) event.getMessage().getHeaders().get("simpConnectMessage"));
        try {
            String username = (String) headerAccessor.getSessionAttributes().get("username");
            if (username == null) {
                throw new UnauthorizedException(ErrorType.NO_AUTHORIZATION_ERROR, "세션에 username 이 비어있습니다.");
            }
        } catch (Exception e) {
            throw new InternalServerException(ErrorType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // 웹소켓 연결 해제 시 호출되는 메서드
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap((Message<?>) event.getMessage().getHeaders().get("simpConnectMessage"));
        try {
            String username = (String) headerAccessor.getSessionAttributes().get("username");
            if (username == null) {
                throw new UnauthorizedException(ErrorType.NO_AUTHORIZATION_ERROR, "세션에 username 이 비어있습니다.");
            }
            Long roomId = (Long) headerAccessor.getSessionAttributes().get("roomId");

            log.info("User Disconnected: {}", username);
            ChatMessage chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .roomId(String.valueOf(roomId))
                    .sender(username)
                    .content(username + " 님의 연결이 종료되었습니다.")
                    .build();
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        } catch (Exception e) {
            throw new InternalServerException(ErrorType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
