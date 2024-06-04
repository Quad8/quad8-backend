package site.keydeuk.store.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.domain.user.dto.OAuth2UserInfo;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.User;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 유저 정보(attributes) 가져오기
        Map<String, Object> oAuth2UserAttributes = oAuth2User.getAttributes();
        // resistrationId 가져오기 -> registrationId = 어떤 소셜 로그인 이용했는지
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        //userNameAttributeName 가져오기
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);

        //TODO: 회원가입
        User user = getOrSave(oAuth2UserInfo);
        if (user.getNickname() == null) {

        }

        return new PrincipalDetails(user, oAuth2UserAttributes, userNameAttributeName);
    }

    private User getOrSave(OAuth2UserInfo oAuth2UserInfo) {
        Optional<User> user = userRepository.findByEmail(oAuth2UserInfo.email());

        if (user.isEmpty()) {
            User newUser = User.builder()
                    .email(oAuth2UserInfo.email())
                    .imgUrl(oAuth2UserInfo.profile())
                    .nickname(oAuth2UserInfo.name())
                    .build();
            log.info("{}", newUser);
            if (newUser.getNickname() == null) {

            }
            return userRepository.save(newUser);
        } else {
            return user.get();
        }
    }
}
