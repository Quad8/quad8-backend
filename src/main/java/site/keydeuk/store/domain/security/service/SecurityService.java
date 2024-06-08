package site.keydeuk.store.domain.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.keydeuk.store.common.api.RestTemplateService;
import site.keydeuk.store.common.security.authentication.token.service.TokenService;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.User;

import static site.keydeuk.store.common.response.ErrorCode.LOGIN_FAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final RestTemplateService restTemplateService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(LOGIN_FAIL.getMessage()));

        log.info("LoadUser User = {}", user);
        return new PrincipalDetails(user);
    }
//
//    public AuthenticationToken reissue(ReissueRequest reissueRequest) {
//        return tokenService.reissueToken(reissueRequest.refreshToken());
//    }

}
