package org.chatchat.chatmessage.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.domain.MessageType;
import org.chatchat.chatmessage.domain.repository.ChatMessageRepository;
import org.chatchat.chatmessage.dto.request.MessageRequest;
import org.chatchat.chatmessage.dto.response.MessageResponse;
import org.chatchat.chatroom.domain.Room;
import org.chatchat.chatroom.dto.request.JoinRoomRequest;
import org.chatchat.chatroom.service.RoomQueryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final RoomQueryService roomQueryService;

    /**
     * 채팅 메세지 저장
     */
    public MessageResponse saveMessage(MessageRequest messageRequest, MessageType type, String username) {
        Room room = roomQueryService.findExistingRoomById(messageRequest.roomId());
        String content = messageRequest.message();

        ChatMessage chatMessage = ChatMessage.builder()
                .type(type)
                .roomId(String.valueOf(room.getId()))
                .sender(username)
                .content(content)
                .sentAt(LocalDateTime.now())
                .build();
        ChatMessage saveMessage = chatMessageRepository.save(chatMessage);

        return MessageResponse.from(saveMessage);
    }

    /**
     * 채팅 참가 메세지 저장
     */
    public MessageResponse joinMessage(JoinRoomRequest joinRoomRequest, MessageType type, String username) {
        Room room = roomQueryService.findExistingRoomById(joinRoomRequest.roomId());

        ChatMessage chatMessage = ChatMessage.builder()
                .type(type)
                .roomId(String.valueOf(room.getId()))
                .sender(username)
                .content(username + "님이 입장하였습니다.")
                .sentAt(LocalDateTime.now())
                .build();
        ChatMessage saveMessage = chatMessageRepository.save(chatMessage);

        return MessageResponse.from(saveMessage);
    }
}
