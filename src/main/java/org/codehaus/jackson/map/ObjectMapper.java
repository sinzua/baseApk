package org.codehaus.jackson.map;

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.FormatSchema;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.PrettyPrinter;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.io.SegmentedStringWriter;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.Module.SetupContext;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.deser.BeanDeserializerModifier;
import org.codehaus.jackson.map.deser.StdDeserializationContext;
import org.codehaus.jackson.map.deser.StdDeserializerProvider;
import org.codehaus.jackson.map.deser.ValueInstantiators;
import org.codehaus.jackson.map.introspect.BasicClassIntrospector;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.introspect.VisibilityChecker.Std;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.jsontype.impl.StdSubtypeResolver;
import org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder;
import org.codehaus.jackson.map.ser.BeanSerializerFactory;
import org.codehaus.jackson.map.ser.BeanSerializerModifier;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.StdSerializerProvider;
import org.codehaus.jackson.map.type.SimpleType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.type.TypeModifier;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.NullNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TreeTraversingParser;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.codehaus.jackson.util.TokenBuffer;
import org.codehaus.jackson.util.VersionUtil;

public class ObjectMapper extends ObjectCodec implements Versioned {
    protected static final AnnotationIntrospector DEFAULT_ANNOTATION_INTROSPECTOR = new JacksonAnnotationIntrospector();
    protected static final ClassIntrospector<? extends BeanDescription> DEFAULT_INTROSPECTOR = BasicClassIntrospector.instance;
    private static final JavaType JSON_NODE_TYPE = SimpleType.constructUnsafe(JsonNode.class);
    protected static final VisibilityChecker<?> STD_VISIBILITY_CHECKER = Std.defaultInstance();
    protected DeserializationConfig _deserializationConfig;
    protected DeserializerProvider _deserializerProvider;
    protected InjectableValues _injectableValues;
    protected final JsonFactory _jsonFactory;
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
    protected SerializationConfig _serializationConfig;
    protected SerializerFactory _serializerFactory;
    protected SerializerProvider _serializerProvider;
    protected SubtypeResolver _subtypeResolver;
    protected TypeFactory _typeFactory;

    public enum DefaultTyping {
        JAVA_LANG_OBJECT,
        OBJECT_AND_NON_CONCRETE,
        NON_CONCRETE_AND_ARRAYS,
        NON_FINAL
    }

    public static class DefaultTypeResolverBuilder extends StdTypeResolverBuilder {
        protected final DefaultTyping _appliesFor;

        public DefaultTypeResolverBuilder(DefaultTyping t) {
            this._appliesFor = t;
        }

        public TypeDeserializer buildTypeDeserializer(DeserializationConfig config, JavaType baseType, Collection<NamedType> subtypes, BeanProperty property) {
            return useForType(baseType) ? super.buildTypeDeserializer(config, baseType, subtypes, property) : null;
        }

