package site.keydeuk.store.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.user.dto.JoinRequest;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.User;

import java.util.UUID;

import static site.keydeuk.store.common.response.ErrorCode.ALREADY_EXIST_EMAIL;
import static site.keydeuk.store.common.response.ErrorCode.ALREADY_EXIST_NICKNAME;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long join(JoinRequest joinRequest) {
        joinValidate(joinRequest);
        log.info("Join User Info = {}", joinRequest);
        String encodePassword = getJoinPassword();
        User user = joinRequest.toEntity(encodePassword);
        userRepository.save(user);
        return user.getId();
    }
    public boolean isExistNickname(String nickname) {
        log.info("Duplicated Check Nickname = {}", nickname);
        return userRepository.existsByNickname(nickname);
    }
    public boolean isExistEmail(String email) {
        log.info("Duplicated Check Email = {}", email);
        return userRepository.findByEmail(email).isPresent();
    }
    private void duplicateNickname(String nickname) {
        if (isExistNickname(nickname)) {
            throw new CustomException(ALREADY_EXIST_NICKNAME);
        }
    }
    private void joinValidate(JoinRequest joinRequest) {
        if (isExistEmail(joinRequest.email())) {
            throw new CustomException(ALREADY_EXIST_EMAIL);
        }
        duplicateNickname(joinRequest.nickname());
    }
    private String getJoinPassword() {
        return passwordEncoder.encode(UUID.randomUUID().toString());
    }


}
