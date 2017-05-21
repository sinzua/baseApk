package org.codehaus.jackson.map.deser;

import org.codehaus.jackson.map.deser.std.ContainerDeserializerBase;

@Deprecated
public abstract class ContainerDeserializer<T> extends ContainerDeserializerBase<T> {
    protected ContainerDeserializer(Class<?> selfType) {
        super(selfType);
    }
}
