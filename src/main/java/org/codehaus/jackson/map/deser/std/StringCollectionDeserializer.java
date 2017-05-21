package org.codehaus.jackson.map.deser.std;

import java.io.IOException;
import java.util.Collection;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.BeanProperty.Std;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ResolvableDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.deser.ValueInstantiator;
import org.codehaus.jackson.map.introspect.AnnotatedWithParams;
import org.codehaus.jackson.type.JavaType;

@JacksonStdImpl
public final class StringCollectionDeserializer extends ContainerDeserializerBase<Collection<String>> implements ResolvableDeserializer {
    protected final JavaType _collectionType;
    protected JsonDeserializer<Object> _delegateDeserializer;
    protected final boolean _isDefaultDeserializer;
    protected final JsonDeserializer<String> _valueDeserializer;
    protected final ValueInstantiator _valueInstantiator;

    public StringCollectionDeserializer(JavaType collectionType, JsonDeserializer<?> valueDeser, ValueInstantiator valueInstantiator) {
        super(collectionType.getRawClass());
        this._collectionType = collectionType;
        this._valueDeserializer = valueDeser;
        this._valueInstantiator = valueInstantiator;
        this._isDefaultDeserializer = isDefaultSerializer(valueDeser);
    }

    protected StringCollectionDeserializer(StringCollectionDeserializer src) {
        super(src._valueClass);
        this._collectionType = src._collectionType;
        this._valueDeserializer = src._valueDeserializer;
        this._valueInstantiator = src._valueInstantiator;
        this._isDefaultDeserializer = src._isDefaultDeserializer;
    }

    public void resolve(DeserializationConfig config, DeserializerProvider provider) throws JsonMappingException {
        AnnotatedWithParams delegateCreator = this._valueInstantiator.getDelegateCreator();
        if (delegateCreator != null) {
            JavaType delegateType = this._valueInstantiator.getDelegateType();
            this._delegateDeserializer = findDeserializer(config, provider, delegateType, new Std(null, delegateType, null, delegateCreator));
        }
    }

    public JavaType getContentType() {
        return this._collectionType.getContentType();
    }

    public JsonDeserializer<Object> getContentDeserializer() {
        return this._valueDeserializer;
    }

    public Collection<String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (this._delegateDeserializer != null) {
            return (Collection) this._valueInstantiator.createUsingDelegate(this._delegateDeserializer.deserialize(jp, ctxt));
        }
        return deserialize(jp, ctxt, (Collection) this._valueInstantiator.createUsingDefault());
    }

    public Collection<String> deserialize(JsonParser jp, DeserializationContext ctxt, Collection<String> result) throws IOException, JsonProcessingException {
        if (!jp.isExpectedStartArrayToken()) {
            return handleNonArray(jp, ctxt, result);
        }
        if (!this._isDefaultDeserializer) {
            return deserializeUsingCustom(jp, ctxt, result);
        }
        while (true) {
            JsonToken t = jp.nextToken();
            if (t == JsonToken.END_ARRAY) {
                return result;
            }
            result.add(t == JsonToken.VALUE_NULL ? null : jp.getText());
        }
    }

    private Collection<String> deserializeUsingCustom(JsonParser jp, DeserializationContext ctxt, Collection<String> result) throws IOException, JsonProcessingException {
        JsonDeserializer<String> deser = this._valueDeserializer;
        while (true) {
            JsonToken t = jp.nextToken();
            if (t == JsonToken.END_ARRAY) {
                return result;
            }
            String value;
            if (t == JsonToken.VALUE_NULL) {
                value = null;
            } else {
                value = (String) deser.deserialize(jp, ctxt);
            }
            result.add(value);
        }
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }

    private final Collection<String> handleNonArray(JsonParser jp, DeserializationContext ctxt, Collection<String> result) throws IOException, JsonProcessingException {
        if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
            String value;
            JsonDeserializer<String> valueDes = this._valueDeserializer;
            if (jp.getCurrentToken() == JsonToken.VALUE_NULL) {
                value = null;
            } else {
                value = valueDes == null ? jp.getText() : (String) valueDes.deserialize(jp, ctxt);
            }
            result.add(value);
            return result;
        }
        throw ctxt.mappingException(this._collectionType.getRawClass());
    }
}
