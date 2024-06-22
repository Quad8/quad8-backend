package site.keydeuk.store.domain.payment;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import site.keydeuk.store.domain.payment.dto.PaymentConfirmRequest;
import site.keydeuk.store.domain.payment.dto.PaymentConfirmResponse;


@Component
public class TossPaymentClient implements PaymentClient {

    private static final String DOMAIN = "https://api.tosspayments.com/v1/payments";
    private static final String CONFIRM_PATH = "/confirm";
    private final RestClient restClient;
    private final String secretKey;

    public TossPaymentClient(RestClient restClient,
                             @Value("${toss.secret-key}") String secretKey) {
        this.restClient = restClient;
        this.secretKey = new String(Base64.encodeBase64(secretKey.getBytes()));
    }

    @Override
    public PaymentConfirmResponse confirm(PaymentConfirmRequest request) {
        return restClient.post()
                .uri(DOMAIN + CONFIRM_PATH)
                .header("Authorization", "Basic " + secretKey)
                .body(request)
                .retrieve()
                .body(PaymentConfirmResponse.class);
    }
}
