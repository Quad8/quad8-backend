package site.keydeuk.store.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.payment.dto.PaymentConfirmRequest;
import site.keydeuk.store.domain.payment.dto.PaymentConfirmResponse;
import site.keydeuk.store.domain.payment.dto.PaymentResponse;
import site.keydeuk.store.domain.payment.dto.request.PaymentRequest;
import site.keydeuk.store.domain.payment.service.PaymentService;
import site.keydeuk.store.entity.Payment;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public CommonResponse<PaymentResponse> confirm(
            @RequestBody PaymentRequest request
    ) {
        log.info("결제 승인 요청: {}", request);
        PaymentResponse confirm = paymentService.confirm(request);
        return CommonResponse.ok(confirm);
    }

    @PostMapping("/success")
    public CommonResponse<String> success(
            @RequestBody PaymentRequest request) {
        log.info("결제 성공 처리: {}", request);
        // 결제 성공 후 추가 처리 로직이 필요하다면 여기서 처리
        return CommonResponse.ok("결제가 성공적으로 처리되었습니다.");
    }

    @PostMapping("/failure")
    public CommonResponse<Void> failure(
            @RequestBody PaymentRequest request) {
        log.info("결제 실패 처리: {}", request);
        // 결제 실패 후 추가 처리 로직이 필요하다면 여기서 처리
        return CommonResponse.fail("결제가 성공적으로 처리되지 못했습니다.");
    }
}
