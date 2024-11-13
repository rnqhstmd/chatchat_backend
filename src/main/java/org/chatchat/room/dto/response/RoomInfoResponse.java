package org.chatchat.room.dto.response;

public record RoomInfoResponse(
        Long roomId,
        String name,
        int unreadCount
) {
}
