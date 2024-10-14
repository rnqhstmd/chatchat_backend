package org.chatchat.chatmessage.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.domain.repository.ChatMessageRepository;
import org.chatchat.chatmessage.dto.MessageRequest;
import org.chatchat.chatroom.domain.Room;
import org.chatchat.chatroom.service.RoomQueryService;
import org.chatchat.user.domain.User;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final RoomQueryService roomQueryService;

    public ChatMessage saveMessage(MessageRequest messageRequest, User user) {
        Room room = roomQueryService.findExistingRoomById(Long.valueOf(messageRequest.room_id()));
        String content = messageRequest.message();
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(String.valueOf(room.getId()))
                .sender(String.valueOf(user.getUsername()))
                .content(content)
                .sentAt(Instant.now())
                .build();
        return chatMessageRepository.save(chatMessage);
    }
}
