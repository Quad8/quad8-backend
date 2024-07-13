package site.keydeuk.store.domain.alarm.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public interface AlarmRepositoy {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);

}
