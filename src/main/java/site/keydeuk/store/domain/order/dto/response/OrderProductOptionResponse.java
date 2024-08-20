package site.keydeuk.store.domain.order.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.keydeuk.store.entity.CustomOption;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"objectMapper"})
public class OrderProductOptionResponse {
    private Object individualColor;
    private CustomOption customOption;
    private ObjectMapper objectMapper;

    public OrderProductOptionResponse(CustomOption customOption, ObjectMapper objectMapper) {
        this.customOption = customOption;
        this.objectMapper = objectMapper;
    }

    @Override
    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Error converting to JSON: " + e.getMessage();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (OrderProductOptionResponse) obj;
        return Objects.equals(this.individualColor, that.individualColor) &&
                Objects.equals(this.customOption, that.customOption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(individualColor, customOption);
    }

}
