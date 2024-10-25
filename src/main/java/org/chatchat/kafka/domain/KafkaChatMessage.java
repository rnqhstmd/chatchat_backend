package org.chatchat.kafka.domain;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaChatMessage implements Serializable {

    private String id;
    private String type;
    private String roomId;
    private String senderName;
    private String content;
    private LocalDateTime sentAt;
}
