package site.keydeuk.store.domain.oauth2.utils;

import site.keydeuk.store.domain.oauth2.dto.OAuth2UserInfo;
import site.keydeuk.store.entity.enums.ProviderType;

import java.util.Map;

/**
 * 소셜 타입 분기처리 진행
 */
public class OAuth2Utils {

    // registrationID 를 보고 어떤 소셜에서 인증을 했는지 반환
    public static ProviderType getSocialType(String registrationId) {
        // Member 에서도 사용하기 위해 대문자로 변경
        if (registrationId != null) {
            registrationId = registrationId.toUpperCase();
        }
        return ProviderType.valueOf(registrationId);
    }

    // 소셜로부터 받은 정보들을 받아 파싱하는 OAuth2UserInfo 를 생성하는 메서드
    public static OAuth2UserInfo getOAuth2UserInfo(ProviderType providerType, Map<String, Object> attributes) {
        return providerType.oAuth2UserInfoFrom(attributes);
    }
}
