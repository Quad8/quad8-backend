package site.keydeuk.store.domain.review.dto;

import site.keydeuk.store.entity.ReviewImg;

public record ReviewImgDto(
        Long id,
        String imageUrl
) {
    public static ReviewImgDto from(ReviewImg reviewImg) {
        return new ReviewImgDto(reviewImg.getId(), reviewImg.getImgUrl());
    }
}