package org.codehaus.jackson.map.deser;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.type.JavaType;

public class AbstractDeserializer extends JsonDeserializer<Object> {
    protected final boolean _acceptBoolean;
    protected final boolean _acceptDouble;
    protected final boolean _acceptInt;
    protected final boolean _acceptString;
    protected final JavaType _baseType;

    public AbstractDeserializer(JavaType bt) {
        boolean z;
        boolean z2 = false;
        this._baseType = bt;
        Class<?> cls = bt.getRawClass();
        this._acceptString = cls.isAssignableFrom(String.class);
        if (cls == Boolean.TYPE || cls.isAssignableFrom(Boolean.class)) {
            z = true;
        } else {
            z = false;
        }
        this._acceptBoolean = z;
        if (cls == Integer.TYPE || cls.isAssignableFrom(Integer.class)) {
            z = true;
        } else {
            z = false;
        }
        this._acceptInt = z;
        if (cls == Double.TYPE || cls.isAssignableFrom(Double.class)) {
            z2 = true;
        }
        this._acceptDouble = z2;
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        Object result = _deserializeIfNatural(jp, ctxt);
        return result != null ? result : typeDeserializer.deserializeTypedFromObject(jp, ctxt);
    }

    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        throw ctxt.instantiationException(this._baseType.getRawClass(), "abstract types can only be instantiated with additional type information");
    }

    protected Object _deserializeIfNatural(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        switch (jp.getCurrentToken()) {
            case VALUE_STRING:
                if (this._acceptString) {
                    return jp.getText();
                }
                break;
            case VALUE_NUMBER_INT:
                if (this._acceptInt) {
                    return Integer.valueOf(jp.getIntValue());
                }
                break;
            case VALUE_NUMBER_FLOAT:
                if (this._acceptDouble) {
                    return Double.valueOf(jp.getDoubleValue());
                }
                break;
            case VALUE_TRUE:
                if (this._acceptBoolean) {
                    return Boolean.TRUE;
                }
                break;
            case VALUE_FALSE:
                if (this._acceptBoolean) {
                    return Boolean.FALSE;
                }
                break;
        }
        return null;
    }
}
