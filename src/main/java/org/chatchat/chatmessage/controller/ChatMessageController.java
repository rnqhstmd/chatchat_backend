package org.chatchat.chatmessage.controller;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatmessage.dto.request.MessageRequest;
import org.chatchat.chatmessage.dto.response.MessageResponse;
import org.chatchat.chatmessage.service.ChatMessageService;
import org.chatchat.chatroom.dto.request.InviteUserToRoomRequest;
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
        MessageResponse messageResponse = chatMessageService.saveMessage(messageRequest, username);
        messagingTemplate.convertAndSend("/topic/room." + messageRequest.roomId(), messageResponse);
    }

    @MessageMapping("/chat.sendInviteMessage")
    public void sendInviteMessage(@Payload InviteUserToRoomRequest inviteUserToRoomRequest, SimpMessageHeaderAccessor headerAccessor) {
        String username = extractUsername(headerAccessor);
        MessageResponse messageResponse = chatMessageService.sendSystemMessage(inviteUserToRoomRequest, username);
        messagingTemplate.convertAndSend("/topic/room." + inviteUserToRoomRequest.roomId(), messageResponse);
    }

    private String extractUsername(SimpMessageHeaderAccessor headerAccessor) {
        Object usernameObject = headerAccessor.getSessionAttributes().get("username");
        if (usernameObject == null) {
            throw new UnauthorizedException(ErrorType.NO_AUTHORIZATION_ERROR);
        }
        return (String) usernameObject;
    }
}