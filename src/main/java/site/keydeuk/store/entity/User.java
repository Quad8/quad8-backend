package site.keydeuk.store.entity;

import jakarta.persistence.*;
import lombok.*;
import site.keydeuk.store.common.entity.BaseTimeEntity;
import site.keydeuk.store.domain.user.dto.request.UpdateProfileRequest;
import site.keydeuk.store.entity.enums.Gender;
import site.keydeuk.store.entity.enums.RoleType;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
@Getter
@Builder(toBuilder = true)
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

    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private RoleType role;
    private boolean status;
    private String nickname;

    private String imgUrl;

    private String provider;

    private String providerId;

    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birth=" + birth +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                ", role=" + role +
                ", status=" + status +
                ", nickname='" + nickname + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", provider='" + provider + '\'' +
                ", providerId='" + providerId + '\'' +
                '}';
    }

    public void updateProfile(UpdateProfileRequest request, String imgUrl) {
        this.phone = request.phone();
        this.gender = request.gender();
        this.nickname = request.nickname();
        this.imgUrl = imgUrl;
    }

    public User updateImgUrl(String imgUrl) {
        return this.toBuilder()
                .imgUrl(imgUrl)
                .build();
    }
}
