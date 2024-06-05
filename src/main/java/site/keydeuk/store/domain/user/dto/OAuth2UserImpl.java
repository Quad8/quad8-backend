package site.keydeuk.store.domain.user.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import site.keydeuk.store.entity.User;

import java.util.Collection;
import java.util.Map;

@Getter
public class OAuth2UserImpl extends DefaultOAuth2User {

    User user;  // 토큰 만들기 위해 Member 객체를 담아서 전달

    public OAuth2UserImpl(Collection<? extends GrantedAuthority> authorities,
                          Map<String, Object> attributes, String nameAttributeKey,
                          User user) {
        super(authorities, attributes, nameAttributeKey);
        this.user = user;
    }
}
