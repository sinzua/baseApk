package org.codehaus.jackson.map.deser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.ContextualDeserializer;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializerFactory;
import org.codehaus.jackson.map.DeserializerFactory.Config;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.KeyDeserializer.None;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.deser.std.AtomicReferenceDeserializer;
import org.codehaus.jackson.map.deser.std.CollectionDeserializer;
import org.codehaus.jackson.map.deser.std.EnumDeserializer;
import org.codehaus.jackson.map.deser.std.EnumMapDeserializer;
import org.codehaus.jackson.map.deser.std.EnumSetDeserializer;
import org.codehaus.jackson.map.deser.std.JsonNodeDeserializer;
import org.codehaus.jackson.map.deser.std.MapDeserializer;
import org.codehaus.jackson.map.deser.std.ObjectArrayDeserializer;
import org.codehaus.jackson.map.deser.std.PrimitiveArrayDeserializers;
import org.codehaus.jackson.map.deser.std.StdKeyDeserializers;
import org.codehaus.jackson.map.deser.std.StringCollectionDeserializer;
import org.codehaus.jackson.map.ext.OptionalHandlerFactory;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.EnumResolver;
import org.codehaus.jackson.type.JavaType;

public abstract class BasicDeserializerFactory extends DeserializerFactory {
    protected static final HashMap<JavaType, JsonDeserializer<Object>> _arrayDeserializers = PrimitiveArrayDeserializers.getAll();
    static final HashMap<String, Class<? extends Collection>> _collectionFallbacks = new HashMap();
    static final HashMap<JavaType, KeyDeserializer> _keyDeserializers = StdKeyDeserializers.constructAll();
    static final HashMap<String, Class<? extends Map>> _mapFallbacks = new HashMap();
    static final HashMap<ClassKey, JsonDeserializer<Object>> _simpleDeserializers = StdDeserializers.constructAll();
    protected OptionalHandlerFactory optionalHandlers = OptionalHandlerFactory.instance;

