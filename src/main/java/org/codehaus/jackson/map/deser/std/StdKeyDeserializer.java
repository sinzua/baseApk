package org.codehaus.jackson.map.deser.std;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.io.NumberInput;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.EnumResolver;

public abstract class StdKeyDeserializer extends KeyDeserializer {
    protected final Class<?> _keyClass;

    static final class BoolKD extends StdKeyDeserializer {
        BoolKD() {
            super(Boolean.class);
        }

        public Boolean _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            if ("true".equals(key)) {
                return Boolean.TRUE;
            }
            if ("false".equals(key)) {
                return Boolean.FALSE;
            }
            throw ctxt.weirdKeyException(this._keyClass, key, "value not 'true' or 'false'");
        }
    }

    static final class ByteKD extends StdKeyDeserializer {
        ByteKD() {
            super(Byte.class);
        }

        public Byte _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            int value = _parseInt(key);
            if (value >= -128 && value <= 255) {
                return Byte.valueOf((byte) value);
            }
            throw ctxt.weirdKeyException(this._keyClass, key, "overflow, value can not be represented as 8-bit value");
        }
    }

    static final class CalendarKD extends StdKeyDeserializer {
        protected CalendarKD() {
            super(Calendar.class);
        }

        public Calendar _parse(String key, DeserializationContext ctxt) throws IllegalArgumentException, JsonMappingException {
            Date date = ctxt.parseDate(key);
            return date == null ? null : ctxt.constructCalendar(date);
        }
    }

    static final class CharKD extends StdKeyDeserializer {
        CharKD() {
            super(Character.class);
        }

        public Character _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            if (key.length() == 1) {
                return Character.valueOf(key.charAt(0));
            }
            throw ctxt.weirdKeyException(this._keyClass, key, "can only convert 1-character Strings");
        }
    }

    static final class DateKD extends StdKeyDeserializer {
        protected DateKD() {
            super(Date.class);
        }

        public Date _parse(String key, DeserializationContext ctxt) throws IllegalArgumentException, JsonMappingException {
            return ctxt.parseDate(key);
        }
    }

    static final class DoubleKD extends StdKeyDeserializer {
        DoubleKD() {
            super(Double.class);
        }

        public Double _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            return Double.valueOf(_parseDouble(key));
        }
    }

    static final class EnumKD extends StdKeyDeserializer {
        protected final AnnotatedMethod _factory;
        protected final EnumResolver<?> _resolver;

        protected EnumKD(EnumResolver<?> er, AnnotatedMethod factory) {
            super(er.getEnumClass());
            this._resolver = er;
            this._factory = factory;
        }

        public Object _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            Object call1;
            if (this._factory != null) {
                try {
                    call1 = this._factory.call1(key);
                } catch (Exception e) {
                    ClassUtil.unwrapAndThrowAsIAE(e);
                }
                return call1;
            }
            call1 = this._resolver.findEnum(key);
            if (call1 == null) {
                throw ctxt.weirdKeyException(this._keyClass, key, "not one of values for Enum class");
            }
            return call1;
        }
    }

    static final class FloatKD extends StdKeyDeserializer {
        FloatKD() {
            super(Float.class);
        }

        public Float _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            return Float.valueOf((float) _parseDouble(key));
        }
    }

    static final class IntKD extends StdKeyDeserializer {
        IntKD() {
            super(Integer.class);
        }

        public Integer _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            return Integer.valueOf(_parseInt(key));
        }
    }

    static final class LongKD extends StdKeyDeserializer {
        LongKD() {
            super(Long.class);
        }

        public Long _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            return Long.valueOf(_parseLong(key));
        }
    }

    static final class ShortKD extends StdKeyDeserializer {
        ShortKD() {
            super(Integer.class);
        }

        public Short _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            int value = _parseInt(key);
            if (value >= -32768 && value <= 32767) {
                return Short.valueOf((short) value);
            }
            throw ctxt.weirdKeyException(this._keyClass, key, "overflow, value can not be represented as 16-bit value");
        }
    }

    static final class StringCtorKeyDeserializer extends StdKeyDeserializer {
        protected final Constructor<?> _ctor;

        public StringCtorKeyDeserializer(Constructor<?> ctor) {
            super(ctor.getDeclaringClass());
            this._ctor = ctor;
        }

        public Object _parse(String key, DeserializationContext ctxt) throws Exception {
            return this._ctor.newInstance(new Object[]{key});
        }
    }

    static final class StringFactoryKeyDeserializer extends StdKeyDeserializer {
        final Method _factoryMethod;

        public StringFactoryKeyDeserializer(Method fm) {
            super(fm.getDeclaringClass());
            this._factoryMethod = fm;
        }

        public Object _parse(String key, DeserializationContext ctxt) throws Exception {
            return this._factoryMethod.invoke(null, new Object[]{key});
        }
    }

    static final class StringKD extends StdKeyDeserializer {
        private static final StringKD sObject = new StringKD(Object.class);
        private static final StringKD sString = new StringKD(String.class);

        private StringKD(Class<?> nominalType) {
            super(nominalType);
        }

        public static StringKD forType(Class<?> nominalType) {
            if (nominalType == String.class) {
                return sString;
            }
            if (nominalType == Object.class) {
                return sObject;
            }
            return new StringKD(nominalType);
        }

        public String _parse(String key, DeserializationContext ctxt) throws JsonMappingException {
            return key;
        }
    }

    static final class UuidKD extends StdKeyDeserializer {
        protected UuidKD() {
            super(UUID.class);
        }

        public UUID _parse(String key, DeserializationContext ctxt) throws IllegalArgumentException, JsonMappingException {
            return UUID.fromString(key);
        }
    }

    protected abstract Object _parse(String str, DeserializationContext deserializationContext) throws Exception;

    protected StdKeyDeserializer(Class<?> cls) {
        this._keyClass = cls;
    }

    public final Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (key == null) {
            return null;
        }
        try {
            Object result = _parse(key, ctxt);
            if (result != null) {
                return result;
            }
            throw ctxt.weirdKeyException(this._keyClass, key, "not a valid representation");
        } catch (Exception re) {
            throw ctxt.weirdKeyException(this._keyClass, key, "not a valid representation: " + re.getMessage());
        }
    }

    public Class<?> getKeyClass() {
        return this._keyClass;
    }

    protected int _parseInt(String key) throws IllegalArgumentException {
        return Integer.parseInt(key);
    }

    protected long _parseLong(String key) throws IllegalArgumentException {
        return Long.parseLong(key);
    }

    protected double _parseDouble(String key) throws IllegalArgumentException {
        return NumberInput.parseDouble(key);
    }
}
