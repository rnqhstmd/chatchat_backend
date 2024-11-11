package org.chatchat.chatmessage.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chatchat.common.exception.InternalServerException;
import org.chatchat.kafka.service.KafkaConsumerService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import static org.chatchat.common.exception.type.ErrorType.WEBSOCKET_DISCONNECTED_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final KafkaConsumerService kafkaConsumerService;
    private final WebSocketTracker webSocketTracker;

    // 웹소켓 연결 시 호출되는 메서드
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap((Message<?>) event.getMessage().getHeaders().get("simpConnectMessage"));
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        log.info("새로운 웹소켓 연결이 있습니다. 유저 : {}", username);

        // 사용자 추가
        webSocketTracker.userConnected();

        // 웹소켓 연결 시 사용자가 아무도 없을 경우
        if (!webSocketTracker.hasActiveConnections()) {
            // 카프카 컨슈머로서 구독 시작
            kafkaConsumerService.startListening();
        }
    }

    // 웹소켓 연결 해제 시 호출되는 메서드
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap((Message<?>) event.getMessage().getHeaders().get("simpConnectMessage"));
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String sessionId = headers.getSessionId();

        // 연결이 비정상적으로 끊어진 경우
        if (event.getCloseStatus() != null) {
            log.error("웹소켓 연결이 비정상적으로 종료되었습니다. 세션 ID: {}, 유저 이름 : {}, 종료 코드: {}, 이유: {}",
                    sessionId, username, event.getCloseStatus().getCode(), event.getCloseStatus().getReason());
            throw new InternalServerException(WEBSOCKET_DISCONNECTED_ERROR);
        }

        // 웹소켓 종료 시 사용자 제거
        webSocketTracker.userDisconnected();
        // 웹소켓 연결한 사용자가 아무도 없을 경우
        if (!webSocketTracker.hasActiveConnections()) {
            // 카프카 구독 종료
            kafkaConsumerService.stopListening();
        }
        log.info("웹소켓 연결이 종료되었습니다. 유저 : {}", username);
    }
}
