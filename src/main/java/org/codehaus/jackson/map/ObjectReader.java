package org.codehaus.jackson.map;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.FormatSchema;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.deser.StdDeserializationContext;
import org.codehaus.jackson.map.type.SimpleType;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.NullNode;
import org.codehaus.jackson.node.TreeTraversingParser;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.util.VersionUtil;

public class ObjectReader extends ObjectCodec implements Versioned {
    private static final JavaType JSON_NODE_TYPE = SimpleType.constructUnsafe(JsonNode.class);
    protected final DeserializationConfig _config;
    protected final InjectableValues _injectableValues;
    protected final JsonFactory _jsonFactory;
    protected final DeserializerProvider _provider;
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
    protected final FormatSchema _schema;
    protected final boolean _unwrapRoot;
    protected final Object _valueToUpdate;
    protected final JavaType _valueType;

    protected ObjectReader(ObjectMapper mapper, DeserializationConfig config) {
        this(mapper, config, null, null, null, null);
    }

    protected ObjectReader(ObjectMapper mapper, DeserializationConfig config, JavaType valueType, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues) {
        this._config = config;
        this._rootDeserializers = mapper._rootDeserializers;
        this._provider = mapper._deserializerProvider;
        this._jsonFactory = mapper._jsonFactory;
        this._valueType = valueType;
        this._valueToUpdate = valueToUpdate;
        if (valueToUpdate == null || !valueType.isArrayType()) {
            this._schema = schema;
            this._injectableValues = injectableValues;
            this._unwrapRoot = config.isEnabled(Feature.UNWRAP_ROOT_VALUE);
            return;
        }
        throw new IllegalArgumentException("Can not update an array value");
    }

    protected ObjectReader(ObjectReader base, DeserializationConfig config, JavaType valueType, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues) {
        this._config = config;
        this._rootDeserializers = base._rootDeserializers;
        this._provider = base._provider;
        this._jsonFactory = base._jsonFactory;
        this._valueType = valueType;
        this._valueToUpdate = valueToUpdate;
        if (valueToUpdate == null || !valueType.isArrayType()) {
            this._schema = schema;
            this._injectableValues = injectableValues;
            this._unwrapRoot = config.isEnabled(Feature.UNWRAP_ROOT_VALUE);
            return;
        }
        throw new IllegalArgumentException("Can not update an array value");
    }

    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    public ObjectReader withType(JavaType valueType) {
        if (valueType == this._valueType) {
            return this;
        }
        return new ObjectReader(this, this._config, valueType, this._valueToUpdate, this._schema, this._injectableValues);
    }

    public ObjectReader withType(Class<?> valueType) {
        return withType(this._config.constructType((Class) valueType));
    }

    public ObjectReader withType(Type valueType) {
        return withType(this._config.getTypeFactory().constructType(valueType));
    }

    public ObjectReader withType(TypeReference<?> valueTypeRef) {
        return withType(this._config.getTypeFactory().constructType(valueTypeRef.getType()));
    }

    public ObjectReader withNodeFactory(JsonNodeFactory f) {
        return f == this._config.getNodeFactory() ? this : new ObjectReader(this, this._config.withNodeFactory(f), this._valueType, this._valueToUpdate, this._schema, this._injectableValues);
    }

    public ObjectReader withValueToUpdate(Object value) {
        if (value == this._valueToUpdate) {
            return this;
        }
        if (value == null) {
            throw new IllegalArgumentException("cat not update null value");
        }
        return new ObjectReader(this, this._config, this._valueType == null ? this._config.constructType(value.getClass()) : this._valueType, value, this._schema, this._injectableValues);
    }

    public ObjectReader withSchema(FormatSchema schema) {
        if (this._schema == schema) {
            return this;
        }
        return new ObjectReader(this, this._config, this._valueType, this._valueToUpdate, schema, this._injectableValues);
    }

    public ObjectReader withInjectableValues(InjectableValues injectableValues) {
        return this._injectableValues == injectableValues ? this : new ObjectReader(this, this._config, this._valueType, this._valueToUpdate, this._schema, injectableValues);
    }

    public <T> T readValue(JsonParser jp) throws IOException, JsonProcessingException {
        return _bind(jp);
    }

    public <T> T readValue(JsonParser jp, Class<T> valueType) throws IOException, JsonProcessingException {
        return withType((Class) valueType).readValue(jp);
    }

    public <T> T readValue(JsonParser jp, TypeReference<?> valueTypeRef) throws IOException, JsonProcessingException {
        return withType((TypeReference) valueTypeRef).readValue(jp);
    }

    public <T> T readValue(JsonParser jp, JavaType valueType) throws IOException, JsonProcessingException {
        return withType(valueType).readValue(jp);
    }

    public JsonNode readTree(JsonParser jp) throws IOException, JsonProcessingException {
        return _bindAsTree(jp);
    }

    public <T> Iterator<T> readValues(JsonParser jp, Class<T> valueType) throws IOException, JsonProcessingException {
        return withType((Class) valueType).readValues(jp);
    }

