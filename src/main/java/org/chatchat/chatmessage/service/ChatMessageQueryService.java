package org.chatchat.chatmessage.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.domain.repository.ChatMessageRepository;
import org.chatchat.chatmessage.dto.response.MessageResponse;
import org.chatchat.chatpart.service.ChatPartQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageQueryService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatPartQueryService chatPartQueryService;

    /**
     * 이전 메세지 불러오기
     */
    public List<MessageResponse> loadMessagesByRoomId(Long roomId, Long userId) {
        chatPartQueryService.validateUserRoomMember(roomId, userId);

        List<ChatMessage> chatMessageList = chatMessageRepository.findByRoomIdOrderBySentAtDesc(String.valueOf(roomId));
        return chatMessageList.stream()
                .map(MessageResponse::from)
                .toList();
    }
}
