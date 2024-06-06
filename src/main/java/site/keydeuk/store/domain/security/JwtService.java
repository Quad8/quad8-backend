package site.keydeuk.store.domain.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.common.response.ErrorCode;
import site.keydeuk.store.domain.security.dto.RefreshToken;
import site.keydeuk.store.domain.security.repository.JwtRepository;
import site.keydeuk.store.domain.security.utils.JwtUtils;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.User;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;

    public RefreshToken save(RefreshToken refreshToken) {
        return jwtRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return jwtRepository.findByToken(token);
    }

    public String renewToken(String refreshToken) {
        // token 이 존재하는지 찾고, 존재한다면 RefreshToken 안의 memberId 를 가져와서 member 를 찾은 후 AccessToken 생성
        RefreshToken token = this.findByToken(refreshToken).orElseThrow(NoSuchElementException::new);
        User user = userRepository.findById(token.getUserId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return JwtUtils.generateAccessToken(user);
    }
}
