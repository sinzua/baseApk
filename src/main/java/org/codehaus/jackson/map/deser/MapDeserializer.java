package org.codehaus.jackson.map.deser;

import java.lang.reflect.Constructor;
import java.util.Map;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.type.JavaType;

@Deprecated
public class MapDeserializer extends org.codehaus.jackson.map.deser.std.MapDeserializer {
    @Deprecated
    public MapDeserializer(JavaType mapType, Constructor<Map<Object, Object>> defCtor, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser) {
        super(mapType, (Constructor) defCtor, keyDeser, (JsonDeserializer) valueDeser, valueTypeDeser);
    }

    public MapDeserializer(JavaType mapType, ValueInstantiator valueInstantiator, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser) {
        super(mapType, valueInstantiator, keyDeser, (JsonDeserializer) valueDeser, valueTypeDeser);
    }

    protected MapDeserializer(MapDeserializer src) {
        super(src);
    }
}
