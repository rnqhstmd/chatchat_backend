package org.chatchat.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Getter
@Document(collection = "notification")
@AllArgsConstructor
public class Notification {
    @Id
    private String id;
    private String userId;
    private String sender;
    private String roomName;
    private String message;
    private LocalDateTime sentAt;

    public Notification(String userId, String sender, String roomName, String message, LocalDateTime sentAt) {
        this.userId = userId;
        this.sender = sender;
        this.roomName = roomName;
        this.message = message;
        this.sentAt = sentAt;
    }
}
