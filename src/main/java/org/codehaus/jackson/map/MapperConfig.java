package org.codehaus.jackson.map;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.AnnotationIntrospector.Pair;
import org.codehaus.jackson.map.ClassIntrospector.MixInResolver;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.jsontype.impl.StdSubtypeResolver;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.StdDateFormat;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

public abstract class MapperConfig<T extends MapperConfig<T>> implements MixInResolver {
    protected static final DateFormat DEFAULT_DATE_FORMAT = StdDateFormat.instance;
    protected Base _base;
    protected HashMap<ClassKey, Class<?>> _mixInAnnotations;
    protected boolean _mixInAnnotationsShared;
    protected SubtypeResolver _subtypeResolver;

    public static class Base {
        protected final AnnotationIntrospector _annotationIntrospector;
        protected final ClassIntrospector<? extends BeanDescription> _classIntrospector;
        protected final DateFormat _dateFormat;
        protected final HandlerInstantiator _handlerInstantiator;
        protected final PropertyNamingStrategy _propertyNamingStrategy;
        protected final TypeFactory _typeFactory;
        protected final TypeResolverBuilder<?> _typeResolverBuilder;
        protected final VisibilityChecker<?> _visibilityChecker;

        public Base(ClassIntrospector<? extends BeanDescription> ci, AnnotationIntrospector ai, VisibilityChecker<?> vc, PropertyNamingStrategy pns, TypeFactory tf, TypeResolverBuilder<?> typer, DateFormat dateFormat, HandlerInstantiator hi) {
            this._classIntrospector = ci;
            this._annotationIntrospector = ai;
            this._visibilityChecker = vc;
            this._propertyNamingStrategy = pns;
            this._typeFactory = tf;
            this._typeResolverBuilder = typer;
            this._dateFormat = dateFormat;
            this._handlerInstantiator = hi;
        }

        public Base withClassIntrospector(ClassIntrospector<? extends BeanDescription> ci) {
            return new Base(ci, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator);
        }

        public Base withAnnotationIntrospector(AnnotationIntrospector ai) {
            return new Base(this._classIntrospector, ai, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator);
        }

        public Base withInsertedAnnotationIntrospector(AnnotationIntrospector ai) {
            return withAnnotationIntrospector(Pair.create(ai, this._annotationIntrospector));
        }

        public Base withAppendedAnnotationIntrospector(AnnotationIntrospector ai) {
            return withAnnotationIntrospector(Pair.create(this._annotationIntrospector, ai));
        }

        public Base withVisibilityChecker(VisibilityChecker<?> vc) {
            return new Base(this._classIntrospector, this._annotationIntrospector, vc, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator);
        }

        public Base withVisibility(JsonMethod forMethod, Visibility visibility) {
            return new Base(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker.withVisibility(forMethod, visibility), this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator);
        }

        public Base withPropertyNamingStrategy(PropertyNamingStrategy pns) {
            return new Base(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, pns, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator);
        }

        public Base withTypeFactory(TypeFactory tf) {
            return new Base(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, tf, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator);
        }

        public Base withTypeResolverBuilder(TypeResolverBuilder<?> typer) {
            return new Base(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, typer, this._dateFormat, this._handlerInstantiator);
        }

        public Base withDateFormat(DateFormat df) {
            return new Base(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, df, this._handlerInstantiator);
        }

        public Base withHandlerInstantiator(HandlerInstantiator hi) {
            return new Base(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, hi);
        }

        public ClassIntrospector<? extends BeanDescription> getClassIntrospector() {
            return this._classIntrospector;
        }

        public AnnotationIntrospector getAnnotationIntrospector() {
            return this._annotationIntrospector;
        }

        public VisibilityChecker<?> getVisibilityChecker() {
            return this._visibilityChecker;
        }

        public PropertyNamingStrategy getPropertyNamingStrategy() {
            return this._propertyNamingStrategy;
        }

        public TypeFactory getTypeFactory() {
            return this._typeFactory;
        }

        public TypeResolverBuilder<?> getTypeResolverBuilder() {
            return this._typeResolverBuilder;
        }

        public DateFormat getDateFormat() {
            return this._dateFormat;
        }

        public HandlerInstantiator getHandlerInstantiator() {
            return this._handlerInstantiator;
        }
    }

    public interface ConfigFeature {
        boolean enabledByDefault();

        int getMask();
    }

    static abstract class Impl<CFG extends ConfigFeature, T extends Impl<CFG, T>> extends MapperConfig<T> {
        protected int _featureFlags;

        public abstract T with(CFG... cfgArr);

        public abstract T without(CFG... cfgArr);

