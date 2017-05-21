package org.codehaus.jackson.map.deser;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.AbstractTypeResolver;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.BeanProperty.Std;
import org.codehaus.jackson.map.BeanPropertyDefinition;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializerFactory;
import org.codehaus.jackson.map.DeserializerFactory.Config;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.Deserializers;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.KeyDeserializers;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.deser.SettableBeanProperty.FieldProperty;
import org.codehaus.jackson.map.deser.SettableBeanProperty.MethodProperty;
import org.codehaus.jackson.map.deser.SettableBeanProperty.SetterlessProperty;
import org.codehaus.jackson.map.deser.impl.CreatorCollector;
import org.codehaus.jackson.map.deser.impl.CreatorProperty;
import org.codehaus.jackson.map.deser.std.StdKeyDeserializers;
import org.codehaus.jackson.map.deser.std.ThrowableDeserializer;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.AnnotatedParameter;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.EnumResolver;
import org.codehaus.jackson.type.JavaType;

public class BeanDeserializerFactory extends BasicDeserializerFactory {
    private static final Class<?>[] INIT_CAUSE_PARAMS = new Class[]{Throwable.class};
    public static final BeanDeserializerFactory instance = new BeanDeserializerFactory(null);
    protected final Config _factoryConfig;

    public static class ConfigImpl extends Config {
        protected static final AbstractTypeResolver[] NO_ABSTRACT_TYPE_RESOLVERS = new AbstractTypeResolver[0];
        protected static final KeyDeserializers[] NO_KEY_DESERIALIZERS = new KeyDeserializers[0];
        protected static final BeanDeserializerModifier[] NO_MODIFIERS = new BeanDeserializerModifier[0];
        protected static final ValueInstantiators[] NO_VALUE_INSTANTIATORS = new ValueInstantiators[0];
        protected final AbstractTypeResolver[] _abstractTypeResolvers;
        protected final Deserializers[] _additionalDeserializers;
        protected final KeyDeserializers[] _additionalKeyDeserializers;
        protected final BeanDeserializerModifier[] _modifiers;
        protected final ValueInstantiators[] _valueInstantiators;

        public ConfigImpl() {
            this(null, null, null, null, null);
        }

        protected ConfigImpl(Deserializers[] allAdditionalDeserializers, KeyDeserializers[] allAdditionalKeyDeserializers, BeanDeserializerModifier[] modifiers, AbstractTypeResolver[] atr, ValueInstantiators[] vi) {
            if (allAdditionalDeserializers == null) {
                allAdditionalDeserializers = BeanDeserializerFactory.NO_DESERIALIZERS;
            }
            this._additionalDeserializers = allAdditionalDeserializers;
            if (allAdditionalKeyDeserializers == null) {
                allAdditionalKeyDeserializers = NO_KEY_DESERIALIZERS;
            }
            this._additionalKeyDeserializers = allAdditionalKeyDeserializers;
            if (modifiers == null) {
                modifiers = NO_MODIFIERS;
            }
            this._modifiers = modifiers;
            if (atr == null) {
                atr = NO_ABSTRACT_TYPE_RESOLVERS;
            }
            this._abstractTypeResolvers = atr;
            if (vi == null) {
                vi = NO_VALUE_INSTANTIATORS;
            }
            this._valueInstantiators = vi;
        }

        public Config withAdditionalDeserializers(Deserializers additional) {
            if (additional != null) {
                return new ConfigImpl((Deserializers[]) ArrayBuilders.insertInListNoDup(this._additionalDeserializers, additional), this._additionalKeyDeserializers, this._modifiers, this._abstractTypeResolvers, this._valueInstantiators);
            }
            throw new IllegalArgumentException("Can not pass null Deserializers");
        }

        public Config withAdditionalKeyDeserializers(KeyDeserializers additional) {
            if (additional == null) {
                throw new IllegalArgumentException("Can not pass null KeyDeserializers");
            }
            return new ConfigImpl(this._additionalDeserializers, (KeyDeserializers[]) ArrayBuilders.insertInListNoDup(this._additionalKeyDeserializers, additional), this._modifiers, this._abstractTypeResolvers, this._valueInstantiators);
        }

        public Config withDeserializerModifier(BeanDeserializerModifier modifier) {
            if (modifier == null) {
                throw new IllegalArgumentException("Can not pass null modifier");
            }
            return new ConfigImpl(this._additionalDeserializers, this._additionalKeyDeserializers, (BeanDeserializerModifier[]) ArrayBuilders.insertInListNoDup(this._modifiers, modifier), this._abstractTypeResolvers, this._valueInstantiators);
        }

