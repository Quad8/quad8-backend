package site.keydeuk.store.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;


@JsonIgnoreProperties(ignoreUnknown = true)
public class VirtualAccount {
    private String accountType;
    private String accountNumber;
    private String bankCode;
    private String customerName;
    private LocalDateTime dueDate;
    private String refundStatus;
    private boolean expired;
    private String settlementStatus;
    private RefundReceiveAccount refundReceiveAccount;
}
