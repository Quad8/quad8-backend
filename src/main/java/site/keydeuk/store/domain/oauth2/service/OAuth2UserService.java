package site.keydeuk.store.domain.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.domain.oauth2.dto.OAuth2Attributes;
import site.keydeuk.store.domain.oauth2.dto.OAuth2UserInfo;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.domain.oauth2.utils.OAuth2Utils;
import site.keydeuk.store.entity.User;
import site.keydeuk.store.entity.enums.RoleType;
import site.keydeuk.store.entity.enums.ProviderType;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // resistrationId 가져오기 -> registrationId = 어떤 소셜 로그인 이용했는지
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        //userNameAttributeName 가져오기
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        ProviderType providerType = OAuth2Utils.getSocialType(registrationId);

        // 유저 정보(attributes) 가져오기
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("{}", attributes);
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(providerType, userNameAttributeName, attributes);
        OAuth2UserInfo oAuth2UserInfo = oAuth2Attributes.getOAuth2UserInfo();

        User user = getUser(oAuth2UserInfo);
        return new PrincipalDetails(user, attributes, userNameAttributeName);
    }

    private User getUser(OAuth2UserInfo oAuth2UserInfo) {
        String provider = oAuth2UserInfo.getProvider().name();
        return userRepository.findByProviderAndProviderId(provider, oAuth2UserInfo.getProviderId())
                .orElseGet(()-> User.builder()
                        .email(oAuth2UserInfo.getEmail())
                        .imgUrl(oAuth2UserInfo.getProfileImgUrl())
                        .nickname(oAuth2UserInfo.getNickname())
                        .role(RoleType.ROLE_GUEST)
                        .provider(provider)
                        .providerId(oAuth2UserInfo.getProviderId())
                        .build());
    }
}
