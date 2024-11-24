package org.chatchat.notification.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class EmitterService {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private static final Long TIMEOUT = 60L * 1000 * 60; // 1시간

    public SseEmitter subscribe(String userId) {
        SseEmitter sseEmitter = new SseEmitter(TIMEOUT);

        sseEmitter.onCompletion(() -> emitters.remove(userId));
        sseEmitter.onTimeout(() -> emitters.remove(userId));

        emitters.put(userId, sseEmitter);

        try {
            sseEmitter.send(SseEmitter.event().name("connect").data("connected!"));

        } catch (IOException e) {
            emitters.remove(userId);
        }

        return sseEmitter;
    }

    public void notify(String userId, Object event) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(event));
            } catch (IOException e) {
                emitters.remove(userId);
                log.error("Failed to send notification to user: " + userId, e);
            }
        }
    }

    public void removeEmitter(String userId) {
        emitters.remove(userId);
    }
}
