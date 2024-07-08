package site.keydeuk.store.domain.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.payment.dto.response.PaymentResponse;
import site.keydeuk.store.domain.payment.dto.request.PaymentRequest;
import site.keydeuk.store.domain.payment.dto.response.PaymentSuccessResponse;
import site.keydeuk.store.domain.payment.service.PaymentService;
import site.keydeuk.store.domain.security.PrincipalDetails;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "결제 관련 API 입니다.")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 승인", description = "결제를 승인합니다.")
    @PostMapping("/confirm")
    public CommonResponse<PaymentResponse> confirm(
            @RequestBody PaymentRequest request
    ) {
        log.info("결제 승인 요청: {}", request);
        PaymentResponse confirm = paymentService.confirm(request);
        return CommonResponse.ok(confirm);
    }
    @Operation(summary = "결제 성공 처리", description = "결제 성공 시 호출되는 API 입니다.")
    @PostMapping("/success")
    public CommonResponse<PaymentSuccessResponse> success(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody PaymentRequest request) {
        log.info("결제 성공 처리: {}", request);
        PaymentSuccessResponse response = paymentService.paymentSuccess(principalDetails.getUserId(), request.paymentKey(), request.paymentOrderId());
        return CommonResponse.ok("결제가 성공적으로 처리되었습니다.", response);
    }

    @PostMapping("/failure")
    public CommonResponse<Void> failure(
            @RequestBody PaymentRequest request) {
        log.info("결제 실패 처리: {}", request);
        // 결제 실패 후 추가 처리 로직이 필요하다면 여기서 처리
        return CommonResponse.fail("결제가 성공적으로 처리되지 못했습니다.");
    }
}
