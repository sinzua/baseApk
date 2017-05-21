package org.codehaus.jackson.map.deser;

@Deprecated
public abstract class FromStringDeserializer<T> extends org.codehaus.jackson.map.deser.std.FromStringDeserializer<T> {
    protected FromStringDeserializer(Class<?> vc) {
        super(vc);
    }
}
