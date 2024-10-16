package org.chatchat.chatmessage.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.domain.MessageType;
import org.chatchat.chatmessage.domain.repository.ChatMessageRepository;
import org.chatchat.chatmessage.dto.MessageRequest;
import org.chatchat.chatroom.domain.Room;
import org.chatchat.chatroom.dto.request.JoinRoomRequest;
import org.chatchat.chatroom.service.RoomQueryService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final RoomQueryService roomQueryService;

    public ChatMessage saveMessage(MessageRequest messageRequest, MessageType type, String username) {
        Room room = roomQueryService.findExistingRoomById(messageRequest.roomId());
        String content = messageRequest.message();

        ChatMessage chatMessage = ChatMessage.builder()
                .type(type)
                .roomId(String.valueOf(room.getId()))
                .sender(username)
                .content(content)
                .sentAt(LocalDateTime.now())
                .build();
        return chatMessageRepository.save(chatMessage);
    }

    public ChatMessage joinMessage(JoinRoomRequest joinRoomRequest, MessageType type, String username) {
        Room room = roomQueryService.findExistingRoomById(joinRoomRequest.roomId());

        ChatMessage chatMessage = ChatMessage.builder()
                .type(type)
                .roomId(String.valueOf(room.getId()))
                .sender(username)
                .content(username + "님이 입장하였습니다.")
                .sentAt(LocalDateTime.now())
                .build();
        return chatMessageRepository.save(chatMessage);
    }
}
