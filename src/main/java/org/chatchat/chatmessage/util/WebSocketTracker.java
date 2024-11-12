package org.chatchat.chatmessage.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketTracker {

    private final StringRedisTemplate redisTemplate;

    // 사용자가 연결될 때
    public void userConnected() {
        redisTemplate.opsForValue().increment("websocket:connection:count");
    }

    // 사용자가 연결을 해제할 때
    public void userDisconnected() {
        redisTemplate.opsForValue().decrement("websocket:connection:count");
    }

    // 활성 연결이 있는지 확인
    public boolean hasActiveConnections() {
        String count = redisTemplate.opsForValue().get("websocket:connection:count");
        return count != null && Integer.parseInt(count) > 0;
    }
}