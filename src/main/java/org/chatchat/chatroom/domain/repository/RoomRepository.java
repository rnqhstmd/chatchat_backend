package org.chatchat.chatroom.domain.repository;

import org.chatchat.chatroom.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    // 방 이름 중복 체크를 위한 메서드
    boolean existsByName(String name);
}
