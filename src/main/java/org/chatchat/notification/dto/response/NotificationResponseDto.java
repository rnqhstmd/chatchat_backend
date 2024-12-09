package org.chatchat.notification.dto.response;

import org.chatchat.notification.domain.Notification;

import java.time.LocalDateTime;

public record NotificationResponseDto(
        Long id,
        String roomName,
        String message,
        LocalDateTime sentAt
) {

    public static NotificationResponseDto from(Notification notification) {
        return new NotificationResponseDto(
                notification.getId(),
                notification.getRoomName(),
                notification.getMessage(),
                notification.getSentAt()
        );
    }
}
