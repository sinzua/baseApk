package org.codehaus.jackson.map.ser.std;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.type.JavaType;

public class EnumSetSerializer extends AsArraySerializerBase<EnumSet<? extends Enum<?>>> {
    public EnumSetSerializer(JavaType elemType, BeanProperty property) {
        super(EnumSet.class, elemType, true, null, property, null);
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
        return this;
    }

    public void serializeContents(EnumSet<? extends Enum<?>> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        JsonSerializer<Object> enumSer = this._elementSerializer;
        Iterator i$ = value.iterator();
        while (i$.hasNext()) {
            Enum<?> en = (Enum) i$.next();
            if (enumSer == null) {
                enumSer = provider.findValueSerializer(en.getDeclaringClass(), this._property);
            }
            enumSer.serialize(en, jgen, provider);
        }
    }
}
