package org.chatchat.chatmessage.dto.response;

import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.kafka.domain.KafkaChatMessage;

import java.time.format.DateTimeFormatter;

public record MessageResponse(
        String id,
        String roomId,
        String sender,
        String content,
        String sentAt
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static MessageResponse fromChatMessage(ChatMessage message) {
        return new MessageResponse(
                message.getId(),
                message.getRoomId(),
                message.getSender(),
                message.getContent(),
                FORMATTER.format(message.getSentAt())
        );
    }

    public static MessageResponse fromKafkaMessage(KafkaChatMessage message) {
        return new MessageResponse(
                message.getId(),
                message.getRoomId(),
                message.getSenderName(),
                message.getContent(),
                String.valueOf(message.getSentAt())
        );
    }
}
