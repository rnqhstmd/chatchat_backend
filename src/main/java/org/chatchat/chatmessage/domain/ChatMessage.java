package org.chatchat.chatmessage.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Builder
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;
    private MessageType type;
    @Indexed
    private String roomId;
    private String sender;
    private String content;
    private LocalDateTime sentAt;
}
