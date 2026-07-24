package com.example.abcxyz.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class SendNotificationRequest {

    @NotBlank(message = "NotificationMessage is required")
    private String notificationMessage;
}
