package com.regulareedge.notificationservice.service.implementation;

import com.regulareedge.notificationservice.common.enums.NotificationStatus;
import com.regulareedge.notificationservice.dto.request.CreateNotificationRequest;
import com.regulareedge.notificationservice.dto.response.NotificationResponse;
import com.regulareedge.notificationservice.entity.Notification;
import com.regulareedge.notificationservice.exception.ResourceNotFoundException;
import com.regulareedge.notificationservice.mapper.NotificationMapper;
import com.regulareedge.notificationservice.repository.NotificationRepository;
import com.regulareedge.notificationservice.service.interfaces.AuditLogService;
import com.regulareedge.notificationservice.service.interfaces.NotificationService;
import com.regulareedge.notificationservice.service.interfaces.UserValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final UserValidationService userValidationService;
    private final AuditLogService auditLogService;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                    UserValidationService userValidationService,
                                    AuditLogService auditLogService) {
        this.notificationRepository = notificationRepository;
        this.userValidationService = userValidationService;
        this.auditLogService = auditLogService;
    }

    @Override
    public NotificationResponse create(CreateNotificationRequest request) {
        try {
            boolean exists = userValidationService.userExists(request.getUserId());
            if (!exists) {
                logger.warn("userExists check reported userId={} as unknown; proceeding anyway "
                        + "(fail-open) since this is a best-effort validation.", request.getUserId());
            }
        } catch (Exception ex) {
            logger.warn("Best-effort userId validation failed for userId={}: {}. Proceeding with "
                    + "notification creation (fail-open).", request.getUserId(), ex.getMessage());
        }

        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setMessage(request.getMessage());
        notification.setCategory(request.getCategory());
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setCreatedDate(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);
        auditLogService.log(saved.getUserId(), "NOTIFICATION_CREATED", "Notification", saved.getNotificationId());
        return NotificationMapper.toResponse(saved);
    }

    @Override
    public Page<NotificationResponse> getMyNotifications(int userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable)
                .map(NotificationMapper::toResponse);
    }

    @Override
    public List<NotificationResponse> getUnread(int userId) {
        return notificationRepository.findByUserIdAndStatus(userId, NotificationStatus.UNREAD).stream()
                .map(NotificationMapper::toResponse)
                .toList();
    }

    @Override
    public long getUnreadCount(int userId) {
        return notificationRepository.countByUserIdAndStatus(userId, NotificationStatus.UNREAD);
    }

    @Override
    public NotificationResponse markRead(int notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with notificationId: " + notificationId));

        notification.setStatus(NotificationStatus.READ);
        Notification saved = notificationRepository.save(notification);
        auditLogService.log(saved.getUserId(), "NOTIFICATION_READ", "Notification", saved.getNotificationId());
        return NotificationMapper.toResponse(saved);
    }

    @Override
    public List<NotificationResponse> markAllRead(int userId) {
        List<Notification> unread = notificationRepository.findByUserIdAndStatus(userId, NotificationStatus.UNREAD);
        unread.forEach(notification -> notification.setStatus(NotificationStatus.READ));
        List<Notification> saved = notificationRepository.saveAll(unread);
        auditLogService.log(userId, "NOTIFICATION_ALL_READ", "Notification", null);
        return saved.stream()
                .map(NotificationMapper::toResponse)
                .toList();
    }
}
