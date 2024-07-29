package site.keydeuk.store.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RefundReceiveAccount {
    private String bankCode;
    private String accountNumber;
    private String holderName;
}
