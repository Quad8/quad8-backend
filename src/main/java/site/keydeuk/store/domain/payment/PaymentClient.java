package site.keydeuk.store.domain.payment;

import site.keydeuk.store.domain.payment.dto.request.PaymentConfirmRequest;
import site.keydeuk.store.domain.payment.dto.response.PaymentConfirmResponse;

public interface PaymentClient {

    PaymentConfirmResponse confirm(PaymentConfirmRequest request);
}
