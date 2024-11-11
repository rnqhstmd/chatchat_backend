package org.chatchat.chatmessage.dto.response;

import lombok.Builder;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.kafka.domain.KafkaChatMessage;

import java.time.format.DateTimeFormatter;

@Builder
public record MessageResponse(
        String id,
        String roomId,
        String sender,
        String content,
        String sentAt,
        int unreadCount // 읽지 않은 메시지 수 필드 추가
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static MessageResponse fromChatMessage(ChatMessage message, int unreadCount) {
        return new MessageResponse(
                message.getId(),
                message.getRoomId(),
                message.getSender(),
                message.getContent(),
                FORMATTER.format(message.getSentAt()),
                unreadCount
        );
    }

    public static MessageResponse fromKafkaMessage(KafkaChatMessage message) {
        return MessageResponse.builder()
                .id(message.getId())
                .roomId(message.getRoomId())
                .sender(message.getSenderName())
                .content(message.getContent())
                .sentAt(String.valueOf(message.getSentAt()))
                .build();
    }
}
