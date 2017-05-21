package org.codehaus.jackson.map.deser.std;

import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.io.IOException;
import java.lang.reflect.Method;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.EnumResolver;

@JsonCachable
public class EnumDeserializer extends StdScalarDeserializer<Enum<?>> {
    protected final EnumResolver<?> _resolver;

    protected static class FactoryBasedDeserializer extends StdScalarDeserializer<Object> {
        protected final Class<?> _enumClass;
        protected final Method _factory;
        protected final Class<?> _inputType;

        public FactoryBasedDeserializer(Class<?> cls, AnnotatedMethod f, Class<?> inputType) {
            super(Enum.class);
            this._enumClass = cls;
            this._factory = f.getAnnotated();
            this._inputType = inputType;
        }

        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String text;
            if (this._inputType == null) {
                text = jp.getText();
            } else if (this._inputType == Integer.class) {
                text = Integer.valueOf(jp.getValueAsInt());
            } else if (this._inputType == Long.class) {
                text = Long.valueOf(jp.getValueAsLong());
            } else {
                throw ctxt.mappingException(this._enumClass);
            }
            try {
                return this._factory.invoke(this._enumClass, new Object[]{text});
            } catch (Exception e) {
                ClassUtil.unwrapAndThrowAsIAE(e);
                return null;
            }
        }
    }

    public EnumDeserializer(EnumResolver<?> res) {
        super(Enum.class);
        this._resolver = res;
    }

    public static JsonDeserializer<?> deserializerForCreator(DeserializationConfig config, Class<?> enumClass, AnnotatedMethod factory) {
        Class<?> raw = factory.getParameterClass(0);
        if (raw == String.class) {
            raw = null;
        } else if (raw == Integer.TYPE || raw == Integer.class) {
            raw = Integer.class;
        } else if (raw == Long.TYPE || raw == Long.class) {
            raw = Long.class;
        } else {
            throw new IllegalArgumentException("Parameter #0 type for factory method (" + factory + ") not suitable, must be java.lang.String or int/Integer/long/Long");
        }
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            ClassUtil.checkAndFixAccess(factory.getMember());
        }
        return new FactoryBasedDeserializer(enumClass, factory, raw);
    }

    public Enum<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken curr = jp.getCurrentToken();
        Enum<?> result;
        if (curr == JsonToken.VALUE_STRING || curr == JsonToken.FIELD_NAME) {
            result = this._resolver.findEnum(jp.getText());
            if (result != null) {
                return result;
            }
            throw ctxt.weirdStringException(this._resolver.getEnumClass(), "value not one of declared Enum instance names");
        } else if (curr != JsonToken.VALUE_NUMBER_INT) {
            throw ctxt.mappingException(this._resolver.getEnumClass());
        } else if (ctxt.isEnabled(Feature.FAIL_ON_NUMBERS_FOR_ENUMS)) {
            throw ctxt.mappingException("Not allowed to deserialize Enum value out of JSON number (disable DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS to allow)");
        } else {
            result = this._resolver.getEnum(jp.getIntValue());
            if (result != null) {
                return result;
            }
            throw ctxt.weirdNumberException(this._resolver.getEnumClass(), "index value outside legal index range [0.." + this._resolver.lastValidIndex() + RequestParameters.RIGHT_BRACKETS);
        }
    }
}
