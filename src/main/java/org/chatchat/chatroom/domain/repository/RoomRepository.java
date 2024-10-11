package org.chatchat.chatroom.domain.repository;

import org.chatchat.chatroom.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
