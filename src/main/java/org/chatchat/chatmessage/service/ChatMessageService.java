package org.chatchat.chatmessage.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.domain.MessageType;
import org.chatchat.chatmessage.domain.repository.ChatMessageRepository;
import org.chatchat.chatmessage.dto.request.MessageRequest;
import org.chatchat.chatmessage.dto.response.MessageResponse;
import org.chatchat.room.domain.Room;
import org.chatchat.room.dto.request.InviteUserToRoomRequest;
import org.chatchat.room.dto.request.LeaveRoomRequest;
import org.chatchat.room.service.RoomQueryService;
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
    public MessageResponse saveTalkMessage(MessageRequest messageRequest, String username) {
        Room room = roomQueryService.findExistingRoomById(messageRequest.roomId());
        String content = messageRequest.message();

        return getMessageResponse(MessageType.TALK, room, username, content);
    }

    /**
     * 초대 메세지 저장
     */
    public MessageResponse saveInviteMessage(InviteUserToRoomRequest inviteUserToRoomRequest, String username) {
        Room room = roomQueryService.findExistingRoomById(inviteUserToRoomRequest.roomId());
        String content = username + "님이 " + inviteUserToRoomRequest.username() + "님을 초대했습니다.";
        String name = "시스템";

        return getMessageResponse(MessageType.SYSTEM, room, name, content);
    }

    /**
     * 나가기 메세지 저장
     */
    public MessageResponse saveLeaveMessage(LeaveRoomRequest leaveRoomRequest, String username) {
        Room room = roomQueryService.findExistingRoomById(leaveRoomRequest.roomId());
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
