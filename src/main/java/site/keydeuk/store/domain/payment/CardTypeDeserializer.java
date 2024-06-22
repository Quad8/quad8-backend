package site.keydeuk.store.domain.payment;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import site.keydeuk.store.domain.payment.dto.CardType;

import java.io.IOException;

public class CardTypeDeserializer extends StdDeserializer<CardType> {

    public CardTypeDeserializer() {
        this(null);
    }

    protected CardTypeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public CardType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return CardType.from(
                jsonParser.getCodec()
                        .<JsonNode>readTree(jsonParser).get("cardType")
                        .asText());
    }
}
