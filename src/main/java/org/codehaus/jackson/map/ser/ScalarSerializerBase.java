package org.codehaus.jackson.map.ser;

import org.codehaus.jackson.map.ser.std.SerializerBase;

@Deprecated
public abstract class ScalarSerializerBase<T> extends SerializerBase<T> {
    protected ScalarSerializerBase(Class<T> t) {
        super((Class) t);
    }

    protected ScalarSerializerBase(Class<?> t, boolean dummy) {
        super((Class) t);
    }
}
