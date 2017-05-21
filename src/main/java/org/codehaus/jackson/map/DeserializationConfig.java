package org.codehaus.jackson.map;

import java.text.DateFormat;
import java.util.HashMap;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.Base64Variants;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ClassIntrospector.MixInResolver;
import org.codehaus.jackson.map.MapperConfig.Base;
import org.codehaus.jackson.map.MapperConfig.ConfigFeature;
import org.codehaus.jackson.map.deser.ValueInstantiator;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.NopAnnotationIntrospector;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.LinkedNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.type.JavaType;

public class DeserializationConfig extends Impl<Feature, DeserializationConfig> {
    protected final JsonNodeFactory _nodeFactory;
    protected LinkedNode<DeserializationProblemHandler> _problemHandlers;
    protected boolean _sortPropertiesAlphabetically;

    public enum Feature implements ConfigFeature {
        USE_ANNOTATIONS(true),
        AUTO_DETECT_SETTERS(true),
        AUTO_DETECT_CREATORS(true),
        AUTO_DETECT_FIELDS(true),
        USE_GETTERS_AS_SETTERS(true),
        CAN_OVERRIDE_ACCESS_MODIFIERS(true),
        USE_BIG_DECIMAL_FOR_FLOATS(false),
        USE_BIG_INTEGER_FOR_INTS(false),
        USE_JAVA_ARRAY_FOR_JSON_ARRAY(false),
        READ_ENUMS_USING_TO_STRING(false),
        FAIL_ON_UNKNOWN_PROPERTIES(true),
        FAIL_ON_NULL_FOR_PRIMITIVES(false),
        FAIL_ON_NUMBERS_FOR_ENUMS(false),
        WRAP_EXCEPTIONS(true),
        ACCEPT_SINGLE_VALUE_AS_ARRAY(false),
        UNWRAP_ROOT_VALUE(false),
        ACCEPT_EMPTY_STRING_AS_NULL_OBJECT(false);
        
        final boolean _defaultState;

        private Feature(boolean defaultState) {
            this._defaultState = defaultState;
        }

        public boolean enabledByDefault() {
            return this._defaultState;
        }

        public int getMask() {
            return 1 << ordinal();
        }
    }

    public DeserializationConfig(ClassIntrospector<? extends BeanDescription> intr, AnnotationIntrospector annIntr, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver, PropertyNamingStrategy propertyNamingStrategy, TypeFactory typeFactory, HandlerInstantiator handlerInstantiator) {
        super(intr, annIntr, vc, subtypeResolver, propertyNamingStrategy, typeFactory, handlerInstantiator, Impl.collectFeatureDefaults(Feature.class));
        this._nodeFactory = JsonNodeFactory.instance;
    }

    protected DeserializationConfig(DeserializationConfig src) {
        this(src, src._base);
    }

    private DeserializationConfig(DeserializationConfig src, HashMap<ClassKey, Class<?>> mixins, SubtypeResolver str) {
        this(src, src._base);
        this._mixInAnnotations = mixins;
        this._subtypeResolver = str;
    }

    protected DeserializationConfig(DeserializationConfig src, Base base) {
        super(src, base, src._subtypeResolver);
        this._problemHandlers = src._problemHandlers;
        this._nodeFactory = src._nodeFactory;
        this._sortPropertiesAlphabetically = src._sortPropertiesAlphabetically;
    }

    protected DeserializationConfig(DeserializationConfig src, JsonNodeFactory f) {
        super(src);
        this._problemHandlers = src._problemHandlers;
        this._nodeFactory = f;
        this._sortPropertiesAlphabetically = src._sortPropertiesAlphabetically;
    }

    protected DeserializationConfig(DeserializationConfig src, int featureFlags) {
        super(src, featureFlags);
        this._problemHandlers = src._problemHandlers;
        this._nodeFactory = src._nodeFactory;
        this._sortPropertiesAlphabetically = src._sortPropertiesAlphabetically;
    }

