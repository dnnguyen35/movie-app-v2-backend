package com.example.abcxyz.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    SseEmitter subscribe();

    void sendNotification(String notificationMessage);
}
