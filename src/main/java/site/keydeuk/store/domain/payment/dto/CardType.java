package site.keydeuk.store.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import site.keydeuk.store.domain.payment.CardTypeDeserializer;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = CardTypeDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum CardType {
    CREDIT("신용"),
    CHECK("체크"),
    GIFT("기프트"),
    UNKNOWN("미확인");

    private static final Map<String, CardType> CACHE = Arrays.stream(CardType.values())
            .collect(Collectors.toUnmodifiableMap(cardType -> cardType.displayName, Function.identity()));

    private String displayName;

    public static CardType from(String displayName) {
        if (CACHE.containsKey(displayName)) {
            return CACHE.get(displayName);
        }
        throw new IllegalArgumentException("Unknown card type: " + displayName);
    }
}
