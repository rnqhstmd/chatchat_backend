package org.chatchat.chatpart.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatpart.domain.repository.ChatPartRepository;
import org.chatchat.common.exception.ConflictException;
import org.springframework.stereotype.Service;
import static org.chatchat.common.exception.type.ErrorType.ALREADY_JOINED_USER_ERROR;

@Service
@RequiredArgsConstructor
public class ChatPartQueryService {

    private final ChatPartRepository chatPartRepository;

    public void isUserMemberOfRoom(Long roomId, Long userId) {
        if (chatPartRepository.existsByRoomIdAndUserId(roomId, userId)) {
            throw new ConflictException(ALREADY_JOINED_USER_ERROR);
        }
    }
}
