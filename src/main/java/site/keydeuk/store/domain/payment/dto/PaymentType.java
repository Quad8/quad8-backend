package site.keydeuk.store.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public enum PaymentType {
    NORMAL,
    BILLING,
    BRANDPAY
}
