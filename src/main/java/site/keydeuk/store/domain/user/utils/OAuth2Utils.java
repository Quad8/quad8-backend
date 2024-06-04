package site.keydeuk.store.domain.user.utils;

import site.keydeuk.store.entity.enums.SocialType;

/**
 * 소셜 타입 분기처리 진행
 */
public class OAuth2Utils {
    // registrationID 를 보고 어떤 소셜에서 인증을 했는지 반환
    public static SocialType getSocialType(String registrationId) {
        // Member 에서도 사용하기 위해 대문자로 변경
        if (registrationId != null) {
            registrationId = registrationId.toUpperCase();
        }

        if ("GOOGLE".equals(registrationId)) {
            return SocialType.GOOGLE;
        } else if ("KAKAO".equals(registrationId)) {
            return SocialType.KAKAO;
        } else if ("GITHUB".equals(registrationId)) {
            return SocialType.GITHUB;
        }
        return null;
    }
}
