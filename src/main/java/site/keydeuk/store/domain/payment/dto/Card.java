package site.keydeuk.store.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private String issuerCode;
    private String acquirerCode;
    private String number;
    private int installmentPlanMonths;
    private boolean isInterestFree;
    private InterestPayer interestPayer;
    private String approveNo;
    private String useCardPoint;
    private CardType cardType;
    private OwnerType ownerType;
    private AcquireStatus acquireStatus;
    private String receiptUrl;
    private int amount;
}
