package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.map.AbstractTypeResolver;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.ContextualDeserializer;
import org.codehaus.jackson.map.ContextualKeyDeserializer;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerFactory;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.Deserializers;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.KeyDeserializers;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.ResolvableDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.RootNameLookup;
import org.codehaus.jackson.type.JavaType;

public class StdDeserializerProvider extends DeserializerProvider {
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _cachedDeserializers;
    protected DeserializerFactory _factory;
    protected final HashMap<JavaType, JsonDeserializer<Object>> _incompleteDeserializers;
    protected final RootNameLookup _rootNames;

    protected static final class WrappedDeserializer extends JsonDeserializer<Object> {
        final JsonDeserializer<Object> _deserializer;
        final TypeDeserializer _typeDeserializer;

        public WrappedDeserializer(TypeDeserializer typeDeser, JsonDeserializer<Object> deser) {
            this._typeDeserializer = typeDeser;
            this._deserializer = deser;
        }

        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return this._deserializer.deserializeWithType(jp, ctxt, this._typeDeserializer);
        }

        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
            throw new IllegalStateException("Type-wrapped deserializer's deserializeWithType should never get called");
        }

        public Object deserialize(JsonParser jp, DeserializationContext ctxt, Object intoValue) throws IOException, JsonProcessingException {
            return this._deserializer.deserialize(jp, ctxt, intoValue);
        }
    }

    public StdDeserializerProvider() {
        this(BeanDeserializerFactory.instance);
    }

    public StdDeserializerProvider(DeserializerFactory f) {
        this._cachedDeserializers = new ConcurrentHashMap(64, 0.75f, 2);
        this._incompleteDeserializers = new HashMap(8);
        this._factory = f;
        this._rootNames = new RootNameLookup();
    }

    public DeserializerProvider withAdditionalDeserializers(Deserializers d) {
        return withFactory(this._factory.withAdditionalDeserializers(d));
    }

    public DeserializerProvider withAdditionalKeyDeserializers(KeyDeserializers d) {
        return withFactory(this._factory.withAdditionalKeyDeserializers(d));
    }

    public DeserializerProvider withDeserializerModifier(BeanDeserializerModifier modifier) {
        return withFactory(this._factory.withDeserializerModifier(modifier));
    }

    public DeserializerProvider withAbstractTypeResolver(AbstractTypeResolver resolver) {
        return withFactory(this._factory.withAbstractTypeResolver(resolver));
    }

    public DeserializerProvider withValueInstantiators(ValueInstantiators instantiators) {
        return withFactory(this._factory.withValueInstantiators(instantiators));
    }

    public StdDeserializerProvider withFactory(DeserializerFactory factory) {
        if (getClass() == StdDeserializerProvider.class) {
            return new StdDeserializerProvider(factory);
        }
        throw new IllegalStateException("DeserializerProvider of type " + getClass().getName() + " does not override 'withFactory()' method");
    }

    public JavaType mapAbstractType(DeserializationConfig config, JavaType type) throws JsonMappingException {
        return this._factory.mapAbstractType(config, type);
    }

    public SerializedString findExpectedRootName(DeserializationConfig config, JavaType type) throws JsonMappingException {
        return this._rootNames.findRootName(type, (MapperConfig) config);
    }

    public JsonDeserializer<Object> findValueDeserializer(DeserializationConfig config, JavaType propertyType, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<Object> deser = _findCachedDeserializer(propertyType);
        if (deser != null) {
            if (deser instanceof ContextualDeserializer) {
                deser = ((ContextualDeserializer) deser).createContextual(config, property);
            }
            return deser;
        }
        deser = _createAndCacheValueDeserializer(config, propertyType, property);
        if (deser == null) {
            deser = _handleUnknownValueDeserializer(propertyType);
        }
        if (deser instanceof ContextualDeserializer) {
            deser = ((ContextualDeserializer) deser).createContextual(config, property);
        }
        return deser;
    }

    public JsonDeserializer<Object> findTypedValueDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<Object> deser = findValueDeserializer(config, type, property);
        TypeDeserializer typeDeser = this._factory.findTypeDeserializer(config, type, property);
        if (typeDeser != null) {
            return new WrappedDeserializer(typeDeser, deser);
        }
        return deser;
    }

    public KeyDeserializer findKeyDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        KeyDeserializer kd = this._factory.createKeyDeserializer(config, type, property);
        if (kd instanceof ContextualKeyDeserializer) {
            kd = ((ContextualKeyDeserializer) kd).createContextual(config, property);
        }
        if (kd == null) {
            return _handleUnknownKeyDeserializer(type);
        }
        return kd;
    }

    public boolean hasValueDeserializerFor(DeserializationConfig config, JavaType type) {
        JsonDeserializer<Object> deser = _findCachedDeserializer(type);
        if (deser == null) {
            try {
                deser = _createAndCacheValueDeserializer(config, type, null);
            } catch (Exception e) {
                return false;
            }
        }
        if (deser != null) {
            return true;
        }
        return false;
    }

    public int cachedDeserializersCount() {
        return this._cachedDeserializers.size();
    }

    public void flushCachedDeserializers() {
        this._cachedDeserializers.clear();
    }

    protected JsonDeserializer<Object> _findCachedDeserializer(JavaType type) {
        if (type != null) {
            return (JsonDeserializer) this._cachedDeserializers.get(type);
        }
        throw new IllegalArgumentException();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected org.codehaus.jackson.map.JsonDeserializer<java.lang.Object> _createAndCacheValueDeserializer(org.codehaus.jackson.map.DeserializationConfig r6, org.codehaus.jackson.type.JavaType r7, org.codehaus.jackson.map.BeanProperty r8) throws org.codehaus.jackson.map.JsonMappingException {
        /*
        r5 = this;
        r3 = r5._incompleteDeserializers;
        monitor-enter(r3);
        r1 = r5._findCachedDeserializer(r7);	 Catch:{ all -> 0x0036 }
        if (r1 == 0) goto L_0x000c;
    L_0x0009:
        monitor-exit(r3);	 Catch:{ all -> 0x0036 }
        r2 = r1;
    L_0x000b:
        return r2;
    L_0x000c:
        r2 = r5._incompleteDeserializers;	 Catch:{ all -> 0x0036 }
        r0 = r2.size();	 Catch:{ all -> 0x0036 }
        if (r0 <= 0) goto L_0x0021;
    L_0x0014:
        r2 = r5._incompleteDeserializers;	 Catch:{ all -> 0x0036 }
        r1 = r2.get(r7);	 Catch:{ all -> 0x0036 }
        r1 = (org.codehaus.jackson.map.JsonDeserializer) r1;	 Catch:{ all -> 0x0036 }
        if (r1 == 0) goto L_0x0021;
    L_0x001e:
        monitor-exit(r3);	 Catch:{ all -> 0x0036 }
        r2 = r1;
        goto L_0x000b;
    L_0x0021:
        r2 = r5._createAndCache2(r6, r7, r8);	 Catch:{ all -> 0x0039 }
        if (r0 != 0) goto L_0x0034;
    L_0x0027:
        r4 = r5._incompleteDeserializers;	 Catch:{ all -> 0x0036 }
        r4 = r4.size();	 Catch:{ all -> 0x0036 }
        if (r4 <= 0) goto L_0x0034;
    L_0x002f:
        r4 = r5._incompleteDeserializers;	 Catch:{ all -> 0x0036 }
        r4.clear();	 Catch:{ all -> 0x0036 }
    L_0x0034:
        monitor-exit(r3);	 Catch:{ all -> 0x0036 }
        goto L_0x000b;
    L_0x0036:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0036 }
        throw r2;
    L_0x0039:
        r2 = move-exception;
        if (r0 != 0) goto L_0x0049;
    L_0x003c:
        r4 = r5._incompleteDeserializers;	 Catch:{ all -> 0x0036 }
        r4 = r4.size();	 Catch:{ all -> 0x0036 }
        if (r4 <= 0) goto L_0x0049;
    L_0x0044:
        r4 = r5._incompleteDeserializers;	 Catch:{ all -> 0x0036 }
        r4.clear();	 Catch:{ all -> 0x0036 }
    L_0x0049:
        throw r2;	 Catch:{ all -> 0x0036 }
        */
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.deser.StdDeserializerProvider._createAndCacheValueDeserializer(org.codehaus.jackson.map.DeserializationConfig, org.codehaus.jackson.type.JavaType, org.codehaus.jackson.map.BeanProperty):org.codehaus.jackson.map.JsonDeserializer<java.lang.Object>");
    }

    protected JsonDeserializer<Object> _createAndCache2(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        try {
            JsonDeserializer<Object> deser = _createDeserializer(config, type, property);
            if (deser == null) {
                return null;
            }
            boolean isResolvable = deser instanceof ResolvableDeserializer;
            boolean addToCache = deser.getClass() == BeanDeserializer.class;
            if (!addToCache && config.isEnabled(Feature.USE_ANNOTATIONS)) {
                AnnotationIntrospector aintr = config.getAnnotationIntrospector();
                Boolean cacheAnn = aintr.findCachability(AnnotatedClass.construct(deser.getClass(), aintr, null));
                if (cacheAnn != null) {
                    addToCache = cacheAnn.booleanValue();
                }
            }
            if (isResolvable) {
                this._incompleteDeserializers.put(type, deser);
                _resolveDeserializer(config, (ResolvableDeserializer) deser);
                this._incompleteDeserializers.remove(type);
            }
            if (!addToCache) {
                return deser;
            }
            this._cachedDeserializers.put(type, deser);
            return deser;
        } catch (IllegalArgumentException iae) {
            throw new JsonMappingException(iae.getMessage(), null, iae);
        }
    }

    protected JsonDeserializer<Object> _createDeserializer(DeserializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        if (type.isEnumType()) {
            return this._factory.createEnumDeserializer(config, this, type, property);
        }
        if (type.isContainerType()) {
            if (type.isArrayType()) {
                return this._factory.createArrayDeserializer(config, this, (ArrayType) type, property);
            }
            if (type.isMapLikeType()) {
                MapLikeType mlt = (MapLikeType) type;
                if (mlt.isTrueMapType()) {
                    return this._factory.createMapDeserializer(config, this, (MapType) mlt, property);
                }
                return this._factory.createMapLikeDeserializer(config, this, mlt, property);
            } else if (type.isCollectionLikeType()) {
                CollectionLikeType clt = (CollectionLikeType) type;
                if (clt.isTrueCollectionType()) {
                    return this._factory.createCollectionDeserializer(config, this, (CollectionType) clt, property);
                }
                return this._factory.createCollectionLikeDeserializer(config, this, clt, property);
            }
        }
        if (JsonNode.class.isAssignableFrom(type.getRawClass())) {
            return this._factory.createTreeDeserializer(config, this, type, property);
        }
        return this._factory.createBeanDeserializer(config, this, type, property);
    }

    protected void _resolveDeserializer(DeserializationConfig config, ResolvableDeserializer ser) throws JsonMappingException {
        ser.resolve(config, this);
    }

    protected JsonDeserializer<Object> _handleUnknownValueDeserializer(JavaType type) throws JsonMappingException {
        if (ClassUtil.isConcrete(type.getRawClass())) {
            throw new JsonMappingException("Can not find a Value deserializer for type " + type);
        }
        throw new JsonMappingException("Can not find a Value deserializer for abstract type " + type);
    }

    protected KeyDeserializer _handleUnknownKeyDeserializer(JavaType type) throws JsonMappingException {
        throw new JsonMappingException("Can not find a (Map) Key deserializer for type " + type);
    }
}
