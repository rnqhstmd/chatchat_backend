package org.chatchat.chatmessage.service;

import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.domain.repository.ChatMessageRepository;
import org.chatchat.chatmessage.dto.request.MessageRequest;
import org.chatchat.kafka.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.chatchat.chatmessage.domain.MessageType.TALK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Mock
    private ChatMessageQueryService chatMessageQueryService;

    @InjectMocks
    private ChatMessageService chatMessageService;


    @Test
    void saveTalkMessage() {
        // Given
        MessageRequest request = new MessageRequest(1L, "안녕하세요?");
        String username = "구본승";
        ChatMessage chatMessage = new ChatMessage(
                TALK,
                String.valueOf(request.roomId()),
                username,
                request.message(),
                LocalDateTime.now()
        );

        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        // When
        chatMessageService.saveTalkMessage(request, username);

        // Then
        verify(chatMessageRepository).save(any(ChatMessage.class));
        verify(kafkaProducerService).publishStoredEvent(any(ChatMessage.class));
    }

    @Test
    void deleteMessage() {
        // Given
        ChatMessage chatMessage = new ChatMessage(
                "messageId1",
                TALK,
                "1L",
                "구본승",
                "메세지 내용",
                LocalDateTime.now()
        );
        String messageId = chatMessage.getId();
        when(chatMessageQueryService.findExistingChatMessage(anyString())).thenReturn(chatMessage);

        // When
        chatMessageService.deleteMessage(messageId);

        // Then
        verify(chatMessageQueryService).findExistingChatMessage(messageId);
        verify(chatMessageRepository).delete(chatMessage);
    }
}