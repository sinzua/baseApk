package org.codehaus.jackson.map.deser.std;

import java.io.IOException;
import java.util.EnumMap;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.util.EnumResolver;

public class EnumMapDeserializer extends StdDeserializer<EnumMap<?, ?>> {
    protected final Class<?> _enumClass;
    protected final JsonDeserializer<Enum<?>> _keyDeserializer;
    protected final JsonDeserializer<Object> _valueDeserializer;

    @Deprecated
    public EnumMapDeserializer(EnumResolver<?> enumRes, JsonDeserializer<Object> valueDeser) {
        this(enumRes.getEnumClass(), new EnumDeserializer(enumRes), valueDeser);
    }

    public EnumMapDeserializer(Class<?> enumClass, JsonDeserializer<?> keyDeserializer, JsonDeserializer<Object> valueDeser) {
        super(EnumMap.class);
        this._enumClass = enumClass;
        this._keyDeserializer = keyDeserializer;
        this._valueDeserializer = valueDeser;
    }

    public EnumMap<?, ?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
            throw ctxt.mappingException(EnumMap.class);
        }
        EnumMap result = constructMap();
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            Enum<?> key = (Enum) this._keyDeserializer.deserialize(jp, ctxt);
            if (key == null) {
                throw ctxt.weirdStringException(this._enumClass, "value not one of declared Enum instance names");
            }
            result.put(key, jp.nextToken() == JsonToken.VALUE_NULL ? null : this._valueDeserializer.deserialize(jp, ctxt));
        }
        return result;
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
    }

    private EnumMap<?, ?> constructMap() {
        return new EnumMap(this._enumClass);
    }
}
