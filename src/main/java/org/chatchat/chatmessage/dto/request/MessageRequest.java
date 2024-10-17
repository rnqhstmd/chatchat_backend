package org.chatchat.chatmessage.dto.request;

public record MessageRequest(
        Long roomId,
        String message
) {
}
