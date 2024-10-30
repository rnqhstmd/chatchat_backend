package org.chatchat.chatmessage.domain.repository;

import org.chatchat.chatmessage.domain.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    Page<ChatMessage> findByRoomIdOrderBySentAtDesc(String roomId, Pageable pageable);

    @Query(value = "{ 'roomId' : ?0 }", sort = "{ 'sentAt' : -1 }")
    List<ChatMessage> findLatestMessageByRoomId(String roomId, Pageable pageable);
}
