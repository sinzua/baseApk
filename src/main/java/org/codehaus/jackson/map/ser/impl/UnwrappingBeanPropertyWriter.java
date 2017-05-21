package org.codehaus.jackson.map.ser.impl;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;

public class UnwrappingBeanPropertyWriter extends BeanPropertyWriter {
    public UnwrappingBeanPropertyWriter(BeanPropertyWriter base) {
        super(base);
    }

    public UnwrappingBeanPropertyWriter(BeanPropertyWriter base, JsonSerializer<Object> ser) {
        super(base, ser);
    }

    public BeanPropertyWriter withSerializer(JsonSerializer<Object> ser) {
        if (getClass() != UnwrappingBeanPropertyWriter.class) {
            throw new IllegalStateException("UnwrappingBeanPropertyWriter sub-class does not override 'withSerializer()'; needs to!");
        }
        if (!ser.isUnwrappingSerializer()) {
            ser = ser.unwrappingSerializer();
        }
        return new UnwrappingBeanPropertyWriter(this, ser);
    }

    public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov) throws Exception {
        Object value = get(bean);
        if (value != null) {
            if (value == bean) {
                _reportSelfReference(bean);
            }
            if (this._suppressableValue == null || !this._suppressableValue.equals(value)) {
                JsonSerializer<Object> ser = this._serializer;
                if (ser == null) {
                    Class<?> cls = value.getClass();
                    PropertySerializerMap map = this._dynamicSerializers;
                    ser = map.serializerFor(cls);
                    if (ser == null) {
                        ser = _findAndAddDynamic(map, cls, prov);
                    }
                }
                if (!ser.isUnwrappingSerializer()) {
                    jgen.writeFieldName(this._name);
                }
                if (this._typeSerializer == null) {
                    ser.serialize(value, jgen, prov);
                } else {
                    ser.serializeWithType(value, jgen, prov, this._typeSerializer);
                }
            }
        }
    }

    protected JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider) throws JsonMappingException {
        JsonSerializer<Object> serializer;
        if (this._nonTrivialBaseType != null) {
            serializer = provider.findValueSerializer(provider.constructSpecializedType(this._nonTrivialBaseType, type), (BeanProperty) this);
        } else {
            serializer = provider.findValueSerializer((Class) type, (BeanProperty) this);
        }
        if (!serializer.isUnwrappingSerializer()) {
            serializer = serializer.unwrappingSerializer();
        }
        this._dynamicSerializers = this._dynamicSerializers.newWith(type, serializer);
        return serializer;
    }
}
