package site.keydeuk.store.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MobilePhone {
    private String customerMobilePhone;
    private SettlementStatus settlementStatus;
    private String receiptUrl;
}
