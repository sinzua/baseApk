package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ClassIntrospector;
import org.codehaus.jackson.map.ClassIntrospector.MixInResolver;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.type.SimpleType;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class BasicClassIntrospector extends ClassIntrospector<BasicBeanDescription> {
    protected static final BasicBeanDescription BOOLEAN_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(Boolean.TYPE), AnnotatedClass.constructWithoutSuperTypes(Boolean.TYPE, null, null));
    @Deprecated
    public static final GetterMethodFilter DEFAULT_GETTER_FILTER = new GetterMethodFilter();
    @Deprecated
    public static final SetterAndGetterMethodFilter DEFAULT_SETTER_AND_GETTER_FILTER = new SetterAndGetterMethodFilter();
    @Deprecated
    public static final SetterMethodFilter DEFAULT_SETTER_FILTER = new SetterMethodFilter();
    protected static final BasicBeanDescription INT_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(Integer.TYPE), AnnotatedClass.constructWithoutSuperTypes(Integer.TYPE, null, null));
    protected static final BasicBeanDescription LONG_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(Long.TYPE), AnnotatedClass.constructWithoutSuperTypes(Long.TYPE, null, null));
    protected static final MethodFilter MINIMAL_FILTER = new MinimalMethodFilter();
    protected static final BasicBeanDescription STRING_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(String.class), AnnotatedClass.constructWithoutSuperTypes(String.class, null, null));
    public static final BasicClassIntrospector instance = new BasicClassIntrospector();

    @Deprecated
    public static class GetterMethodFilter implements MethodFilter {
        private GetterMethodFilter() {
        }

        public boolean includeMethod(Method m) {
            return ClassUtil.hasGetterSignature(m);
        }
    }

    private static class MinimalMethodFilter implements MethodFilter {
        private MinimalMethodFilter() {
        }

        public boolean includeMethod(Method m) {
            if (!Modifier.isStatic(m.getModifiers()) && m.getParameterTypes().length <= 2) {
                return true;
            }
            return false;
        }
    }

    @Deprecated
    public static class SetterMethodFilter implements MethodFilter {
        public boolean includeMethod(Method m) {
            if (Modifier.isStatic(m.getModifiers())) {
                return false;
            }
            switch (m.getParameterTypes().length) {
                case 1:
                    return true;
                case 2:
                    return true;
                default:
                    return false;
            }
        }
    }

    @Deprecated
    public static final class SetterAndGetterMethodFilter extends SetterMethodFilter {
        public boolean includeMethod(Method m) {
            if (super.includeMethod(m)) {
                return true;
            }
            if (!ClassUtil.hasGetterSignature(m)) {
                return false;
            }
            Class<?> rt = m.getReturnType();
            if (Collection.class.isAssignableFrom(rt) || Map.class.isAssignableFrom(rt)) {
                return true;
            }
            return false;
        }
    }

    public BasicBeanDescription forSerialization(SerializationConfig cfg, JavaType type, MixInResolver r) {
        BasicBeanDescription desc = _findCachedDesc(type);
        if (desc == null) {
            return BasicBeanDescription.forSerialization(collectProperties(cfg, type, r, true));
        }
        return desc;
    }

    public BasicBeanDescription forDeserialization(DeserializationConfig cfg, JavaType type, MixInResolver r) {
        BasicBeanDescription desc = _findCachedDesc(type);
        if (desc == null) {
            return BasicBeanDescription.forDeserialization(collectProperties(cfg, type, r, false));
        }
        return desc;
    }

    public BasicBeanDescription forCreation(DeserializationConfig cfg, JavaType type, MixInResolver r) {
        BasicBeanDescription desc = _findCachedDesc(type);
        if (desc == null) {
            return BasicBeanDescription.forDeserialization(collectProperties(cfg, type, r, false));
        }
        return desc;
    }

    public BasicBeanDescription forClassAnnotations(MapperConfig<?> cfg, JavaType type, MixInResolver r) {
        boolean useAnnotations = cfg.isAnnotationProcessingEnabled();
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        Class rawClass = type.getRawClass();
        if (!useAnnotations) {
            ai = null;
        }
        return BasicBeanDescription.forOtherUse(cfg, type, AnnotatedClass.construct(rawClass, ai, r));
    }

    public BasicBeanDescription forDirectClassAnnotations(MapperConfig<?> cfg, JavaType type, MixInResolver r) {
        boolean useAnnotations = cfg.isAnnotationProcessingEnabled();
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        Class rawClass = type.getRawClass();
        if (!useAnnotations) {
            ai = null;
        }
        return BasicBeanDescription.forOtherUse(cfg, type, AnnotatedClass.constructWithoutSuperTypes(rawClass, ai, r));
    }

    public POJOPropertiesCollector collectProperties(MapperConfig<?> config, JavaType type, MixInResolver r, boolean forSerialization) {
        AnnotatedClass ac = classWithCreators(config, type, r);
        ac.resolveMemberMethods(MINIMAL_FILTER);
        ac.resolveFields();
        return constructPropertyCollector(config, ac, type, forSerialization).collect();
    }

    protected POJOPropertiesCollector constructPropertyCollector(MapperConfig<?> config, AnnotatedClass ac, JavaType type, boolean forSerialization) {
        return new POJOPropertiesCollector(config, forSerialization, type, ac);
    }

    public AnnotatedClass classWithCreators(MapperConfig<?> config, JavaType type, MixInResolver r) {
        boolean useAnnotations = config.isAnnotationProcessingEnabled();
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        Class rawClass = type.getRawClass();
        if (!useAnnotations) {
            ai = null;
        }
        AnnotatedClass ac = AnnotatedClass.construct(rawClass, ai, r);
        ac.resolveMemberMethods(MINIMAL_FILTER);
        ac.resolveCreators(true);
        return ac;
    }

    protected BasicBeanDescription _findCachedDesc(JavaType type) {
        Class<?> cls = type.getRawClass();
        if (cls == String.class) {
            return STRING_DESC;
        }
        if (cls == Boolean.TYPE) {
            return BOOLEAN_DESC;
        }
        if (cls == Integer.TYPE) {
            return INT_DESC;
        }
        if (cls == Long.TYPE) {
            return LONG_DESC;
        }
        return null;
    }

    @Deprecated
    protected MethodFilter getSerializationMethodFilter(SerializationConfig cfg) {
        return DEFAULT_GETTER_FILTER;
    }

    @Deprecated
    protected MethodFilter getDeserializationMethodFilter(DeserializationConfig cfg) {
        if (cfg.isEnabled(Feature.USE_GETTERS_AS_SETTERS)) {
            return DEFAULT_SETTER_AND_GETTER_FILTER;
        }
        return DEFAULT_SETTER_FILTER;
    }
}
