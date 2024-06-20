package site.keydeuk.store.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.keydeuk.store.common.entity.BaseTimeEntity;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "community")
public class Community extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    private Integer customOptionId;

    private String title;

    private String content;

    private int viewCount;

    @OneToMany(mappedBy = "community")
    @JsonManagedReference
    private List<CommunityImg> communityImg;

    public void setViewCount(int viewCount){this.viewCount = viewCount;}

}
