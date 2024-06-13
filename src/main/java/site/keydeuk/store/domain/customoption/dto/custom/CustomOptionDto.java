package site.keydeuk.store.domain.customoption.dto.custom;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomOptionDto {

    private String type;

    private String texture;

    private String boardColor;

    private String switchType;

    private Boolean hasPointKeyCap;

    private String baseKeyColor;

    private String pointKeyType;

    private String pointSetColor;

    private int price;

    private Object individualColor;

    private List<Integer> option;

    private String imgUrl;

}
