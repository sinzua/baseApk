package org.codehaus.jackson.map.ser.std;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
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
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap;
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap.SerializerAndMapResult;
import org.codehaus.jackson.map.ser.std.StdArraySerializers.ArraySerializerBase;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.schema.SchemaAware;
import org.codehaus.jackson.type.JavaType;

@JacksonStdImpl
public class ObjectArraySerializer extends ArraySerializerBase<Object[]> implements ResolvableSerializer {
    protected PropertySerializerMap _dynamicSerializers;
    protected JsonSerializer<Object> _elementSerializer;
    protected final JavaType _elementType;
    protected final boolean _staticTyping;

    @Deprecated
    public ObjectArraySerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
        this(elemType, staticTyping, vts, property, null);
    }

    public ObjectArraySerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> elementSerializer) {
        super(Object[].class, vts, property);
        this._elementType = elemType;
        this._staticTyping = staticTyping;
        this._dynamicSerializers = PropertySerializerMap.emptyMap();
        this._elementSerializer = elementSerializer;
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
        return new ObjectArraySerializer(this._elementType, this._staticTyping, vts, this._property, this._elementSerializer);
    }

    public void serializeContents(Object[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        int len = value.length;
        if (len != 0) {
            if (this._elementSerializer != null) {
                serializeContentsUsing(value, jgen, provider, this._elementSerializer);
            } else if (this._valueTypeSerializer != null) {
                serializeTypedContents(value, jgen, provider);
            } else {
                int i = 0;
                Object obj = null;
                try {
                    PropertySerializerMap serializers = this._dynamicSerializers;
                    while (i < len) {
                        obj = value[i];
                        if (obj == null) {
                            provider.defaultSerializeNull(jgen);
                        } else {
                            Class cc = obj.getClass();
                            JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                            if (serializer == null) {
                                if (this._elementType.hasGenericTypes()) {
                                    serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._elementType, cc), provider);
                                } else {
                                    serializer = _findAndAddDynamic(serializers, cc, provider);
                                }
                            }
                            serializer.serialize(obj, jgen, provider);
                        }
                        i++;
                    }
                } catch (IOException ioe) {
                    throw ioe;
                } catch (Throwable e) {
                    Throwable t = e;
                    while ((t instanceof InvocationTargetException) && t.getCause() != null) {
                        t = t.getCause();
                    }
                    if (t instanceof Error) {
                        throw ((Error) t);
                    }
                    throw JsonMappingException.wrapWithPath(t, obj, i);
                }
            }
        }
    }

    public void serializeContentsUsing(Object[] value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser) throws IOException, JsonGenerationException {
        int len = value.length;
        TypeSerializer typeSer = this._valueTypeSerializer;
        int i = 0;
        Object obj = null;
        while (i < len) {
            try {
                obj = value[i];
                if (obj == null) {
                    provider.defaultSerializeNull(jgen);
                } else if (typeSer == null) {
                    ser.serialize(obj, jgen, provider);
                } else {
                    ser.serializeWithType(obj, jgen, provider, typeSer);
                }
                i++;
            } catch (IOException ioe) {
                throw ioe;
            } catch (Throwable e) {
                Throwable t = e;
                while ((t instanceof InvocationTargetException) && t.getCause() != null) {
                    t = t.getCause();
                }
                if (t instanceof Error) {
                    throw ((Error) t);
                }
                throw JsonMappingException.wrapWithPath(t, obj, i);
            }
        }
    }

    public void serializeTypedContents(Object[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        int len = value.length;
        TypeSerializer typeSer = this._valueTypeSerializer;
        int i = 0;
        try {
            PropertySerializerMap serializers = this._dynamicSerializers;
            while (i < len) {
                Object elem = value[i];
                if (elem == null) {
                    provider.defaultSerializeNull(jgen);
                } else {
                    Class cc = elem.getClass();
                    JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                    if (serializer == null) {
                        serializer = _findAndAddDynamic(serializers, cc, provider);
                    }
                    serializer.serializeWithType(elem, jgen, provider, typeSer);
                }
                i++;
            }
        } catch (IOException ioe) {
            throw ioe;
        } catch (Throwable e) {
            Throwable t = e;
            while ((t instanceof InvocationTargetException) && t.getCause() != null) {
                t = t.getCause();
            }
            if (t instanceof Error) {
                throw ((Error) t);
            }
            throw JsonMappingException.wrapWithPath(t, null, 0);
        }
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        ObjectNode o = createSchemaNode("array", true);
        if (typeHint != null) {
            JavaType javaType = provider.constructType(typeHint);
            if (javaType.isArrayType()) {
                Class componentType = ((ArrayType) javaType).getContentType().getRawClass();
                if (componentType == Object.class) {
                    o.put("items", JsonSchema.getDefaultSchemaNode());
                } else {
                    JsonSerializer<Object> ser = provider.findValueSerializer(componentType, this._property);
                    o.put("items", ser instanceof SchemaAware ? ((SchemaAware) ser).getSchema(provider, null) : JsonSchema.getDefaultSchemaNode());
                }
            }
        }
        return o;
    }

    public void resolve(SerializerProvider provider) throws JsonMappingException {
        if (this._staticTyping && this._elementSerializer == null) {
            this._elementSerializer = provider.findValueSerializer(this._elementType, this._property);
        }
    }

    protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider) throws JsonMappingException {
        SerializerAndMapResult result = map.findAndAddSerializer((Class) type, provider, this._property);
        if (map != result.map) {
            this._dynamicSerializers = result.map;
        }
        return result.serializer;
    }

    protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider) throws JsonMappingException {
        SerializerAndMapResult result = map.findAndAddSerializer(type, provider, this._property);
        if (map != result.map) {
            this._dynamicSerializers = result.map;
        }
        return result.serializer;
    }
}
