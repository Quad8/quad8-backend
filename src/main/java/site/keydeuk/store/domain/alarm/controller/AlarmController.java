package site.keydeuk.store.domain.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.alarm.service.AlarmService;
import site.keydeuk.store.domain.security.PrincipalDetails;

@Slf4j
@Tag(name = "Alarm", description = "알람 관련 API입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm/")
@RestController
public class AlarmController {

    private final AlarmService alarmService;
    @Operation(summary = "sse세션연결", description = "서버와 연결하여 SSE connection을 맺습니다.")
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public CommonResponse<?> subscribe(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                       @RequestHeader(value = "Last-Event-ID",required = false)String lastEventId){

        return CommonResponse.ok();
    }

}
