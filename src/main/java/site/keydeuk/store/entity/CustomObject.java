package site.keydeuk.store.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Document(collection = "custom_keyboard_option")
@NoArgsConstructor
@AllArgsConstructor
public class CustomObject {
    @Id
    private Integer id;

    private Object objects;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
