package org.chatchat.notification.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chatchat.common.exception.InternalServerException;
import org.chatchat.common.exception.type.ErrorType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.chatchat.common.exception.type.ErrorType.SSE_CONNECTION_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmitterService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS_KEY_PREFIX = "emitter:";
    private static final String EVENT_NAME = "notification";
    private static final Long TIMEOUT = 60L * 1000 * 60; // 1시간

    public SseEmitter subscribe(String userId) {
        removeEmitter(userId); // 기존 연결이 있다면 제거
        SseEmitter sseEmitter = new SseEmitter(TIMEOUT);

        try {
            // Redis에 사용자 정보를 저장하기 전에 연결 확인 메시지 전송
            sseEmitter.send(SseEmitter.event()
                    .id(String.valueOf(System.currentTimeMillis()))
                    .name("connect")
                    .data("connected!"));

            // Redis에 사용자 정보를 저장
            redisTemplate.opsForValue().set(
                    REDIS_KEY_PREFIX + userId,
                    sseEmitter,
                    TIMEOUT,
                    TimeUnit.MILLISECONDS
            );

            // 이벤트 핸들러 등록
            sseEmitter.onCompletion(() -> {
                log.info("SSE Connection completed for user: {}", userId);
                removeEmitter(userId);
            });
            sseEmitter.onTimeout(() -> {
                log.info("SSE Connection timeout for user: {}", userId);
                removeEmitter(userId);
            });
            sseEmitter.onError((e) -> {
                log.error("SSE Connection error for user: {}", userId, e);
                removeEmitter(userId);
            });

        } catch (IOException e) {
            log.error("Error while setting up SSE connection for user: {}", userId, e);
            removeEmitter(userId);
            throw new InternalServerException(SSE_CONNECTION_ERROR, e.toString());
        }

        return sseEmitter;
    }

    public void notify(String userId, Object event) {
        SseEmitter emitter = (SseEmitter) redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + userId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(System.currentTimeMillis()))
                        .name(EVENT_NAME)
                        .data(event));

            } catch (IOException e) {
                log.error("Failed to send notification to user: {}", userId, e);
                removeEmitter(userId);
            }
        }
    }

    public void removeEmitter(String userId) {
        String key = REDIS_KEY_PREFIX + userId;
        Boolean deleted = redisTemplate.delete(key);
        if (Boolean.TRUE.equals(deleted)) {
            log.debug("Successfully removed emitter for user: {}", userId);
        }
    }
}