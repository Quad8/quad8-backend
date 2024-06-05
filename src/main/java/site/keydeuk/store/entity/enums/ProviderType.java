package site.keydeuk.store.entity.enums;

import lombok.RequiredArgsConstructor;
import site.keydeuk.store.domain.user.dto.KakaoOAuth2UserInfo;
import site.keydeuk.store.domain.user.dto.OAuth2UserInfo;

import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public enum ProviderType {
    KAKAO(KakaoOAuth2UserInfo::new),
    GOOGLE(null),
    GITHUB(null);

    private final Function<Map<String, Object>, OAuth2UserInfo> function;

    public OAuth2UserInfo oAuth2UserInfoFrom(Map<String, Object> attributes) {
        return function.apply(attributes);
    }
}
