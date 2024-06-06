package site.keydeuk.store.domain.productimg.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Setter;
import java.util.ArrayList;

@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ThumbnailListDto {

    private ArrayList thumbnailList;
}
