package org.codehaus.jackson.map.ser.std;

import java.io.IOException;
import java.util.Collection;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;

@JacksonStdImpl
public class StringCollectionSerializer extends StaticListSerializerBase<Collection<String>> implements ResolvableSerializer {
    protected JsonSerializer<String> _serializer;

    @Deprecated
    public StringCollectionSerializer(BeanProperty property) {
        this(property, null);
    }

    public StringCollectionSerializer(BeanProperty property, JsonSerializer<?> ser) {
        super(Collection.class, property);
        this._serializer = ser;
    }

    protected JsonNode contentSchema() {
        return createSchemaNode("string", true);
    }

    public void resolve(SerializerProvider provider) throws JsonMappingException {
        if (this._serializer == null) {
            JsonSerializer<?> ser = provider.findValueSerializer(String.class, this._property);
            if (!isDefaultSerializer(ser)) {
                this._serializer = ser;
            }
        }
    }

    public void serialize(Collection<String> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartArray();
        if (this._serializer == null) {
            serializeContents(value, jgen, provider);
        } else {
            serializeUsingCustom(value, jgen, provider);
        }
        jgen.writeEndArray();
    }

    public void serializeWithType(Collection<String> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
        typeSer.writeTypePrefixForArray(value, jgen);
        if (this._serializer == null) {
            serializeContents(value, jgen, provider);
        } else {
            serializeUsingCustom(value, jgen, provider);
        }
        typeSer.writeTypeSuffixForArray(value, jgen);
    }

    private final void serializeContents(Collection<String> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        if (this._serializer != null) {
            serializeUsingCustom(value, jgen, provider);
            return;
        }
        int i = 0;
        for (String str : value) {
            if (str == null) {
                try {
                    provider.defaultSerializeNull(jgen);
                } catch (Exception e) {
                    wrapAndThrow(provider, (Throwable) e, (Object) value, i);
                }
            } else {
                jgen.writeString(str);
            }
            i++;
        }
    }

    private void serializeUsingCustom(Collection<String> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        JsonSerializer<String> ser = this._serializer;
        for (String str : value) {
            if (str == null) {
                try {
                    provider.defaultSerializeNull(jgen);
                } catch (Exception e) {
                    wrapAndThrow(provider, (Throwable) e, (Object) value, 0);
                }
            } else {
                ser.serialize(str, jgen, provider);
            }
        }
    }
}
