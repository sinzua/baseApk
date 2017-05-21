package org.codehaus.jackson;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.codehaus.jackson.JsonParser.NumberType;

public abstract class JsonNode implements Iterable<JsonNode> {
    protected static final List<JsonNode> NO_NODES = Collections.emptyList();
    protected static final List<String> NO_STRINGS = Collections.emptyList();

    public abstract String asText();

    public abstract JsonToken asToken();

    public abstract boolean equals(Object obj);

    public abstract JsonNode findParent(String str);

    public abstract List<JsonNode> findParents(String str, List<JsonNode> list);

    public abstract JsonNode findPath(String str);

    public abstract JsonNode findValue(String str);

    public abstract List<JsonNode> findValues(String str, List<JsonNode> list);

    public abstract List<String> findValuesAsText(String str, List<String> list);

    public abstract NumberType getNumberType();

    public abstract JsonNode path(int i);

    public abstract JsonNode path(String str);

    public abstract String toString();

    public abstract JsonParser traverse();

    protected JsonNode() {
    }

    public boolean isValueNode() {
        return false;
    }

    public boolean isContainerNode() {
        return false;
    }

    public boolean isMissingNode() {
        return false;
    }

    public boolean isArray() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isPojo() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isIntegralNumber() {
        return false;
    }

    public boolean isFloatingPointNumber() {
        return false;
    }

    public boolean isInt() {
        return false;
    }

    public boolean isLong() {
        return false;
    }

    public boolean isDouble() {
        return false;
    }

    public boolean isBigDecimal() {
        return false;
    }

    public boolean isBigInteger() {
        return false;
    }

    public boolean isTextual() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public boolean isBinary() {
        return false;
    }

    public String getTextValue() {
        return null;
    }

    public byte[] getBinaryValue() throws IOException {
        return null;
    }

    public boolean getBooleanValue() {
        return false;
    }

    public Number getNumberValue() {
        return null;
    }

    public int getIntValue() {
        return 0;
    }

    public long getLongValue() {
        return 0;
    }

    public double getDoubleValue() {
        return 0.0d;
    }

    public BigDecimal getDecimalValue() {
        return BigDecimal.ZERO;
    }

    public BigInteger getBigIntegerValue() {
        return BigInteger.ZERO;
    }

    public JsonNode get(int index) {
        return null;
    }

    public JsonNode get(String fieldName) {
        return null;
    }

    public int asInt() {
        return asInt(0);
    }

    public int asInt(int defaultValue) {
        return defaultValue;
    }

    public long asLong() {
        return asLong(0);
    }

    public long asLong(long defaultValue) {
        return defaultValue;
    }

    public double asDouble() {
        return asDouble(0.0d);
    }

    public double asDouble(double defaultValue) {
        return defaultValue;
    }

    public boolean asBoolean() {
        return asBoolean(false);
    }

    public boolean asBoolean(boolean defaultValue) {
        return defaultValue;
    }

    @Deprecated
    public String getValueAsText() {
        return asText();
    }

    @Deprecated
    public int getValueAsInt() {
        return asInt(0);
    }

    @Deprecated
    public int getValueAsInt(int defaultValue) {
        return asInt(defaultValue);
    }

    @Deprecated
    public long getValueAsLong() {
        return asLong(0);
    }

    @Deprecated
    public long getValueAsLong(long defaultValue) {
        return asLong(defaultValue);
    }

    @Deprecated
    public double getValueAsDouble() {
        return asDouble(0.0d);
    }

    @Deprecated
    public double getValueAsDouble(double defaultValue) {
        return asDouble(defaultValue);
    }

    @Deprecated
    public boolean getValueAsBoolean() {
        return asBoolean(false);
    }

    @Deprecated
    public boolean getValueAsBoolean(boolean defaultValue) {
        return asBoolean(defaultValue);
    }

    public boolean has(String fieldName) {
        return get(fieldName) != null;
    }

    public boolean has(int index) {
        return get(index) != null;
    }

    public final List<JsonNode> findValues(String fieldName) {
        List<JsonNode> result = findValues(fieldName, null);
        if (result == null) {
            return Collections.emptyList();
        }
        return result;
    }

    public final List<String> findValuesAsText(String fieldName) {
        List<String> result = findValuesAsText(fieldName, null);
        if (result == null) {
            return Collections.emptyList();
        }
        return result;
    }

    public final List<JsonNode> findParents(String fieldName) {
        List<JsonNode> result = findParents(fieldName, null);
        if (result == null) {
            return Collections.emptyList();
        }
        return result;
    }

    public int size() {
        return 0;
    }

    public final Iterator<JsonNode> iterator() {
        return getElements();
    }

    public Iterator<JsonNode> getElements() {
        return NO_NODES.iterator();
    }

    public Iterator<String> getFieldNames() {
        return NO_STRINGS.iterator();
    }

    public Iterator<Entry<String, JsonNode>> getFields() {
        return Collections.emptyList().iterator();
    }

    @Deprecated
    public final JsonNode getPath(String fieldName) {
        return path(fieldName);
    }

    @Deprecated
    public final JsonNode getPath(int index) {
        return path(index);
    }

    public JsonNode with(String propertyName) {
        throw new UnsupportedOperationException("JsonNode not of type ObjectNode (but " + getClass().getName() + "), can not call with() on it");
    }
}
