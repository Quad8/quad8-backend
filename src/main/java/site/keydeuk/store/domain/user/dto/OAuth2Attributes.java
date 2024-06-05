package site.keydeuk.store.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import site.keydeuk.store.domain.user.utils.OAuth2Utils;
import site.keydeuk.store.entity.enums.ProviderType;

import java.util.Map;

@Getter
public class OAuth2Attributes {
    private final String nameAttributeKey;
    private final OAuth2UserInfo oAuth2UserInfo;

    @Builder
    public OAuth2Attributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    // OAuth2Utils 를 통해 분기처리 없이 생성된 OAuth2UserInfo 를 반환
    public static OAuth2Attributes of(ProviderType providerType, String userNameAttributeName,
                                      Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(OAuth2Utils.getOAuth2UserInfo(providerType, attributes))
                .build();
    }
}
