package org.codehaus.jackson.map.deser.std;

import java.io.IOException;
import java.util.EnumSet;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.util.EnumResolver;

public class EnumSetDeserializer extends StdDeserializer<EnumSet<?>> {
    protected final Class<Enum> _enumClass;
    protected final JsonDeserializer<Enum<?>> _enumDeserializer;

    public EnumSetDeserializer(EnumResolver enumRes) {
        this(enumRes.getEnumClass(), new EnumDeserializer(enumRes));
    }

    public EnumSetDeserializer(Class<?> enumClass, JsonDeserializer<?> deser) {
        super(EnumSet.class);
        this._enumClass = enumClass;
        this._enumDeserializer = deser;
    }

    public EnumSet<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (jp.isExpectedStartArrayToken()) {
            EnumSet result = constructSet();
            while (true) {
                JsonToken t = jp.nextToken();
                if (t == JsonToken.END_ARRAY) {
                    return result;
                }
                if (t == JsonToken.VALUE_NULL) {
                    throw ctxt.mappingException(this._enumClass);
                }
                result.add((Enum) this._enumDeserializer.deserialize(jp, ctxt));
            }
        } else {
            throw ctxt.mappingException(EnumSet.class);
        }
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }

    private EnumSet constructSet() {
        return EnumSet.noneOf(this._enumClass);
    }
}
