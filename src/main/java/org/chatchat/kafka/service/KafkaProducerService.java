package org.chatchat.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chatchat.chatmessage.domain.ChatMessage;
import org.chatchat.common.exception.BadRequestException;
import org.chatchat.common.exception.InternalServerException;
import org.chatchat.kafka.domain.KafkaChatMessage;
import org.chatchat.kafka.domain.KafkaErrorMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import static org.chatchat.common.exception.type.ErrorType.INVALID_REQUEST_FORMAT_ERROR;
import static org.chatchat.common.exception.type.ErrorType.KAFKA_SERVER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    // 메세지 저장 성공 후 이벤트
    public void publishStoredEvent(ChatMessage chatMessage) {
        try {
            KafkaChatMessage kafkaChatMessage = convertToKafkaMessage(chatMessage);
            String jsonMessage = objectMapper.writeValueAsString(kafkaChatMessage);
            kafkaTemplate.send("chat-message-send", jsonMessage);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(INVALID_REQUEST_FORMAT_ERROR, e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(KAFKA_SERVER_ERROR, e.getMessage());
        }
    }

    // 작업 실패 이벤트
    public void publishFailureEvent(KafkaErrorMessage errorMessage) {
        try {
            String jsonErrorMessage = objectMapper.writeValueAsString(errorMessage);
            kafkaTemplate.send("chat-message-failure", jsonErrorMessage);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(INVALID_REQUEST_FORMAT_ERROR, e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(KAFKA_SERVER_ERROR, e.getMessage());
        }
    }

    private KafkaChatMessage convertToKafkaMessage(ChatMessage chatMessage) {
        return new KafkaChatMessage(
                chatMessage.getId(),
                String.valueOf(chatMessage.getType()),
                chatMessage.getRoomId(),
                chatMessage.getSender(),
                chatMessage.getContent(),
                chatMessage.getSentAt()
        );
    }
}
