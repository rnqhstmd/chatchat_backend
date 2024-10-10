package org.chatchat.chatroom.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.chatchat.chatpart.domain.ChatPart;
import org.chatchat.common.entity.BaseEntity;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rooms")
public class Room extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "Room")
    private List<ChatPart> users;

    public Room(String name) {
        this.name = name;
    }
}
