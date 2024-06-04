package site.keydeuk.store.domain.user.dto;

import jakarta.security.auth.message.AuthException;
import lombok.Builder;
import site.keydeuk.store.common.response.ErrorCode;

import java.util.Map;

@Builder
public record OAuth2UserInfo(
        String name,
        String email,
        String profile
){
    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) { // registration id별로 userInfo 생성
            case "kakao" -> ofKakao(attributes);
            default -> {
                try {
                    throw new AuthException(ErrorCode.ILLEGAL_REGISTRATION_ID.getMessage());
                } catch (AuthException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        return OAuth2UserInfo.builder()
                .name((String) properties.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .profile((String) properties.get("profile_image"))
                .build();
    }
}
