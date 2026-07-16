package com.regulareedge.notificationservice.service;

import com.regulareedge.notificationservice.common.enums.NotificationCategory;
import com.regulareedge.notificationservice.common.enums.NotificationStatus;
import com.regulareedge.notificationservice.dto.request.CreateNotificationRequest;
import com.regulareedge.notificationservice.dto.response.NotificationResponse;
import com.regulareedge.notificationservice.entity.Notification;
import com.regulareedge.notificationservice.exception.ResourceNotFoundException;
import com.regulareedge.notificationservice.repository.NotificationRepository;
import com.regulareedge.notificationservice.service.implementation.NotificationServiceImpl;
import com.regulareedge.notificationservice.service.interfaces.UserValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserValidationService userValidationService;

    private NotificationServiceImpl notificationService;

    private Notification notification;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationServiceImpl(notificationRepository, userValidationService);

        notification = new Notification();
        notification.setNotificationId(1);
        notification.setUserId(10);
        notification.setMessage("Reminder: Annual Return is due on 2026-08-01. Please submit on time.");
        notification.setCategory(NotificationCategory.RETURN);
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setCreatedDate(LocalDateTime.of(2026, 7, 16, 9, 0));
    }

    @Test
    void create_shouldPersistNotificationAsUnread() {
        CreateNotificationRequest request = new CreateNotificationRequest();
        request.setUserId(10);
        request.setMessage("New data request: KYC documents. Please upload the data.");
        request.setCategory(NotificationCategory.DATA_COLLECTION);

        when(userValidationService.userExists(anyInt())).thenReturn(true);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        NotificationResponse response = notificationService.create(request);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository).save(captor.capture());

        assertEquals(NotificationStatus.UNREAD, captor.getValue().getStatus());
        assertEquals(10, captor.getValue().getUserId());
        assertEquals(1, response.getNotificationId());
    }

    @Test
    void create_shouldProceed_whenUserValidationFailsOpen() {
        CreateNotificationRequest request = new CreateNotificationRequest();
        request.setUserId(10);
        request.setMessage("Some message");
        request.setCategory(NotificationCategory.OBLIGATION);

        when(userValidationService.userExists(anyInt())).thenThrow(new RuntimeException("downstream unavailable"));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        NotificationResponse response = notificationService.create(request);

        assertEquals(1, response.getNotificationId());
    }

    @Test
    void getMyNotifications_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notification> page = new PageImpl<>(List.of(notification), pageable, 1);
        when(notificationRepository.findByUserId(10, pageable)).thenReturn(page);

        Page<NotificationResponse> result = notificationService.getMyNotifications(10, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(10, result.getContent().get(0).getUserId());
    }

    @Test
    void markRead_shouldThrow_whenNotificationNotFound() {
        when(notificationRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> notificationService.markRead(99));
    }

    @Test
    void markRead_shouldUpdateStatusToRead_whenFound() {
        when(notificationRepository.findById(1)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        NotificationResponse response = notificationService.markRead(1);

        assertEquals(NotificationStatus.READ, notification.getStatus());
        assertEquals(1, response.getNotificationId());
    }

    @Test
    void markAllRead_shouldMarkAllUnreadNotificationsAsRead() {
        Notification second = new Notification();
        second.setNotificationId(2);
        second.setUserId(10);
        second.setMessage("Another message");
        second.setCategory(NotificationCategory.PENALTY);
        second.setStatus(NotificationStatus.UNREAD);
        second.setCreatedDate(LocalDateTime.now());

        when(notificationRepository.findByUserIdAndStatus(10, NotificationStatus.UNREAD))
                .thenReturn(List.of(notification, second));
        when(notificationRepository.saveAll(any())).thenReturn(List.of(notification, second));

        List<NotificationResponse> result = notificationService.markAllRead(10);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(r -> r.getUserId() == 10));
        assertEquals(NotificationStatus.READ, notification.getStatus());
        assertEquals(NotificationStatus.READ, second.getStatus());
    }
}
