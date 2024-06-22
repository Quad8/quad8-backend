package site.keydeuk.store.domain.payment.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.payment.dto.PaymentConfirmRequest;
import site.keydeuk.store.domain.payment.dto.PaymentConfirmResponse;

@RestController("/api/v1/payments")
public class PaymentController {

    @PostMapping("/confirm")
    public CommonResponse<PaymentConfirmResponse> confirm(PaymentConfirmRequest request) {
        return CommonResponse.ok(null);
    }
}
