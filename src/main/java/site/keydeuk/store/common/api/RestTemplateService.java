package site.keydeuk.store.common.api;

import java.util.List;
import java.util.Optional;

public interface RestTemplateService {
    <T> Optional<T> get(String url, Class<T> type);
}
