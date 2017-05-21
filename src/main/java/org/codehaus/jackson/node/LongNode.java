package org.codehaus.jackson.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.NumberOutput;
import org.codehaus.jackson.map.SerializerProvider;

public final class LongNode extends NumericNode {
    final long _value;

    public LongNode(long v) {
        this._value = v;
    }

    public static LongNode valueOf(long l) {
        return new LongNode(l);
    }

    public JsonToken asToken() {
        return JsonToken.VALUE_NUMBER_INT;
    }

    public NumberType getNumberType() {
        return NumberType.LONG;
    }

    public boolean isIntegralNumber() {
        return true;
    }

    public boolean isLong() {
        return true;
    }

    public Number getNumberValue() {
        return Long.valueOf(this._value);
    }

    public int getIntValue() {
        return (int) this._value;
    }

    public long getLongValue() {
        return this._value;
    }

    public double getDoubleValue() {
        return (double) this._value;
    }

    public BigDecimal getDecimalValue() {
        return BigDecimal.valueOf(this._value);
    }

    public BigInteger getBigIntegerValue() {
        return BigInteger.valueOf(this._value);
    }

    public String asText() {
        return NumberOutput.toString(this._value);
    }

    public boolean asBoolean(boolean defaultValue) {
        return this._value != 0;
    }

    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
        jg.writeNumber(this._value);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        if (((LongNode) o)._value != this._value) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ((int) this._value) ^ ((int) (this._value >> 32));
    }
}
