package org.codehaus.jackson.map.deser.std;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.type.JavaType;

public abstract class StdScalarDeserializer<T> extends StdDeserializer<T> {
    protected StdScalarDeserializer(Class<?> vc) {
        super((Class) vc);
    }

    protected StdScalarDeserializer(JavaType valueType) {
        super(valueType);
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromScalar(jp, ctxt);
    }
}
