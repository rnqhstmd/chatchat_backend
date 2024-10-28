package org.chatchat.chatmessage.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "chat_messages")
@AllArgsConstructor
public class ChatMessage {

    @Id
    private String id;
    private MessageType type;
    @Indexed
    private String roomId;
    private String sender;
    private String content;
    private LocalDateTime sentAt;
    private int unreadCount;  // 읽지 않은 사람의 수

    public ChatMessage(MessageType type, String roomId, String sender, String content, LocalDateTime sentAt) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.content = content;
        this.sentAt = sentAt;
    }
}
