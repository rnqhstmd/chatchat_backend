package org.chatchat.kafka.domain;

import lombok.*;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.domain.MessageType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessage implements Serializable {

    private String id;
    private String type;
    private String roomId;
    private String senderName;
    private String content;
    private LocalDateTime sentAt;

    // Kafka 메시지를 MongoDB 저장용 ChatMessage 객체로 변환
    public ChatMessage toChatMessage() {
        return ChatMessage.builder()
                .id(this.id)
                .type(MessageType.valueOf(this.type))
                .roomId(this.roomId)
                .sender(this.senderName)
                .content(this.content)
                .sentAt(this.sentAt)
                .build();
    }
}
