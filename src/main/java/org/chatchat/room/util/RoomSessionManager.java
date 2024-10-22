package org.chatchat.room.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomSessionManager {

    private Map<Long, String> userSessionMap = new ConcurrentHashMap<>(); // 사용자 ID와 세션 ID 매핑
    private Map<String, String> sessionRoomMap = new ConcurrentHashMap<>(); // 세션 ID와 채팅방 ID 매핑

    public void mapUserSession(Long userId, String sessionId) {
        userSessionMap.put(userId, sessionId);
    }

    public void enterRoom(String sessionId, String roomId) {
        sessionRoomMap.put(sessionId, roomId);
    }

    public void leaveRoom(String sessionId) {
        sessionRoomMap.remove(sessionId);
        userSessionMap.values().remove(sessionId);
    }

    public String getCurrentRoom(String sessionId) {
        return sessionRoomMap.get(sessionId);
    }

    public String getSessionIdForUser(Long userId) {
        return userSessionMap.get(userId);
    }

    public boolean isSessionInRoom(String sessionId, String roomId) {
        return roomId.equals(sessionRoomMap.get(sessionId));
    }
}
