package site.keydeuk.store.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    @JsonIgnore
    private ProductCategory productCategory;

    private String name;

    @ColumnDefault("1000")
    private int stocks;

    private Long price;

    @ColumnDefault("0")
    private int views;

    private String detailUrl;

    private String company;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<ProductImg> productImgs;

    @OneToMany(mappedBy = "product")
    private List<ProductSwitchOption> switchOptions;

    @JsonProperty("categoryId")
    public int getCategoryId(){
        return productCategory.getId();
    }

    public void setViews(int views){
        this.views = views;
    }
}
