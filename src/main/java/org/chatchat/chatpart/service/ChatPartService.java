package org.chatchat.chatpart.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatpart.domain.ChatPart;
import org.chatchat.chatpart.domain.repository.ChatPartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatPartService {

    private final ChatPartRepository chatPartRepository;

    @Transactional
    public void saveChatPart(final ChatPart chatPart) {
        chatPartRepository.save(chatPart);
    }
}
