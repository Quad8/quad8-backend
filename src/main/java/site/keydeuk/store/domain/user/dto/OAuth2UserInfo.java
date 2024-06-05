package site.keydeuk.store.domain.user.dto;

import site.keydeuk.store.entity.enums.ProviderType;

public interface OAuth2UserInfo {
    String getNickname();

    String getEmail();

    String getProfileImgUrl();
    ProviderType getProvider();

    String getProviderId();
}