        protected Impl(ClassIntrospector<? extends BeanDescription> ci, AnnotationIntrospector ai, VisibilityChecker<?> vc, SubtypeResolver str, PropertyNamingStrategy pns, TypeFactory tf, HandlerInstantiator hi, int defaultFeatures) {
            super(ci, ai, vc, str, pns, tf, hi);
            this._featureFlags = defaultFeatures;
        }

        protected Impl(Impl<CFG, T> src) {
            super(src);
            this._featureFlags = src._featureFlags;
        }

        protected Impl(Impl<CFG, T> src, int features) {
            super(src);
            this._featureFlags = features;
        }

        protected Impl(Impl<CFG, T> src, Base base, SubtypeResolver str) {
            super(src, base, str);
            this._featureFlags = src._featureFlags;
        }

        static <F extends Enum<F> & ConfigFeature> int collectFeatureDefaults(Class<F> enumClass) {
            int flags = 0;
            for (F value : (Enum[]) enumClass.getEnumConstants()) {
                if (((ConfigFeature) value).enabledByDefault()) {
                    flags |= ((ConfigFeature) value).getMask();
                }
            }
            return flags;
        }

        public boolean isEnabled(ConfigFeature f) {
            return (this._featureFlags & f.getMask()) != 0;
        }

        @Deprecated
        public void enable(CFG f) {
            this._featureFlags |= f.getMask();
        }

        @Deprecated
        public void disable(CFG f) {
            this._featureFlags &= f.getMask() ^ -1;
        }

        @Deprecated
        public void set(CFG f, boolean state) {
            if (state) {
                enable(f);
            } else {
                disable(f);
            }
        }
    }

    public abstract boolean canOverrideAccessModifiers();

    public abstract T createUnshared(SubtypeResolver subtypeResolver);

    @Deprecated
    public abstract void fromAnnotations(Class<?> cls);

    public abstract <DESC extends BeanDescription> DESC introspectClassAnnotations(JavaType javaType);

    public abstract <DESC extends BeanDescription> DESC introspectDirectClassAnnotations(JavaType javaType);

    public abstract boolean isAnnotationProcessingEnabled();

    public abstract boolean isEnabled(ConfigFeature configFeature);

    public abstract boolean shouldSortPropertiesAlphabetically();

    public abstract T withAnnotationIntrospector(AnnotationIntrospector annotationIntrospector);

    public abstract T withAppendedAnnotationIntrospector(AnnotationIntrospector annotationIntrospector);

    public abstract T withClassIntrospector(ClassIntrospector<? extends BeanDescription> classIntrospector);

    public abstract T withDateFormat(DateFormat dateFormat);

    public abstract T withHandlerInstantiator(HandlerInstantiator handlerInstantiator);

    public abstract T withInsertedAnnotationIntrospector(AnnotationIntrospector annotationIntrospector);

    public abstract T withPropertyNamingStrategy(PropertyNamingStrategy propertyNamingStrategy);

    public abstract T withSubtypeResolver(SubtypeResolver subtypeResolver);

    public abstract T withTypeFactory(TypeFactory typeFactory);

    public abstract T withTypeResolverBuilder(TypeResolverBuilder<?> typeResolverBuilder);

    public abstract T withVisibility(JsonMethod jsonMethod, Visibility visibility);

    public abstract T withVisibilityChecker(VisibilityChecker<?> visibilityChecker);

    protected MapperConfig(ClassIntrospector<? extends BeanDescription> ci, AnnotationIntrospector ai, VisibilityChecker<?> vc, SubtypeResolver str, PropertyNamingStrategy pns, TypeFactory tf, HandlerInstantiator hi) {
        this._base = new Base(ci, ai, vc, pns, tf, null, DEFAULT_DATE_FORMAT, hi);
        this._subtypeResolver = str;
        this._mixInAnnotationsShared = true;
    }

    protected MapperConfig(MapperConfig<T> src) {
        this(src, src._base, src._subtypeResolver);
    }

    protected MapperConfig(MapperConfig<T> src, Base base, SubtypeResolver str) {
        this._base = base;
        this._subtypeResolver = str;
        this._mixInAnnotationsShared = true;
        this._mixInAnnotations = src._mixInAnnotations;
    }

    public ClassIntrospector<? extends BeanDescription> getClassIntrospector() {
        return this._base.getClassIntrospector();
    }

    public AnnotationIntrospector getAnnotationIntrospector() {
        return this._base.getAnnotationIntrospector();
    }

    @Deprecated
    public final void insertAnnotationIntrospector(AnnotationIntrospector introspector) {
        this._base = this._base.withAnnotationIntrospector(Pair.create(introspector, getAnnotationIntrospector()));
    }

