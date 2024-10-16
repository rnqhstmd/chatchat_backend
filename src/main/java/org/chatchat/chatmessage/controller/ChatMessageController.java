package org.chatchat.chatmessage.controller;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.domain.MessageType;
import org.chatchat.chatmessage.dto.MessageRequest;
import org.chatchat.chatmessage.service.ChatMessageService;
import org.chatchat.chatroom.dto.request.JoinRoomRequest;
import org.chatchat.security.auth.annotation.AuthUser;
import org.chatchat.user.domain.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageRequest messageRequest, @AuthUser User user) {
        ChatMessage chatMessage = chatMessageService.saveMessage(messageRequest, MessageType.TALK, user);
        messagingTemplate.convertAndSend("/topic/room." + messageRequest.roomId(), chatMessage);
    }

    @MessageMapping("/chat.joinRoom")
    public void addUser(@Payload JoinRoomRequest joinRoomRequest,
                               SimpMessageHeaderAccessor headerAccessor,
                               @AuthUser User user) {
        // 세션에 방 ID와 사용자 정보 저장
        headerAccessor.getSessionAttributes().put("roomId", joinRoomRequest.roomId());
        headerAccessor.getSessionAttributes().put("username", user.getUsername());

        ChatMessage joinMessage = chatMessageService.joinMessage(joinRoomRequest, MessageType.ENTER, user);
        messagingTemplate.convertAndSend("/topic/room." + joinRoomRequest.roomId(), joinMessage);
    }
}