    protected DeserializationConfig passSerializationFeatures(int serializationFeatureFlags) {
        this._sortPropertiesAlphabetically = (org.codehaus.jackson.map.SerializationConfig.Feature.SORT_PROPERTIES_ALPHABETICALLY.getMask() & serializationFeatureFlags) != 0;
        return this;
    }

    public DeserializationConfig withClassIntrospector(ClassIntrospector<? extends BeanDescription> ci) {
        return new DeserializationConfig(this, this._base.withClassIntrospector(ci));
    }

    public DeserializationConfig withAnnotationIntrospector(AnnotationIntrospector ai) {
        return new DeserializationConfig(this, this._base.withAnnotationIntrospector(ai));
    }

    public DeserializationConfig withVisibilityChecker(VisibilityChecker<?> vc) {
        return new DeserializationConfig(this, this._base.withVisibilityChecker(vc));
    }

    public DeserializationConfig withVisibility(JsonMethod forMethod, Visibility visibility) {
        return new DeserializationConfig(this, this._base.withVisibility(forMethod, visibility));
    }

    public DeserializationConfig withTypeResolverBuilder(TypeResolverBuilder<?> trb) {
        return new DeserializationConfig(this, this._base.withTypeResolverBuilder(trb));
    }

    public DeserializationConfig withSubtypeResolver(SubtypeResolver str) {
        DeserializationConfig cfg = new DeserializationConfig(this);
        cfg._subtypeResolver = str;
        return cfg;
    }

    public DeserializationConfig withPropertyNamingStrategy(PropertyNamingStrategy pns) {
        return new DeserializationConfig(this, this._base.withPropertyNamingStrategy(pns));
    }

    public DeserializationConfig withTypeFactory(TypeFactory tf) {
        return tf == this._base.getTypeFactory() ? this : new DeserializationConfig(this, this._base.withTypeFactory(tf));
    }

    public DeserializationConfig withDateFormat(DateFormat df) {
        return df == this._base.getDateFormat() ? this : new DeserializationConfig(this, this._base.withDateFormat(df));
    }

    public DeserializationConfig withHandlerInstantiator(HandlerInstantiator hi) {
        return hi == this._base.getHandlerInstantiator() ? this : new DeserializationConfig(this, this._base.withHandlerInstantiator(hi));
    }

    public DeserializationConfig withInsertedAnnotationIntrospector(AnnotationIntrospector ai) {
        return new DeserializationConfig(this, this._base.withInsertedAnnotationIntrospector(ai));
    }

    public DeserializationConfig withAppendedAnnotationIntrospector(AnnotationIntrospector ai) {
        return new DeserializationConfig(this, this._base.withAppendedAnnotationIntrospector(ai));
    }

    public DeserializationConfig withNodeFactory(JsonNodeFactory f) {
        return new DeserializationConfig(this, f);
    }

    public DeserializationConfig with(Feature... features) {
        int flags = this._featureFlags;
        for (Feature f : features) {
            flags |= f.getMask();
        }
        return new DeserializationConfig(this, flags);
    }

    public DeserializationConfig without(Feature... features) {
        int flags = this._featureFlags;
        for (Feature f : features) {
            flags &= f.getMask() ^ -1;
        }
        return new DeserializationConfig(this, flags);
    }

    @Deprecated
    public void fromAnnotations(Class<?> cls) {
        AnnotationIntrospector ai = getAnnotationIntrospector();
        this._base = this._base.withVisibilityChecker(ai.findAutoDetectVisibility(AnnotatedClass.construct(cls, ai, null), getDefaultVisibilityChecker()));
    }

    public DeserializationConfig createUnshared(SubtypeResolver subtypeResolver) {
        HashMap<ClassKey, Class<?>> mixins = this._mixInAnnotations;
        this._mixInAnnotationsShared = true;
        return new DeserializationConfig(this, mixins, subtypeResolver);
    }

    public AnnotationIntrospector getAnnotationIntrospector() {
        if (isEnabled(Feature.USE_ANNOTATIONS)) {
            return super.getAnnotationIntrospector();
        }
        return NopAnnotationIntrospector.instance;
    }

