package org.chatchat.roomuser.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.chatchat.common.entity.BaseEntity;
import org.chatchat.room.domain.Room;
import org.chatchat.user.domain.User;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room_users")
public class RoomUser extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private Long inviter;

    @Column
    private int unreadCount; // 채팅방별 읽지 않은 메세지 갯수

    @Column
    private String lastReadMessageId;  // 마지막으로 읽은 메시지의 ID

    public void incrementCount() {
        this.unreadCount += 1;
    }

    public void resetCount() {
        this.unreadCount = 0;
    }

    public void updateLastReadMessageId(String messageId) {
        this.lastReadMessageId = messageId;
    }

    public RoomUser(Room room, User user, Long inviter, int unreadCount) {
        this.room = room;
        this.user = user;
        this.inviter = inviter;
        this.unreadCount = unreadCount;
    }
}