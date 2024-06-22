package site.keydeuk.store.domain.payment;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import site.keydeuk.store.domain.payment.dto.OwnerType;

import java.io.IOException;

public class OwnerTypeDeserializer extends StdDeserializer<OwnerType> {

    public OwnerTypeDeserializer() {
        this(null);
    }

    protected OwnerTypeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public OwnerType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String displayName = node.get("ownerType").asText();
        return OwnerType.from(displayName);
    }
}
