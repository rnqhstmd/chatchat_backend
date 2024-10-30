package org.chatchat.chatmessage.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.domain.repository.ChatMessageRepository;
import org.chatchat.chatmessage.dto.response.MessageResponse;
import org.chatchat.common.exception.NotFoundException;
import org.chatchat.roomuser.service.RoomUserQueryService;
import org.chatchat.common.page.dto.request.PageRequestDto;
import org.chatchat.common.page.dto.response.PageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.chatchat.common.exception.type.ErrorType.CHAT_MESSAGE_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
public class ChatMessageQueryService {

    private final ChatMessageRepository chatMessageRepository;
    private final RoomUserQueryService roomUserQueryService;

    /**
     * 이전 메세지 불러오기
     */
    public PageResponseDto<MessageResponse> loadMessagesByRoomId(Long roomId, Long userId, int page) {
        roomUserQueryService.isUserMemberOfRoom(roomId, userId);

        PageRequestDto pageRequestDto = new PageRequestDto(page);
        Pageable pageable = pageRequestDto.toMessagePageable();

        Page<ChatMessage> messagePage = chatMessageRepository.findByRoomIdOrderBySentAtDesc(String.valueOf(roomId), pageable);
        List<MessageResponse> messageResponses = messagePage.getContent()
                .stream()
                .map(MessageResponse::fromChatMessage)
                .toList();

        return PageResponseDto.of(messageResponses, messagePage.getNumber(), messagePage.getTotalPages());
    }

    public ChatMessage findExistingChatMessage(String id) {
        return chatMessageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CHAT_MESSAGE_NOT_FOUND_ERROR));
    }

    // 특정 채팅방의 최신 메시지 ID를 반환
    public String getLatestMessageId(String roomId) {
        Pageable limit = PageRequest.of(0, 1);  // 최신 1개 메시지만 조회
        List<ChatMessage> latestMessages = chatMessageRepository.findLatestMessageByRoomId(roomId, limit);
        return latestMessages.isEmpty() ? null : latestMessages.get(0).getId();
    }
}
