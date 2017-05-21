package org.codehaus.jackson.node;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.JsonParser.NumberType;

public abstract class NumericNode extends ValueNode {
    public abstract String asText();

    public abstract BigInteger getBigIntegerValue();

    public abstract BigDecimal getDecimalValue();

    public abstract double getDoubleValue();

    public abstract int getIntValue();

    public abstract long getLongValue();

    public abstract NumberType getNumberType();

    public abstract Number getNumberValue();

    protected NumericNode() {
    }

    public final boolean isNumber() {
        return true;
    }

    public int asInt() {
        return getIntValue();
    }

    public int asInt(int defaultValue) {
        return getIntValue();
    }

    public long asLong() {
        return getLongValue();
    }

    public long asLong(long defaultValue) {
        return getLongValue();
    }

    public double asDouble() {
        return getDoubleValue();
    }

    public double asDouble(double defaultValue) {
        return getDoubleValue();
    }
}
