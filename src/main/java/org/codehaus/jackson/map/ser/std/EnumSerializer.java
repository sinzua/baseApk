package org.codehaus.jackson.map.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.util.EnumValues;
import org.codehaus.jackson.node.ArrayNode;

@JacksonStdImpl
public class EnumSerializer extends ScalarSerializerBase<Enum<?>> {
    protected final EnumValues _values;

    public EnumSerializer(EnumValues v) {
        super(Enum.class, false);
        this._values = v;
    }

    public static EnumSerializer construct(Class<Enum<?>> enumClass, SerializationConfig config, BasicBeanDescription beanDesc) {
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        return new EnumSerializer(config.isEnabled(Feature.WRITE_ENUMS_USING_TO_STRING) ? EnumValues.constructFromToString(enumClass, intr) : EnumValues.constructFromName(enumClass, intr));
    }

    public final void serialize(Enum<?> en, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        if (provider.isEnabled(Feature.WRITE_ENUMS_USING_INDEX)) {
            jgen.writeNumber(en.ordinal());
        } else {
            jgen.writeString(this._values.serializedValueFor(en));
        }
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        if (provider.isEnabled(Feature.WRITE_ENUMS_USING_INDEX)) {
            return createSchemaNode("integer", true);
        }
        JsonNode objectNode = createSchemaNode("string", true);
        if (typeHint == null || !provider.constructType(typeHint).isEnumType()) {
            return objectNode;
        }
        ArrayNode enumNode = objectNode.putArray("enum");
        for (SerializedString value : this._values.values()) {
            enumNode.add(value.getValue());
        }
        return objectNode;
    }

    public EnumValues getEnumValues() {
        return this._values;
    }
}
