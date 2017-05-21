package org.codehaus.jackson.map.ser.std;

import org.codehaus.jackson.map.TypeSerializer;

public abstract class ContainerSerializerBase<T> extends SerializerBase<T> {
    public abstract ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer typeSerializer);

    protected ContainerSerializerBase(Class<T> t) {
        super((Class) t);
    }

    protected ContainerSerializerBase(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    public ContainerSerializerBase<?> withValueTypeSerializer(TypeSerializer vts) {
        return vts == null ? this : _withValueTypeSerializer(vts);
    }
}
