package org.chatchat.chatmessage.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.chatmessage.domain.repository.ChatMessageRepository;
import org.chatchat.chatmessage.dto.request.MessageRequest;
import org.chatchat.kafka.domain.KafkaMessage;
import org.chatchat.kafka.service.KafkaProducerService;
import org.chatchat.room.dto.request.InviteUserToRoomRequest;
import org.chatchat.room.dto.request.QuitRoomRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.chatchat.chatmessage.domain.MessageType.SYSTEM;
import static org.chatchat.chatmessage.domain.MessageType.TALK;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final KafkaProducerService kafkaProducerService;

    public void saveAndSendChatMessage(MessageRequest messageRequest, String username) {
        KafkaMessage kafkaMessage = KafkaMessage.builder()
                .roomId(String.valueOf(messageRequest.roomId()))
                .senderName(username)
                .content(messageRequest.message())
                .sentAt(LocalDateTime.now())
                .type(String.valueOf(TALK))
                .build();

        // ChatMessage 저장
        ChatMessage chatMessage = kafkaMessage.toChatMessage();
        chatMessageRepository.save(chatMessage);

        kafkaProducerService.sendMessage("chat-messages", kafkaMessage);
    }

    public void sendInviteMessage(InviteUserToRoomRequest inviteUserToRoomRequest, String username) {
        KafkaMessage kafkaMessage = KafkaMessage.builder()
                .roomId(String.valueOf(inviteUserToRoomRequest.roomId()))
                .senderName("시스템")
                .content(username + "님이 " + inviteUserToRoomRequest.username() + "님을 초대했습니다.")
                .sentAt(LocalDateTime.now())
                .type(String.valueOf(SYSTEM))
                .build();

        kafkaProducerService.sendMessage("chat-messages", kafkaMessage);
    }

    public void sendQuitMessage(QuitRoomRequest quitRoomRequest, String username) {
        KafkaMessage kafkaMessage = KafkaMessage.builder()
                .roomId(String.valueOf(quitRoomRequest.roomId()))
                .senderName("시스템")
                .content(username + "님이 나갔습니다.")
                .sentAt(LocalDateTime.now())
                .type(String.valueOf(SYSTEM))
                .build();

        kafkaProducerService.sendMessage("chat-messages", kafkaMessage);
    }
}
