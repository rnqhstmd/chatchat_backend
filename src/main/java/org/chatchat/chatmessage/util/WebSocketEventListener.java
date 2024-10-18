package org.chatchat.chatmessage.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chatchat.common.exception.InternalServerException;
import org.chatchat.common.exception.UnauthorizedException;
import org.chatchat.common.exception.type.ErrorType;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    // 웹소켓 연결 시 호출되는 메서드
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("새로운 웹소켓 연결이 있습니다.");
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
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        Long roomId = (Long) headerAccessor.getSessionAttributes().get("roomId");
        if (username != null) {
            log.info(username + "님이 " + roomId + "번 방의 소켓 연결을 끊었습니다.");
        } else {
            log.info("세션에 username 이 비어있는 유저의 웹소켓 연결이 끊어졌습니다.");
        }
    }
}
