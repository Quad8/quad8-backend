package site.keydeuk.store.domain.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.keydeuk.store.domain.payment.dto.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentConfirmResponse {
    private String mId;
    private String lastTransactionKey;
    private String paymentKey;
    private String orderId;
    private String orderName;
    private long taxExemptionAmount;
    private PaymentStatus status;
    private ZonedDateTime requestedAt;
    private ZonedDateTime approvedAt;
    private boolean useEscrow;
    private boolean cultureExpense;
    private Card card;
    private VirtualAccount virtualAccount; // null 가능
    private Transfer transfer; // null 가능
    private MobilePhone mobilePhone; // null 가능
    private GiftCertificate giftCertificate; // null 가능
    private String cashReceipt; // null 가능
    private String cashReceipts; // null 가능
    private String discount; // null 가능
    private List<Cancel> cancels; // null 가능
    private String secret; // null 가능
    private PaymentType type;
    private EasyPay easyPay;
    private long easyPayAmount;
    private long easyPayDiscountAmount;
    private String country;
    private String failure; // null 가능
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
