package com.example.abcxyz.service.implement;

import com.example.abcxyz.service.SseService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseServiceImpl implements SseService {

    private final List<SseEmitter> listEmitters = new CopyOnWriteArrayList<>();

    @Override
    public SseEmitter subscribe() {
        SseEmitter newSseEmitter = new SseEmitter(0L);

        listEmitters.add(newSseEmitter);

        newSseEmitter.onCompletion(() -> {
            listEmitters.remove(newSseEmitter);
        });

        newSseEmitter.onTimeout(() -> {
            listEmitters.remove(newSseEmitter);
        });

        newSseEmitter.onError((error) -> {
            newSseEmitter.completeWithError(error);
            listEmitters.remove(newSseEmitter);
        });

        return newSseEmitter;
    }

    @Override
    public void broadcast(String eventName, String notificationMessage) {
        for (SseEmitter emitter : listEmitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(notificationMessage));
            } catch (IOException ex) {
                emitter.completeWithError(ex);
                listEmitters.remove(emitter);
            }
        }
    }
}
