package site.keydeuk.store.domain.user.dto;

import site.keydeuk.store.entity.enums.Gender;
import site.keydeuk.store.entity.enums.RoleType;

import java.time.LocalDate;

public record UserDto(
    Long id,
    String email,
    LocalDate birth,
    String phone,
    Gender gender,
    RoleType role,
    Boolean status,
    String nickname,
    String provider,
    String providerId
){
}
