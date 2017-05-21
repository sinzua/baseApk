package org.codehaus.jackson.map.deser;

import java.io.IOException;
import org.codehaus.jackson.Base64Variants;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.deser.std.StdScalarDeserializer;
import org.codehaus.jackson.type.JavaType;

@Deprecated
public abstract class StdDeserializer<T> extends org.codehaus.jackson.map.deser.std.StdDeserializer<T> {

    @Deprecated
    @JacksonStdImpl
    public static final class StringDeserializer extends StdScalarDeserializer<String> {
        public StringDeserializer() {
            super(String.class);
        }

        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonToken curr = jp.getCurrentToken();
            if (curr == JsonToken.VALUE_STRING) {
                return jp.getText();
            }
            if (curr == JsonToken.VALUE_EMBEDDED_OBJECT) {
                Object ob = jp.getEmbeddedObject();
                if (ob == null) {
                    return null;
                }
                if (ob instanceof byte[]) {
                    return Base64Variants.getDefaultVariant().encode((byte[]) ob, false);
                }
                return ob.toString();
            } else if (curr.isScalarValue()) {
                return jp.getText();
            } else {
                throw ctxt.mappingException(this._valueClass, curr);
            }
        }

        public String deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
            return deserialize(jp, ctxt);
        }
    }

    @Deprecated
    @JacksonStdImpl
    public class CalendarDeserializer extends org.codehaus.jackson.map.deser.std.CalendarDeserializer {
    }

    @Deprecated
    @JacksonStdImpl
    public class ClassDeserializer extends org.codehaus.jackson.map.deser.std.ClassDeserializer {
    }

    protected StdDeserializer(Class<?> vc) {
        super((Class) vc);
    }

    protected StdDeserializer(JavaType valueType) {
        super(valueType);
    }
}
