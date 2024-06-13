package site.keydeuk.store.domain.customoption.dto.custom;

import lombok.*;
import site.keydeuk.store.entity.CustomObject;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectDto {
    private Object individualColor;

    public CustomObject toEntity(Integer id){
        return CustomObject.builder()
                .id(id)
                .objects(this.individualColor)
                .build();
    }

}