    @Deprecated
    public final void appendAnnotationIntrospector(AnnotationIntrospector introspector) {
        this._base = this._base.withAnnotationIntrospector(Pair.create(getAnnotationIntrospector(), introspector));
    }

    public VisibilityChecker<?> getDefaultVisibilityChecker() {
        return this._base.getVisibilityChecker();
    }

    public final PropertyNamingStrategy getPropertyNamingStrategy() {
        return this._base.getPropertyNamingStrategy();
    }

    public final HandlerInstantiator getHandlerInstantiator() {
        return this._base.getHandlerInstantiator();
    }

    public final void setMixInAnnotations(Map<Class<?>, Class<?>> sourceMixins) {
        HashMap<ClassKey, Class<?>> mixins = null;
        if (sourceMixins != null && sourceMixins.size() > 0) {
            mixins = new HashMap(sourceMixins.size());
            for (Entry<Class<?>, Class<?>> en : sourceMixins.entrySet()) {
                mixins.put(new ClassKey((Class) en.getKey()), en.getValue());
            }
        }
        this._mixInAnnotationsShared = false;
        this._mixInAnnotations = mixins;
    }

    public final void addMixInAnnotations(Class<?> target, Class<?> mixinSource) {
        if (this._mixInAnnotations == null) {
            this._mixInAnnotationsShared = false;
            this._mixInAnnotations = new HashMap();
        } else if (this._mixInAnnotationsShared) {
            this._mixInAnnotationsShared = false;
            this._mixInAnnotations = new HashMap(this._mixInAnnotations);
        }
        this._mixInAnnotations.put(new ClassKey(target), mixinSource);
    }

    public final Class<?> findMixInClassFor(Class<?> cls) {
        return this._mixInAnnotations == null ? null : (Class) this._mixInAnnotations.get(new ClassKey(cls));
    }

    public final int mixInCount() {
        return this._mixInAnnotations == null ? 0 : this._mixInAnnotations.size();
    }

    public final TypeResolverBuilder<?> getDefaultTyper(JavaType baseType) {
        return this._base.getTypeResolverBuilder();
    }

    public final SubtypeResolver getSubtypeResolver() {
        if (this._subtypeResolver == null) {
            this._subtypeResolver = new StdSubtypeResolver();
        }
        return this._subtypeResolver;
    }

    public final TypeFactory getTypeFactory() {
        return this._base.getTypeFactory();
    }

    public final JavaType constructType(Class<?> cls) {
        return getTypeFactory().constructType((Type) cls, (TypeBindings) null);
    }

    public final JavaType constructType(TypeReference<?> valueTypeRef) {
        return getTypeFactory().constructType(valueTypeRef.getType(), (TypeBindings) null);
    }

    public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
        return getTypeFactory().constructSpecializedType(baseType, subclass);
    }

    public final DateFormat getDateFormat() {
        return this._base.getDateFormat();
    }

    public <DESC extends BeanDescription> DESC introspectClassAnnotations(Class<?> cls) {
        return introspectClassAnnotations(constructType((Class) cls));
    }

    public <DESC extends BeanDescription> DESC introspectDirectClassAnnotations(Class<?> cls) {
        return introspectDirectClassAnnotations(constructType((Class) cls));
    }

    public TypeResolverBuilder<?> typeResolverBuilderInstance(Annotated annotated, Class<? extends TypeResolverBuilder<?>> builderClass) {
        HandlerInstantiator hi = getHandlerInstantiator();
        if (hi != null) {
            TypeResolverBuilder<?> builder = hi.typeResolverBuilderInstance(this, annotated, builderClass);
            if (builder != null) {
                return builder;
            }
        }
        return (TypeResolverBuilder) ClassUtil.createInstance(builderClass, canOverrideAccessModifiers());
    }

    public TypeIdResolver typeIdResolverInstance(Annotated annotated, Class<? extends TypeIdResolver> resolverClass) {
        HandlerInstantiator hi = getHandlerInstantiator();
        if (hi != null) {
            TypeIdResolver builder = hi.typeIdResolverInstance(this, annotated, resolverClass);
            if (builder != null) {
                return builder;
            }
        }
        return (TypeIdResolver) ClassUtil.createInstance(resolverClass, canOverrideAccessModifiers());
    }

    @Deprecated
    public final void setAnnotationIntrospector(AnnotationIntrospector ai) {
        this._base = this._base.withAnnotationIntrospector(ai);
    }

    @Deprecated
    public void setDateFormat(DateFormat df) {
        if (df == null) {
            df = DEFAULT_DATE_FORMAT;
        }
        this._base = this._base.withDateFormat(df);
    }
}
