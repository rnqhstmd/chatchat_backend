package org.chatchat.chatmessage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.dto.MessageRequest;
import org.chatchat.chatmessage.service.ChatMessageService;
import org.chatchat.chatmessage.util.WebSocketEventListener;
import org.chatchat.security.auth.annotation.AuthUser;
import org.chatchat.user.domain.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final WebSocketEventListener webSocketEventListener;
    private final List<String> channelList = new CopyOnWriteArrayList<>();

    @SubscribeMapping("/user-count")
    public int getInitialUserCount() {
        return webSocketEventListener.getTotalSubscriberCount();
    }

    @SubscribeMapping("/user-list")
    public Map<String, Set<String>> getInitialUserList() {
        return webSocketEventListener.getSessionMap();
    }

    @MessageMapping("/channel-list")
    public void channel(String channelName) {
        if (!channelList.contains(channelName)) {
            channelList.add(channelName);
        }

        simpMessageSendingOperations.convertAndSend("/sub/room-list", channelList);
    }

    @SubscribeMapping("/channel-list")
    public List<String> getInitialChannelList() {
        return channelList;
    }

    @MessageMapping("/chat")
    public void sendMessage(@RequestBody MessageRequest messageRequest, @AuthUser User user) {
        ChatMessage chatMessage = chatMessageService.saveMessage(messageRequest, user);
        webSocketEventListener.broadcastMessage(messageRequest, chatMessage);
    }
}