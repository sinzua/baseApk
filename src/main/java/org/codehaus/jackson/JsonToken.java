package org.codehaus.jackson;

import com.supersonicads.sdk.utils.Constants.RequestParameters;

public enum JsonToken {
    NOT_AVAILABLE(null),
    START_OBJECT("{"),
    END_OBJECT("}"),
    START_ARRAY(RequestParameters.LEFT_BRACKETS),
    END_ARRAY(RequestParameters.RIGHT_BRACKETS),
    FIELD_NAME(null),
    VALUE_EMBEDDED_OBJECT(null),
    VALUE_STRING(null),
    VALUE_NUMBER_INT(null),
    VALUE_NUMBER_FLOAT(null),
    VALUE_TRUE("true"),
    VALUE_FALSE("false"),
    VALUE_NULL("null");
    
    final String _serialized;
    final byte[] _serializedBytes;
    final char[] _serializedChars;

    private JsonToken(String token) {
        if (token == null) {
            this._serialized = null;
            this._serializedChars = null;
            this._serializedBytes = null;
            return;
        }
        this._serialized = token;
        this._serializedChars = token.toCharArray();
        int len = this._serializedChars.length;
        this._serializedBytes = new byte[len];
        for (int i = 0; i < len; i++) {
            this._serializedBytes[i] = (byte) this._serializedChars[i];
        }
    }

    public String asString() {
        return this._serialized;
    }

    public char[] asCharArray() {
        return this._serializedChars;
    }

    public byte[] asByteArray() {
        return this._serializedBytes;
    }

    public boolean isNumeric() {
        return this == VALUE_NUMBER_INT || this == VALUE_NUMBER_FLOAT;
    }

    public boolean isScalarValue() {
        return ordinal() >= VALUE_EMBEDDED_OBJECT.ordinal();
    }
}
