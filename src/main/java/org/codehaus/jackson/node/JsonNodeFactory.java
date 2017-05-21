package org.codehaus.jackson.node;

import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonNodeFactory {
    public static final JsonNodeFactory instance = new JsonNodeFactory();

    protected JsonNodeFactory() {
    }

    public BooleanNode booleanNode(boolean v) {
        return v ? BooleanNode.getTrue() : BooleanNode.getFalse();
    }

    public NullNode nullNode() {
        return NullNode.getInstance();
    }

    public NumericNode numberNode(byte v) {
        return IntNode.valueOf(v);
    }

    public ValueNode numberNode(Byte value) {
        return value == null ? nullNode() : IntNode.valueOf(value.intValue());
    }

    public NumericNode numberNode(short v) {
        return IntNode.valueOf(v);
    }

    public ValueNode numberNode(Short value) {
        return value == null ? nullNode() : IntNode.valueOf(value.shortValue());
    }

    public NumericNode numberNode(int v) {
        return IntNode.valueOf(v);
    }

    public ValueNode numberNode(Integer value) {
        return value == null ? nullNode() : IntNode.valueOf(value.intValue());
    }

    public NumericNode numberNode(long v) {
        return LongNode.valueOf(v);
    }

    public ValueNode numberNode(Long value) {
        return value == null ? nullNode() : LongNode.valueOf(value.longValue());
    }

    public NumericNode numberNode(BigInteger v) {
        return BigIntegerNode.valueOf(v);
    }

    public NumericNode numberNode(float v) {
        return DoubleNode.valueOf((double) v);
    }

    public ValueNode numberNode(Float value) {
        return value == null ? nullNode() : DoubleNode.valueOf(value.doubleValue());
    }

    public NumericNode numberNode(double v) {
        return DoubleNode.valueOf(v);
    }

    public ValueNode numberNode(Double value) {
        return value == null ? nullNode() : DoubleNode.valueOf(value.doubleValue());
    }

    public NumericNode numberNode(BigDecimal v) {
        return DecimalNode.valueOf(v);
    }

    public TextNode textNode(String text) {
        return TextNode.valueOf(text);
    }

    public BinaryNode binaryNode(byte[] data) {
        return BinaryNode.valueOf(data);
    }

    public BinaryNode binaryNode(byte[] data, int offset, int length) {
        return BinaryNode.valueOf(data, offset, length);
    }

    public ArrayNode arrayNode() {
        return new ArrayNode(this);
    }

    public ObjectNode objectNode() {
        return new ObjectNode(this);
    }

    public POJONode POJONode(Object pojo) {
        return new POJONode(pojo);
    }
}