    public <T> Iterator<T> readValues(JsonParser jp, TypeReference<?> valueTypeRef) throws IOException, JsonProcessingException {
        return withType((TypeReference) valueTypeRef).readValues(jp);
    }

    public <T> Iterator<T> readValues(JsonParser jp, JavaType valueType) throws IOException, JsonProcessingException {
        return withType(valueType).readValues(jp);
    }

    public <T> T readValue(InputStream src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(Reader src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(String src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(byte[] src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(byte[] src, int offset, int length) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src, offset, length));
    }

    public <T> T readValue(File src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(URL src) throws IOException, JsonProcessingException {
        return _bindAndClose(this._jsonFactory.createJsonParser(src));
    }

    public <T> T readValue(JsonNode src) throws IOException, JsonProcessingException {
        return _bindAndClose(treeAsTokens(src));
    }

    public JsonNode readTree(InputStream in) throws IOException, JsonProcessingException {
        return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(in));
    }

    public JsonNode readTree(Reader r) throws IOException, JsonProcessingException {
        return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(r));
    }

    public JsonNode readTree(String content) throws IOException, JsonProcessingException {
        return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(content));
    }

    public <T> MappingIterator<T> readValues(JsonParser jp) throws IOException, JsonProcessingException {
        return new MappingIterator(this._valueType, jp, _createDeserializationContext(jp, this._config), _findRootDeserializer(this._config, this._valueType), false, this._valueToUpdate);
    }

    public <T> MappingIterator<T> readValues(InputStream src) throws IOException, JsonProcessingException {
        JsonParser jp = this._jsonFactory.createJsonParser(src);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        return new MappingIterator(this._valueType, jp, _createDeserializationContext(jp, this._config), _findRootDeserializer(this._config, this._valueType), true, this._valueToUpdate);
    }

    public <T> MappingIterator<T> readValues(Reader src) throws IOException, JsonProcessingException {
        JsonParser jp = this._jsonFactory.createJsonParser(src);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        return new MappingIterator(this._valueType, jp, _createDeserializationContext(jp, this._config), _findRootDeserializer(this._config, this._valueType), true, this._valueToUpdate);
    }

    public <T> MappingIterator<T> readValues(String json) throws IOException, JsonProcessingException {
        JsonParser jp = this._jsonFactory.createJsonParser(json);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        return new MappingIterator(this._valueType, jp, _createDeserializationContext(jp, this._config), _findRootDeserializer(this._config, this._valueType), true, this._valueToUpdate);
    }

    public <T> MappingIterator<T> readValues(byte[] src, int offset, int length) throws IOException, JsonProcessingException {
        JsonParser jp = this._jsonFactory.createJsonParser(src, offset, length);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        return new MappingIterator(this._valueType, jp, _createDeserializationContext(jp, this._config), _findRootDeserializer(this._config, this._valueType), true, this._valueToUpdate);
    }

    public final <T> MappingIterator<T> readValues(byte[] src) throws IOException, JsonProcessingException {
        return readValues(src, 0, src.length);
    }

    public <T> MappingIterator<T> readValues(File src) throws IOException, JsonProcessingException {
        JsonParser jp = this._jsonFactory.createJsonParser(src);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        return new MappingIterator(this._valueType, jp, _createDeserializationContext(jp, this._config), _findRootDeserializer(this._config, this._valueType), true, this._valueToUpdate);
    }

    public <T> MappingIterator<T> readValues(URL src) throws IOException, JsonProcessingException {
        JsonParser jp = this._jsonFactory.createJsonParser(src);
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        return new MappingIterator(this._valueType, jp, _createDeserializationContext(jp, this._config), _findRootDeserializer(this._config, this._valueType), true, this._valueToUpdate);
    }

    protected Object _bind(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        Object result;
        JsonToken t = _initForReading(jp);
        if (t == JsonToken.VALUE_NULL) {
            if (this._valueToUpdate == null) {
                result = _findRootDeserializer(this._config, this._valueType).getNullValue();
            } else {
                result = this._valueToUpdate;
            }
        } else if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
            result = this._valueToUpdate;
        } else {
            DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
            JsonDeserializer<Object> deser = _findRootDeserializer(this._config, this._valueType);
            if (this._unwrapRoot) {
                result = _unwrapAndDeserialize(jp, ctxt, this._valueType, deser);
            } else if (this._valueToUpdate == null) {
                result = deser.deserialize(jp, ctxt);
            } else {
                deser.deserialize(jp, ctxt, this._valueToUpdate);
                result = this._valueToUpdate;
            }
        }
        jp.clearCurrentToken();
        return result;
    }

    protected Object _bindAndClose(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        try {
            Object result;
            JsonToken t = _initForReading(jp);
            if (t == JsonToken.VALUE_NULL) {
                if (this._valueToUpdate == null) {
                    result = _findRootDeserializer(this._config, this._valueType).getNullValue();
                } else {
                    result = this._valueToUpdate;
                }
            } else if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
                result = this._valueToUpdate;
            } else {
                DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
                JsonDeserializer<Object> deser = _findRootDeserializer(this._config, this._valueType);
                if (this._unwrapRoot) {
                    result = _unwrapAndDeserialize(jp, ctxt, this._valueType, deser);
                } else if (this._valueToUpdate == null) {
                    result = deser.deserialize(jp, ctxt);
                } else {
                    deser.deserialize(jp, ctxt, this._valueToUpdate);
                    result = this._valueToUpdate;
                }
            }
            return result;
        } finally {
            try {
                jp.close();
            } catch (IOException e) {
            }
        }
    }

    protected JsonNode _bindAsTree(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        JsonNode result;
        JsonToken t = _initForReading(jp);
        if (t == JsonToken.VALUE_NULL || t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
            result = NullNode.instance;
        } else {
            DeserializationContext ctxt = _createDeserializationContext(jp, this._config);
            JsonDeserializer<Object> deser = _findRootDeserializer(this._config, JSON_NODE_TYPE);
            if (this._unwrapRoot) {
                result = (JsonNode) _unwrapAndDeserialize(jp, ctxt, JSON_NODE_TYPE, deser);
            } else {
                result = (JsonNode) deser.deserialize(jp, ctxt);
            }
        }
        jp.clearCurrentToken();
        return result;
    }

    protected JsonNode _bindAndCloseAsTree(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        if (this._schema != null) {
            jp.setSchema(this._schema);
        }
        try {
            JsonNode _bindAsTree = _bindAsTree(jp);
            return _bindAsTree;
        } finally {
            try {
                jp.close();
            } catch (IOException e) {
            }
        }
    }

    protected static JsonToken _initForReading(JsonParser jp) throws IOException, JsonParseException, JsonMappingException {
        JsonToken t = jp.getCurrentToken();
        if (t == null) {
            t = jp.nextToken();
            if (t == null) {
                throw new EOFException("No content to map to Object due to end of input");
            }
        }
        return t;
    }

    protected JsonDeserializer<Object> _findRootDeserializer(DeserializationConfig cfg, JavaType valueType) throws JsonMappingException {
        if (valueType == null) {
            throw new JsonMappingException("No value type configured for ObjectReader");
        }
        JsonDeserializer<Object> deser = (JsonDeserializer) this._rootDeserializers.get(valueType);
        if (deser != null) {
            return deser;
        }
        deser = this._provider.findTypedValueDeserializer(cfg, valueType, null);
        if (deser == null) {
            throw new JsonMappingException("Can not find a deserializer for type " + valueType);
        }
        this._rootDeserializers.put(valueType, deser);
        return deser;
    }

    protected DeserializationContext _createDeserializationContext(JsonParser jp, DeserializationConfig cfg) {
        return new StdDeserializationContext(cfg, jp, this._provider, this._injectableValues);
    }

    protected Object _unwrapAndDeserialize(JsonParser jp, DeserializationContext ctxt, JavaType rootType, JsonDeserializer<Object> deser) throws IOException, JsonParseException, JsonMappingException {
        SerializedString rootName = this._provider.findExpectedRootName(ctxt.getConfig(), rootType);
        if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
            throw JsonMappingException.from(jp, "Current token not START_OBJECT (needed to unwrap root name '" + rootName + "'), but " + jp.getCurrentToken());
        } else if (jp.nextToken() != JsonToken.FIELD_NAME) {
            throw JsonMappingException.from(jp, "Current token not FIELD_NAME (to contain expected root name '" + rootName + "'), but " + jp.getCurrentToken());
        } else {
            String actualName = jp.getCurrentName();
            if (rootName.getValue().equals(actualName)) {
                Object result;
                jp.nextToken();
                if (this._valueToUpdate == null) {
                    result = deser.deserialize(jp, ctxt);
                } else {
                    deser.deserialize(jp, ctxt, this._valueToUpdate);
                    result = this._valueToUpdate;
                }
                if (jp.nextToken() == JsonToken.END_OBJECT) {
                    return result;
                }
                throw JsonMappingException.from(jp, "Current token not END_OBJECT (to match wrapper object with root name '" + rootName + "'), but " + jp.getCurrentToken());
            }
            throw JsonMappingException.from(jp, "Root name '" + actualName + "' does not match expected ('" + rootName + "') for type " + rootType);
        }
    }

    public JsonNode createArrayNode() {
        return this._config.getNodeFactory().arrayNode();
    }

    public JsonNode createObjectNode() {
        return this._config.getNodeFactory().objectNode();
    }

    public JsonParser treeAsTokens(JsonNode n) {
        return new TreeTraversingParser(n, this);
    }

    public <T> T treeToValue(JsonNode n, Class<T> valueType) throws IOException, JsonProcessingException {
        return readValue(treeAsTokens(n), (Class) valueType);
    }

    public void writeTree(JsonGenerator jgen, JsonNode rootNode) throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException("Not implemented for ObjectReader");
    }

    public void writeValue(JsonGenerator jgen, Object value) throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException("Not implemented for ObjectReader");
    }
}
