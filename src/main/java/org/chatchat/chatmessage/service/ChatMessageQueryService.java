package org.chatchat.chatmessage.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.domain.repository.ChatMessageRepository;
import org.chatchat.chatmessage.dto.response.MessageResponse;
import org.chatchat.chatpart.service.ChatPartQueryService;
import org.chatchat.common.page.dto.request.PageRequestDto;
import org.chatchat.common.page.dto.response.PageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public PageResponseDto<MessageResponse> loadMessagesByRoomId(Long roomId, Long userId, int page) {
        chatPartQueryService.isUserMemberOfRoom(roomId, userId);

        PageRequestDto pageRequestDto = new PageRequestDto(page);
        Pageable pageable = pageRequestDto.toMessagePageable();

        Page<ChatMessage> messagePage = chatMessageRepository.findByRoomIdOrderBySentAtDesc(String.valueOf(roomId), pageable);
        List<MessageResponse> messageResponses = messagePage.getContent()
                .stream()
                .map(MessageResponse::from)
                .toList();

        return PageResponseDto.of(messageResponses, messagePage.getNumber(), messagePage.getTotalPages());
    }
}
