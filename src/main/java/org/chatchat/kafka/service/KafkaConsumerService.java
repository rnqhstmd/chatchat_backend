package org.chatchat.kafka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chatchat.chatmessage.dto.response.MessageResponse;
import org.chatchat.common.exception.InternalServerException;
import org.chatchat.kafka.domain.KafkaMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import static org.chatchat.common.exception.type.ErrorType.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;
    private final KafkaListenerEndpointRegistry endpointRegistry;

    @KafkaListener(id = "dynamicListener", topics = {"chat-messages"}, groupId = "chat-group", autoStartup = "false")
    public void listen(String messageJson) {
        try {
            KafkaMessage kafkaMessage = objectMapper.readValue(messageJson, KafkaMessage.class);
            MessageResponse messageResponse = MessageResponse.fromKafkaMessage(kafkaMessage);
            messagingTemplate.convertAndSend("/topic/room." + messageResponse.roomId(), messageResponse);
        } catch (Exception e) {
            throw new InternalServerException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void startListening() {
        endpointRegistry.getListenerContainer("dynamicListener").start();
        log.info("------Start Listening------");
    }

    public void stopListening() {
        endpointRegistry.getListenerContainer("dynamicListener").stop();
        log.info("------stop Listening------");
    }
}