    public <T extends BeanDescription> T introspectClassAnnotations(JavaType type) {
        return getClassIntrospector().forClassAnnotations((MapperConfig) this, type, (MixInResolver) this);
    }

    public <T extends BeanDescription> T introspectDirectClassAnnotations(JavaType type) {
        return getClassIntrospector().forDirectClassAnnotations((MapperConfig) this, type, (MixInResolver) this);
    }

    public boolean isAnnotationProcessingEnabled() {
        return isEnabled(Feature.USE_ANNOTATIONS);
    }

    public boolean canOverrideAccessModifiers() {
        return isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
    }

    public boolean shouldSortPropertiesAlphabetically() {
        return this._sortPropertiesAlphabetically;
    }

    public VisibilityChecker<?> getDefaultVisibilityChecker() {
        VisibilityChecker<?> vchecker = super.getDefaultVisibilityChecker();
        if (!isEnabled(Feature.AUTO_DETECT_SETTERS)) {
            vchecker = vchecker.withSetterVisibility(Visibility.NONE);
        }
        if (!isEnabled(Feature.AUTO_DETECT_CREATORS)) {
            vchecker = vchecker.withCreatorVisibility(Visibility.NONE);
        }
        if (isEnabled(Feature.AUTO_DETECT_FIELDS)) {
            return vchecker;
        }
        return vchecker.withFieldVisibility(Visibility.NONE);
    }

    public boolean isEnabled(Feature f) {
        return (this._featureFlags & f.getMask()) != 0;
    }

    @Deprecated
    public void enable(Feature f) {
        super.enable(f);
    }

    @Deprecated
    public void disable(Feature f) {
        super.disable(f);
    }

    @Deprecated
    public void set(Feature f, boolean state) {
        super.set(f, state);
    }

    public LinkedNode<DeserializationProblemHandler> getProblemHandlers() {
        return this._problemHandlers;
    }

    public void addHandler(DeserializationProblemHandler h) {
        if (!LinkedNode.contains(this._problemHandlers, h)) {
            this._problemHandlers = new LinkedNode(h, this._problemHandlers);
        }
    }

    public void clearHandlers() {
        this._problemHandlers = null;
    }

    public Base64Variant getBase64Variant() {
        return Base64Variants.getDefaultVariant();
    }

    public final JsonNodeFactory getNodeFactory() {
        return this._nodeFactory;
    }

    public <T extends BeanDescription> T introspect(JavaType type) {
        return getClassIntrospector().forDeserialization(this, type, this);
    }

    public <T extends BeanDescription> T introspectForCreation(JavaType type) {
        return getClassIntrospector().forCreation(this, type, this);
    }

    public JsonDeserializer<Object> deserializerInstance(Annotated annotated, Class<? extends JsonDeserializer<?>> deserClass) {
        HandlerInstantiator hi = getHandlerInstantiator();
        if (hi != null) {
            JsonDeserializer<?> deser = hi.deserializerInstance(this, annotated, deserClass);
            if (deser != null) {
                return deser;
            }
        }
        return (JsonDeserializer) ClassUtil.createInstance(deserClass, canOverrideAccessModifiers());
    }

    public KeyDeserializer keyDeserializerInstance(Annotated annotated, Class<? extends KeyDeserializer> keyDeserClass) {
        HandlerInstantiator hi = getHandlerInstantiator();
        if (hi != null) {
            KeyDeserializer keyDeser = hi.keyDeserializerInstance(this, annotated, keyDeserClass);
            if (keyDeser != null) {
                return keyDeser;
            }
        }
        return (KeyDeserializer) ClassUtil.createInstance(keyDeserClass, canOverrideAccessModifiers());
    }

    public ValueInstantiator valueInstantiatorInstance(Annotated annotated, Class<? extends ValueInstantiator> instClass) {
        HandlerInstantiator hi = getHandlerInstantiator();
        if (hi != null) {
            ValueInstantiator inst = hi.valueInstantiatorInstance(this, annotated, instClass);
            if (inst != null) {
                return inst;
            }
        }
        return (ValueInstantiator) ClassUtil.createInstance(instClass, canOverrideAccessModifiers());
    }
}
