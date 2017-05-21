package org.codehaus.jackson.map.ser;

import org.codehaus.jackson.type.JavaType;

@Deprecated
public abstract class SerializerBase<T> extends org.codehaus.jackson.map.ser.std.SerializerBase<T> {
    protected SerializerBase(Class<T> t) {
        super((Class) t);
    }

    protected SerializerBase(JavaType type) {
        super(type);
    }

    protected SerializerBase(Class<?> t, boolean dummy) {
        super(t, dummy);
    }
}
