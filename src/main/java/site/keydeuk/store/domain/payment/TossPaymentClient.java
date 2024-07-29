package site.keydeuk.store.domain.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import site.keydeuk.store.domain.payment.dto.request.PaymentConfirmRequest;
import site.keydeuk.store.domain.payment.dto.response.PaymentConfirmResponse;

@Component
@Slf4j
public class TossPaymentClient implements PaymentClient {

    private static final String DOMAIN = "https://api.tosspayments.com/v1/payments";
    private static final String CONFIRM_PATH = "/confirm";
    private final RestClient restClient;
    private final String secretKey;
    private final ObjectMapper objectMapper;

    public TossPaymentClient(RestClient restClient,
                             @Value("${toss.secret-key}") String secretKey,
                             ObjectMapper objectMapper) {
        this.restClient = restClient;
        String key = secretKey + ":";
        this.secretKey = new String(Base64.encodeBase64(key.getBytes()));
        this.objectMapper = objectMapper;
    }

    @Override
    public PaymentConfirmResponse confirm(PaymentConfirmRequest request){
        String s = restClient.post()
                .uri(DOMAIN + CONFIRM_PATH)
                .header("Authorization", "Basic " + secretKey)
                .body(request)
                .retrieve()
                .body(String.class);
        log.info("결제 승인 요청시 응답, {}",s);
        try {
            return objectMapper.readValue(s, PaymentConfirmResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
