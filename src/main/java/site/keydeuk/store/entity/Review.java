package site.keydeuk.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.keydeuk.store.common.entity.BaseTimeEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review")
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Integer score;
    private Integer option1;
    private Integer option2;
    private Integer option3;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reviewId")
    private List<ReviewImg> reviewImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Product product;

    public void addReviewImgs(Collection<ReviewImg> reviewImgs) {
        this.reviewImages.addAll(reviewImgs);
    }

    public void update(String content, Integer score, Integer option1, Integer option2, Integer option3) {
        this.content = content;
        this.score = score;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
    }

    @Builder
    public Review(String content, Integer score, Integer option1, Integer option2, Integer option3, User user, Product product) {
        this.content = content;
        this.score = score;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.user = user;
        this.product = product;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", score=" + score +
                ", option1=" + option1 +
                ", option2=" + option2 +
                ", option3=" + option3 +
                ", reviewImages=" + reviewImages +
                ", user=" + user +
                ", product=" + product +
                '}';
    }
}
