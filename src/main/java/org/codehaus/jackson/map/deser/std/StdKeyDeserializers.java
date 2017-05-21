package org.codehaus.jackson.map.deser.std;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.EnumResolver;
import org.codehaus.jackson.type.JavaType;

public class StdKeyDeserializers {
    protected final HashMap<JavaType, KeyDeserializer> _keyDeserializers = new HashMap();

    protected StdKeyDeserializers() {
        add(new BoolKD());
        add(new ByteKD());
        add(new CharKD());
        add(new ShortKD());
        add(new IntKD());
        add(new LongKD());
        add(new FloatKD());
        add(new DoubleKD());
        add(new DateKD());
        add(new CalendarKD());
        add(new UuidKD());
    }

    private void add(StdKeyDeserializer kdeser) {
        this._keyDeserializers.put(TypeFactory.defaultInstance().uncheckedSimpleType(kdeser.getKeyClass()), kdeser);
    }

    public static HashMap<JavaType, KeyDeserializer> constructAll() {
        return new StdKeyDeserializers()._keyDeserializers;
    }

    public static KeyDeserializer constructStringKeyDeserializer(DeserializationConfig config, JavaType type) {
        return StringKD.forType(type.getClass());
    }

    public static KeyDeserializer constructEnumKeyDeserializer(EnumResolver<?> enumResolver) {
        return new EnumKD(enumResolver, null);
    }

    public static KeyDeserializer constructEnumKeyDeserializer(EnumResolver<?> enumResolver, AnnotatedMethod factory) {
        return new EnumKD(enumResolver, factory);
    }

    public static KeyDeserializer findStringBasedKeyDeserializer(DeserializationConfig config, JavaType type) {
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspect(type);
        Constructor<?> ctor = beanDesc.findSingleArgConstructor(String.class);
        if (ctor != null) {
            if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
                ClassUtil.checkAndFixAccess(ctor);
            }
            return new StringCtorKeyDeserializer(ctor);
        }
        Method m = beanDesc.findFactoryMethod(String.class);
        if (m == null) {
            return null;
        }
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            ClassUtil.checkAndFixAccess(m);
        }
        return new StringFactoryKeyDeserializer(m);
    }
}
