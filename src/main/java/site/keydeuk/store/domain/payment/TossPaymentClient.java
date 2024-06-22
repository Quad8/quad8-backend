package site.keydeuk.store.domain.payment;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import site.keydeuk.store.domain.payment.dto.PaymentConfirmRequest;
import site.keydeuk.store.domain.payment.dto.PaymentConfirmResponse;


@Component
@RequiredArgsConstructor
public class TossPaymentClient implements PaymentClient {

    private static final String DOMAIN = "https://api.tosspayments.com/v1/payments";
    private static final String CONFIRM_PATH = "/confirm";
    //    private static final String SECRET_KEY = new String(Base64.encodeBase64("test_sk_6BYq7GWPVvy5eRMWwjam3NE5vbo1:".getBytes()));
        private static final String SECRET_KEY = new String(Base64.encodeBase64("test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6:".getBytes()));

    private final RestClient restClient;

    @Override
    public PaymentConfirmResponse confirm(PaymentConfirmRequest request) {
        return restClient.post()
                .uri(DOMAIN + CONFIRM_PATH)
                .header("Authorization", "Basic "+SECRET_KEY)
                .body(request)
                .retrieve()
                .body(PaymentConfirmResponse.class);
    }
}
