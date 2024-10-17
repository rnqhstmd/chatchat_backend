package org.chatchat.chatpart.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatpart.domain.ChatPart;
import org.chatchat.chatpart.domain.repository.ChatPartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatPartService {

    private final ChatPartRepository chatPartRepository;

    public void saveChatPart(final ChatPart chatPart) {
        chatPartRepository.save(chatPart);
    }

    public void removeChatPart(final ChatPart chatPart) {
        chatPartRepository.delete(chatPart);
    }
}
