package org.chatchat.chatpart.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatpart.domain.repository.ChatPartRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatPartQueryService {

    private final ChatPartRepository chatPartRepository;

    public boolean validateUserRoomMember(Long roomId, Long userId) {
        return chatPartRepository.existsByRoomIdAndUserId(roomId, userId);
    }
}
