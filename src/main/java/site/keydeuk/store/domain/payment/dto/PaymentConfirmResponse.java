package site.keydeuk.store.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PaymentConfirmResponse {
    private String mId;
    private String lastTransactionKey;
    private String paymentKey;
    private String orderId;
    private String orderName;
    private String taxExemptionAmount;
    private PaymentStatus status;
    private ZonedDateTime requestedAt;
    private ZonedDateTime approvedAt;
    private boolean useEscrow;
    private boolean cultureExpense;
    private Card card;
    private VirtualAccount virtualAccount;
    private Transfer transfer;
    private MobilePhone mobilePhone;
    private GiftCertificate giftCertificate;
    private String cashReceipt;
    private String cashReceipts;
    private String discount;
    private List<Cancel> cancels;
    private String secret;
    private PaymentType type;
    private EasyPay easyPay;
    private String easyPayAmount;
    private String easyPayDiscountAmount;
    private String country;
    private String failure;
    private boolean isPartialCancelable;
    private Receipt receipt;
    private Checkout checkout;
    private String currency;
    private long totalAmount;
    private long balanceAmount;
    private long suppliedAmount;
    private long vat;
    private long taxFreeAmount;
    private String method;
    private String version;

}
