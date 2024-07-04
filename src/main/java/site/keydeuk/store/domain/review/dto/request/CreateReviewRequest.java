package site.keydeuk.store.domain.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import site.keydeuk.store.entity.Product;
import site.keydeuk.store.entity.Review;
import site.keydeuk.store.entity.User;

public record CreateReviewRequest(
        @Schema(description = "주문 번호", example = "4")
        @NotNull(message = "주문 번호는 필수 입력 항목입니다.")
        Long orderId,
        @Schema(description = "리뷰 내용", example = "Great product!")
        @NotEmpty(message = "리뷰 내용은 필수 입력 항목입니다.")
        String content,

        @Schema(description = "리뷰 점수", example = "4")
        @NotNull(message = "리뷰 점수는 필수 입력 항목입니다.")
        Integer score,

        @Schema(description = "옵션1", example = "1")
        Integer option1,

        @Schema(description = "옵션2", example = "1")
        Integer option2,

        @Schema(description = "옵션3", example = "1")
        Integer option3
) {
        public Review toEntity(CreateReviewRequest request, User user, Product product) {
                return Review.builder()
                        .orderId(request.orderId)
                        .content(request.content)
                        .score(request.score)
                        .option1(request.option1)
                        .option2(request.option2)
                        .option3(request.option3)
                        .user(user)
                        .product(product)
                        .build();
        }
}