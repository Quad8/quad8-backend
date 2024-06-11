package site.keydeuk.store.domain.oauth2.dto;

import site.keydeuk.store.domain.oauth2.dto.OAuth2UserInfo;
import site.keydeuk.store.entity.enums.ProviderType;

import java.util.Map;

import static site.keydeuk.store.entity.enums.ProviderType.*;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {
    private final String providerId;
    private final Map<String, Object> properties;
    private final Map<String, Object> kakaoAccount;

    public KakaoOAuth2UserInfo(Map<String, Object> oAuth2UserAttributes) {
        this.providerId = String.valueOf(oAuth2UserAttributes.get("id"));
        this.properties = (Map<String, Object>) oAuth2UserAttributes.get("properties");
        this.kakaoAccount = (Map<String, Object>) oAuth2UserAttributes.get("kakao_account");
    }


    @Override
    public String getNickname() {
        return String.valueOf(properties.get("nickname"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(kakaoAccount.get("email"));
    }

    @Override
    public String getProfileImgUrl() {
        return String.valueOf(properties.get("profile_image"));
    }

    @Override
    public ProviderType getProvider() {
        return KAKAO;
    }

    @Override
    public String getProviderId() {
        return providerId;
    }
}
