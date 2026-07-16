package com.regulareedge.notificationservice.service.interfaces;

import com.regulareedge.notificationservice.dto.request.CreateNotificationRequest;
import com.regulareedge.notificationservice.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    NotificationResponse create(CreateNotificationRequest request);

    Page<NotificationResponse> getMyNotifications(int userId, Pageable pageable);

    List<NotificationResponse> getUnread(int userId);

    long getUnreadCount(int userId);

    NotificationResponse markRead(int notificationId);

    List<NotificationResponse> markAllRead(int userId);
}
