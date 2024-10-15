package org.chatchat.chatmessage.dto;

public record MessageRequest(
        Long roomId,
        String message
) {
}
