package org.chatchat.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.chatchat.common.exception.BadRequestException;
import org.chatchat.kafka.domain.KafkaMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import static org.chatchat.common.exception.type.ErrorType.INVALID_REQUEST_FORMAT_ERROR;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(String topic, KafkaMessage kafkaMessage) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(kafkaMessage);
            kafkaTemplate.send(topic, jsonMessage);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(INVALID_REQUEST_FORMAT_ERROR, e.getMessage());
        }
    }
}
