package org.chatchat.chatmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.domain.repository.ChatMessageRepository;
import org.chatchat.chatmessage.dto.request.MessageRequest;
import org.chatchat.chatmessage.dto.response.MessageResponse;
import org.chatchat.kafka.domain.KafkaChatMessage;
import org.chatchat.kafka.domain.KafkaErrorMessage;
import org.chatchat.kafka.service.KafkaProducerService;
import org.chatchat.room.dto.request.InviteUserToRoomRequest;
import org.chatchat.room.dto.request.QuitRoomRequest;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.chatchat.chatmessage.domain.MessageType.SYSTEM;
import static org.chatchat.chatmessage.domain.MessageType.TALK;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ChatMessageQueryService chatMessageQueryService;
    private final SimpMessageSendingOperations messagingTemplate;

    public void saveTalkMessage(MessageRequest messageRequest, String username) {
        try {
            ChatMessage chatMessage = ChatMessage.builder()
                    .type(TALK)
                    .roomId(String.valueOf(messageRequest.roomId()))
                    .sender(username)
                    .content(messageRequest.message())
                    .sentAt(LocalDateTime.now())
                    .build();

            ChatMessage saveChatMessage = chatMessageRepository.save(chatMessage);
            kafkaProducerService.publishStoredEvent(saveChatMessage);
        } catch (Exception e) {
            log.error("Error saving chat message: ", e);
            KafkaErrorMessage errorMessage = KafkaErrorMessage.builder()
                    .errorCode("CHAT_MESSAGE_SAVE_FAILURE")
                    .errorMessage("Failed to save chat message: " + e.getMessage())
                    .build();
            kafkaProducerService.publishFailureEvent(errorMessage);
        }
    }

    public void saveInviteMessage(InviteUserToRoomRequest inviteUserToRoomRequest, String username) {
        try {
            ChatMessage chatMessage = ChatMessage.builder()
                    .type(SYSTEM)
                    .roomId(String.valueOf(inviteUserToRoomRequest.roomId()))
                    .sender("시스템")
                    .content(username + "님이 " + inviteUserToRoomRequest.username() + "님을 초대했습니다.")
                    .sentAt(LocalDateTime.now())
                    .build();

            chatMessageRepository.save(chatMessage);
            kafkaProducerService.publishStoredEvent(chatMessage);
        } catch (Exception e) {
            log.error("Error saving invite message: ", e);
            KafkaErrorMessage errorMessage = KafkaErrorMessage.builder()
                    .errorCode("SYSTEM_INVITE_MESSAGE_SAVE_FAILURE")
                    .errorMessage("Failed to save invite message: " + e.getMessage())
                    .build();

            kafkaProducerService.publishFailureEvent(errorMessage);
        }
    }

    public void saveQuitMessage(QuitRoomRequest quitRoomRequest, String username) {
        try {
            ChatMessage chatMessage = ChatMessage.builder()
                    .type(SYSTEM)
                    .roomId(String.valueOf(quitRoomRequest.roomId()))
                    .sender("시스템")
                    .content(username + "님이 나갔습니다.")
                    .sentAt(LocalDateTime.now())
                    .build();

            chatMessageRepository.save(chatMessage);
            kafkaProducerService.publishStoredEvent(chatMessage);
        } catch (Exception e) {
            log.error("Error saving quit message: ", e);
            KafkaErrorMessage errorMessage = KafkaErrorMessage.builder()
                    .errorCode("SYSTEM_LEAVE_MESSAGE_SAVE_FAILURE")
                    .errorMessage("Failed to save quit message: " + e.getMessage())
                    .build();

            kafkaProducerService.publishFailureEvent(errorMessage);
        }
    }

    public void sendMessage(KafkaChatMessage kafkaChatMessage) {
        MessageResponse messageResponse = MessageResponse.fromKafkaMessage(kafkaChatMessage);
        String destination = "/topic/room." + messageResponse.roomId();
        try {
            messagingTemplate.convertAndSend(destination, messageResponse);
        } catch (Exception e) {
            log.error("Error sending chat message: ", e);
            KafkaErrorMessage errorMessage = KafkaErrorMessage.builder()
                    .errorDomainId(messageResponse.id())
                    .errorCode("MESSAGE_SEND_FAILURE")
                    .errorMessage("Failed to send message over WebSocket: " + e.getMessage())
                    .build();
            kafkaProducerService.publishFailureEvent(errorMessage);
        }
    }

    public void deleteMessage(String messageId) {
        ChatMessage chatMessage = chatMessageQueryService.findExistingChatMessage(messageId);
        chatMessageRepository.delete(chatMessage);
    }
}
