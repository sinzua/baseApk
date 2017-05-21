package org.codehaus.jackson.map;

import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.text.DateFormat;
import java.util.HashMap;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ClassIntrospector.MixInResolver;
import org.codehaus.jackson.map.MapperConfig.Base;
import org.codehaus.jackson.map.MapperConfig.ConfigFeature;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class SerializationConfig extends Impl<Feature, SerializationConfig> {
    protected FilterProvider _filterProvider;
    protected Inclusion _serializationInclusion;
    protected Class<?> _serializationView;

    public enum Feature implements ConfigFeature {
        USE_ANNOTATIONS(true),
        AUTO_DETECT_GETTERS(true),
        AUTO_DETECT_IS_GETTERS(true),
        AUTO_DETECT_FIELDS(true),
        CAN_OVERRIDE_ACCESS_MODIFIERS(true),
        REQUIRE_SETTERS_FOR_GETTERS(false),
        WRITE_NULL_PROPERTIES(true),
        USE_STATIC_TYPING(false),
        DEFAULT_VIEW_INCLUSION(true),
        WRAP_ROOT_VALUE(false),
        INDENT_OUTPUT(false),
        SORT_PROPERTIES_ALPHABETICALLY(false),
        FAIL_ON_EMPTY_BEANS(true),
        WRAP_EXCEPTIONS(true),
        CLOSE_CLOSEABLE(false),
        FLUSH_AFTER_WRITE_VALUE(true),
        WRITE_DATES_AS_TIMESTAMPS(true),
        WRITE_DATE_KEYS_AS_TIMESTAMPS(false),
        WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS(false),
        WRITE_ENUMS_USING_TO_STRING(false),
        WRITE_ENUMS_USING_INDEX(false),
        WRITE_NULL_MAP_VALUES(true),
        WRITE_EMPTY_JSON_ARRAYS(true);
        
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

    public SerializationConfig(ClassIntrospector<? extends BeanDescription> intr, AnnotationIntrospector annIntr, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver, PropertyNamingStrategy propertyNamingStrategy, TypeFactory typeFactory, HandlerInstantiator handlerInstantiator) {
        super(intr, annIntr, vc, subtypeResolver, propertyNamingStrategy, typeFactory, handlerInstantiator, Impl.collectFeatureDefaults(Feature.class));
        this._serializationInclusion = null;
        this._filterProvider = null;
    }

    protected SerializationConfig(SerializationConfig src) {
        this(src, src._base);
    }

    protected SerializationConfig(SerializationConfig src, HashMap<ClassKey, Class<?>> mixins, SubtypeResolver str) {
        this(src, src._base);
        this._mixInAnnotations = mixins;
        this._subtypeResolver = str;
    }

    protected SerializationConfig(SerializationConfig src, Base base) {
        super(src, base, src._subtypeResolver);
        this._serializationInclusion = null;
        this._serializationInclusion = src._serializationInclusion;
        this._serializationView = src._serializationView;
        this._filterProvider = src._filterProvider;
    }

    protected SerializationConfig(SerializationConfig src, FilterProvider filters) {
        super(src);
        this._serializationInclusion = null;
        this._serializationInclusion = src._serializationInclusion;
        this._serializationView = src._serializationView;
        this._filterProvider = filters;
    }

    protected SerializationConfig(SerializationConfig src, Class<?> view) {
        super(src);
        this._serializationInclusion = null;
        this._serializationInclusion = src._serializationInclusion;
        this._serializationView = view;
        this._filterProvider = src._filterProvider;
    }

    protected SerializationConfig(SerializationConfig src, Inclusion incl) {
        super(src);
        this._serializationInclusion = null;
        this._serializationInclusion = incl;
        if (incl == Inclusion.NON_NULL) {
            this._featureFlags &= Feature.WRITE_NULL_PROPERTIES.getMask() ^ -1;
        } else {
            this._featureFlags |= Feature.WRITE_NULL_PROPERTIES.getMask();
        }
        this._serializationView = src._serializationView;
        this._filterProvider = src._filterProvider;
    }

    protected SerializationConfig(SerializationConfig src, int features) {
        super(src, features);
        this._serializationInclusion = null;
        this._serializationInclusion = src._serializationInclusion;
        this._serializationView = src._serializationView;
        this._filterProvider = src._filterProvider;
    }

    public SerializationConfig withClassIntrospector(ClassIntrospector<? extends BeanDescription> ci) {
        return new SerializationConfig(this, this._base.withClassIntrospector(ci));
    }

    public SerializationConfig withAnnotationIntrospector(AnnotationIntrospector ai) {
        return new SerializationConfig(this, this._base.withAnnotationIntrospector(ai));
    }

    public SerializationConfig withInsertedAnnotationIntrospector(AnnotationIntrospector ai) {
        return new SerializationConfig(this, this._base.withInsertedAnnotationIntrospector(ai));
    }

    public SerializationConfig withAppendedAnnotationIntrospector(AnnotationIntrospector ai) {
        return new SerializationConfig(this, this._base.withAppendedAnnotationIntrospector(ai));
    }

    public SerializationConfig withVisibilityChecker(VisibilityChecker<?> vc) {
        return new SerializationConfig(this, this._base.withVisibilityChecker(vc));
    }

    public SerializationConfig withVisibility(JsonMethod forMethod, Visibility visibility) {
        return new SerializationConfig(this, this._base.withVisibility(forMethod, visibility));
    }

    public SerializationConfig withTypeResolverBuilder(TypeResolverBuilder<?> trb) {
        return new SerializationConfig(this, this._base.withTypeResolverBuilder(trb));
    }

    public SerializationConfig withSubtypeResolver(SubtypeResolver str) {
        SerializationConfig cfg = new SerializationConfig(this);
        cfg._subtypeResolver = str;
        return cfg;
    }

    public SerializationConfig withPropertyNamingStrategy(PropertyNamingStrategy pns) {
        return new SerializationConfig(this, this._base.withPropertyNamingStrategy(pns));
    }

    public SerializationConfig withTypeFactory(TypeFactory tf) {
        return new SerializationConfig(this, this._base.withTypeFactory(tf));
    }

    public SerializationConfig withDateFormat(DateFormat df) {
        SerializationConfig cfg = new SerializationConfig(this, this._base.withDateFormat(df));
        if (df == null) {
            return cfg.with(Feature.WRITE_DATES_AS_TIMESTAMPS);
        }
        return cfg.without(Feature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public SerializationConfig withHandlerInstantiator(HandlerInstantiator hi) {
        return new SerializationConfig(this, this._base.withHandlerInstantiator(hi));
    }

    public SerializationConfig withFilters(FilterProvider filterProvider) {
        return new SerializationConfig(this, filterProvider);
    }

    public SerializationConfig withView(Class<?> view) {
        return new SerializationConfig(this, (Class) view);
    }

    public SerializationConfig withSerializationInclusion(Inclusion incl) {
        return new SerializationConfig(this, incl);
    }

    public SerializationConfig with(Feature... features) {
        int flags = this._featureFlags;
        for (Feature f : features) {
            flags |= f.getMask();
        }
        return new SerializationConfig(this, flags);
    }

    public SerializationConfig without(Feature... features) {
        int flags = this._featureFlags;
        for (Feature f : features) {
            flags &= f.getMask() ^ -1;
        }
        return new SerializationConfig(this, flags);
    }

    @Deprecated
    public void fromAnnotations(Class<?> cls) {
        AnnotationIntrospector ai = getAnnotationIntrospector();
        AnnotatedClass ac = AnnotatedClass.construct(cls, ai, null);
        this._base = this._base.withVisibilityChecker(ai.findAutoDetectVisibility(ac, getDefaultVisibilityChecker()));
        Inclusion incl = ai.findSerializationInclusion(ac, null);
        if (incl != this._serializationInclusion) {
            setSerializationInclusion(incl);
        }
        Typing typing = ai.findSerializationTyping(ac);
        if (typing != null) {
            set(Feature.USE_STATIC_TYPING, typing == Typing.STATIC);
        }
    }

    public SerializationConfig createUnshared(SubtypeResolver subtypeResolver) {
        HashMap<ClassKey, Class<?>> mixins = this._mixInAnnotations;
        this._mixInAnnotationsShared = true;
        return new SerializationConfig(this, mixins, subtypeResolver);
    }

    public AnnotationIntrospector getAnnotationIntrospector() {
        if (isEnabled(Feature.USE_ANNOTATIONS)) {
            return super.getAnnotationIntrospector();
        }
        return AnnotationIntrospector.nopInstance();
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
        return isEnabled(Feature.SORT_PROPERTIES_ALPHABETICALLY);
    }

    public VisibilityChecker<?> getDefaultVisibilityChecker() {
        VisibilityChecker<?> vchecker = super.getDefaultVisibilityChecker();
        if (!isEnabled(Feature.AUTO_DETECT_GETTERS)) {
            vchecker = vchecker.withGetterVisibility(Visibility.NONE);
        }
        if (!isEnabled(Feature.AUTO_DETECT_IS_GETTERS)) {
            vchecker = vchecker.withIsGetterVisibility(Visibility.NONE);
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

    public Class<?> getSerializationView() {
        return this._serializationView;
    }

    public Inclusion getSerializationInclusion() {
        if (this._serializationInclusion != null) {
            return this._serializationInclusion;
        }
        return isEnabled(Feature.WRITE_NULL_PROPERTIES) ? Inclusion.ALWAYS : Inclusion.NON_NULL;
    }

    @Deprecated
    public void setSerializationInclusion(Inclusion props) {
        this._serializationInclusion = props;
        if (props == Inclusion.NON_NULL) {
            disable(Feature.WRITE_NULL_PROPERTIES);
        } else {
            enable(Feature.WRITE_NULL_PROPERTIES);
        }
    }

    public FilterProvider getFilterProvider() {
        return this._filterProvider;
    }

    public <T extends BeanDescription> T introspect(JavaType type) {
        return getClassIntrospector().forSerialization(this, type, this);
    }

    public JsonSerializer<Object> serializerInstance(Annotated annotated, Class<? extends JsonSerializer<?>> serClass) {
        HandlerInstantiator hi = getHandlerInstantiator();
        if (hi != null) {
            JsonSerializer<?> ser = hi.serializerInstance(this, annotated, serClass);
            if (ser != null) {
                return ser;
            }
        }
        return (JsonSerializer) ClassUtil.createInstance(serClass, canOverrideAccessModifiers());
    }

    @Deprecated
    public final void setDateFormat(DateFormat df) {
        super.setDateFormat(df);
        set(Feature.WRITE_DATES_AS_TIMESTAMPS, df == null);
    }

    @Deprecated
    public void setSerializationView(Class<?> view) {
        this._serializationView = view;
    }

    public String toString() {
        return "[SerializationConfig: flags=0x" + Integer.toHexString(this._featureFlags) + RequestParameters.RIGHT_BRACKETS;
    }
}