        public TypeSerializer buildTypeSerializer(SerializationConfig config, JavaType baseType, Collection<NamedType> subtypes, BeanProperty property) {
            return useForType(baseType) ? super.buildTypeSerializer(config, baseType, subtypes, property) : null;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean useForType(org.codehaus.jackson.type.JavaType r5) {
            /*
            r4 = this;
            r1 = 1;
            r0 = 0;
            r2 = org.codehaus.jackson.map.ObjectMapper.AnonymousClass2.$SwitchMap$org$codehaus$jackson$map$ObjectMapper$DefaultTyping;
            r3 = r4._appliesFor;
            r3 = r3.ordinal();
            r2 = r2[r3];
            switch(r2) {
                case 1: goto L_0x0018;
                case 2: goto L_0x0023;
                case 3: goto L_0x0034;
                default: goto L_0x000f;
            };
        L_0x000f:
            r2 = r5.getRawClass();
            r3 = java.lang.Object.class;
            if (r2 != r3) goto L_0x0047;
        L_0x0017:
            return r1;
        L_0x0018:
            r2 = r5.isArrayType();
            if (r2 == 0) goto L_0x0023;
        L_0x001e:
            r5 = r5.getContentType();
            goto L_0x0018;
        L_0x0023:
            r2 = r5.getRawClass();
            r3 = java.lang.Object.class;
            if (r2 == r3) goto L_0x0031;
        L_0x002b:
            r2 = r5.isConcrete();
            if (r2 != 0) goto L_0x0032;
        L_0x0031:
            r0 = r1;
        L_0x0032:
            r1 = r0;
            goto L_0x0017;
        L_0x0034:
            r2 = r5.isArrayType();
            if (r2 == 0) goto L_0x003f;
        L_0x003a:
            r5 = r5.getContentType();
            goto L_0x0034;
        L_0x003f:
            r2 = r5.isFinal();
            if (r2 == 0) goto L_0x0017;
        L_0x0045:
            r1 = r0;
            goto L_0x0017;
        L_0x0047:
            r1 = r0;
            goto L_0x0017;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.ObjectMapper.DefaultTypeResolverBuilder.useForType(org.codehaus.jackson.type.JavaType):boolean");
        }
    }

    public ObjectMapper() {
        this(null, null, null);
    }

    public ObjectMapper(JsonFactory jf) {
        this(jf, null, null);
    }

    @Deprecated
    public ObjectMapper(SerializerFactory sf) {
        this(null, null, null);
        setSerializerFactory(sf);
    }

    public ObjectMapper(JsonFactory jf, SerializerProvider sp, DeserializerProvider dp) {
        this(jf, sp, dp, null, null);
    }

    public ObjectMapper(JsonFactory jf, SerializerProvider sp, DeserializerProvider dp, SerializationConfig sconfig, DeserializationConfig dconfig) {
        this._rootDeserializers = new ConcurrentHashMap(64, 0.6f, 2);
        if (jf == null) {
            this._jsonFactory = new MappingJsonFactory(this);
        } else {
            this._jsonFactory = jf;
            if (jf.getCodec() == null) {
                this._jsonFactory.setCodec(this);
            }
        }
        this._typeFactory = TypeFactory.defaultInstance();
        if (sconfig == null) {
            sconfig = new SerializationConfig(DEFAULT_INTROSPECTOR, DEFAULT_ANNOTATION_INTROSPECTOR, STD_VISIBILITY_CHECKER, null, null, this._typeFactory, null);
        }
        this._serializationConfig = sconfig;
        if (dconfig == null) {
            dconfig = new DeserializationConfig(DEFAULT_INTROSPECTOR, DEFAULT_ANNOTATION_INTROSPECTOR, STD_VISIBILITY_CHECKER, null, null, this._typeFactory, null);
        }
        this._deserializationConfig = dconfig;
        if (sp == null) {
            sp = new StdSerializerProvider();
        }
        this._serializerProvider = sp;
        if (dp == null) {
            dp = new StdDeserializerProvider();
        }
        this._deserializerProvider = dp;
        this._serializerFactory = BeanSerializerFactory.instance;
    }

    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    public void registerModule(Module module) {
        if (module.getModuleName() == null) {
            throw new IllegalArgumentException("Module without defined name");
        } else if (module.version() == null) {
            throw new IllegalArgumentException("Module without defined version");
        } else {
            final ObjectMapper mapper = this;
            module.setupModule(new SetupContext() {
                public Version getMapperVersion() {
                    return ObjectMapper.this.version();
                }

                public DeserializationConfig getDeserializationConfig() {
                    return mapper.getDeserializationConfig();
                }

                public SerializationConfig getSerializationConfig() {
                    return mapper.getSerializationConfig();
                }

                public boolean isEnabled(Feature f) {
                    return mapper.isEnabled(f);
                }

                public boolean isEnabled(SerializationConfig.Feature f) {
                    return mapper.isEnabled(f);
                }

                public boolean isEnabled(JsonParser.Feature f) {
                    return mapper.isEnabled(f);
                }

                public boolean isEnabled(JsonGenerator.Feature f) {
                    return mapper.isEnabled(f);
                }

                public void addDeserializers(Deserializers d) {
                    mapper._deserializerProvider = mapper._deserializerProvider.withAdditionalDeserializers(d);
                }

                public void addKeyDeserializers(KeyDeserializers d) {
                    mapper._deserializerProvider = mapper._deserializerProvider.withAdditionalKeyDeserializers(d);
                }

                public void addSerializers(Serializers s) {
                    mapper._serializerFactory = mapper._serializerFactory.withAdditionalSerializers(s);
                }

                public void addKeySerializers(Serializers s) {
                    mapper._serializerFactory = mapper._serializerFactory.withAdditionalKeySerializers(s);
                }

                public void addBeanSerializerModifier(BeanSerializerModifier modifier) {
                    mapper._serializerFactory = mapper._serializerFactory.withSerializerModifier(modifier);
                }

                public void addBeanDeserializerModifier(BeanDeserializerModifier modifier) {
                    mapper._deserializerProvider = mapper._deserializerProvider.withDeserializerModifier(modifier);
                }

                public void addAbstractTypeResolver(AbstractTypeResolver resolver) {
                    mapper._deserializerProvider = mapper._deserializerProvider.withAbstractTypeResolver(resolver);
                }

                public void addTypeModifier(TypeModifier modifier) {
                    mapper.setTypeFactory(mapper._typeFactory.withModifier(modifier));
                }

                public void addValueInstantiators(ValueInstantiators instantiators) {
                    mapper._deserializerProvider = mapper._deserializerProvider.withValueInstantiators(instantiators);
                }

                public void insertAnnotationIntrospector(AnnotationIntrospector ai) {
                    mapper._deserializationConfig = mapper._deserializationConfig.withInsertedAnnotationIntrospector(ai);
                    mapper._serializationConfig = mapper._serializationConfig.withInsertedAnnotationIntrospector(ai);
                }

                public void appendAnnotationIntrospector(AnnotationIntrospector ai) {
                    mapper._deserializationConfig = mapper._deserializationConfig.withAppendedAnnotationIntrospector(ai);
                    mapper._serializationConfig = mapper._serializationConfig.withAppendedAnnotationIntrospector(ai);
                }

                public void setMixInAnnotations(Class<?> target, Class<?> mixinSource) {
                    mapper._deserializationConfig.addMixInAnnotations(target, mixinSource);
                    mapper._serializationConfig.addMixInAnnotations(target, mixinSource);
                }
            });
        }
    }

    public ObjectMapper withModule(Module module) {
        registerModule(module);
        return this;
    }

    public SerializationConfig getSerializationConfig() {
        return this._serializationConfig;
    }

    public SerializationConfig copySerializationConfig() {
        return this._serializationConfig.createUnshared(this._subtypeResolver);
    }

    public ObjectMapper setSerializationConfig(SerializationConfig cfg) {
        this._serializationConfig = cfg;
        return this;
    }

    public DeserializationConfig getDeserializationConfig() {
        return this._deserializationConfig;
    }

    public DeserializationConfig copyDeserializationConfig() {
        return this._deserializationConfig.createUnshared(this._subtypeResolver).passSerializationFeatures(this._serializationConfig._featureFlags);
    }

    public ObjectMapper setDeserializationConfig(DeserializationConfig cfg) {
        this._deserializationConfig = cfg;
        return this;
    }

    public ObjectMapper setSerializerFactory(SerializerFactory f) {
        this._serializerFactory = f;
        return this;
    }

    public ObjectMapper setSerializerProvider(SerializerProvider p) {
        this._serializerProvider = p;
        return this;
    }

    public SerializerProvider getSerializerProvider() {
        return this._serializerProvider;
    }

    public ObjectMapper setDeserializerProvider(DeserializerProvider p) {
        this._deserializerProvider = p;
        return this;
    }

    public DeserializerProvider getDeserializerProvider() {
        return this._deserializerProvider;
    }

    public VisibilityChecker<?> getVisibilityChecker() {
        return this._serializationConfig.getDefaultVisibilityChecker();
    }

    public void setVisibilityChecker(VisibilityChecker<?> vc) {
        this._deserializationConfig = this._deserializationConfig.withVisibilityChecker((VisibilityChecker) vc);
        this._serializationConfig = this._serializationConfig.withVisibilityChecker((VisibilityChecker) vc);
    }

    public ObjectMapper setVisibility(JsonMethod forMethod, Visibility visibility) {
        this._deserializationConfig = this._deserializationConfig.withVisibility(forMethod, visibility);
        this._serializationConfig = this._serializationConfig.withVisibility(forMethod, visibility);
        return this;
    }

    public SubtypeResolver getSubtypeResolver() {
        if (this._subtypeResolver == null) {
            this._subtypeResolver = new StdSubtypeResolver();
        }
        return this._subtypeResolver;
    }

    public void setSubtypeResolver(SubtypeResolver r) {
        this._subtypeResolver = r;
    }

    public ObjectMapper setAnnotationIntrospector(AnnotationIntrospector ai) {
        this._serializationConfig = this._serializationConfig.withAnnotationIntrospector(ai);
        this._deserializationConfig = this._deserializationConfig.withAnnotationIntrospector(ai);
        return this;
    }

    public ObjectMapper setPropertyNamingStrategy(PropertyNamingStrategy s) {
        this._serializationConfig = this._serializationConfig.withPropertyNamingStrategy(s);
        this._deserializationConfig = this._deserializationConfig.withPropertyNamingStrategy(s);
        return this;
    }

    public ObjectMapper setSerializationInclusion(Inclusion incl) {
        this._serializationConfig = this._serializationConfig.withSerializationInclusion(incl);
        return this;
    }

    public ObjectMapper enableDefaultTyping() {
        return enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE);
    }

    public ObjectMapper enableDefaultTyping(DefaultTyping dti) {
        return enableDefaultTyping(dti, As.WRAPPER_ARRAY);
    }

    public ObjectMapper enableDefaultTyping(DefaultTyping applicability, As includeAs) {
        return setDefaultTyping(new DefaultTypeResolverBuilder(applicability).init(Id.CLASS, null).inclusion(includeAs));
    }

    public ObjectMapper enableDefaultTypingAsProperty(DefaultTyping applicability, String propertyName) {
        return setDefaultTyping(new DefaultTypeResolverBuilder(applicability).init(Id.CLASS, null).inclusion(As.PROPERTY).typeProperty(propertyName));
    }

    public ObjectMapper disableDefaultTyping() {
        return setDefaultTyping(null);
    }

    public ObjectMapper setDefaultTyping(TypeResolverBuilder<?> typer) {
        this._deserializationConfig = this._deserializationConfig.withTypeResolverBuilder((TypeResolverBuilder) typer);
        this._serializationConfig = this._serializationConfig.withTypeResolverBuilder((TypeResolverBuilder) typer);
        return this;
    }

    public void registerSubtypes(Class<?>... classes) {
        getSubtypeResolver().registerSubtypes((Class[]) classes);
    }

    public void registerSubtypes(NamedType... types) {
        getSubtypeResolver().registerSubtypes(types);
    }

    public TypeFactory getTypeFactory() {
        return this._typeFactory;
    }

    public ObjectMapper setTypeFactory(TypeFactory f) {
        this._typeFactory = f;
        this._deserializationConfig = this._deserializationConfig.withTypeFactory(f);
        this._serializationConfig = this._serializationConfig.withTypeFactory(f);
        return this;
    }

    public JavaType constructType(Type t) {
        return this._typeFactory.constructType(t);
    }

    public ObjectMapper setNodeFactory(JsonNodeFactory f) {
        this._deserializationConfig = this._deserializationConfig.withNodeFactory(f);
        return this;
    }

    public void setFilters(FilterProvider filterProvider) {
        this._serializationConfig = this._serializationConfig.withFilters(filterProvider);
    }

    public JsonFactory getJsonFactory() {
        return this._jsonFactory;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this._deserializationConfig = this._deserializationConfig.withDateFormat(dateFormat);
        this._serializationConfig = this._serializationConfig.withDateFormat(dateFormat);
    }

    public void setHandlerInstantiator(HandlerInstantiator hi) {
        this._deserializationConfig = this._deserializationConfig.withHandlerInstantiator(hi);
        this._serializationConfig = this._serializationConfig.withHandlerInstantiator(hi);
    }

    public ObjectMapper setInjectableValues(InjectableValues injectableValues) {
        this._injectableValues = injectableValues;
        return this;
    }

    public ObjectMapper configure(SerializationConfig.Feature f, boolean state) {
        this._serializationConfig.set(f, state);
        return this;
    }

    public ObjectMapper configure(Feature f, boolean state) {
        this._deserializationConfig.set(f, state);
        return this;
    }

    public ObjectMapper configure(JsonParser.Feature f, boolean state) {
        this._jsonFactory.configure(f, state);
        return this;
    }

    public ObjectMapper configure(JsonGenerator.Feature f, boolean state) {
        this._jsonFactory.configure(f, state);
        return this;
    }

    public ObjectMapper enable(Feature... f) {
        this._deserializationConfig = this._deserializationConfig.with(f);
        return this;
    }

    public ObjectMapper disable(Feature... f) {
        this._deserializationConfig = this._deserializationConfig.without(f);
        return this;
    }

    public ObjectMapper enable(SerializationConfig.Feature... f) {
        this._serializationConfig = this._serializationConfig.with(f);
        return this;
    }

    public ObjectMapper disable(SerializationConfig.Feature... f) {
        this._serializationConfig = this._serializationConfig.without(f);
        return this;
    }

    public boolean isEnabled(SerializationConfig.Feature f) {
        return this._serializationConfig.isEnabled(f);
    }

    public boolean isEnabled(Feature f) {
        return this._deserializationConfig.isEnabled(f);
    }

    public boolean isEnabled(JsonParser.Feature f) {
        return this._jsonFactory.isEnabled(f);
    }

    public boolean isEnabled(JsonGenerator.Feature f) {
        return this._jsonFactory.isEnabled(f);
    }

    public JsonNodeFactory getNodeFactory() {
        return this._deserializationConfig.getNodeFactory();
    }

    public <T> T readValue(JsonParser jp, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), jp, this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(JsonParser jp, TypeReference<?> valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), jp, this._typeFactory.constructType((TypeReference) valueTypeRef));
    }

    public <T> T readValue(JsonParser jp, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), jp, valueType);
    }

    public JsonNode readTree(JsonParser jp) throws IOException, JsonProcessingException {
        DeserializationConfig cfg = copyDeserializationConfig();
        if (jp.getCurrentToken() == null && jp.nextToken() == null) {
            return null;
        }
        JsonNode n = (JsonNode) _readValue(cfg, jp, JSON_NODE_TYPE);
        return n == null ? getNodeFactory().nullNode() : n;
    }

    public <T> MappingIterator<T> readValues(JsonParser jp, JavaType valueType) throws IOException, JsonProcessingException {
        DeserializationConfig config = copyDeserializationConfig();
        return new MappingIterator(valueType, jp, _createDeserializationContext(jp, config), _findRootDeserializer(config, valueType), false, null);
    }

    public <T> MappingIterator<T> readValues(JsonParser jp, Class<T> valueType) throws IOException, JsonProcessingException {
        return readValues(jp, this._typeFactory.constructType((Type) valueType));
    }

    public <T> MappingIterator<T> readValues(JsonParser jp, TypeReference<?> valueTypeRef) throws IOException, JsonProcessingException {
        return readValues(jp, this._typeFactory.constructType((TypeReference) valueTypeRef));
    }

    public <T> T readValue(JsonParser jp, Class<T> valueType, DeserializationConfig cfg) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(cfg, jp, this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(JsonParser jp, TypeReference<?> valueTypeRef, DeserializationConfig cfg) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(cfg, jp, this._typeFactory.constructType((TypeReference) valueTypeRef));
    }

    public <T> T readValue(JsonParser jp, JavaType valueType, DeserializationConfig cfg) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(cfg, jp, valueType);
    }

    public JsonNode readTree(JsonParser jp, DeserializationConfig cfg) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) _readValue(cfg, jp, JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(InputStream in) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) _readMapAndClose(this._jsonFactory.createJsonParser(in), JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(Reader r) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) _readMapAndClose(this._jsonFactory.createJsonParser(r), JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(String content) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) _readMapAndClose(this._jsonFactory.createJsonParser(content), JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(byte[] content) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) _readMapAndClose(this._jsonFactory.createJsonParser(content), JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(File file) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) _readMapAndClose(this._jsonFactory.createJsonParser(file), JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public JsonNode readTree(URL source) throws IOException, JsonProcessingException {
        JsonNode n = (JsonNode) _readMapAndClose(this._jsonFactory.createJsonParser(source), JSON_NODE_TYPE);
        return n == null ? NullNode.instance : n;
    }

    public void writeValue(JsonGenerator jgen, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        SerializationConfig config = copySerializationConfig();
        if (config.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            _writeCloseableValue(jgen, value, config);
            return;
        }
        this._serializerProvider.serializeValue(config, jgen, value, this._serializerFactory);
        if (config.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE)) {
            jgen.flush();
        }
    }

    public void writeValue(JsonGenerator jgen, Object value, SerializationConfig config) throws IOException, JsonGenerationException, JsonMappingException {
        if (config.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            _writeCloseableValue(jgen, value, config);
            return;
        }
        this._serializerProvider.serializeValue(config, jgen, value, this._serializerFactory);
        if (config.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE)) {
            jgen.flush();
        }
    }

    public void writeTree(JsonGenerator jgen, JsonNode rootNode) throws IOException, JsonProcessingException {
        SerializationConfig config = copySerializationConfig();
        this._serializerProvider.serializeValue(config, jgen, rootNode, this._serializerFactory);
        if (config.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE)) {
            jgen.flush();
        }
    }

    public void writeTree(JsonGenerator jgen, JsonNode rootNode, SerializationConfig cfg) throws IOException, JsonProcessingException {
        this._serializerProvider.serializeValue(cfg, jgen, rootNode, this._serializerFactory);
        if (cfg.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE)) {
            jgen.flush();
        }
    }

    public ObjectNode createObjectNode() {
        return this._deserializationConfig.getNodeFactory().objectNode();
    }

    public ArrayNode createArrayNode() {
        return this._deserializationConfig.getNodeFactory().arrayNode();
    }

    public JsonParser treeAsTokens(JsonNode n) {
        return new TreeTraversingParser(n, this);
    }

    public <T> T treeToValue(JsonNode n, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return readValue(treeAsTokens(n), (Class) valueType);
    }

    public <T extends JsonNode> T valueToTree(Object fromValue) throws IllegalArgumentException {
        if (fromValue == null) {
            return null;
        }
        JsonGenerator buf = new TokenBuffer(this);
        try {
            writeValue(buf, fromValue);
            JsonParser jp = buf.asParser();
            T result = readTree(jp);
            jp.close();
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public boolean canSerialize(Class<?> type) {
        return this._serializerProvider.hasSerializerFor(copySerializationConfig(), type, this._serializerFactory);
    }

    public boolean canDeserialize(JavaType type) {
        return this._deserializerProvider.hasValueDeserializerFor(copyDeserializationConfig(), type);
    }

    public <T> T readValue(File src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(File src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(File src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
    }

    public <T> T readValue(URL src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(URL src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(URL src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
    }

    public <T> T readValue(String content, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(content), this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(String content, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(content), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(String content, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(content), valueType);
    }

    public <T> T readValue(Reader src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(Reader src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(Reader src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
    }

    public <T> T readValue(InputStream src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(InputStream src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(InputStream src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
    }

    public <T> T readValue(byte[] src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(byte[] src, int offset, int len, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src, offset, len), this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(byte[] src, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(byte[] src, int offset, int len, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src, offset, len), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(byte[] src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src), valueType);
    }

    public <T> T readValue(byte[] src, int offset, int len, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(this._jsonFactory.createJsonParser(src, offset, len), valueType);
    }

    public <T> T readValue(JsonNode root, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), treeAsTokens(root), this._typeFactory.constructType((Type) valueType));
    }

    public <T> T readValue(JsonNode root, TypeReference valueTypeRef) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), treeAsTokens(root), this._typeFactory.constructType(valueTypeRef));
    }

    public <T> T readValue(JsonNode root, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        return _readValue(copyDeserializationConfig(), treeAsTokens(root), valueType);
    }

    public void writeValue(File resultFile, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(resultFile, JsonEncoding.UTF8), value);
    }

    public void writeValue(OutputStream out, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8), value);
    }

    public void writeValue(Writer w, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(w), value);
    }

    public String writeValueAsString(Object value) throws IOException, JsonGenerationException, JsonMappingException {
        Writer sw = new SegmentedStringWriter(this._jsonFactory._getBufferRecycler());
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(sw), value);
        return sw.getAndClear();
    }

    public byte[] writeValueAsBytes(Object value) throws IOException, JsonGenerationException, JsonMappingException {
        OutputStream bb = new ByteArrayBuilder(this._jsonFactory._getBufferRecycler());
        _configAndWriteValue(this._jsonFactory.createJsonGenerator(bb, JsonEncoding.UTF8), value);
        byte[] result = bb.toByteArray();
        bb.release();
        return result;
    }

    public ObjectWriter writer() {
        return new ObjectWriter(this, copySerializationConfig());
    }

    public ObjectWriter writer(DateFormat df) {
        return new ObjectWriter(this, copySerializationConfig().withDateFormat(df));
    }

    public ObjectWriter writerWithView(Class<?> serializationView) {
        return new ObjectWriter(this, copySerializationConfig().withView(serializationView));
    }

    public ObjectWriter writerWithType(Class<?> rootType) {
        return new ObjectWriter(this, copySerializationConfig(), rootType == null ? null : this._typeFactory.constructType((Type) rootType), null);
    }

    public ObjectWriter writerWithType(JavaType rootType) {
        return new ObjectWriter(this, copySerializationConfig(), rootType, null);
    }

    public ObjectWriter writerWithType(TypeReference<?> rootType) {
        return new ObjectWriter(this, copySerializationConfig(), rootType == null ? null : this._typeFactory.constructType((TypeReference) rootType), null);
    }

    public ObjectWriter writer(PrettyPrinter pp) {
        if (pp == null) {
            pp = ObjectWriter.NULL_PRETTY_PRINTER;
        }
        return new ObjectWriter(this, copySerializationConfig(), null, pp);
    }

    public ObjectWriter writerWithDefaultPrettyPrinter() {
        return new ObjectWriter(this, copySerializationConfig(), null, _defaultPrettyPrinter());
    }

    public ObjectWriter writer(FilterProvider filterProvider) {
        return new ObjectWriter(this, copySerializationConfig().withFilters(filterProvider));
    }

    public ObjectWriter writer(FormatSchema schema) {
        return new ObjectWriter(this, copySerializationConfig(), schema);
    }

    @Deprecated
    public ObjectWriter typedWriter(Class<?> rootType) {
        return writerWithType((Class) rootType);
    }

    @Deprecated
    public ObjectWriter typedWriter(JavaType rootType) {
        return writerWithType(rootType);
    }

    @Deprecated
    public ObjectWriter typedWriter(TypeReference<?> rootType) {
        return writerWithType((TypeReference) rootType);
    }

    @Deprecated
    public ObjectWriter viewWriter(Class<?> serializationView) {
        return writerWithView(serializationView);
    }

    @Deprecated
    public ObjectWriter prettyPrintingWriter(PrettyPrinter pp) {
        return writer(pp);
    }

    @Deprecated
    public ObjectWriter defaultPrettyPrintingWriter() {
        return writerWithDefaultPrettyPrinter();
    }

    @Deprecated
    public ObjectWriter filteredWriter(FilterProvider filterProvider) {
        return writer(filterProvider);
    }

    @Deprecated
    public ObjectWriter schemaBasedWriter(FormatSchema schema) {
        return writer(schema);
    }

    public ObjectReader reader() {
        return new ObjectReader(this, copyDeserializationConfig()).withInjectableValues(this._injectableValues);
    }

    public ObjectReader readerForUpdating(Object valueToUpdate) {
        return new ObjectReader(this, copyDeserializationConfig(), this._typeFactory.constructType(valueToUpdate.getClass()), valueToUpdate, null, this._injectableValues);
    }

    public ObjectReader reader(JavaType type) {
        return new ObjectReader(this, copyDeserializationConfig(), type, null, null, this._injectableValues);
    }

    public ObjectReader reader(Class<?> type) {
        return reader(this._typeFactory.constructType((Type) type));
    }

    public ObjectReader reader(TypeReference<?> type) {
        return reader(this._typeFactory.constructType((TypeReference) type));
    }

    public ObjectReader reader(JsonNodeFactory f) {
        return new ObjectReader(this, copyDeserializationConfig()).withNodeFactory(f);
    }

    public ObjectReader reader(FormatSchema schema) {
        return new ObjectReader(this, copyDeserializationConfig(), null, null, schema, this._injectableValues);
    }

    public ObjectReader reader(InjectableValues injectableValues) {
        return new ObjectReader(this, copyDeserializationConfig(), null, null, null, injectableValues);
    }

    @Deprecated
    public ObjectReader updatingReader(Object valueToUpdate) {
        return readerForUpdating(valueToUpdate);
    }

    @Deprecated
    public ObjectReader schemaBasedReader(FormatSchema schema) {
        return reader(schema);
    }

    public <T> T convertValue(Object fromValue, Class<T> toValueType) throws IllegalArgumentException {
        return _convert(fromValue, this._typeFactory.constructType((Type) toValueType));
    }

    public <T> T convertValue(Object fromValue, TypeReference toValueTypeRef) throws IllegalArgumentException {
        return _convert(fromValue, this._typeFactory.constructType(toValueTypeRef));
    }

    public <T> T convertValue(Object fromValue, JavaType toValueType) throws IllegalArgumentException {
        return _convert(fromValue, toValueType);
    }

    protected Object _convert(Object fromValue, JavaType toValueType) throws IllegalArgumentException {
        if (fromValue == null) {
            return null;
        }
        JsonGenerator buf = new TokenBuffer(this);
        try {
            writeValue(buf, fromValue);
            JsonParser jp = buf.asParser();
            Object result = readValue(jp, toValueType);
            jp.close();
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public JsonSchema generateJsonSchema(Class<?> t) throws JsonMappingException {
        return generateJsonSchema(t, copySerializationConfig());
    }

    public JsonSchema generateJsonSchema(Class<?> t, SerializationConfig cfg) throws JsonMappingException {
        return this._serializerProvider.generateJsonSchema(t, cfg, this._serializerFactory);
    }

    protected PrettyPrinter _defaultPrettyPrinter() {
        return new DefaultPrettyPrinter();
    }

    protected final void _configAndWriteValue(JsonGenerator jgen, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        SerializationConfig cfg = copySerializationConfig();
        if (cfg.isEnabled(SerializationConfig.Feature.INDENT_OUTPUT)) {
            jgen.useDefaultPrettyPrinter();
        }
        if (cfg.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            _configAndWriteCloseable(jgen, value, cfg);
            return;
        }
        boolean closed = false;
        try {
            this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
            closed = true;
            jgen.close();
            if (1 == null) {
                try {
                    jgen.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            if (!closed) {
                try {
                    jgen.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    protected final void _configAndWriteValue(JsonGenerator jgen, Object value, Class<?> viewClass) throws IOException, JsonGenerationException, JsonMappingException {
        SerializationConfig cfg = copySerializationConfig().withView(viewClass);
        if (cfg.isEnabled(SerializationConfig.Feature.INDENT_OUTPUT)) {
            jgen.useDefaultPrettyPrinter();
        }
        if (cfg.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE) && (value instanceof Closeable)) {
            _configAndWriteCloseable(jgen, value, cfg);
            return;
        }
        boolean closed = false;
        try {
            this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
            closed = true;
            jgen.close();
            if (1 == null) {
                try {
                    jgen.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            if (!closed) {
                try {
                    jgen.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    private final void _configAndWriteCloseable(JsonGenerator jgen, Object value, SerializationConfig cfg) throws IOException, JsonGenerationException, JsonMappingException {
        Closeable toClose = (Closeable) value;
        try {
            this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
            JsonGenerator tmpJgen = jgen;
            jgen = null;
            tmpJgen.close();
            Closeable tmpToClose = toClose;
            toClose = null;
            tmpToClose.close();
            if (jgen != null) {
                try {
                    jgen.close();
                } catch (IOException e) {
                }
            }
            if (toClose != null) {
                try {
                    toClose.close();
                } catch (IOException e2) {
                }
            }
        } catch (Throwable th) {
            if (jgen != null) {
                try {
                    jgen.close();
                } catch (IOException e3) {
                }
            }
            if (toClose != null) {
                try {
                    toClose.close();
                } catch (IOException e4) {
                }
            }
        }
    }

    private final void _writeCloseableValue(JsonGenerator jgen, Object value, SerializationConfig cfg) throws IOException, JsonGenerationException, JsonMappingException {
        Closeable toClose = (Closeable) value;
        try {
            this._serializerProvider.serializeValue(cfg, jgen, value, this._serializerFactory);
            if (cfg.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE)) {
                jgen.flush();
            }
            Closeable tmpToClose = toClose;
            toClose = null;
            tmpToClose.close();
            if (toClose != null) {
                try {
                    toClose.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            if (toClose != null) {
                try {
                    toClose.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    protected Object _readValue(DeserializationConfig cfg, JsonParser jp, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        Object nullValue;
        JsonToken t = _initForReading(jp);
        if (t == JsonToken.VALUE_NULL) {
            nullValue = _findRootDeserializer(cfg, valueType).getNullValue();
        } else if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
            nullValue = null;
        } else {
            DeserializationContext ctxt = _createDeserializationContext(jp, cfg);
            JsonDeserializer<Object> deser = _findRootDeserializer(cfg, valueType);
            if (cfg.isEnabled(Feature.UNWRAP_ROOT_VALUE)) {
                nullValue = _unwrapAndDeserialize(jp, valueType, ctxt, deser);
            } else {
                nullValue = deser.deserialize(jp, ctxt);
            }
        }
        jp.clearCurrentToken();
        return nullValue;
    }

    protected Object _readMapAndClose(JsonParser jp, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
        try {
            Object nullValue;
            JsonToken t = _initForReading(jp);
            if (t == JsonToken.VALUE_NULL) {
                nullValue = _findRootDeserializer(this._deserializationConfig, valueType).getNullValue();
            } else if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
                nullValue = null;
            } else {
                DeserializationConfig cfg = copyDeserializationConfig();
                DeserializationContext ctxt = _createDeserializationContext(jp, cfg);
                JsonDeserializer<Object> deser = _findRootDeserializer(cfg, valueType);
                if (cfg.isEnabled(Feature.UNWRAP_ROOT_VALUE)) {
                    nullValue = _unwrapAndDeserialize(jp, valueType, ctxt, deser);
                } else {
                    nullValue = deser.deserialize(jp, ctxt);
                }
            }
            jp.clearCurrentToken();
            return nullValue;
        } finally {
            try {
                jp.close();
            } catch (IOException e) {
            }
        }
    }

    protected JsonToken _initForReading(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        JsonToken t = jp.getCurrentToken();
        if (t == null) {
            t = jp.nextToken();
            if (t == null) {
                throw new EOFException("No content to map to Object due to end of input");
            }
        }
        return t;
    }

    protected Object _unwrapAndDeserialize(JsonParser jp, JavaType rootType, DeserializationContext ctxt, JsonDeserializer<Object> deser) throws IOException, JsonParseException, JsonMappingException {
        SerializedString rootName = this._deserializerProvider.findExpectedRootName(ctxt.getConfig(), rootType);
        if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
            throw JsonMappingException.from(jp, "Current token not START_OBJECT (needed to unwrap root name '" + rootName + "'), but " + jp.getCurrentToken());
        } else if (jp.nextToken() != JsonToken.FIELD_NAME) {
            throw JsonMappingException.from(jp, "Current token not FIELD_NAME (to contain expected root name '" + rootName + "'), but " + jp.getCurrentToken());
        } else {
            String actualName = jp.getCurrentName();
            if (rootName.getValue().equals(actualName)) {
                jp.nextToken();
                Object result = deser.deserialize(jp, ctxt);
                if (jp.nextToken() == JsonToken.END_OBJECT) {
                    return result;
                }
                throw JsonMappingException.from(jp, "Current token not END_OBJECT (to match wrapper object with root name '" + rootName + "'), but " + jp.getCurrentToken());
            }
            throw JsonMappingException.from(jp, "Root name '" + actualName + "' does not match expected ('" + rootName + "') for type " + rootType);
        }
    }

    protected JsonDeserializer<Object> _findRootDeserializer(DeserializationConfig cfg, JavaType valueType) throws JsonMappingException {
        JsonDeserializer<Object> deser = (JsonDeserializer) this._rootDeserializers.get(valueType);
        if (deser != null) {
            return deser;
        }
        deser = this._deserializerProvider.findTypedValueDeserializer(cfg, valueType, null);
        if (deser == null) {
            throw new JsonMappingException("Can not find a deserializer for type " + valueType);
        }
        this._rootDeserializers.put(valueType, deser);
        return deser;
    }

    protected DeserializationContext _createDeserializationContext(JsonParser jp, DeserializationConfig cfg) {
        return new StdDeserializationContext(cfg, jp, this._deserializerProvider, this._injectableValues);
    }
}
