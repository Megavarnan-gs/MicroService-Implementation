package com.regulareedge.notificationservice.controller;

import com.regulareedge.notificationservice.dto.request.CreateNotificationRequest;
import com.regulareedge.notificationservice.dto.response.NotificationResponse;
import com.regulareedge.notificationservice.dto.response.UnreadCountResponse;
import com.regulareedge.notificationservice.service.interfaces.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * POST /notifications is an internal, service-to-service endpoint (other microservices
 * call it via Feign, propagating the calling end-user's JWT) - it therefore requires only
 * a valid authenticated JWT, with no role restriction. All other endpoints are consumed
 * directly by end users and likewise only require authentication (no specific role),
 * per the current notification-service requirements.
 */
@RestController
@RequestMapping("/notifications")
@Validated
@Tag(name = "Notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/my/{userId}")
    public ResponseEntity<Page<NotificationResponse>> getMyNotifications(@PathVariable @Positive int userId,
                                                                          Pageable pageable) {
        return ResponseEntity.ok(notificationService.getMyNotifications(userId, pageable));
    }

    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<NotificationResponse>> getUnread(@PathVariable @Positive int userId) {
        return ResponseEntity.ok(notificationService.getUnread(userId));
    }

    @GetMapping("/count/{userId}")
    public ResponseEntity<UnreadCountResponse> getUnreadCount(@PathVariable @Positive int userId) {
        return ResponseEntity.ok(new UnreadCountResponse(notificationService.getUnreadCount(userId)));
    }

    @PutMapping("/markRead/{notificationId}")
    public ResponseEntity<NotificationResponse> markRead(@PathVariable @Positive int notificationId) {
        return ResponseEntity.ok(notificationService.markRead(notificationId));
    }

    @PutMapping("/markAllRead/{userId}")
    public ResponseEntity<List<NotificationResponse>> markAllRead(@PathVariable @Positive int userId) {
        return ResponseEntity.ok(notificationService.markAllRead(userId));
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> create(@Valid @RequestBody CreateNotificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.create(request));
    }
}
