package site.keydeuk.store.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "community")
public class Community {

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

    @CreationTimestamp
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "community")
    @JsonManagedReference
    private List<CommunityImg> communityImg;

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
    public void setTitle(String title){this.title = title;}
    public void setContent(String content){this.content = content;}
    public void setUpdatedAt(LocalDateTime modifiedAt) {
        this.updatedAt = modifiedAt;
    }
}
