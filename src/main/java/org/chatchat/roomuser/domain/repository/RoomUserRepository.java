package org.chatchat.roomuser.domain.repository;

import org.chatchat.roomuser.domain.RoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomUserRepository extends JpaRepository<RoomUser, Long> {

    boolean existsByRoomIdAndUserId(Long roomId, Long userId);

    Optional<RoomUser> findByRoomIdAndUserId(Long roomId, Long userId);
}
