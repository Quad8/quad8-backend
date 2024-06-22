package site.keydeuk.store.domain.payment.dto;

import java.time.LocalDateTime;

public class VirtualAccount {
    private String accountType;
    private String accountNumber;
    private String bankCode;
    private String customerName;
    private LocalDateTime dueDate;
    private RefundStatus refundStatus;
    private boolean expired;
    private SettlementStatus settlementStatus;
    private RefundReceiveAccount refundReceiveAccount;
}
