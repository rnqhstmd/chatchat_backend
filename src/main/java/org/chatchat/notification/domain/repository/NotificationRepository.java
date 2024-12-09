package org.chatchat.notification.domain.repository;

import org.chatchat.notification.domain.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserIdOrderBySentAtDesc(String userId);
}
