package org.chatchat.chatmessage.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final AtomicInteger totalSubscribers = new AtomicInteger(0);

    /**
     * key : name
     * value : sessionId
     */
    @Getter
    private final Map<String, Set<String>> sessionMap = new ConcurrentHashMap<>();

    @EventListener
    public void handleConnectEvent(SessionConnectEvent event) {
        totalSubscribers.incrementAndGet();
        notifyTotalSubscriberCountChanged();

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String nickname = headerAccessor.getFirstNativeHeader("name");

        sessionMap.computeIfAbsent(nickname, k -> ConcurrentHashMap.newKeySet()).add(sessionId);

        log.info("[ {} ][ 세션 연결 ] - nickname: {}", sessionId, nickname);
        simpMessageSendingOperations.convertAndSend("/sub/user-list", sessionMap);
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        totalSubscribers.decrementAndGet();
        notifyTotalSubscriberCountChanged();

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        sessionMap.values().forEach(sessions -> sessions.remove(sessionId));

        log.info("[ {} ][ 세션 연결 종료 ]", sessionId);
        simpMessageSendingOperations.convertAndSend("/sub/user-list", sessionMap);
    }

    public int getTotalSubscriberCount() {
        return totalSubscribers.get();
    }

    private void notifyTotalSubscriberCountChanged() {
        int count = getTotalSubscriberCount();
        simpMessageSendingOperations.convertAndSend("/sub/user-count", count);
    }

    public void broadcastMessage(Long roomId, ChatMessage chatMessage) {
        simpMessageSendingOperations.convertAndSend("/sub/room" + roomId, chatMessage);
    }
}
