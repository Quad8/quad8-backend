package site.keydeuk.store.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.alarm.dto.AlarmDto;
import site.keydeuk.store.domain.alarm.repository.AlarmRepositoy;
import site.keydeuk.store.domain.alarm.repository.EmitterRepository;
import site.keydeuk.store.entity.Notification;
import site.keydeuk.store.entity.User;
import site.keydeuk.store.entity.enums.NotificationType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static site.keydeuk.store.common.response.ErrorCode.AlARM_NOT_FOUND;

@RequiredArgsConstructor
@Slf4j
@Service
public class AlarmService {

    private final AlarmRepositoy alarmRepositoy;
    private final EmitterRepository emitterRepository;

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    public SseEmitter subscribe(User user, String lastEventId){
        String userId = user.getId()+"";
        String emitterId = user.getId()+"_"+System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId,new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // (1-5) 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = user.getId()+"_"+System.currentTimeMillis();
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + user.getId() + "]");

        // (1-6) 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (!lastEventId.isEmpty()) {
            sendLostData(lastEventId, userId, emitterId, emitter);}

        return emitter; // (1-7)

    }

    @Transactional
    public void send(User receiver, NotificationType notificationType, String content, Long relatedId) {
        Notification notification = alarmRepositoy.save(createNotification(receiver, notificationType, content, relatedId));
        String userId = receiver.getId()+"";
        String eventId = receiver.getId() + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(userId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, new AlarmDto(notification));
                }
        );
    }
    @Transactional
    public void deleteAlarm(Long id){
        Notification notification = alarmRepositoy.findById(id).orElseThrow(()-> new CustomException(AlARM_NOT_FOUND));
        alarmRepositoy.delete(notification);
    }

    @Transactional
    public void maskAsRead(Long id){
        Notification notification = alarmRepositoy.findById(id).orElseThrow(()-> new CustomException(AlARM_NOT_FOUND));
        notification.setRead(true);
        alarmRepositoy.save(notification);
    }

    private Notification createNotification(User receiver, NotificationType notificationType, String message, Long relatedId) { // (7)
        return Notification.builder()
                .userId(receiver.getId())
                .notificationType(notificationType)
                .message(message)
                .isRead(false)
                .relatedId(relatedId)
                .build();
    }



    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) { // (4)
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data)
            );
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private void sendLostData(String lastEventId, String userId, String emitterId, SseEmitter emitter) { // (6)
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserId(userId);
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }
}
