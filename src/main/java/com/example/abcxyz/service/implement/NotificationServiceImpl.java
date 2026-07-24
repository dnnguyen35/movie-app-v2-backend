package com.example.abcxyz.service.implement;

import com.example.abcxyz.service.NotificationService;
import com.example.abcxyz.service.SseService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final SseService sseService;

    public NotificationServiceImpl(SseService sseService) {
        this.sseService = sseService;
    }

    @Override
    public void sendNotification(String notificationMessage) {
        this.sseService.broadcast("maintenance", notificationMessage);
    }

    @Override
    public SseEmitter subscribe() {
        return this.sseService.subscribe();
    }
}
