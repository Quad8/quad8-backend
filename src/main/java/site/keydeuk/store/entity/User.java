package site.keydeuk.store.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import site.keydeuk.store.common.entity.BaseTimeEntity;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String email;
    private String password;
    private LocalDate birth;
    private String phone;

    //TODO: enum 클래스 생성 -> gender
    private String gender;
    private RoleType role;
    private boolean status;
    private String nickname;

    private String imgUrl;

    private String provider;

    private String providerId;
}
