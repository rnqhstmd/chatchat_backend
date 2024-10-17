package org.chatchat.chatmessage.domain.repository;

import org.chatchat.chatmessage.domain.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByRoomIdOrderBySentAtDesc(String roomId);
}
