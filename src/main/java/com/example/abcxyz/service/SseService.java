package com.example.abcxyz.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {

    SseEmitter subscribe();

    void broadcast(String eventName, String notificationMessage);
}
