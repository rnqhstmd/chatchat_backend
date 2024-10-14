package org.chatchat.chatmessage.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Builder
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;
    public enum MessageType {
        ENTER, TALK,
    }
    private MessageType type;
    private String roomId;
    private String sender;
    private String content;
    private Instant sentAt;

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
