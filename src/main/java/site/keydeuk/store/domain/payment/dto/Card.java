package site.keydeuk.store.domain.payment.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Card {
    private final String issuerCode;
    private final String acquirerCode;
    private final String number;
    private final int installmentPlanMonths;
    private final boolean isInterestFree;
    private final InterestPayer interestPayer;
    private final String approveNo;
    private final String useCardPoint;
    private final CardType cardType;
    private final OwnerType ownerType;
    private final AcquireStatus acquireStatus;
    private final String receiptUrl;
    private final int amount;
}
