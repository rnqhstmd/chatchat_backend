package org.chatchat.chatmessage.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    private Set<String> readByUsers = new HashSet<>();  // 메시지를 읽은 사용자의 ID 목록

    public ChatMessage(MessageType type, String roomId, String sender, String content, LocalDateTime sentAt) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.content = content;
        this.sentAt = sentAt;
    }

    public void markAsRead(String userId) {
        this.readByUsers.add(userId);
    }
}
