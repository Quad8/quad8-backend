package site.keydeuk.store.domain.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.alarm.service.AlarmService;
import site.keydeuk.store.domain.security.PrincipalDetails;

import java.util.List;

import static site.keydeuk.store.common.response.ErrorCode.COMMON_INVALID_PARAMETER;

@Slf4j
@Tag(name = "Alarm", description = "알람 관련 API입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
@RestController
public class AlarmController {

    private final AlarmService alarmService;
    @Operation(summary = "sse세션연결", description = "서버와 연결하여 SSE connection을 맺습니다.")
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@AuthenticationPrincipal PrincipalDetails principalDetails
                , @RequestHeader(value = "Last-Event-ID",required = false,defaultValue = "")String lastEventId
    ){
        log.info("last-id: {}",lastEventId);
        return ResponseEntity.ok(alarmService.subscribe(principalDetails.getUser(),lastEventId));
    }

    @Operation(summary = "알림 목록",description = "알림 목록 내역을 조회합니다.")
    @GetMapping
    public CommonResponse<?> getNotifications(@AuthenticationPrincipal PrincipalDetails principalDetails){
        return CommonResponse.ok(alarmService.getNotifications(principalDetails.getUser()));
    }

    @Operation(summary = "알림 읽음",description = "알림을 읽었음으로 처리합니다.")
    @PostMapping("/maskAsRead/{alarmId}")
    public CommonResponse<?> maskAsRead(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @PathVariable Long alarmId){
        alarmService.maskAsRead(alarmId);
        return CommonResponse.ok("알림 읽음 처리 되었습니다.",null);
    }

    @Operation(summary = "알림 삭제", description = "알림을 삭제합니다.(여러개 삭제 가능)")
    @DeleteMapping
    public CommonResponse<?> deleteAlarms(@AuthenticationPrincipal PrincipalDetails principalDetails
                                          ,@RequestBody List<Long> ids){

        if (ids.isEmpty()) return CommonResponse.fail("알람 id가 빈값입니다",null);
        alarmService.deleteAlarms(ids);
        return CommonResponse.ok("알림이 삭제되었습니다.",null);

    }

}
