package org.chatchat.chatmessage.controller;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.domain.MessageType;
import org.chatchat.chatmessage.dto.MessageRequest;
import org.chatchat.chatmessage.service.ChatMessageService;
import org.chatchat.chatroom.dto.request.JoinRoomRequest;
import org.chatchat.common.exception.UnauthorizedException;
import org.chatchat.common.exception.type.ErrorType;
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
    public void sendMessage(@Payload MessageRequest messageRequest, SimpMessageHeaderAccessor headerAccessor) {
        String username = extractUsername(headerAccessor);
        ChatMessage chatMessage = chatMessageService.saveMessage(messageRequest, MessageType.TALK, username);
        messagingTemplate.convertAndSend("/topic/room." + messageRequest.roomId(), chatMessage);
    }

    @MessageMapping("/chat.joinRoom")
    public void addUser(@Payload JoinRoomRequest joinRoomRequest,
                        SimpMessageHeaderAccessor headerAccessor) {
        String username = extractUsername(headerAccessor);
        ChatMessage joinMessage = chatMessageService.joinMessage(joinRoomRequest, MessageType.ENTER, username);
        messagingTemplate.convertAndSend("/topic/room." + joinRoomRequest.roomId(), joinMessage);
    }

    private String extractUsername(SimpMessageHeaderAccessor headerAccessor) {
        Object usernameObject = headerAccessor.getSessionAttributes().get("username");
        if (usernameObject == null) {
            throw new UnauthorizedException(ErrorType.NO_AUTHORIZATION_ERROR);
        }
        return (String) usernameObject;
    }
}