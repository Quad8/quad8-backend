package site.keydeuk.store.domain.security.dto;

import lombok.Data;

@Data
public class RefreshToken {
    private String token;
    private Long userId;

    public RefreshToken(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }
}
