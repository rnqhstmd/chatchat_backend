package org.chatchat.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.ZonedDateTime;

@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;

    private String chatRoomId;
    private String senderId;
    private String content;
    private ZonedDateTime sentAt;

}
