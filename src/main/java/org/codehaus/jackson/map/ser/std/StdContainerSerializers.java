package org.codehaus.jackson.map.ser.std;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap;
import org.codehaus.jackson.type.JavaType;

public class StdContainerSerializers {

    @JacksonStdImpl
    public static class IndexedListSerializer extends AsArraySerializerBase<List<?>> {
        public IndexedListSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> valueSerializer) {
            super(List.class, elemType, staticTyping, vts, property, valueSerializer);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new IndexedListSerializer(this._elementType, this._staticTyping, vts, this._property, this._elementSerializer);
        }

        public void serializeContents(List<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            if (this._elementSerializer != null) {
                serializeContentsUsing(value, jgen, provider, this._elementSerializer);
            } else if (this._valueTypeSerializer != null) {
                serializeTypedContents(value, jgen, provider);
            } else {
                int len = value.size();
                if (len != 0) {
                    int i = 0;
                    try {
                        PropertySerializerMap serializers = this._dynamicSerializers;
                        while (i < len) {
                            Object elem = value.get(i);
                            if (elem == null) {
                                provider.defaultSerializeNull(jgen);
                            } else {
                                Class<?> cc = elem.getClass();
                                JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                                if (serializer == null) {
                                    if (this._elementType.hasGenericTypes()) {
                                        serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._elementType, cc), provider);
                                    } else {
                                        serializer = _findAndAddDynamic(serializers, (Class) cc, provider);
                                    }
                                    serializers = this._dynamicSerializers;
                                }
                                serializer.serialize(elem, jgen, provider);
                            }
                            i++;
                        }
                    } catch (Exception e) {
                        wrapAndThrow(provider, (Throwable) e, (Object) value, i);
                    }
                }
            }
        }

        public void serializeContentsUsing(List<?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser) throws IOException, JsonGenerationException {
            int len = value.size();
            if (len != 0) {
                TypeSerializer typeSer = this._valueTypeSerializer;
                for (int i = 0; i < len; i++) {
                    Object elem = value.get(i);
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
                }
            }
        }

        public void serializeTypedContents(List<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            int len = value.size();
            if (len != 0) {
                int i = 0;
                try {
                    TypeSerializer typeSer = this._valueTypeSerializer;
                    PropertySerializerMap serializers = this._dynamicSerializers;
                    while (i < len) {
                        Object elem = value.get(i);
                        if (elem == null) {
                            provider.defaultSerializeNull(jgen);
                        } else {
                            Class<?> cc = elem.getClass();
                            JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                            if (serializer == null) {
                                if (this._elementType.hasGenericTypes()) {
                                    serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._elementType, cc), provider);
                                } else {
                                    serializer = _findAndAddDynamic(serializers, (Class) cc, provider);
                                }
                                serializers = this._dynamicSerializers;
                            }
                            serializer.serializeWithType(elem, jgen, provider, typeSer);
                        }
                        i++;
                    }
                } catch (Exception e) {
                    wrapAndThrow(provider, (Throwable) e, (Object) value, i);
                }
            }
        }
    }

    @JacksonStdImpl
    public static class IteratorSerializer extends AsArraySerializerBase<Iterator<?>> {
        public IteratorSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
            super(Iterator.class, elemType, staticTyping, vts, property, null);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new IteratorSerializer(this._elementType, this._staticTyping, vts, this._property);
        }

        public void serializeContents(Iterator<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            if (value.hasNext()) {
                TypeSerializer typeSer = this._valueTypeSerializer;
                JsonSerializer<Object> prevSerializer = null;
                Class<?> prevClass = null;
                do {
                    Object elem = value.next();
                    if (elem == null) {
                        provider.defaultSerializeNull(jgen);
                    } else {
                        JsonSerializer<Object> currSerializer;
                        Class<?> cc = elem.getClass();
                        if (cc == prevClass) {
                            currSerializer = prevSerializer;
                        } else {
                            currSerializer = provider.findValueSerializer((Class) cc, this._property);
                            prevSerializer = currSerializer;
                            prevClass = cc;
                        }
                        if (typeSer == null) {
                            currSerializer.serialize(elem, jgen, provider);
                        } else {
                            currSerializer.serializeWithType(elem, jgen, provider, typeSer);
                        }
                    }
                } while (value.hasNext());
            }
        }
    }

    protected StdContainerSerializers() {
    }

    public static ContainerSerializerBase<?> indexedListSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> valueSerializer) {
        return new IndexedListSerializer(elemType, staticTyping, vts, property, valueSerializer);
    }

    public static ContainerSerializerBase<?> collectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> valueSerializer) {
        return new CollectionSerializer(elemType, staticTyping, vts, property, valueSerializer);
    }

    public static ContainerSerializerBase<?> iteratorSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
        return new IteratorSerializer(elemType, staticTyping, vts, property);
    }

    public static ContainerSerializerBase<?> iterableSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
        return new IterableSerializer(elemType, staticTyping, vts, property);
    }

    public static JsonSerializer<?> enumSetSerializer(JavaType enumType, BeanProperty property) {
        return new EnumSetSerializer(enumType, property);
    }
}
