package org.chatchat.chatmessage.dto.response;

import org.chatchat.chatmessage.domain.ChatMessage;

import java.time.format.DateTimeFormatter;

public record MessageResponse(
        String id,
        String roomId,
        String sender,
        String content,
        String sentAt
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd HH:mm");

    public static MessageResponse from(ChatMessage message) {
        return new MessageResponse(
                message.getId(),
                message.getRoomId(),
                message.getSender(),
                message.getContent(),
                FORMATTER.format(message.getSentAt())
        );
    }
}
