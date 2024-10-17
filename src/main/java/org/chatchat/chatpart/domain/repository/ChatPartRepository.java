package org.chatchat.chatpart.domain.repository;

import org.chatchat.chatpart.domain.ChatPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatPartRepository extends JpaRepository<ChatPart, Long> {

    boolean existsByRoomIdAndUserId(Long roomId, Long userId);

    Optional<ChatPart> findByRoomIdAndUserId(Long roomId, Long userId);
}
