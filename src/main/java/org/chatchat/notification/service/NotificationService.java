package org.chatchat.notification.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.kafka.domain.KafkaChatMessage;
import org.chatchat.notification.domain.Notification;
import org.chatchat.notification.domain.repository.NotificationRepository;
import org.chatchat.notification.dto.response.NotificationResponseDto;
import org.chatchat.notification.util.EmitterService;
import org.chatchat.room.domain.Room;
import org.chatchat.room.service.RoomQueryService;
import org.chatchat.roomuser.domain.RoomUser;
import org.chatchat.roomuser.service.RoomUserQueryService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final RoomQueryService roomQueryService;
    private final RoomUserQueryService roomUserQueryService;
    private final EmitterService emitterService;

    @Async
    @Transactional
    public CompletableFuture<Void> processNotification(KafkaChatMessage kafkaChatMessage) {
        Room room = roomQueryService.findExistingRoomByRoomId(Long.valueOf(kafkaChatMessage.getRoomId()));
        List<RoomUser> roomUsers = roomUserQueryService.findExistingRoomUserListByRoomId(Long.valueOf(kafkaChatMessage.getRoomId()));

        return CompletableFuture.runAsync(() ->
                roomUsers.stream()
                        .map(RoomUser::getUser)
                        .filter(user -> !user.getUsername().equals(kafkaChatMessage.getSenderName()))
                        .forEach(user -> {
                            // 알림 생성
                            Notification notification = new Notification(
                                    user,
                                    room.getName(),
                                    kafkaChatMessage.getContent(),
                                    kafkaChatMessage.getSentAt()
                            );

                            // 알림 저장
                            notificationRepository.save(notification);

                            // SSE 로 실시간 알림 전송
                            emitterService.notify(
                                    user.getId().toString(),
                                    NotificationResponseDto.from(notification)
                            );
                        })
        );
    }
}
