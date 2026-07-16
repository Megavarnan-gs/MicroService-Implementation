package com.regulareedge.notificationservice.repository;

import com.regulareedge.notificationservice.common.enums.NotificationStatus;
import com.regulareedge.notificationservice.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    Page<Notification> findByUserId(int userId, Pageable pageable);

    List<Notification> findByUserIdAndStatus(int userId, NotificationStatus status);

    long countByUserIdAndStatus(int userId, NotificationStatus status);
}
