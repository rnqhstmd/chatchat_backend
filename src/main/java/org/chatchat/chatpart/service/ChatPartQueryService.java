package org.chatchat.chatpart.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatpart.domain.ChatPart;
import org.chatchat.chatpart.domain.repository.ChatPartRepository;
import org.chatchat.common.exception.ForbiddenException;
import org.springframework.stereotype.Service;

import static org.chatchat.common.exception.type.ErrorType.NOT_ROOM_MEMBER_ERROR;

@Service
@RequiredArgsConstructor
public class ChatPartQueryService {

    private final ChatPartRepository chatPartRepository;

    public void isUserMemberOfRoom(Long roomId, Long userId) {
        if (!chatPartRepository.existsByRoomIdAndUserId(roomId, userId)) {
            throw new ForbiddenException(NOT_ROOM_MEMBER_ERROR);
        }
    }

    public ChatPart findExistingChatPartByRoomIdAndUserId(Long roomId, Long userId) {
        return chatPartRepository.findByRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new ForbiddenException(NOT_ROOM_MEMBER_ERROR));
    }
}
