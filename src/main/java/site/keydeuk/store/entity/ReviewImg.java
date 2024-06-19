package site.keydeuk.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review_img")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imgUrl;
    private Long reviewId;


    public ReviewImg(String imgUrl, Long reviewId) {
        this.imgUrl = imgUrl;
        this.reviewId = reviewId;
    }
}