        public Config withAbstractTypeResolver(AbstractTypeResolver resolver) {
            if (resolver == null) {
                throw new IllegalArgumentException("Can not pass null resolver");
            }
            return new ConfigImpl(this._additionalDeserializers, this._additionalKeyDeserializers, this._modifiers, (AbstractTypeResolver[]) ArrayBuilders.insertInListNoDup(this._abstractTypeResolvers, resolver), this._valueInstantiators);
        }

        public Config withValueInstantiators(ValueInstantiators instantiators) {
            if (instantiators == null) {
                throw new IllegalArgumentException("Can not pass null resolver");
            }
            return new ConfigImpl(this._additionalDeserializers, this._additionalKeyDeserializers, this._modifiers, this._abstractTypeResolvers, (ValueInstantiators[]) ArrayBuilders.insertInListNoDup(this._valueInstantiators, instantiators));
        }

        public boolean hasDeserializers() {
            return this._additionalDeserializers.length > 0;
        }

        public boolean hasKeyDeserializers() {
            return this._additionalKeyDeserializers.length > 0;
        }

        public boolean hasDeserializerModifiers() {
            return this._modifiers.length > 0;
        }

        public boolean hasAbstractTypeResolvers() {
            return this._abstractTypeResolvers.length > 0;
        }

        public boolean hasValueInstantiators() {
            return this._valueInstantiators.length > 0;
        }

        public Iterable<Deserializers> deserializers() {
            return ArrayBuilders.arrayAsIterable(this._additionalDeserializers);
        }

        public Iterable<KeyDeserializers> keyDeserializers() {
            return ArrayBuilders.arrayAsIterable(this._additionalKeyDeserializers);
        }

        public Iterable<BeanDeserializerModifier> deserializerModifiers() {
            return ArrayBuilders.arrayAsIterable(this._modifiers);
        }

        public Iterable<AbstractTypeResolver> abstractTypeResolvers() {
            return ArrayBuilders.arrayAsIterable(this._abstractTypeResolvers);
        }

        public Iterable<ValueInstantiators> valueInstantiators() {
            return ArrayBuilders.arrayAsIterable(this._valueInstantiators);
        }
    }

    @Deprecated
    public BeanDeserializerFactory() {
        this(null);
    }

    public BeanDeserializerFactory(Config config) {
        if (config == null) {
            config = new ConfigImpl();
        }
        this._factoryConfig = config;
    }

    public final Config getConfig() {
        return this._factoryConfig;
    }

    public DeserializerFactory withConfig(Config config) {
        if (this._factoryConfig == config) {
            return this;
        }
        if (getClass() != BeanDeserializerFactory.class) {
            throw new IllegalStateException("Subtype of BeanDeserializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalDeserializers': can not instantiate subtype with " + "additional deserializer definitions");
        }
        this(config);
        return this;
    }

