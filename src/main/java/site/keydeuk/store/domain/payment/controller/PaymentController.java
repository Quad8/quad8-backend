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
        log.info("컨트롤러: {}", request);
        PaymentResponse confirm = paymentService.confirm(request);
        return CommonResponse.ok(confirm);
    }
}
