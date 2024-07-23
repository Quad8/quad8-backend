package site.keydeuk.store.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cancel {
    private BigDecimal cancelAmount;
    private String cancelReason;
    private BigDecimal taxFreeAmount;
    private Long taxExemptionAmount;
    private BigDecimal refundableAmount;
    private BigDecimal easyPayDiscountAmount;
    private LocalDateTime canceledAt;
    private String transactionKey;
    private String receiptKey;
    private String cancelStatus;
    private String cancelRequestId;
}
