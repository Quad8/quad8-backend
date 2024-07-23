package site.keydeuk.store.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EasyPay {
    private EasyPayProvider provider;
    private long amount;
    private long discountAmount;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private enum EasyPayProvider {

    }
}
