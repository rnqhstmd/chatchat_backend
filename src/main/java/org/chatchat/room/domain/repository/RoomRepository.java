package org.chatchat.room.domain.repository;

import org.chatchat.room.domain.Room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {
    // 방 이름 중복 체크를 위한 메서드
    boolean existsByName(String name);

    // 참여 중인 채팅방 전체 조회
    @Query("SELECT r FROM Room r JOIN r.users u WHERE u.user.id = :userId")
    Page<Room> findByUserId(@Param("userId") Long userId, Pageable pageable);
}
