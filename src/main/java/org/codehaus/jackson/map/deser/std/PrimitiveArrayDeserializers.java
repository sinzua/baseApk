package org.codehaus.jackson.map.deser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import org.codehaus.jackson.Base64Variants;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ArrayBuilders.BooleanBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.ByteBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.DoubleBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.FloatBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.IntBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.LongBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.ShortBuilder;
import org.codehaus.jackson.map.util.ObjectBuffer;
import org.codehaus.jackson.type.JavaType;

public class PrimitiveArrayDeserializers {
    static final PrimitiveArrayDeserializers instance = new PrimitiveArrayDeserializers();
    HashMap<JavaType, JsonDeserializer<Object>> _allDeserializers = new HashMap();

    static abstract class Base<T> extends StdDeserializer<T> {
        protected Base(Class<T> cls) {
            super((Class) cls);
        }

        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
            return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
        }
    }

    @JacksonStdImpl
    static final class BooleanDeser extends Base<boolean[]> {
        public BooleanDeser() {
            super(boolean[].class);
        }

        public boolean[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            BooleanBuilder builder = ctxt.getArrayBuilders().getBooleanBuilder();
            boolean[] chunk = (boolean[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                boolean value = _parseBooleanPrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = (boolean[]) builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                int ix2 = ix + 1;
                chunk[ix] = value;
                ix = ix2;
            }
            return (boolean[]) builder.completeAndClearBuffer(chunk, ix);
        }

        private final boolean[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING && ctxt.isEnabled(Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getText().length() == 0) {
                return null;
            }
            if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new boolean[]{_parseBooleanPrimitive(jp, ctxt)};
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class ByteDeser extends Base<byte[]> {
        public ByteDeser() {
            super(byte[].class);
        }

        public byte[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_STRING) {
                return jp.getBinaryValue(ctxt.getBase64Variant());
            }
            if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
                Object ob = jp.getEmbeddedObject();
                if (ob == null) {
                    return null;
                }
                if (ob instanceof byte[]) {
                    return (byte[]) ob;
                }
            }
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            ByteBuilder builder = ctxt.getArrayBuilders().getByteBuilder();
            byte[] chunk = (byte[]) builder.resetAndStart();
            int ix = 0;
            while (true) {
                t = jp.nextToken();
                if (t == JsonToken.END_ARRAY) {
                    return (byte[]) builder.completeAndClearBuffer(chunk, ix);
                }
                byte value;
                if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
                    value = jp.getByteValue();
                } else if (t != JsonToken.VALUE_NULL) {
                    throw ctxt.mappingException(this._valueClass.getComponentType());
                } else {
                    value = (byte) 0;
                }
                if (ix >= chunk.length) {
                    chunk = (byte[]) builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                int ix2 = ix + 1;
                chunk[ix] = value;
                ix = ix2;
            }
        }

        private final byte[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING && ctxt.isEnabled(Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getText().length() == 0) {
                return null;
            }
            if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                byte value;
                JsonToken t = jp.getCurrentToken();
                if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
                    value = jp.getByteValue();
                } else if (t != JsonToken.VALUE_NULL) {
                    throw ctxt.mappingException(this._valueClass.getComponentType());
                } else {
                    value = (byte) 0;
                }
                return new byte[]{value};
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class CharDeser extends Base<char[]> {
        public CharDeser() {
            super(char[].class);
        }

        public char[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_STRING) {
                char[] buffer = jp.getTextCharacters();
                int offset = jp.getTextOffset();
                int len = jp.getTextLength();
                char[] result = new char[len];
                System.arraycopy(buffer, offset, result, 0, len);
                return result;
            } else if (jp.isExpectedStartArrayToken()) {
                StringBuilder sb = new StringBuilder(64);
                while (true) {
                    t = jp.nextToken();
                    if (t == JsonToken.END_ARRAY) {
                        return sb.toString().toCharArray();
                    }
                    if (t != JsonToken.VALUE_STRING) {
                        throw ctxt.mappingException(Character.TYPE);
                    }
                    String str = jp.getText();
                    if (str.length() != 1) {
                        throw JsonMappingException.from(jp, "Can not convert a JSON String of length " + str.length() + " into a char element of char array");
                    }
                    sb.append(str.charAt(0));
                }
            } else {
                if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
                    Object ob = jp.getEmbeddedObject();
                    if (ob == null) {
                        return null;
                    }
                    if (ob instanceof char[]) {
                        return (char[]) ob;
                    }
                    if (ob instanceof String) {
                        return ((String) ob).toCharArray();
                    }
                    if (ob instanceof byte[]) {
                        return Base64Variants.getDefaultVariant().encode((byte[]) ob, false).toCharArray();
                    }
                }
                throw ctxt.mappingException(this._valueClass);
            }
        }
    }

    @JacksonStdImpl
    static final class DoubleDeser extends Base<double[]> {
        public DoubleDeser() {
            super(double[].class);
        }

        public double[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            DoubleBuilder builder = ctxt.getArrayBuilders().getDoubleBuilder();
            double[] chunk = (double[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                double value = _parseDoublePrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = (double[]) builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                int ix2 = ix + 1;
                chunk[ix] = value;
                ix = ix2;
            }
            return (double[]) builder.completeAndClearBuffer(chunk, ix);
        }

        private final double[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING && ctxt.isEnabled(Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getText().length() == 0) {
                return null;
            }
            if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new double[]{_parseDoublePrimitive(jp, ctxt)};
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class FloatDeser extends Base<float[]> {
        public FloatDeser() {
            super(float[].class);
        }

        public float[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            FloatBuilder builder = ctxt.getArrayBuilders().getFloatBuilder();
            float[] chunk = (float[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                float value = _parseFloatPrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = (float[]) builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                int ix2 = ix + 1;
                chunk[ix] = value;
                ix = ix2;
            }
            return (float[]) builder.completeAndClearBuffer(chunk, ix);
        }

        private final float[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING && ctxt.isEnabled(Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getText().length() == 0) {
                return null;
            }
            if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new float[]{_parseFloatPrimitive(jp, ctxt)};
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class IntDeser extends Base<int[]> {
        public IntDeser() {
            super(int[].class);
        }

        public int[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            IntBuilder builder = ctxt.getArrayBuilders().getIntBuilder();
            int[] chunk = (int[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                int value = _parseIntPrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = (int[]) builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                int ix2 = ix + 1;
                chunk[ix] = value;
                ix = ix2;
            }
            return (int[]) builder.completeAndClearBuffer(chunk, ix);
        }

        private final int[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING && ctxt.isEnabled(Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getText().length() == 0) {
                return null;
            }
            if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new int[]{_parseIntPrimitive(jp, ctxt)};
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class LongDeser extends Base<long[]> {
        public LongDeser() {
            super(long[].class);
        }

        public long[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            LongBuilder builder = ctxt.getArrayBuilders().getLongBuilder();
            long[] chunk = (long[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                long value = _parseLongPrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = (long[]) builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                int ix2 = ix + 1;
                chunk[ix] = value;
                ix = ix2;
            }
            return (long[]) builder.completeAndClearBuffer(chunk, ix);
        }

        private final long[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING && ctxt.isEnabled(Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getText().length() == 0) {
                return null;
            }
            if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new long[]{_parseLongPrimitive(jp, ctxt)};
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class ShortDeser extends Base<short[]> {
        public ShortDeser() {
            super(short[].class);
        }

        public short[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            ShortBuilder builder = ctxt.getArrayBuilders().getShortBuilder();
            short[] chunk = (short[]) builder.resetAndStart();
            int ix = 0;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                short value = _parseShortPrimitive(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = (short[]) builder.appendCompletedChunk(chunk, ix);
                    ix = 0;
                }
                int ix2 = ix + 1;
                chunk[ix] = value;
                ix = ix2;
            }
            return (short[]) builder.completeAndClearBuffer(chunk, ix);
        }

        private final short[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING && ctxt.isEnabled(Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getText().length() == 0) {
                return null;
            }
            if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                return new short[]{_parseShortPrimitive(jp, ctxt)};
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class StringDeser extends Base<String[]> {
        public StringDeser() {
            super(String[].class);
        }

        public String[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!jp.isExpectedStartArrayToken()) {
                return handleNonArray(jp, ctxt);
            }
            ObjectBuffer buffer = ctxt.leaseObjectBuffer();
            Object[] chunk = buffer.resetAndStart();
            int ix = 0;
            while (true) {
                JsonToken t = jp.nextToken();
                if (t != JsonToken.END_ARRAY) {
                    String value = t == JsonToken.VALUE_NULL ? null : jp.getText();
                    if (ix >= chunk.length) {
                        chunk = buffer.appendCompletedChunk(chunk);
                        ix = 0;
                    }
                    int ix2 = ix + 1;
                    chunk[ix] = value;
                    ix = ix2;
                } else {
                    String[] result = (String[]) buffer.completeAndClearBuffer(chunk, ix, String.class);
                    ctxt.returnObjectBuffer(buffer);
                    return result;
                }
            }
        }

        private final String[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String[] strArr = null;
            if (ctxt.isEnabled(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
                String[] strArr2 = new String[1];
                if (jp.getCurrentToken() != JsonToken.VALUE_NULL) {
                    strArr = jp.getText();
                }
                strArr2[0] = strArr;
                return strArr2;
            } else if (jp.getCurrentToken() == JsonToken.VALUE_STRING && ctxt.isEnabled(Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jp.getText().length() == 0) {
                return null;
            } else {
                throw ctxt.mappingException(this._valueClass);
            }
        }
    }

    protected PrimitiveArrayDeserializers() {
        add(Boolean.TYPE, new BooleanDeser());
        add(Byte.TYPE, new ByteDeser());
        add(Short.TYPE, new ShortDeser());
        add(Integer.TYPE, new IntDeser());
        add(Long.TYPE, new LongDeser());
        add(Float.TYPE, new FloatDeser());
        add(Double.TYPE, new DoubleDeser());
        add(String.class, new StringDeser());
        add(Character.TYPE, new CharDeser());
    }

    public static HashMap<JavaType, JsonDeserializer<Object>> getAll() {
        return instance._allDeserializers;
    }

    private void add(Class<?> cls, JsonDeserializer<?> deser) {
        this._allDeserializers.put(TypeFactory.defaultInstance().constructType((Type) cls), deser);
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }
}
