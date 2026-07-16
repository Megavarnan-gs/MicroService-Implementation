package com.regulareedge.notificationservice.mapper;

import com.regulareedge.notificationservice.dto.response.NotificationResponse;
import com.regulareedge.notificationservice.entity.Notification;

public final class NotificationMapper {

    private NotificationMapper() {
    }

    public static NotificationResponse toResponse(Notification entity) {
        if (entity == null) {
            return null;
        }
        return new NotificationResponse(
                entity.getNotificationId(),
                entity.getUserId(),
                entity.getMessage(),
                entity.getCategory(),
                entity.getStatus(),
                entity.getCreatedDate());
    }
}
