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
    private String imageUrl;

    public ReviewImg(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
