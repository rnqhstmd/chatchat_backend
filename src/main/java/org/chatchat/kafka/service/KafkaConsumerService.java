package org.chatchat.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chatchat.chatmessage.service.ChatMessageService;
import org.chatchat.common.exception.BadRequestException;
import org.chatchat.common.exception.InternalServerException;
import org.chatchat.kafka.domain.KafkaChatMessage;
import org.chatchat.kafka.domain.KafkaErrorMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Service;

import static org.chatchat.common.exception.type.ErrorType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final ObjectMapper objectMapper;
    private final ChatMessageService chatMessageService;
    private final KafkaListenerEndpointRegistry endpointRegistry;

    @KafkaListener(id = "successListener", topics = {"chat-message-send"}, groupId = "chat-group", autoStartup = "false")
    public void listenToSaveSuccessMessage(String messageJson) {
        try {
            KafkaChatMessage kafkaChatMessage = objectMapper.readValue(messageJson, KafkaChatMessage.class);
            chatMessageService.sendMessage(kafkaChatMessage);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(INVALID_REQUEST_FORMAT_ERROR, e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(KAFKA_SERVER_ERROR, e.getMessage());
        }
    }

    @KafkaListener(id = "failureListener", topics = {"chat-message-failure"}, groupId = "chat-group", autoStartup = "false")
    public void listenToFailureMessage(String errorMessageJson) {
        try {
            KafkaErrorMessage kafkaErrorMessage = objectMapper.readValue(errorMessageJson, KafkaErrorMessage.class);
            chatMessageService.deleteMessage(kafkaErrorMessage.getErrorDomainId());
        } catch (JsonProcessingException e) {
            throw new BadRequestException(INVALID_REQUEST_FORMAT_ERROR, e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // 사용자가 연결된 경우에만 successListener 실행
    public void startListening() {
        if (!endpointRegistry.getListenerContainer("successListener").isRunning()) {
            endpointRegistry.getListenerContainer("successListener").start();
            log.info("Kafka successListener started.");
        }
    }

    // 모든 사용자가 연결 해제되면 successListener 중지
    public void stopListening() {
        if (endpointRegistry.getListenerContainer("successListener").isRunning()) {
            endpointRegistry.getListenerContainer("successListener").stop();
            log.info("Kafka successListener stopped.");
        }
    }
}