    public KeyDeserializer createKeyDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        if (this._factoryConfig.hasKeyDeserializers()) {
            BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectClassAnnotations(type.getRawClass());
            for (KeyDeserializers d : this._factoryConfig.keyDeserializers()) {
                KeyDeserializer deser = d.findKeyDeserializer(type, config, beanDesc, property);
                if (deser != null) {
                    return deser;
                }
            }
        }
        Class<?> raw = type.getRawClass();
        if (raw == String.class || raw == Object.class) {
            return StdKeyDeserializers.constructStringKeyDeserializer(config, type);
        }
        KeyDeserializer kdes = (KeyDeserializer) _keyDeserializers.get(type);
        if (kdes != null) {
            return kdes;
        }
        if (type.isEnumType()) {
            return _createEnumKeyDeserializer(config, type, property);
        }
        return StdKeyDeserializers.findStringBasedKeyDeserializer(config, type);
    }

    private KeyDeserializer _createEnumKeyDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspect(type);
        Class<?> enumClass = type.getRawClass();
        EnumResolver<?> enumRes = constructEnumResolver(enumClass, config);
        for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
            if (config.getAnnotationIntrospector().hasCreatorAnnotation(factory)) {
                if (factory.getParameterCount() != 1 || !factory.getRawType().isAssignableFrom(enumClass)) {
                    throw new IllegalArgumentException("Unsuitable method (" + factory + ") decorated with @JsonCreator (for Enum type " + enumClass.getName() + ")");
                } else if (factory.getParameterType(0) != String.class) {
                    throw new IllegalArgumentException("Parameter #0 type for factory method (" + factory + ") not suitable, must be java.lang.String");
                } else {
                    if (config.canOverrideAccessModifiers()) {
                        ClassUtil.checkAndFixAccess(factory.getMember());
                    }
                    return StdKeyDeserializers.constructEnumKeyDeserializer(enumRes, factory);
                }
            }
        }
        return StdKeyDeserializers.constructEnumKeyDeserializer(enumRes);
    }

    protected JsonDeserializer<?> _findCustomArrayDeserializer(ArrayType type, DeserializationConfig config, DeserializerProvider provider, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findArrayDeserializer(type, config, provider, property, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<?> _findCustomCollectionDeserializer(CollectionType type, DeserializationConfig config, DeserializerProvider provider, BasicBeanDescription beanDesc, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findCollectionDeserializer(type, config, provider, beanDesc, property, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<?> _findCustomCollectionLikeDeserializer(CollectionLikeType type, DeserializationConfig config, DeserializerProvider provider, BasicBeanDescription beanDesc, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findCollectionLikeDeserializer(type, config, provider, beanDesc, property, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<?> _findCustomEnumDeserializer(Class<?> type, DeserializationConfig config, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findEnumDeserializer(type, config, beanDesc, property);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<?> _findCustomMapDeserializer(MapType type, DeserializationConfig config, DeserializerProvider provider, BasicBeanDescription beanDesc, BeanProperty property, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findMapDeserializer(type, config, provider, beanDesc, property, keyDeserializer, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<?> _findCustomMapLikeDeserializer(MapLikeType type, DeserializationConfig config, DeserializerProvider provider, BasicBeanDescription beanDesc, BeanProperty property, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findMapLikeDeserializer(type, config, provider, beanDesc, property, keyDeserializer, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<?> _findCustomTreeNodeDeserializer(Class<? extends JsonNode> type, DeserializationConfig config, BeanProperty property) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findTreeNodeDeserializer(type, config, property);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<Object> _findCustomBeanDeserializer(JavaType type, DeserializationConfig config, DeserializerProvider provider, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<Object> deser = d.findBeanDeserializer(type, config, provider, beanDesc, property);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    public JavaType mapAbstractType(DeserializationConfig config, JavaType type) throws JsonMappingException {
        JavaType next;
        while (true) {
            next = _mapAbstractType2(config, type);
            if (next == null) {
                return type;
            }
            Class<?> prevCls = type.getRawClass();
            Class<?> nextCls = next.getRawClass();
            if (prevCls != nextCls && prevCls.isAssignableFrom(nextCls)) {
                type = next;
            }
        }
        throw new IllegalArgumentException("Invalid abstract type resolution from " + type + " to " + next + ": latter is not a subtype of former");
    }

    public ValueInstantiator findValueInstantiator(DeserializationConfig config, BasicBeanDescription beanDesc) throws JsonMappingException {
        ValueInstantiator instantiator;
        AnnotatedClass ac = beanDesc.getClassInfo();
        ValueInstantiator instDef = config.getAnnotationIntrospector().findValueInstantiator(ac);
        if (instDef == null) {
            instantiator = constructDefaultValueInstantiator(config, beanDesc);
        } else if (instDef instanceof ValueInstantiator) {
            instantiator = instDef;
        } else if (instDef instanceof Class) {
            Class<?> cls = (Class) instDef;
            if (ValueInstantiator.class.isAssignableFrom(cls)) {
                instantiator = config.valueInstantiatorInstance(ac, cls);
            } else {
                throw new IllegalStateException("Invalid instantiator Class<?> returned for type " + beanDesc + ": " + cls.getName() + " not a ValueInstantiator");
            }
        } else {
            throw new IllegalStateException("Invalid value instantiator returned for type " + beanDesc + ": neither a Class nor ValueInstantiator");
        }
        if (this._factoryConfig.hasValueInstantiators()) {
            for (ValueInstantiators insts : this._factoryConfig.valueInstantiators()) {
                instantiator = insts.findValueInstantiator(config, beanDesc, instantiator);
                if (instantiator == null) {
                    throw new JsonMappingException("Broken registered ValueInstantiators (of type " + insts.getClass().getName() + "): returned null ValueInstantiator");
                }
            }
        }
        return instantiator;
    }

    public JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType type, BeanProperty property) throws JsonMappingException {
        if (type.isAbstract()) {
            type = mapAbstractType(config, type);
        }
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspect(type);
        JsonDeserializer<Object> ad = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (ad != null) {
            return ad;
        }
        JavaType newType = modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type, null);
        if (newType.getRawClass() != type.getRawClass()) {
            type = newType;
            beanDesc = (BasicBeanDescription) config.introspect(type);
        }
        JsonDeserializer<Object> custom = _findCustomBeanDeserializer(type, config, p, beanDesc, property);
        if (custom != null) {
            return custom;
        }
        if (type.isThrowable()) {
            return buildThrowableDeserializer(config, type, beanDesc, property);
        }
        if (type.isAbstract()) {
            JavaType concreteType = materializeAbstractType(config, beanDesc);
            if (concreteType != null) {
                return buildBeanDeserializer(config, concreteType, (BasicBeanDescription) config.introspect(concreteType), property);
            }
        }
        JsonDeserializer<Object> deser = findStdBeanDeserializer(config, p, type, property);
        if (deser != null) {
            return deser;
        }
        if (isPotentialBeanType(type.getRawClass())) {
            return buildBeanDeserializer(config, type, beanDesc, property);
        }
        return null;
    }

    protected JavaType _mapAbstractType2(DeserializationConfig config, JavaType type) throws JsonMappingException {
        Class<?> currClass = type.getRawClass();
        if (this._factoryConfig.hasAbstractTypeResolvers()) {
            for (AbstractTypeResolver resolver : this._factoryConfig.abstractTypeResolvers()) {
                JavaType concrete = resolver.findTypeMapping(config, type);
                if (concrete != null && concrete.getRawClass() != currClass) {
                    return concrete;
                }
            }
        }
        return null;
    }

    protected JavaType materializeAbstractType(DeserializationConfig config, BasicBeanDescription beanDesc) throws JsonMappingException {
        JavaType abstractType = beanDesc.getType();
        for (AbstractTypeResolver r : this._factoryConfig.abstractTypeResolvers()) {
            JavaType concrete = r.resolveAbstractType(config, abstractType);
            if (concrete != null) {
                return concrete;
            }
        }
        return null;
    }

    public JsonDeserializer<Object> buildBeanDeserializer(DeserializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        ValueInstantiator valueInstantiator = findValueInstantiator(config, beanDesc);
        if (type.isAbstract() && !valueInstantiator.canInstantiate()) {
            return new AbstractDeserializer(type);
        }
        BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(beanDesc);
        builder.setValueInstantiator(valueInstantiator);
        addBeanProps(config, beanDesc, builder);
        addReferenceProperties(config, beanDesc, builder);
        addInjectables(config, beanDesc, builder);
        if (this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
                builder = mod.updateBuilder(config, beanDesc, builder);
            }
        }
        JsonDeserializer<Object> deserializer = builder.build(property);
        if (!this._factoryConfig.hasDeserializerModifiers()) {
            return deserializer;
        }
        for (BeanDeserializerModifier mod2 : this._factoryConfig.deserializerModifiers()) {
            deserializer = mod2.modifyDeserializer(config, beanDesc, deserializer);
        }
        return deserializer;
    }

    public JsonDeserializer<Object> buildThrowableDeserializer(DeserializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(beanDesc);
        builder.setValueInstantiator(findValueInstantiator(config, beanDesc));
        addBeanProps(config, beanDesc, builder);
        AnnotatedMethod am = beanDesc.findMethod("initCause", INIT_CAUSE_PARAMS);
        if (am != null) {
            SettableBeanProperty prop = constructSettableProperty(config, beanDesc, "cause", am);
            if (prop != null) {
                builder.addOrReplaceProperty(prop, true);
            }
        }
        builder.addIgnorable("localizedMessage");
        builder.addIgnorable("message");
        builder.addIgnorable("suppressed");
        if (this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
                builder = mod.updateBuilder(config, beanDesc, builder);
            }
        }
        JsonDeserializer<?> build = builder.build(property);
        if (build instanceof BeanDeserializer) {
            build = new ThrowableDeserializer((BeanDeserializer) build);
        }
        if (this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod2 : this._factoryConfig.deserializerModifiers()) {
                build = mod2.modifyDeserializer(config, beanDesc, build);
            }
        }
        return build;
    }

    protected BeanDeserializerBuilder constructBeanDeserializerBuilder(BasicBeanDescription beanDesc) {
        return new BeanDeserializerBuilder(beanDesc);
    }

    protected ValueInstantiator constructDefaultValueInstantiator(DeserializationConfig config, BasicBeanDescription beanDesc) throws JsonMappingException {
        boolean fixAccess = config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
        CreatorCollector creators = new CreatorCollector(beanDesc, fixAccess);
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        if (beanDesc.getType().isConcrete()) {
            AnnotatedConstructor defaultCtor = beanDesc.findDefaultConstructor();
            if (defaultCtor != null) {
                if (fixAccess) {
                    ClassUtil.checkAndFixAccess(defaultCtor.getAnnotated());
                }
                creators.setDefaultConstructor(defaultCtor);
            }
        }
        VisibilityChecker<?> vchecker = config.getAnnotationIntrospector().findAutoDetectVisibility(beanDesc.getClassInfo(), config.getDefaultVisibilityChecker());
        _addDeserializerFactoryMethods(config, beanDesc, vchecker, intr, creators);
        _addDeserializerConstructors(config, beanDesc, vchecker, intr, creators);
        return creators.constructValueInstantiator(config);
    }

    protected void _addDeserializerConstructors(DeserializationConfig config, BasicBeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorCollector creators) throws JsonMappingException {
        for (AnnotatedMember ctor : beanDesc.getConstructors()) {
            int argCount = ctor.getParameterCount();
            if (argCount >= 1) {
                boolean isCreator = intr.hasCreatorAnnotation(ctor);
                boolean isVisible = vchecker.isCreatorVisible(ctor);
                if (argCount == 1) {
                    _handleSingleArgumentConstructor(config, beanDesc, vchecker, intr, creators, ctor, isCreator, isVisible);
                } else if (isCreator || isVisible) {
                    AnnotatedParameter nonAnnotatedParam = null;
                    int namedCount = 0;
                    int injectCount = 0;
                    CreatorProperty[] properties = new CreatorProperty[argCount];
                    for (int i = 0; i < argCount; i++) {
                        AnnotatedMember param = ctor.getParameter(i);
                        String name = param == null ? null : intr.findPropertyNameForParam(param);
                        Object injectId = intr.findInjectableValueId(param);
                        if (name != null && name.length() > 0) {
                            namedCount++;
                            properties[i] = constructCreatorProperty(config, beanDesc, name, i, param, injectId);
                        } else if (injectId != null) {
                            injectCount++;
                            properties[i] = constructCreatorProperty(config, beanDesc, name, i, param, injectId);
                        } else if (nonAnnotatedParam == null) {
                            AnnotatedMember nonAnnotatedParam2 = param;
                        }
                    }
                    if (isCreator || namedCount > 0 || injectCount > 0) {
                        if (namedCount + injectCount == argCount) {
                            creators.addPropertyCreator(ctor, properties);
                        } else if (namedCount == 0 && injectCount + 1 == argCount) {
                            throw new IllegalArgumentException("Delegated constructor with Injectables not yet supported (see [JACKSON-712]) for " + ctor);
                        } else {
                            throw new IllegalArgumentException("Argument #" + nonAnnotatedParam.getIndex() + " of constructor " + ctor + " has no property name annotation; must have name when multiple-paramater constructor annotated as Creator");
                        }
                    }
                    if (false) {
                        creators.addPropertyCreator(ctor, properties);
                    }
                }
            }
        }
    }

    protected boolean _handleSingleArgumentConstructor(DeserializationConfig config, BasicBeanDescription beanDesc, VisibilityChecker<?> visibilityChecker, AnnotationIntrospector intr, CreatorCollector creators, AnnotatedConstructor ctor, boolean isCreator, boolean isVisible) throws JsonMappingException {
        AnnotatedParameter param = ctor.getParameter(0);
        String name = intr.findPropertyNameForParam(param);
        if (intr.findInjectableValueId(param) != null || (name != null && name.length() > 0)) {
            creators.addPropertyCreator(ctor, new CreatorProperty[]{constructCreatorProperty(config, beanDesc, name, 0, param, injectId)});
            return true;
        }
        Class<?> type = ctor.getParameterClass(0);
        if (type == String.class) {
            if (isCreator || isVisible) {
                creators.addStringCreator(ctor);
            }
            return true;
        } else if (type == Integer.TYPE || type == Integer.class) {
            if (isCreator || isVisible) {
                creators.addIntCreator(ctor);
            }
            return true;
        } else if (type == Long.TYPE || type == Long.class) {
            if (isCreator || isVisible) {
                creators.addLongCreator(ctor);
            }
            return true;
        } else if (type == Double.TYPE || type == Double.class) {
            if (isCreator || isVisible) {
                creators.addDoubleCreator(ctor);
            }
            return true;
        } else if (!isCreator) {
            return false;
        } else {
            creators.addDelegatingCreator(ctor);
            return true;
        }
    }

    protected void _addDeserializerFactoryMethods(DeserializationConfig config, BasicBeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorCollector creators) throws JsonMappingException {
        for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
            int argCount = factory.getParameterCount();
            if (argCount >= 1) {
                AnnotatedParameter param;
                String name;
                boolean isCreator = intr.hasCreatorAnnotation(factory);
                if (argCount == 1) {
                    param = factory.getParameter(0);
                    name = intr.findPropertyNameForParam(param);
                    if (intr.findInjectableValueId(param) == null && (name == null || name.length() == 0)) {
                        _handleSingleArgumentFactory(config, beanDesc, vchecker, intr, creators, factory, isCreator);
                    }
                } else if (!intr.hasCreatorAnnotation(factory)) {
                    continue;
                }
                CreatorProperty[] properties = new CreatorProperty[argCount];
                for (int i = 0; i < argCount; i++) {
                    param = factory.getParameter(i);
                    name = intr.findPropertyNameForParam(param);
                    Object injectableId = intr.findInjectableValueId(param);
                    if ((name == null || name.length() == 0) && injectableId == null) {
                        throw new IllegalArgumentException("Argument #" + i + " of factory method " + factory + " has no property name annotation; must have when multiple-paramater static method annotated as Creator");
                    }
                    properties[i] = constructCreatorProperty(config, beanDesc, name, i, param, injectableId);
                }
                creators.addPropertyCreator(factory, properties);
            }
        }
    }

    protected boolean _handleSingleArgumentFactory(DeserializationConfig config, BasicBeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorCollector creators, AnnotatedMethod factory, boolean isCreator) throws JsonMappingException {
        Class<?> type = factory.getParameterClass(0);
        if (type == String.class) {
            if (!isCreator && !vchecker.isCreatorVisible((AnnotatedMember) factory)) {
                return true;
            }
            creators.addStringCreator(factory);
            return true;
        } else if (type == Integer.TYPE || type == Integer.class) {
            if (!isCreator && !vchecker.isCreatorVisible((AnnotatedMember) factory)) {
                return true;
            }
            creators.addIntCreator(factory);
            return true;
        } else if (type == Long.TYPE || type == Long.class) {
            if (!isCreator && !vchecker.isCreatorVisible((AnnotatedMember) factory)) {
                return true;
            }
            creators.addLongCreator(factory);
            return true;
        } else if (type == Double.TYPE || type == Double.class) {
            if (!isCreator && !vchecker.isCreatorVisible((AnnotatedMember) factory)) {
                return true;
            }
            creators.addDoubleCreator(factory);
            return true;
        } else if (type == Boolean.TYPE || type == Boolean.class) {
            if (!isCreator && !vchecker.isCreatorVisible((AnnotatedMember) factory)) {
                return true;
            }
            creators.addBooleanCreator(factory);
            return true;
        } else if (!intr.hasCreatorAnnotation(factory)) {
            return false;
        } else {
            creators.addDelegatingCreator(factory);
            return true;
        }
    }

    protected CreatorProperty constructCreatorProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, int index, AnnotatedParameter param, Object injectableValueId) throws JsonMappingException {
        JavaType t0 = config.getTypeFactory().constructType(param.getParameterType(), beanDesc.bindingsForBeanType());
        Std property = new Std(name, t0, beanDesc.getClassAnnotations(), param);
        JavaType type = resolveType(config, beanDesc, t0, param, property);
        if (type != t0) {
            property = property.withType(type);
        }
        JsonDeserializer<Object> deser = findDeserializerFromAnnotation(config, param, property);
        type = modifyTypeByAnnotation(config, param, type, name);
        TypeDeserializer typeDeser = (TypeDeserializer) type.getTypeHandler();
        if (typeDeser == null) {
            typeDeser = findTypeDeserializer(config, type, property);
        }
        CreatorProperty prop = new CreatorProperty(name, type, typeDeser, beanDesc.getClassAnnotations(), param, index, injectableValueId);
        if (deser != null) {
            return prop.withValueDeserializer((JsonDeserializer) deser);
        }
        return prop;
    }

    protected void addBeanProps(DeserializationConfig config, BasicBeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
        List<BeanPropertyDefinition> props = beanDesc.findProperties();
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        Boolean B = intr.findIgnoreUnknownProperties(beanDesc.getClassInfo());
        if (B != null) {
            builder.setIgnoreUnknownProperties(B.booleanValue());
        }
        Set<String> ignored = ArrayBuilders.arrayToSet(intr.findPropertiesToIgnore(beanDesc.getClassInfo()));
        for (String propName : ignored) {
            builder.addIgnorable(propName);
        }
        AnnotatedMethod anySetter = beanDesc.findAnySetter();
        Collection<String> ignored2 = anySetter == null ? beanDesc.getIgnoredPropertyNames() : beanDesc.getIgnoredPropertyNamesForDeser();
        if (ignored2 != null) {
            for (String propName2 : ignored2) {
                builder.addIgnorable(propName2);
            }
        }
        HashMap<Class<?>, Boolean> ignoredTypes = new HashMap();
        for (BeanPropertyDefinition property : props) {
            String name = property.getName();
            if (!ignored.contains(name)) {
                if (property.hasConstructorParameter()) {
                    builder.addCreatorProperty(property);
                } else if (property.hasSetter()) {
                    AnnotatedMethod setter = property.getSetter();
                    if (isIgnorableType(config, beanDesc, setter.getParameterClass(0), ignoredTypes)) {
                        builder.addIgnorable(name);
                    } else {
                        prop = constructSettableProperty(config, beanDesc, name, setter);
                        if (prop != null) {
                            builder.addProperty(prop);
                        }
                    }
                } else if (property.hasField()) {
                    AnnotatedField field = property.getField();
                    if (isIgnorableType(config, beanDesc, field.getRawType(), ignoredTypes)) {
                        builder.addIgnorable(name);
                    } else {
                        prop = constructSettableProperty(config, beanDesc, name, field);
                        if (prop != null) {
                            builder.addProperty(prop);
                        }
                    }
                }
            }
        }
        if (anySetter != null) {
            builder.setAnySetter(constructAnySetter(config, beanDesc, anySetter));
        }
        if (config.isEnabled(Feature.USE_GETTERS_AS_SETTERS)) {
            for (BeanPropertyDefinition property2 : props) {
                if (property2.hasGetter()) {
                    name = property2.getName();
                    if (!(builder.hasProperty(name) || ignored.contains(name))) {
                        AnnotatedMethod getter = property2.getGetter();
                        Class<?> rt = getter.getRawType();
                        if (!((!Collection.class.isAssignableFrom(rt) && !Map.class.isAssignableFrom(rt)) || ignored.contains(name) || builder.hasProperty(name))) {
                            builder.addProperty(constructSetterlessProperty(config, beanDesc, name, getter));
                        }
                    }
                }
            }
        }
    }

    protected void addReferenceProperties(DeserializationConfig config, BasicBeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
        Map<String, AnnotatedMember> refs = beanDesc.findBackReferenceProperties();
        if (refs != null) {
            for (Entry<String, AnnotatedMember> en : refs.entrySet()) {
                String name = (String) en.getKey();
                AnnotatedMember m = (AnnotatedMember) en.getValue();
                if (m instanceof AnnotatedMethod) {
                    builder.addBackReferenceProperty(name, constructSettableProperty(config, beanDesc, m.getName(), (AnnotatedMethod) m));
                } else {
                    builder.addBackReferenceProperty(name, constructSettableProperty(config, beanDesc, m.getName(), (AnnotatedField) m));
                }
            }
        }
    }

    protected void addInjectables(DeserializationConfig config, BasicBeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
        Map<Object, AnnotatedMember> raw = beanDesc.findInjectables();
        if (raw != null) {
            boolean fixAccess = config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
            for (Entry<Object, AnnotatedMember> entry : raw.entrySet()) {
                AnnotatedMember m = (AnnotatedMember) entry.getValue();
                if (fixAccess) {
                    m.fixAccess();
                }
                builder.addInjectable(m.getName(), beanDesc.resolveType(m.getGenericType()), beanDesc.getClassAnnotations(), m, entry.getKey());
            }
        }
    }

    protected SettableAnyProperty constructAnySetter(DeserializationConfig config, BasicBeanDescription beanDesc, AnnotatedMethod setter) throws JsonMappingException {
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            setter.fixAccess();
        }
        JavaType type = beanDesc.bindingsForBeanType().resolveType(setter.getParameterType(1));
        BeanProperty property = new Std(setter.getName(), type, beanDesc.getClassAnnotations(), setter);
        type = resolveType(config, beanDesc, type, setter, property);
        JsonDeserializer deser = findDeserializerFromAnnotation(config, setter, property);
        if (deser != null) {
            return new SettableAnyProperty(property, setter, type, deser);
        }
        return new SettableAnyProperty(property, setter, modifyTypeByAnnotation(config, setter, type, property.getName()), null);
    }

    protected SettableBeanProperty constructSettableProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, AnnotatedMethod setter) throws JsonMappingException {
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            setter.fixAccess();
        }
        JavaType t0 = beanDesc.bindingsForBeanType().resolveType(setter.getParameterType(0));
        Std property = new Std(name, t0, beanDesc.getClassAnnotations(), setter);
        JavaType type = resolveType(config, beanDesc, t0, setter, property);
        if (type != t0) {
            property = property.withType(type);
        }
        JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(config, setter, property);
        type = modifyTypeByAnnotation(config, setter, type, name);
        SettableBeanProperty prop = new MethodProperty(name, type, (TypeDeserializer) type.getTypeHandler(), beanDesc.getClassAnnotations(), setter);
        if (propDeser != null) {
            prop = prop.withValueDeserializer(propDeser);
        }
        ReferenceProperty ref = config.getAnnotationIntrospector().findReferenceType(setter);
        if (ref != null && ref.isManagedReference()) {
            prop.setManagedReferenceName(ref.getName());
        }
        return prop;
    }

    protected SettableBeanProperty constructSettableProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, AnnotatedField field) throws JsonMappingException {
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            field.fixAccess();
        }
        JavaType t0 = beanDesc.bindingsForBeanType().resolveType(field.getGenericType());
        Std property = new Std(name, t0, beanDesc.getClassAnnotations(), field);
        JavaType type = resolveType(config, beanDesc, t0, field, property);
        if (type != t0) {
            property = property.withType(type);
        }
        JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(config, field, property);
        type = modifyTypeByAnnotation(config, field, type, name);
        SettableBeanProperty prop = new FieldProperty(name, type, (TypeDeserializer) type.getTypeHandler(), beanDesc.getClassAnnotations(), field);
        if (propDeser != null) {
            prop = prop.withValueDeserializer(propDeser);
        }
        ReferenceProperty ref = config.getAnnotationIntrospector().findReferenceType(field);
        if (ref != null && ref.isManagedReference()) {
            prop.setManagedReferenceName(ref.getName());
        }
        return prop;
    }

    protected SettableBeanProperty constructSetterlessProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, AnnotatedMethod getter) throws JsonMappingException {
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            getter.fixAccess();
        }
        JavaType type = getter.getType(beanDesc.bindingsForBeanType());
        JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(config, getter, new Std(name, type, beanDesc.getClassAnnotations(), getter));
        type = modifyTypeByAnnotation(config, getter, type, name);
        SettableBeanProperty prop = new SetterlessProperty(name, type, (TypeDeserializer) type.getTypeHandler(), beanDesc.getClassAnnotations(), getter);
        if (propDeser != null) {
            return prop.withValueDeserializer(propDeser);
        }
        return prop;
    }

    protected boolean isPotentialBeanType(Class<?> type) {
        String typeStr = ClassUtil.canBeABeanType(type);
        if (typeStr != null) {
            throw new IllegalArgumentException("Can not deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
        } else if (ClassUtil.isProxyType(type)) {
            throw new IllegalArgumentException("Can not deserialize Proxy class " + type.getName() + " as a Bean");
        } else {
            typeStr = ClassUtil.isLocalType(type, true);
            if (typeStr == null) {
                return true;
            }
            throw new IllegalArgumentException("Can not deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
        }
    }

    protected boolean isIgnorableType(DeserializationConfig config, BasicBeanDescription beanDesc, Class<?> type, Map<Class<?>, Boolean> ignoredTypes) {
        Boolean status = (Boolean) ignoredTypes.get(type);
        if (status == null) {
            status = config.getAnnotationIntrospector().isIgnorableType(((BasicBeanDescription) config.introspectClassAnnotations((Class) type)).getClassInfo());
            if (status == null) {
                status = Boolean.FALSE;
            }
        }
        return status.booleanValue();
    }
}
