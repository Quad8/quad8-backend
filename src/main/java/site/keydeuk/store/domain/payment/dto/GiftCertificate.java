package site.keydeuk.store.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GiftCertificate {
    private String approveNo;
    private SettlementStatus settlementStatus;
}
