package org.chatchat.kafka.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaErrorMessage implements Serializable {

    private String errorDomainId; // 작업에 실패한 도메인의 ID 값 (PK)
    private String errorCode;
    private String errorMessage;
}
