package site.keydeuk.store.domain.security.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.common.security.authentication.token.dto.RefreshToken;
import site.keydeuk.store.domain.security.constants.JwtConstants;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class JwtRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public RefreshToken save(RefreshToken refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.refreshToken(), refreshToken.email());
        redisTemplate.expire(refreshToken.refreshToken(), JwtConstants.REFRESH_EXP_TIME, TimeUnit.MILLISECONDS); // 설정된 시간 동안 Redis에 저장
        return refreshToken;
    }

    public Optional<RefreshToken> findByToken(String refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String userId = valueOperations.get(refreshToken);

        if (Objects.isNull(userId)) {
            return Optional.empty();
        }
        return Optional.of(new RefreshToken(refreshToken,userId));
    }

    public void deleteByToken(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }
}
