package org.codehaus.jackson.map.ser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.BeanProperty.Std;
import org.codehaus.jackson.map.BeanPropertyDefinition;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerFactory;
import org.codehaus.jackson.map.SerializerFactory.Config;
import org.codehaus.jackson.map.Serializers;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.ser.std.MapSerializer;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class BeanSerializerFactory extends BasicSerializerFactory {
    public static final BeanSerializerFactory instance = new BeanSerializerFactory(null);
    protected final Config _factoryConfig;

    public static class ConfigImpl extends Config {
        protected static final BeanSerializerModifier[] NO_MODIFIERS = new BeanSerializerModifier[0];
        protected static final Serializers[] NO_SERIALIZERS = new Serializers[0];
        protected final Serializers[] _additionalKeySerializers;
        protected final Serializers[] _additionalSerializers;
        protected final BeanSerializerModifier[] _modifiers;

        public ConfigImpl() {
            this(null, null, null);
        }

        protected ConfigImpl(Serializers[] allAdditionalSerializers, Serializers[] allAdditionalKeySerializers, BeanSerializerModifier[] modifiers) {
            if (allAdditionalSerializers == null) {
                allAdditionalSerializers = NO_SERIALIZERS;
            }
            this._additionalSerializers = allAdditionalSerializers;
            if (allAdditionalKeySerializers == null) {
                allAdditionalKeySerializers = NO_SERIALIZERS;
            }
            this._additionalKeySerializers = allAdditionalKeySerializers;
            if (modifiers == null) {
                modifiers = NO_MODIFIERS;
            }
            this._modifiers = modifiers;
        }

        public Config withAdditionalSerializers(Serializers additional) {
            if (additional != null) {
                return new ConfigImpl((Serializers[]) ArrayBuilders.insertInListNoDup(this._additionalSerializers, additional), this._additionalKeySerializers, this._modifiers);
            }
            throw new IllegalArgumentException("Can not pass null Serializers");
        }

        public Config withAdditionalKeySerializers(Serializers additional) {
            if (additional == null) {
                throw new IllegalArgumentException("Can not pass null Serializers");
            }
            return new ConfigImpl(this._additionalSerializers, (Serializers[]) ArrayBuilders.insertInListNoDup(this._additionalKeySerializers, additional), this._modifiers);
        }

        public Config withSerializerModifier(BeanSerializerModifier modifier) {
            if (modifier == null) {
                throw new IllegalArgumentException("Can not pass null modifier");
            }
            return new ConfigImpl(this._additionalSerializers, this._additionalKeySerializers, (BeanSerializerModifier[]) ArrayBuilders.insertInListNoDup(this._modifiers, modifier));
        }

        public boolean hasSerializers() {
            return this._additionalSerializers.length > 0;
        }

        public boolean hasKeySerializers() {
            return this._additionalKeySerializers.length > 0;
        }

        public boolean hasSerializerModifiers() {
            return this._modifiers.length > 0;
        }

        public Iterable<Serializers> serializers() {
            return ArrayBuilders.arrayAsIterable(this._additionalSerializers);
        }

        public Iterable<Serializers> keySerializers() {
            return ArrayBuilders.arrayAsIterable(this._additionalKeySerializers);
        }

        public Iterable<BeanSerializerModifier> serializerModifiers() {
            return ArrayBuilders.arrayAsIterable(this._modifiers);
        }
    }

    protected BeanSerializerFactory(Config config) {
        if (config == null) {
            config = new ConfigImpl();
        }
        this._factoryConfig = config;
    }

    public Config getConfig() {
        return this._factoryConfig;
    }

    public SerializerFactory withConfig(Config config) {
        if (this._factoryConfig == config) {
            return this;
        }
        if (getClass() != BeanSerializerFactory.class) {
            throw new IllegalStateException("Subtype of BeanSerializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalSerializers': can not instantiate subtype with " + "additional serializer definitions");
        }
        this(config);
        return this;
    }

    protected Iterable<Serializers> customSerializers() {
        return this._factoryConfig.serializers();
    }

    public JsonSerializer<Object> createSerializer(SerializationConfig config, JavaType origType, BeanProperty property) throws JsonMappingException {
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspect(origType);
        JsonSerializer<?> ser = findSerializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (ser != null) {
            return ser;
        }
        JavaType type = modifyTypeByAnnotation(config, beanDesc.getClassInfo(), origType);
        boolean staticTyping = type != origType;
        if (!(type == origType || type.getRawClass() == origType.getRawClass())) {
            beanDesc = (BasicBeanDescription) config.introspect(type);
        }
        if (origType.isContainerType()) {
            return buildContainerSerializer(config, type, beanDesc, property, staticTyping);
        }
        for (Serializers serializers : this._factoryConfig.serializers()) {
            ser = serializers.findSerializer(config, type, beanDesc, property);
            if (ser != null) {
                return ser;
            }
        }
        ser = findSerializerByLookup(type, config, beanDesc, property, staticTyping);
        if (ser == null) {
            ser = findSerializerByPrimaryType(type, config, beanDesc, property, staticTyping);
            if (ser == null) {
                ser = findBeanSerializer(config, type, beanDesc, property);
                if (ser == null) {
                    ser = findSerializerByAddonType(config, type, beanDesc, property, staticTyping);
                }
            }
        }
        return ser;
    }

    public JsonSerializer<Object> createKeySerializer(SerializationConfig config, JavaType type, BeanProperty property) {
        if (!this._factoryConfig.hasKeySerializers()) {
            return null;
        }
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectClassAnnotations(type.getRawClass());
        JsonSerializer<Object> ser = null;
        for (Serializers serializers : this._factoryConfig.keySerializers()) {
            ser = serializers.findSerializer(config, type, beanDesc, property);
            if (ser != null) {
                return ser;
            }
        }
        return ser;
    }

    public JsonSerializer<Object> findBeanSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        if (!isPotentialBeanType(type.getRawClass())) {
            return null;
        }
        JsonSerializer<Object> serializer = constructBeanSerializer(config, beanDesc, property);
        if (!this._factoryConfig.hasSerializerModifiers()) {
            return serializer;
        }
        for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
            serializer = mod.modifySerializer(config, beanDesc, serializer);
        }
        return serializer;
    }

    public TypeSerializer findPropertyTypeSerializer(JavaType baseType, SerializationConfig config, AnnotatedMember accessor, BeanProperty property) throws JsonMappingException {
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findPropertyTypeResolver(config, accessor, baseType);
        if (b == null) {
            return createTypeSerializer(config, baseType, property);
        }
        return b.buildTypeSerializer(config, baseType, config.getSubtypeResolver().collectAndResolveSubtypes(accessor, (MapperConfig) config, ai), property);
    }

    public TypeSerializer findPropertyContentTypeSerializer(JavaType containerType, SerializationConfig config, AnnotatedMember accessor, BeanProperty property) throws JsonMappingException {
        JavaType contentType = containerType.getContentType();
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findPropertyContentTypeResolver(config, accessor, containerType);
        if (b == null) {
            return createTypeSerializer(config, contentType, property);
        }
        return b.buildTypeSerializer(config, contentType, config.getSubtypeResolver().collectAndResolveSubtypes(accessor, (MapperConfig) config, ai), property);
    }

    protected JsonSerializer<Object> constructBeanSerializer(SerializationConfig config, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        if (beanDesc.getBeanClass() == Object.class) {
            throw new IllegalArgumentException("Can not create bean serializer for Object.class");
        }
        BeanSerializerBuilder builder = constructBeanSerializerBuilder(beanDesc);
        List<BeanPropertyWriter> findBeanProperties = findBeanProperties(config, beanDesc);
        if (findBeanProperties == null) {
            findBeanProperties = new ArrayList();
        }
        if (this._factoryConfig.hasSerializerModifiers()) {
            for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
                findBeanProperties = mod.changeProperties(config, beanDesc, findBeanProperties);
            }
        }
        findBeanProperties = sortBeanProperties(config, beanDesc, filterBeanProperties(config, beanDesc, findBeanProperties));
        if (this._factoryConfig.hasSerializerModifiers()) {
            for (BeanSerializerModifier mod2 : this._factoryConfig.serializerModifiers()) {
                findBeanProperties = mod2.orderProperties(config, beanDesc, findBeanProperties);
            }
        }
        builder.setProperties(findBeanProperties);
        builder.setFilterId(findFilterId(config, beanDesc));
        AnnotatedMethod anyGetter = beanDesc.findAnyGetter();
        if (anyGetter != null) {
            if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
                anyGetter.fixAccess();
            }
            JavaType type = anyGetter.getType(beanDesc.bindingsForBeanType());
            builder.setAnyGetter(new AnyGetterWriter(anyGetter, MapSerializer.construct(null, type, config.isEnabled(Feature.USE_STATIC_TYPING), createTypeSerializer(config, type.getContentType(), property), property, null, null)));
        }
        processViews(config, builder);
        if (this._factoryConfig.hasSerializerModifiers()) {
            for (BeanSerializerModifier mod22 : this._factoryConfig.serializerModifiers()) {
                builder = mod22.updateBuilder(config, beanDesc, builder);
            }
        }
        JsonSerializer<Object> ser = builder.build();
        if (ser == null && beanDesc.hasKnownClassAnnotations()) {
            return builder.createDummy();
        }
        return ser;
    }

    protected BeanPropertyWriter constructFilteredBeanWriter(BeanPropertyWriter writer, Class<?>[] inViews) {
        return FilteredBeanPropertyWriter.constructViewBased(writer, inViews);
    }

    protected PropertyBuilder constructPropertyBuilder(SerializationConfig config, BasicBeanDescription beanDesc) {
        return new PropertyBuilder(config, beanDesc);
    }

    protected BeanSerializerBuilder constructBeanSerializerBuilder(BasicBeanDescription beanDesc) {
        return new BeanSerializerBuilder(beanDesc);
    }

    protected Object findFilterId(SerializationConfig config, BasicBeanDescription beanDesc) {
        return config.getAnnotationIntrospector().findFilterId(beanDesc.getClassInfo());
    }

    protected boolean isPotentialBeanType(Class<?> type) {
        return ClassUtil.canBeABeanType(type) == null && !ClassUtil.isProxyType(type);
    }

    protected List<BeanPropertyWriter> findBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc) throws JsonMappingException {
        List<BeanPropertyDefinition> properties = beanDesc.findProperties();
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        removeIgnorableTypes(config, beanDesc, properties);
        if (config.isEnabled(Feature.REQUIRE_SETTERS_FOR_GETTERS)) {
            removeSetterlessGetters(config, beanDesc, properties);
        }
        if (properties.isEmpty()) {
            return null;
        }
        boolean staticTyping = usesStaticTyping(config, beanDesc, null, null);
        PropertyBuilder pb = constructPropertyBuilder(config, beanDesc);
        List<BeanPropertyWriter> arrayList = new ArrayList(properties.size());
        TypeBindings typeBind = beanDesc.bindingsForBeanType();
        for (BeanPropertyDefinition property : properties) {
            AnnotatedMember accessor = property.getAccessor();
            ReferenceProperty prop = intr.findReferenceType(accessor);
            if (prop == null || !prop.isBackReference()) {
                String name = property.getName();
                if (accessor instanceof AnnotatedMethod) {
                    arrayList = arrayList;
                    arrayList.add(_constructWriter(config, typeBind, pb, staticTyping, name, (AnnotatedMethod) accessor));
                } else {
                    arrayList = arrayList;
                    arrayList.add(_constructWriter(config, typeBind, pb, staticTyping, name, (AnnotatedField) accessor));
                }
            }
        }
        return arrayList;
    }

    protected List<BeanPropertyWriter> filterBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc, List<BeanPropertyWriter> props) {
        String[] ignored = config.getAnnotationIntrospector().findPropertiesToIgnore(beanDesc.getClassInfo());
        if (ignored != null && ignored.length > 0) {
            HashSet<String> ignoredSet = ArrayBuilders.arrayToSet(ignored);
            Iterator<BeanPropertyWriter> it = props.iterator();
            while (it.hasNext()) {
                if (ignoredSet.contains(((BeanPropertyWriter) it.next()).getName())) {
                    it.remove();
                }
            }
        }
        return props;
    }

    @Deprecated
    protected List<BeanPropertyWriter> sortBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc, List<BeanPropertyWriter> props) {
        return props;
    }

    protected void processViews(SerializationConfig config, BeanSerializerBuilder builder) {
        List<BeanPropertyWriter> props = builder.getProperties();
        boolean includeByDefault = config.isEnabled(Feature.DEFAULT_VIEW_INCLUSION);
        int propCount = props.size();
        int viewsFound = 0;
        BeanPropertyWriter[] filtered = new BeanPropertyWriter[propCount];
        for (int i = 0; i < propCount; i++) {
            BeanPropertyWriter bpw = (BeanPropertyWriter) props.get(i);
            Class<?>[] views = bpw.getViews();
            if (views != null) {
                viewsFound++;
                filtered[i] = constructFilteredBeanWriter(bpw, views);
            } else if (includeByDefault) {
                filtered[i] = bpw;
            }
        }
        if (!includeByDefault || viewsFound != 0) {
            builder.setFilteredProperties(filtered);
        }
    }

    protected void removeIgnorableTypes(SerializationConfig config, BasicBeanDescription beanDesc, List<BeanPropertyDefinition> properties) {
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        HashMap<Class<?>, Boolean> ignores = new HashMap();
        Iterator<BeanPropertyDefinition> it = properties.iterator();
        while (it.hasNext()) {
            AnnotatedMember accessor = ((BeanPropertyDefinition) it.next()).getAccessor();
            if (accessor == null) {
                it.remove();
            } else {
                Class<?> type = accessor.getRawType();
                Boolean result = (Boolean) ignores.get(type);
                if (result == null) {
                    result = intr.isIgnorableType(((BasicBeanDescription) config.introspectClassAnnotations((Class) type)).getClassInfo());
                    if (result == null) {
                        result = Boolean.FALSE;
                    }
                    ignores.put(type, result);
                }
                if (result.booleanValue()) {
                    it.remove();
                }
            }
        }
    }

    protected void removeSetterlessGetters(SerializationConfig config, BasicBeanDescription beanDesc, List<BeanPropertyDefinition> properties) {
        Iterator<BeanPropertyDefinition> it = properties.iterator();
        while (it.hasNext()) {
            BeanPropertyDefinition property = (BeanPropertyDefinition) it.next();
            if (!(property.couldDeserialize() || property.isExplicitlyIncluded())) {
                it.remove();
            }
        }
    }

    protected BeanPropertyWriter _constructWriter(SerializationConfig config, TypeBindings typeContext, PropertyBuilder pb, boolean staticTyping, String name, AnnotatedMember accessor) throws JsonMappingException {
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            accessor.fixAccess();
        }
        JavaType type = accessor.getType(typeContext);
        Std property = new Std(name, type, pb.getClassAnnotations(), accessor);
        JsonSerializer<Object> annotatedSerializer = findSerializerFromAnnotation(config, accessor, property);
        TypeSerializer contentTypeSer = null;
        if (ClassUtil.isCollectionMapOrArray(type.getRawClass())) {
            contentTypeSer = findPropertyContentTypeSerializer(type, config, accessor, property);
        }
        BeanPropertyWriter pbw = pb.buildWriter(name, type, annotatedSerializer, findPropertyTypeSerializer(type, config, accessor, property), contentTypeSer, accessor, staticTyping);
        pbw.setViews(config.getAnnotationIntrospector().findSerializationViews(accessor));
        return pbw;
    }
}
