package org.codehaus.jackson.map.ser.std;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

public abstract class NonTypedScalarSerializerBase<T> extends ScalarSerializerBase<T> {
    protected NonTypedScalarSerializerBase(Class<T> t) {
        super(t);
    }

    public final void serializeWithType(T value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
        serialize(value, jgen, provider);
    }
}
