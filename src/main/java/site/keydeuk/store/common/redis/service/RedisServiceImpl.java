package site.keydeuk.store.common.redis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import site.keydeuk.store.common.exception.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static site.keydeuk.store.common.response.ErrorCode.COMMON_SYSTEM_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        log.info("Find Redis Key = [{}], Type = [{}]", key, type.getName());
        String serializedValue = redisTemplate.opsForValue().get(key);

        try {
            return Optional.of(objectMapper.readValue(serializedValue, type));
        } catch (IllegalArgumentException | InvalidFormatException e) {
            return Optional.empty();
        } catch (Exception e) {
            log.error("Redis Get Exception", e);
            throw new CustomException("Redis get() Error", COMMON_SYSTEM_ERROR);
        }
    }

    @Override
    public void set(String key, Object value, Long expiredTime) {
        log.info("Save Redis Key = [{}], Value = [{}], expiredTime = [{}]", key, value, expiredTime);
        try {
            String serializedValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, serializedValue, expiredTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis Set Exception", e);
            throw new CustomException("Redis get() Error", COMMON_SYSTEM_ERROR);
        }
    }

    @Override
    public boolean setIfAbsent(String key, Object value, Long expiredTime) {
        log.info("Save If Absent Redis Key = [{}], Value = [{}], expiredTime = [{}]", key, value, expiredTime);
        try {
            String serializedValue = objectMapper.writeValueAsString(value);
            return Boolean.TRUE.equals(
                    redisTemplate.opsForValue().setIfAbsent(
                            key,
                            serializedValue,
                            expiredTime, TimeUnit.SECONDS
                    ));
        } catch (Exception e) {
            log.error("Redis Set Exception", e);
            throw new CustomException("Redis get() Error", COMMON_SYSTEM_ERROR);
        }
    }

    @Override
    public boolean delete(String key) {
        log.info("Delete Redis Key = [{}]", key);
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    @Override
    public void addToSortedSet(String prefixKey, String key){
        log.info("[확인]: prefix {}, key {}", prefixKey,key);
        long now = System.currentTimeMillis();
        redisTemplate.opsForZSet().add(prefixKey,key,now);
    }

    @Override
    public void checkSizeAndRemoveKey(String prefixKey, int size){
        Long currentSize = redisTemplate.opsForZSet().size(prefixKey);

        log.info("[확인]: prefix {}, size {}, current:{}", prefixKey,size,currentSize);


        if (currentSize!= null && currentSize > size){
            // 오래된 키 삭제
            String oldestKey = redisTemplate.opsForZSet().range(prefixKey,0,0)
                .stream().findFirst().orElse(null);
            if (oldestKey!= null){
                redisTemplate.opsForZSet().remove(prefixKey, oldestKey);
                delete(oldestKey);
            }

        }
    }
}
