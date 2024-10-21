package org.chatchat.room.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.chatchat.roomuser.domain.RoomUser;
import org.chatchat.common.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rooms")
public class Room extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column
    private LocalDateTime lastMessageTime;  // 최근 메시지 시간 필드 추가

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoomUser> users = new ArrayList<>();

    public Room(String name) {
        this.name = name;
    }

    public void updateLastMessageTime(LocalDateTime time) {
        this.lastMessageTime = time;
    }
}
