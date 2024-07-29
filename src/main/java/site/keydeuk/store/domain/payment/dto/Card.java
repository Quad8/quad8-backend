package site.keydeuk.store.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {
    private String issuerCode;
    private String acquirerCode;
    private String number;
    private int installmentPlanMonths;
    private boolean isInterestFree;
    private InterestPayer interestPayer;
    private String approveNo;
    private String useCardPoint;
    private String cardType;
    private OwnerType ownerType;
    private AcquireStatus acquireStatus;
    private String receiptUrl;
    private int amount;
}
