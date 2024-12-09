package org.chatchat.notification.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.chatchat.common.entity.BaseEntity;
import org.chatchat.user.domain.User;

import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {

    @Column
    private String roomName;

    @Column
    private String message;

    @Column
    private LocalDateTime sentAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Notification(User user, String roomName, String message, LocalDateTime sentAt) {
        this.user = user;
        this.roomName = roomName;
        this.message = message;
        this.sentAt = sentAt;
    }
}
