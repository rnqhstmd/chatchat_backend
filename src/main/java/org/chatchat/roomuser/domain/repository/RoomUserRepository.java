package org.chatchat.roomuser.domain.repository;

import org.chatchat.roomuser.domain.RoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomUserRepository extends JpaRepository<RoomUser, Long> {

    boolean existsByRoomIdAndUserId(Long roomId, Long userId);

    Optional<RoomUser> findByRoomIdAndUserId(Long roomId, Long userId);

    List<RoomUser> findByRoomId(Long roomId);

    @Query("SELECT COUNT(ru) FROM RoomUser ru WHERE ru.room.id = :roomId AND ru.lastReadMessageId < :messageId")
    Long countUnreadUsers(@Param("roomId") Long roomId, @Param("messageId") String messageId);
}
