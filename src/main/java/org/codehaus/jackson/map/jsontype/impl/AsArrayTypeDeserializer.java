package org.codehaus.jackson.map.jsontype.impl;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;
import org.codehaus.jackson.type.JavaType;

public class AsArrayTypeDeserializer extends TypeDeserializerBase {
    @Deprecated
    public AsArrayTypeDeserializer(JavaType bt, TypeIdResolver idRes, BeanProperty property) {
        this(bt, idRes, property, null);
    }

    public AsArrayTypeDeserializer(JavaType bt, TypeIdResolver idRes, BeanProperty property, Class<?> defaultImpl) {
        super(bt, idRes, property, defaultImpl);
    }

    public As getTypeInclusion() {
        return As.WRAPPER_ARRAY;
    }

    public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return _deserialize(jp, ctxt);
    }

    public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return _deserialize(jp, ctxt);
    }

    public Object deserializeTypedFromScalar(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return _deserialize(jp, ctxt);
    }

    public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return _deserialize(jp, ctxt);
    }

    private final Object _deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        boolean hadStartArray = jp.isExpectedStartArrayToken();
        Object value = _findDeserializer(ctxt, _locateTypeId(jp, ctxt)).deserialize(jp, ctxt);
        if (!hadStartArray || jp.nextToken() == JsonToken.END_ARRAY) {
            return value;
        }
        throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "expected closing END_ARRAY after type information and deserialized value");
    }

    protected final String _locateTypeId(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (jp.isExpectedStartArrayToken()) {
            if (jp.nextToken() == JsonToken.VALUE_STRING) {
                String result = jp.getText();
                jp.nextToken();
                return result;
            } else if ((this._idResolver instanceof TypeIdResolverBase) && this._defaultImpl != null) {
                return ((TypeIdResolverBase) this._idResolver).idFromBaseType();
            } else {
                throw ctxt.wrongTokenException(jp, JsonToken.VALUE_STRING, "need JSON String that contains type id (for subtype of " + baseTypeName() + ")");
            }
        } else if ((this._idResolver instanceof TypeIdResolverBase) && this._defaultImpl != null) {
            return ((TypeIdResolverBase) this._idResolver).idFromBaseType();
        } else {
            throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "need JSON Array to contain As.WRAPPER_ARRAY type information for class " + baseTypeName());
        }
    }
}
