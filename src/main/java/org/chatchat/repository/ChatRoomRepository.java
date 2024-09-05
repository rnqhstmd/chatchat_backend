package org.chatchat.repository;

import org.chatchat.entity.ChatRoom;
import org.chatchat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
