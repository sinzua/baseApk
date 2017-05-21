package org.codehaus.jackson.map.deser;

import java.lang.reflect.Constructor;
import java.util.Collection;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.type.JavaType;

@Deprecated
public class CollectionDeserializer extends org.codehaus.jackson.map.deser.std.CollectionDeserializer {
    @Deprecated
    public CollectionDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, Constructor<Collection<Object>> defCtor) {
        super(collectionType, (JsonDeserializer) valueDeser, valueTypeDeser, (Constructor) defCtor);
    }

    public CollectionDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, ValueInstantiator valueInstantiator) {
        super(collectionType, (JsonDeserializer) valueDeser, valueTypeDeser, valueInstantiator);
    }

    protected CollectionDeserializer(CollectionDeserializer src) {
        super(src);
    }
}
