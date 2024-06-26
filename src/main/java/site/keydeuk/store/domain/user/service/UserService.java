package site.keydeuk.store.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.image.service.ImageService;
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
    private final ImageService imageService;

    @Transactional
    public Long join(JoinRequest joinRequest, MultipartFile imgFile) {
        joinValidate(joinRequest);
        log.info("Join User Info = {}", joinRequest);

        String encodePassword = getJoinPassword(joinRequest);
        User user = joinRequest.toEntity(encodePassword);

        if (imgFile == null || imgFile.isEmpty()) {
            //소셜 회원가입 햇거나 프로필 이미지 등록 안하는 사람
            User savedUser = userRepository.save(user);
            return savedUser.getId();
        }
        String imgUrl = imageService.uploadUserImage(imgFile);
        User updateUser = user.updateImgUrl(imgUrl);
        User savedUser = userRepository.save(updateUser);
        return savedUser.getId();


    }

    @Transactional
    public void updateProfile(Long userId, UpdateProfileRequest updateProfileRequest, MultipartFile imgFile) {
        log.info("User ID [{}] Update Profile = {}", userId, updateProfileRequest);
        updateProfileValidate(userId, updateProfileRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (imgFile == null || imgFile.isEmpty()) {
            user.updateProfile(updateProfileRequest, updateProfileRequest.imgUrl());
            return;
        }
        String imgUrl = imageService.uploadUserImage(imgFile);
        user.updateProfile(updateProfileRequest, imgUrl);
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

    private void updateProfileValidate(Long userId, UpdateProfileRequest updateProfileRequest) {
        if (getCurrentNickname(userId).equals(updateProfileRequest.nickname())) {
            return;
        }
        duplicateNickname(updateProfileRequest.nickname());
    }

    private String getCurrentNickname(Long userId) {
        return findById(userId).getNickname();
    }

    private String getJoinPassword(JoinRequest joinRequest) {
        return passwordEncoder.encode(joinRequest.password());
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
