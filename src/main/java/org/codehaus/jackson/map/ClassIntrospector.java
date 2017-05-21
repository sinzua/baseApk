package org.codehaus.jackson.map;

import org.codehaus.jackson.type.JavaType;

public abstract class ClassIntrospector<T extends BeanDescription> {

    public interface MixInResolver {
        Class<?> findMixInClassFor(Class<?> cls);
    }

    public abstract T forClassAnnotations(MapperConfig<?> mapperConfig, JavaType javaType, MixInResolver mixInResolver);

    public abstract T forCreation(DeserializationConfig deserializationConfig, JavaType javaType, MixInResolver mixInResolver);

    public abstract T forDeserialization(DeserializationConfig deserializationConfig, JavaType javaType, MixInResolver mixInResolver);

    public abstract T forDirectClassAnnotations(MapperConfig<?> mapperConfig, JavaType javaType, MixInResolver mixInResolver);

    public abstract T forSerialization(SerializationConfig serializationConfig, JavaType javaType, MixInResolver mixInResolver);

    protected ClassIntrospector() {
    }

    @Deprecated
    public T forClassAnnotations(MapperConfig<?> cfg, Class<?> cls, MixInResolver r) {
        return forClassAnnotations((MapperConfig) cfg, cfg.constructType((Class) cls), r);
    }

    @Deprecated
    public T forDirectClassAnnotations(MapperConfig<?> cfg, Class<?> cls, MixInResolver r) {
        return forDirectClassAnnotations((MapperConfig) cfg, cfg.constructType((Class) cls), r);
    }
}
