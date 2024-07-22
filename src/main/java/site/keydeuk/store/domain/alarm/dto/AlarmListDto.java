package site.keydeuk.store.domain.alarm.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Setter;

import java.util.List;

@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AlarmListDto {

    private Long count;

    private List<AlarmDto> alarmDtoList;
}
