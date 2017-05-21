package org.codehaus.jackson.node;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;

public final class BooleanNode extends ValueNode {
    public static final BooleanNode FALSE = new BooleanNode();
    public static final BooleanNode TRUE = new BooleanNode();

    private BooleanNode() {
    }

    public static BooleanNode getTrue() {
        return TRUE;
    }

    public static BooleanNode getFalse() {
        return FALSE;
    }

    public static BooleanNode valueOf(boolean b) {
        return b ? TRUE : FALSE;
    }

    public JsonToken asToken() {
        return this == TRUE ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE;
    }

    public boolean isBoolean() {
        return true;
    }

    public boolean getBooleanValue() {
        return this == TRUE;
    }

    public String asText() {
        return this == TRUE ? "true" : "false";
    }

    public boolean asBoolean() {
        return this == TRUE;
    }

    public boolean asBoolean(boolean defaultValue) {
        return this == TRUE;
    }

    public int asInt(int defaultValue) {
        return this == TRUE ? 1 : 0;
    }

    public long asLong(long defaultValue) {
        return this == TRUE ? 1 : 0;
    }

    public double asDouble(double defaultValue) {
        return this == TRUE ? 1.0d : 0.0d;
    }

    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
        jg.writeBoolean(this == TRUE);
    }

    public boolean equals(Object o) {
        return o == this;
    }
}
