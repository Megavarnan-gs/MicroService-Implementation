package com.regulareedge.notificationservice.dto.request;

import com.regulareedge.notificationservice.common.enums.NotificationCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateNotificationRequest {

    @Positive
    private int userId;

    @NotBlank
    @Size(max = 1000)
    private String message;

    @NotNull
    private NotificationCategory category;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationCategory getCategory() {
        return category;
    }

    public void setCategory(NotificationCategory category) {
        this.category = category;
    }
}
