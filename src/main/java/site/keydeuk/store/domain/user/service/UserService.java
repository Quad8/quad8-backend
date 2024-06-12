package site.keydeuk.store.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.user.dto.request.JoinRequest;
import site.keydeuk.store.domain.user.dto.request.UpdateProfileRequest;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.User;

import static site.keydeuk.store.common.response.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(JoinRequest joinRequest) {
        joinValidate(joinRequest);
        log.info("Join User Info = {}", joinRequest);

        String encodePassword = getJoinPassword(joinRequest);
        User user = joinRequest.toEntity(encodePassword);
        userRepository.save(user);
        return user.getId();
    }

    @Transactional
    public void updateProfile(Long userId, UpdateProfileRequest updateProfileRequest) {
        log.info("User ID [{}] Update Profile = {}", userId, updateProfileRequest);
        updateProfileValidate(updateProfileRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        user.updateProfile(updateProfileRequest.phone(), updateProfileRequest.gender(), updateProfileRequest.nickname(), updateProfileRequest.imgUrl());
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

    private void updateProfileValidate(UpdateProfileRequest updateProfileRequest) {
        duplicateNickname(updateProfileRequest.nickname());
    }

    private String getJoinPassword(JoinRequest joinRequest) {
        return passwordEncoder.encode(joinRequest.password());
    }

    public User findById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
