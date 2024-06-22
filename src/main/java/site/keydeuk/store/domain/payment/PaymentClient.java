package site.keydeuk.store.domain.payment;

import site.keydeuk.store.domain.payment.dto.PaymentConfirmRequest;
import site.keydeuk.store.domain.payment.dto.PaymentConfirmResponse;

public interface PaymentClient {

    PaymentConfirmResponse confirm(PaymentConfirmRequest request);
}
