package site.keydeuk.store.domain.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserTestResponseDto {

    private String email;

    private String password;

    private String nickname;
}
