package site.keydeuk.store.domain.payment.dto;

public class EasyPay {
    private EasyPayProvider provider;
    private long amount;
    private long discountAmount;

    private enum EasyPayProvider {

    }
}
