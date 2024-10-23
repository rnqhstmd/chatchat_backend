package org.chatchat.chatmessage.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WebSocketTracker {

    private AtomicInteger connectionCount = new AtomicInteger(0);

    public void userConnected() {
        connectionCount.incrementAndGet();
    }

    public void userDisconnected() {
        connectionCount.decrementAndGet();
    }

    public boolean hasActiveConnections() {
        return connectionCount.get() > 0;
    }
}