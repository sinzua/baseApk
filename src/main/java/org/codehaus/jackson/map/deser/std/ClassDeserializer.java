package org.codehaus.jackson.map.deser.std;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.util.ClassUtil;

@JacksonStdImpl
public class ClassDeserializer extends StdScalarDeserializer<Class<?>> {
    public ClassDeserializer() {
        super(Class.class);
    }

    public Class<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken curr = jp.getCurrentToken();
        if (curr == JsonToken.VALUE_STRING) {
            try {
                return ClassUtil.findClass(jp.getText());
            } catch (Throwable e) {
                throw ctxt.instantiationException(this._valueClass, e);
            }
        }
        throw ctxt.mappingException(this._valueClass, curr);
    }
}
