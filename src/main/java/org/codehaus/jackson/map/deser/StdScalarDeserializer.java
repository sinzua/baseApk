package org.codehaus.jackson.map.deser;

import org.codehaus.jackson.map.deser.std.StdDeserializer;

@Deprecated
public abstract class StdScalarDeserializer<T> extends StdDeserializer<T> {
    protected StdScalarDeserializer(Class<?> vc) {
        super((Class) vc);
    }
}
