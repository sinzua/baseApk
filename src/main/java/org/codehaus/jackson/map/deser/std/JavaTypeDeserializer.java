package org.codehaus.jackson.map.deser.std;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.type.JavaType;

public class JavaTypeDeserializer extends StdScalarDeserializer<JavaType> {
    public JavaTypeDeserializer() {
        super(JavaType.class);
    }

    public JavaType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken curr = jp.getCurrentToken();
        if (curr == JsonToken.VALUE_STRING) {
            String str = jp.getText().trim();
            if (str.length() == 0) {
                return (JavaType) getEmptyValue();
            }
            return ctxt.getTypeFactory().constructFromCanonical(str);
        } else if (curr == JsonToken.VALUE_EMBEDDED_OBJECT) {
            return (JavaType) jp.getEmbeddedObject();
        } else {
            throw ctxt.mappingException(this._valueClass);
        }
    }
}