    protected abstract JsonDeserializer<?> _findCustomArrayDeserializer(ArrayType arrayType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BeanProperty beanProperty, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    protected abstract JsonDeserializer<?> _findCustomCollectionDeserializer(CollectionType collectionType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BasicBeanDescription basicBeanDescription, BeanProperty beanProperty, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    protected abstract JsonDeserializer<?> _findCustomCollectionLikeDeserializer(CollectionLikeType collectionLikeType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BasicBeanDescription basicBeanDescription, BeanProperty beanProperty, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    protected abstract JsonDeserializer<?> _findCustomEnumDeserializer(Class<?> cls, DeserializationConfig deserializationConfig, BasicBeanDescription basicBeanDescription, BeanProperty beanProperty) throws JsonMappingException;

    protected abstract JsonDeserializer<?> _findCustomMapDeserializer(MapType mapType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BasicBeanDescription basicBeanDescription, BeanProperty beanProperty, KeyDeserializer keyDeserializer, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    protected abstract JsonDeserializer<?> _findCustomMapLikeDeserializer(MapLikeType mapLikeType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BasicBeanDescription basicBeanDescription, BeanProperty beanProperty, KeyDeserializer keyDeserializer, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    protected abstract JsonDeserializer<?> _findCustomTreeNodeDeserializer(Class<? extends JsonNode> cls, DeserializationConfig deserializationConfig, BeanProperty beanProperty) throws JsonMappingException;

    public abstract ValueInstantiator findValueInstantiator(DeserializationConfig deserializationConfig, BasicBeanDescription basicBeanDescription) throws JsonMappingException;

    public abstract JavaType mapAbstractType(DeserializationConfig deserializationConfig, JavaType javaType) throws JsonMappingException;

    public abstract DeserializerFactory withConfig(Config config);

    static {
        _mapFallbacks.put(Map.class.getName(), LinkedHashMap.class);
        _mapFallbacks.put(ConcurrentMap.class.getName(), ConcurrentHashMap.class);
        _mapFallbacks.put(SortedMap.class.getName(), TreeMap.class);
        _mapFallbacks.put("java.util.NavigableMap", TreeMap.class);
        try {
            Class<?> key = Class.forName("java.util.concurrent.ConcurrentNavigableMap");
            _mapFallbacks.put(key.getName(), Class.forName("java.util.concurrent.ConcurrentSkipListMap"));
        } catch (ClassNotFoundException e) {
        } catch (SecurityException e2) {
        }
        _collectionFallbacks.put(Collection.class.getName(), ArrayList.class);
        _collectionFallbacks.put(List.class.getName(), ArrayList.class);
        _collectionFallbacks.put(Set.class.getName(), HashSet.class);
        _collectionFallbacks.put(SortedSet.class.getName(), TreeSet.class);
        _collectionFallbacks.put(Queue.class.getName(), LinkedList.class);
        _collectionFallbacks.put("java.util.Deque", LinkedList.class);
        _collectionFallbacks.put("java.util.NavigableSet", TreeSet.class);
    }

    protected BasicDeserializerFactory() {
    }

    public JsonDeserializer<?> createArrayDeserializer(DeserializationConfig config, DeserializerProvider p, ArrayType type, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<?> custom;
        JavaType elemType = type.getContentType();
        JsonDeserializer<Object> contentDeser = (JsonDeserializer) elemType.getValueHandler();
        if (contentDeser == null) {
            JsonDeserializer<?> deser = (JsonDeserializer) _arrayDeserializers.get(elemType);
            if (deser != null) {
                custom = _findCustomArrayDeserializer(type, config, p, property, null, null);
                return custom != null ? custom : deser;
            } else if (elemType.isPrimitive()) {
                throw new IllegalArgumentException("Internal error: primitive type (" + type + ") passed, no array deserializer found");
            }
        }
        TypeDeserializer elemTypeDeser = (TypeDeserializer) elemType.getTypeHandler();
        if (elemTypeDeser == null) {
            elemTypeDeser = findTypeDeserializer(config, elemType, property);
        }
        custom = _findCustomArrayDeserializer(type, config, p, property, elemTypeDeser, contentDeser);
        if (custom != null) {
            return custom;
        }
        if (contentDeser == null) {
            contentDeser = p.findValueDeserializer(config, elemType, property);
        }
        return new ObjectArrayDeserializer(type, contentDeser, elemTypeDeser);
    }

    public JsonDeserializer<?> createCollectionDeserializer(DeserializationConfig config, DeserializerProvider p, CollectionType type, BeanProperty property) throws JsonMappingException {
        JavaType type2 = (CollectionType) mapAbstractType(config, type);
        Class<?> collectionClass = type2.getRawClass();
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectForCreation(type2);
        JsonDeserializer<Object> deser = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (deser != null) {
            return deser;
        }
        type2 = (CollectionType) modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type2, null);
        JavaType contentType = type2.getContentType();
        JsonDeserializer contentDeser = (JsonDeserializer) contentType.getValueHandler();
        TypeDeserializer contentTypeDeser = (TypeDeserializer) contentType.getTypeHandler();
        if (contentTypeDeser == null) {
            contentTypeDeser = findTypeDeserializer(config, contentType, property);
        }
        JsonDeserializer<?> custom = _findCustomCollectionDeserializer(type2, config, p, beanDesc, property, contentTypeDeser, contentDeser);
        if (custom != null) {
            return custom;
        }
        if (contentDeser == null) {
            if (EnumSet.class.isAssignableFrom(collectionClass)) {
                return new EnumSetDeserializer(contentType.getRawClass(), createEnumDeserializer(config, p, contentType, property));
            }
            contentDeser = p.findValueDeserializer(config, contentType, property);
        }
        if (type2.isInterface() || type2.isAbstract()) {
            Class<? extends Collection> fallback = (Class) _collectionFallbacks.get(collectionClass.getName());
            if (fallback == null) {
                throw new IllegalArgumentException("Can not find a deserializer for non-concrete Collection type " + type2);
            }
            type2 = (CollectionType) config.constructSpecializedType(type2, fallback);
            beanDesc = (BasicBeanDescription) config.introspectForCreation(type2);
        }
        ValueInstantiator inst = findValueInstantiator(config, beanDesc);
        if (contentType.getRawClass() == String.class) {
            return new StringCollectionDeserializer(type2, contentDeser, inst);
        }
        return new CollectionDeserializer(type2, contentDeser, contentTypeDeser, inst);
    }

    public JsonDeserializer<?> createCollectionLikeDeserializer(DeserializationConfig config, DeserializerProvider p, CollectionLikeType type, BeanProperty property) throws JsonMappingException {
        type = (CollectionLikeType) mapAbstractType(config, type);
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectClassAnnotations((Class) type.getRawClass());
        JsonDeserializer<Object> deser = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (deser != null) {
            return deser;
        }
        type = (CollectionLikeType) modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type, null);
        JavaType contentType = type.getContentType();
        JsonDeserializer<Object> contentDeser = (JsonDeserializer) contentType.getValueHandler();
        TypeDeserializer contentTypeDeser = (TypeDeserializer) contentType.getTypeHandler();
        if (contentTypeDeser == null) {
            contentTypeDeser = findTypeDeserializer(config, contentType, property);
        }
        return _findCustomCollectionLikeDeserializer(type, config, p, beanDesc, property, contentTypeDeser, contentDeser);
    }

    public JsonDeserializer<?> createMapDeserializer(DeserializationConfig config, DeserializerProvider p, MapType type, BeanProperty property) throws JsonMappingException {
        JavaType type2 = (MapType) mapAbstractType(config, type);
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectForCreation(type2);
        JsonDeserializer<Object> deser = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (deser != null) {
            return deser;
        }
        type2 = (MapType) modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type2, null);
        JavaType keyType = type2.getKeyType();
        JavaType contentType = type2.getContentType();
        JsonDeserializer<Object> contentDeser = (JsonDeserializer) contentType.getValueHandler();
        KeyDeserializer keyDes = (KeyDeserializer) keyType.getValueHandler();
        if (keyDes == null) {
            keyDes = p.findKeyDeserializer(config, keyType, property);
        }
        TypeDeserializer contentTypeDeser = (TypeDeserializer) contentType.getTypeHandler();
        if (contentTypeDeser == null) {
            contentTypeDeser = findTypeDeserializer(config, contentType, property);
        }
        JsonDeserializer<?> custom = _findCustomMapDeserializer(type2, config, p, beanDesc, property, keyDes, contentTypeDeser, contentDeser);
        if (custom != null) {
            return custom;
        }
        if (contentDeser == null) {
            contentDeser = p.findValueDeserializer(config, contentType, property);
        }
        Class<?> mapClass = type2.getRawClass();
        if (EnumMap.class.isAssignableFrom(mapClass)) {
            Class<?> kt = keyType.getRawClass();
            if (kt != null && kt.isEnum()) {
                return new EnumMapDeserializer(keyType.getRawClass(), createEnumDeserializer(config, p, keyType, property), contentDeser);
            }
            throw new IllegalArgumentException("Can not construct EnumMap; generic (key) type not available");
        }
        if (type2.isInterface() || type2.isAbstract()) {
            Class<? extends Map> fallback = (Class) _mapFallbacks.get(mapClass.getName());
            if (fallback == null) {
                throw new IllegalArgumentException("Can not find a deserializer for non-concrete Map type " + type2);
            }
            type2 = (MapType) config.constructSpecializedType(type2, fallback);
            beanDesc = (BasicBeanDescription) config.introspectForCreation(type2);
        }
        JsonDeserializer md = new MapDeserializer(type2, findValueInstantiator(config, beanDesc), keyDes, (JsonDeserializer) contentDeser, contentTypeDeser);
        md.setIgnorableProperties(config.getAnnotationIntrospector().findPropertiesToIgnore(beanDesc.getClassInfo()));
        return md;
    }

    public JsonDeserializer<?> createMapLikeDeserializer(DeserializationConfig config, DeserializerProvider p, MapLikeType type, BeanProperty property) throws JsonMappingException {
        JavaType type2 = (MapLikeType) mapAbstractType(config, type);
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectForCreation(type2);
        JsonDeserializer<Object> deser = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (deser != null) {
            return deser;
        }
        type = (MapLikeType) modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type2, null);
        JavaType keyType = type.getKeyType();
        JavaType contentType = type.getContentType();
        JsonDeserializer<Object> contentDeser = (JsonDeserializer) contentType.getValueHandler();
        KeyDeserializer keyDes = (KeyDeserializer) keyType.getValueHandler();
        if (keyDes == null) {
            keyDes = p.findKeyDeserializer(config, keyType, property);
        }
        TypeDeserializer contentTypeDeser = (TypeDeserializer) contentType.getTypeHandler();
        if (contentTypeDeser == null) {
            contentTypeDeser = findTypeDeserializer(config, contentType, property);
        }
        return _findCustomMapLikeDeserializer(type, config, p, beanDesc, property, keyDes, contentTypeDeser, contentDeser);
    }

    public JsonDeserializer<?> createEnumDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType type, BeanProperty property) throws JsonMappingException {
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectForCreation(type);
        JsonDeserializer<?> des = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (des != null) {
            return des;
        }
        Class<?> enumClass = type.getRawClass();
        JsonDeserializer<?> custom = _findCustomEnumDeserializer(enumClass, config, beanDesc, property);
        if (custom != null) {
            return custom;
        }
        for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
            if (config.getAnnotationIntrospector().hasCreatorAnnotation(factory)) {
                if (factory.getParameterCount() == 1 && factory.getRawType().isAssignableFrom(enumClass)) {
                    return EnumDeserializer.deserializerForCreator(config, enumClass, factory);
                }
                throw new IllegalArgumentException("Unsuitable method (" + factory + ") decorated with @JsonCreator (for Enum type " + enumClass.getName() + ")");
            }
        }
        return new EnumDeserializer(constructEnumResolver(enumClass, config));
    }

    public JsonDeserializer<?> createTreeDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType nodeType, BeanProperty property) throws JsonMappingException {
        Class<? extends JsonNode> nodeClass = nodeType.getRawClass();
        JsonDeserializer<?> custom = _findCustomTreeNodeDeserializer(nodeClass, config, property);
        return custom != null ? custom : JsonNodeDeserializer.getDeserializer(nodeClass);
    }

    protected JsonDeserializer<Object> findStdBeanDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType type, BeanProperty property) throws JsonMappingException {
        Class<?> cls = type.getRawClass();
        JsonDeserializer<Object> deser = (JsonDeserializer) _simpleDeserializers.get(new ClassKey(cls));
        if (deser != null) {
            return deser;
        }
        if (AtomicReference.class.isAssignableFrom(cls)) {
            JavaType referencedType;
            JavaType[] params = config.getTypeFactory().findTypeParameters(type, AtomicReference.class);
            if (params == null || params.length < 1) {
                referencedType = TypeFactory.unknownType();
            } else {
                referencedType = params[0];
            }
            return new AtomicReferenceDeserializer(referencedType, property);
        }
        JsonDeserializer<?> d = this.optionalHandlers.findDeserializer(type, config, p);
        if (d != null) {
            return d;
        }
        return null;
    }

    public TypeDeserializer findTypeDeserializer(DeserializationConfig config, JavaType baseType, BeanProperty property) throws JsonMappingException {
        AnnotatedClass ac = ((BasicBeanDescription) config.introspectClassAnnotations((Class) baseType.getRawClass())).getClassInfo();
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findTypeResolver(config, ac, baseType);
        Collection<NamedType> subtypes = null;
        if (b == null) {
            b = config.getDefaultTyper(baseType);
            if (b == null) {
                return null;
            }
        }
        subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(ac, (MapperConfig) config, ai);
        if (b.getDefaultImpl() == null && baseType.isAbstract()) {
            JavaType defaultType = mapAbstractType(config, baseType);
            if (!(defaultType == null || defaultType.getRawClass() == baseType.getRawClass())) {
                b = b.defaultImpl(defaultType.getRawClass());
            }
        }
        return b.buildTypeDeserializer(config, baseType, subtypes, property);
    }

    public TypeDeserializer findPropertyTypeDeserializer(DeserializationConfig config, JavaType baseType, AnnotatedMember annotated, BeanProperty property) throws JsonMappingException {
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findPropertyTypeResolver(config, annotated, baseType);
        if (b == null) {
            return findTypeDeserializer(config, baseType, property);
        }
        return b.buildTypeDeserializer(config, baseType, config.getSubtypeResolver().collectAndResolveSubtypes(annotated, (MapperConfig) config, ai), property);
    }

    public TypeDeserializer findPropertyContentTypeDeserializer(DeserializationConfig config, JavaType containerType, AnnotatedMember propertyEntity, BeanProperty property) throws JsonMappingException {
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findPropertyContentTypeResolver(config, propertyEntity, containerType);
        JavaType contentType = containerType.getContentType();
        if (b == null) {
            return findTypeDeserializer(config, contentType, property);
        }
        return b.buildTypeDeserializer(config, contentType, config.getSubtypeResolver().collectAndResolveSubtypes(propertyEntity, (MapperConfig) config, ai), property);
    }

    protected JsonDeserializer<Object> findDeserializerFromAnnotation(DeserializationConfig config, Annotated ann, BeanProperty property) throws JsonMappingException {
        Object deserDef = config.getAnnotationIntrospector().findDeserializer(ann);
        if (deserDef != null) {
            return _constructDeserializer(config, ann, property, deserDef);
        }
        return null;
    }

    JsonDeserializer<Object> _constructDeserializer(DeserializationConfig config, Annotated ann, BeanProperty property, Object deserDef) throws JsonMappingException {
        JsonDeserializer<Object> deser;
        if (deserDef instanceof JsonDeserializer) {
            deser = (JsonDeserializer) deserDef;
            if (deser instanceof ContextualDeserializer) {
                return ((ContextualDeserializer) deser).createContextual(config, property);
            }
            return deser;
        } else if (deserDef instanceof Class) {
            Class<? extends JsonDeserializer<?>> deserClass = (Class) deserDef;
            if (JsonDeserializer.class.isAssignableFrom(deserClass)) {
                deser = config.deserializerInstance(ann, deserClass);
                if (deser instanceof ContextualDeserializer) {
                    return ((ContextualDeserializer) deser).createContextual(config, property);
                }
                return deser;
            }
            throw new IllegalStateException("AnnotationIntrospector returned Class " + deserClass.getName() + "; expected Class<JsonDeserializer>");
        } else {
            throw new IllegalStateException("AnnotationIntrospector returned deserializer definition of type " + deserDef.getClass().getName() + "; expected type JsonDeserializer or Class<JsonDeserializer> instead");
        }
    }

    protected <T extends JavaType> T modifyTypeByAnnotation(DeserializationConfig config, Annotated a, T type, String propName) throws JsonMappingException {
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        Class<?> subclass = intr.findDeserializationType(a, type, propName);
        if (subclass != null) {
            try {
                type = type.narrowBy(subclass);
            } catch (IllegalArgumentException iae) {
                throw new JsonMappingException("Failed to narrow type " + type + " with concrete-type annotation (value " + subclass.getName() + "), method '" + a.getName() + "': " + iae.getMessage(), null, iae);
            }
        }
        if (type.isContainerType()) {
            Class<?> keyClass = intr.findDeserializationKeyType(a, type.getKeyType(), propName);
            if (keyClass != null) {
                if (type instanceof MapLikeType) {
                    try {
                        type = ((MapLikeType) type).narrowKey(keyClass);
                    } catch (IllegalArgumentException iae2) {
                        throw new JsonMappingException("Failed to narrow key type " + type + " with key-type annotation (" + keyClass.getName() + "): " + iae2.getMessage(), null, iae2);
                    }
                }
                throw new JsonMappingException("Illegal key-type annotation: type " + type + " is not a Map(-like) type");
            }
            JavaType keyType = type.getKeyType();
            if (keyType != null && keyType.getValueHandler() == null) {
                Class<? extends KeyDeserializer> kdClass = intr.findKeyDeserializer(a);
                if (!(kdClass == null || kdClass == None.class)) {
                    keyType.setValueHandler(config.keyDeserializerInstance(a, kdClass));
                }
            }
            Class<?> cc = intr.findDeserializationContentType(a, type.getContentType(), propName);
            if (cc != null) {
                try {
                    type = type.narrowContentsBy(cc);
                } catch (IllegalArgumentException iae22) {
                    throw new JsonMappingException("Failed to narrow content type " + type + " with content-type annotation (" + cc.getName() + "): " + iae22.getMessage(), null, iae22);
                }
            }
            if (type.getContentType().getValueHandler() == null) {
                Class<? extends JsonDeserializer<?>> cdClass = intr.findContentDeserializer(a);
                if (!(cdClass == null || cdClass == JsonDeserializer.None.class)) {
                    type.getContentType().setValueHandler(config.deserializerInstance(a, cdClass));
                }
            }
        }
        return type;
    }

    protected JavaType resolveType(DeserializationConfig config, BasicBeanDescription beanDesc, JavaType type, AnnotatedMember member, BeanProperty property) throws JsonMappingException {
        TypeDeserializer valueTypeDeser;
        if (type.isContainerType()) {
            AnnotationIntrospector intr = config.getAnnotationIntrospector();
            JavaType keyType = type.getKeyType();
            if (keyType != null) {
                Class<? extends KeyDeserializer> kdClass = intr.findKeyDeserializer(member);
                if (!(kdClass == null || kdClass == None.class)) {
                    keyType.setValueHandler(config.keyDeserializerInstance(member, kdClass));
                }
            }
            Class<? extends JsonDeserializer<?>> cdClass = intr.findContentDeserializer(member);
            if (!(cdClass == null || cdClass == JsonDeserializer.None.class)) {
                type.getContentType().setValueHandler(config.deserializerInstance(member, cdClass));
            }
            if (member instanceof AnnotatedMember) {
                TypeDeserializer contentTypeDeser = findPropertyContentTypeDeserializer(config, type, member, property);
                if (contentTypeDeser != null) {
                    type = type.withContentTypeHandler(contentTypeDeser);
                }
            }
        }
        if (member instanceof AnnotatedMember) {
            valueTypeDeser = findPropertyTypeDeserializer(config, type, member, property);
        } else {
            valueTypeDeser = findTypeDeserializer(config, type, null);
        }
        if (valueTypeDeser != null) {
            return type.withTypeHandler(valueTypeDeser);
        }
        return type;
    }

    protected EnumResolver<?> constructEnumResolver(Class<?> enumClass, DeserializationConfig config) {
        if (config.isEnabled(Feature.READ_ENUMS_USING_TO_STRING)) {
            return EnumResolver.constructUnsafeUsingToString(enumClass);
        }
        return EnumResolver.constructUnsafe(enumClass, config.getAnnotationIntrospector());
    }
}
