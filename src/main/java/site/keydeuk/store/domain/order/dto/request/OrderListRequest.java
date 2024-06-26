package site.keydeuk.store.domain.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderListRequest {
    @Schema(description = "현재 페이지, default 0", example = "0")
    private int page;

    @Schema(description = "개수, default 10", example = "10")
    private int size;

    @Schema(description = "리뷰 조회 시작 날짜", example = "2023-01-01T00:00:00")
    private LocalDateTime startDate;

    @Schema(description = "리뷰 조회 종료 날짜", example = "2023-12-31T23:59:59")
    private LocalDateTime endDate;
}
