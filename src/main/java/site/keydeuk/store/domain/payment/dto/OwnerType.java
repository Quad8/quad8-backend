package site.keydeuk.store.domain.payment.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.RequiredArgsConstructor;
import site.keydeuk.store.domain.payment.OwnerTypeDeserializer;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@JsonDeserialize(using = OwnerTypeDeserializer.class)
public enum OwnerType {
    INDIVIDUAL("개인"),
    CORPORATE("법인"),
    UNKNOWN("미확인");

    private static final Map<String, OwnerType> CACHE = Arrays.stream(OwnerType.values())
            .collect(Collectors.toUnmodifiableMap(ownerType -> ownerType.displayName, Function.identity()));

    private final String displayName;

    public static OwnerType from(String displayName) {
        if (CACHE.containsKey(displayName)) {
            return CACHE.get(displayName);
        }
        throw new IllegalArgumentException("Unknown owner type: " + displayName);
    }
}
