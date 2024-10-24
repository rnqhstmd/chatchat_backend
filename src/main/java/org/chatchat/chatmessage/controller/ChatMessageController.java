package org.chatchat.chatmessage.controller;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatmessage.dto.request.MessageRequest;
import org.chatchat.chatmessage.service.ChatMessageService;
import org.chatchat.room.dto.request.InviteUserToRoomRequest;
import org.chatchat.room.dto.request.QuitRoomRequest;
import org.chatchat.common.exception.UnauthorizedException;
import org.chatchat.common.exception.type.ErrorType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat.sendMessage")
    public void sendTalkMessage(@Payload MessageRequest messageRequest,SimpMessageHeaderAccessor headerAccessor) {
        String username = extractUsername(headerAccessor);
        chatMessageService.saveAndSendChatMessage(messageRequest,username);
    }

    @MessageMapping("/chat.sendInviteMessage")
    public void sendInviteMessage(@Payload InviteUserToRoomRequest inviteUserToRoomRequest, SimpMessageHeaderAccessor headerAccessor) {
        String username = extractUsername(headerAccessor);
        chatMessageService.sendInviteMessage(inviteUserToRoomRequest,username);
    }

    @MessageMapping("/chat.sendQuitMessage")
    public void sendQuitMessage(@Payload QuitRoomRequest quitRoomRequest, SimpMessageHeaderAccessor headerAccessor) {
        String username = extractUsername(headerAccessor);
        chatMessageService.sendQuitMessage(quitRoomRequest,username);
    }

    private String extractUsername(SimpMessageHeaderAccessor headerAccessor) {
        Object usernameObject = headerAccessor.getSessionAttributes().get("username");
        if (usernameObject == null) {
            throw new UnauthorizedException(ErrorType.NO_AUTHORIZATION_ERROR);
        }
        return (String) usernameObject;
    }
}