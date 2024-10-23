package org.chatchat.chatmessage.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chatchat.kafka.service.KafkaConsumerService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final KafkaConsumerService kafkaConsumerService;
    private final WebSocketTracker webSocketTracker;

    // 웹소켓 연결 시 호출되는 메서드
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("새로운 웹소켓 연결이 있습니다.");
        // 웹소켓 연결 시 사용자가 아무도 없을 경우
        if (!webSocketTracker.hasActiveConnections()) {
            // 카프카 컨슈머로서 구독 시작
            kafkaConsumerService.startListening();
        }
        // 사용자 추가
        webSocketTracker.userConnected();
    }

    // 웹소켓 연결 해제 시 호출되는 메서드
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // 웹소켓 종료 시 사용자 제거
        webSocketTracker.userDisconnected();
        // 웹소켓 연결한 사용자가 아무도 없을 경우
        if (!webSocketTracker.hasActiveConnections()) {
            // 카프카 구독 종료
            kafkaConsumerService.stopListening();
        }
    }
}
