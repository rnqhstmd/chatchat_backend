package org.chatchat.chatmessage.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.domain.MessageType;
import org.chatchat.chatmessage.domain.repository.ChatMessageRepository;
import org.chatchat.chatmessage.dto.request.MessageRequest;
import org.chatchat.chatmessage.dto.response.MessageResponse;
import org.chatchat.room.domain.Room;
import org.chatchat.room.dto.request.InviteUserToRoomRequest;
import org.chatchat.room.dto.request.QuitRoomRequest;
import org.chatchat.room.service.RoomQueryService;
import org.chatchat.roomuser.service.RoomUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final RoomQueryService roomQueryService;
    private final RoomUserService roomUserService;

    /**
     * 채팅 메세지 저장
     */
    public MessageResponse saveTalkMessage(MessageRequest messageRequest, String username) {
        Room room = roomQueryService.findExistingRoomByRoomId(messageRequest.roomId());
        String content = messageRequest.message();
        LocalDateTime now = LocalDateTime.now();
        room.updateLastMessageTime(now);
        roomUserService.increaseUnreadMessageCount(room.getId(), username);

        return getMessageResponse(MessageType.TALK, room, username, content);
    }

    /**
     * 초대 메세지 저장
     */
    public MessageResponse saveInviteMessage(InviteUserToRoomRequest inviteUserToRoomRequest, String username) {
        Room room = roomQueryService.findExistingRoomByRoomId(inviteUserToRoomRequest.roomId());
        String content = username + "님이 " + inviteUserToRoomRequest.username() + "님을 초대했습니다.";
        String name = "시스템";

        return getMessageResponse(MessageType.SYSTEM, room, name, content);
    }

    /**
     * 나가기 메세지 저장
     */
    public MessageResponse saveQuitMessage(QuitRoomRequest quitRoomRequest, String username) {
        Room room = roomQueryService.findExistingRoomByRoomId(quitRoomRequest.roomId());
        String content = username + "님이 나갔습니다.";
        String name = "시스템";

        return getMessageResponse(MessageType.SYSTEM, room, name, content);
    }

    private MessageResponse getMessageResponse(MessageType system, Room room, String senderName, String content) {
        ChatMessage chatMessage = ChatMessage.builder()
                .type(system)
                .roomId(String.valueOf(room.getId()))
                .sender(senderName)
                .content(content)
                .sentAt(LocalDateTime.now())
                .build();
        ChatMessage saveMessage = chatMessageRepository.save(chatMessage);

        return MessageResponse.from(saveMessage);
    }
}
