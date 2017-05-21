package org.codehaus.jackson.map.ser.std;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap;
import org.codehaus.jackson.type.JavaType;

public class CollectionSerializer extends AsArraySerializerBase<Collection<?>> {
    public CollectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> valueSerializer) {
        super(Collection.class, elemType, staticTyping, vts, property, valueSerializer);
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
        return new CollectionSerializer(this._elementType, this._staticTyping, vts, this._property, this._elementSerializer);
    }

    public void serializeContents(Collection<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        if (this._elementSerializer != null) {
            serializeContentsUsing(value, jgen, provider, this._elementSerializer);
            return;
        }
        Iterator<?> it = value.iterator();
        if (it.hasNext()) {
            PropertySerializerMap serializers = this._dynamicSerializers;
            TypeSerializer typeSer = this._valueTypeSerializer;
            int i = 0;
            do {
                Object elem = it.next();
                if (elem == null) {
                    provider.defaultSerializeNull(jgen);
                } else {
                    Class<?> cc = elem.getClass();
                    JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                    if (serializer == null) {
                        if (this._elementType.hasGenericTypes()) {
                            serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._elementType, cc), provider);
                        } else {
                            try {
                                serializer = _findAndAddDynamic(serializers, (Class) cc, provider);
                            } catch (Exception e) {
                                wrapAndThrow(provider, (Throwable) e, (Object) value, i);
                                return;
                            }
                        }
                        serializers = this._dynamicSerializers;
                    }
                    if (typeSer == null) {
                        serializer.serialize(elem, jgen, provider);
                    } else {
                        serializer.serializeWithType(elem, jgen, provider, typeSer);
                    }
                }
                i++;
            } while (it.hasNext());
        }
    }

    public void serializeContentsUsing(Collection<?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser) throws IOException, JsonGenerationException {
        Iterator<?> it = value.iterator();
        if (it.hasNext()) {
            TypeSerializer typeSer = this._valueTypeSerializer;
            int i = 0;
            do {
                Object elem = it.next();
                if (elem == null) {
                    try {
                        provider.defaultSerializeNull(jgen);
                    } catch (Exception e) {
                        wrapAndThrow(provider, (Throwable) e, (Object) value, i);
                    }
                } else if (typeSer == null) {
                    ser.serialize(elem, jgen, provider);
                } else {
                    ser.serializeWithType(elem, jgen, provider, typeSer);
                }
                i++;
            } while (it.hasNext());
        }
    }
}
