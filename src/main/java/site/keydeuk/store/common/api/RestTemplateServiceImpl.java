package site.keydeuk.store.common.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import site.keydeuk.store.common.exception.CustomException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static site.keydeuk.store.common.response.ErrorCode.COMMON_SYSTEM_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestTemplateServiceImpl implements RestTemplateService {

    private final RestTemplate restTemplate;

    @Override
    public <T> Optional<T> get(String url, Class<T> type) {
        try {
            return Optional.ofNullable(restTemplate.getForObject(url, type));
        } catch (HttpClientErrorException e) {
            log.error("RestTemplate Get Exception Message = {}", e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("RestTemplate Get Exception", e);
            throw new CustomException(COMMON_SYSTEM_ERROR);
        }
    }
}
