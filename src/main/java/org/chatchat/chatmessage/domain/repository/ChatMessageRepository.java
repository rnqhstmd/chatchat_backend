package org.chatchat.chatmessage.domain.repository;

import org.chatchat.chatmessage.domain.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    Page<ChatMessage> findByRoomIdOrderBySentAtDesc(String roomId, Pageable pageable);
}
