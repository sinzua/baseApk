package org.codehaus.jackson.map.module;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import org.codehaus.jackson.map.AbstractTypeResolver;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.type.JavaType;

public class SimpleAbstractTypeResolver extends AbstractTypeResolver {
    protected final HashMap<ClassKey, Class<?>> _mappings = new HashMap();

    public <T> SimpleAbstractTypeResolver addMapping(Class<T> superType, Class<? extends T> subType) {
        if (superType == subType) {
            throw new IllegalArgumentException("Can not add mapping from class to itself");
        } else if (!superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException("Can not add mapping from class " + superType.getName() + " to " + subType.getName() + ", as latter is not a subtype of former");
        } else if (Modifier.isAbstract(superType.getModifiers())) {
            this._mappings.put(new ClassKey(superType), subType);
            return this;
        } else {
            throw new IllegalArgumentException("Can not add mapping from class " + superType.getName() + " since it is not abstract");
        }
    }

    public JavaType findTypeMapping(DeserializationConfig config, JavaType type) {
        Class<?> dst = (Class) this._mappings.get(new ClassKey(type.getRawClass()));
        if (dst == null) {
            return null;
        }
        return type.narrowBy(dst);
    }

    public JavaType resolveAbstractType(DeserializationConfig config, JavaType type) {
        return null;
    }
}
