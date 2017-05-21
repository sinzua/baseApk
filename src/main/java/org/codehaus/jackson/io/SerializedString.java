package org.codehaus.jackson.io;

import org.codehaus.jackson.SerializableString;

public class SerializedString implements SerializableString {
    protected char[] _quotedChars;
    protected byte[] _quotedUTF8Ref;
    protected byte[] _unquotedUTF8Ref;
    protected final String _value;

    public SerializedString(String v) {
        this._value = v;
    }

    public final String getValue() {
        return this._value;
    }

    public final int charLength() {
        return this._value.length();
    }

    public final char[] asQuotedChars() {
        char[] result = this._quotedChars;
        if (result != null) {
            return result;
        }
        result = JsonStringEncoder.getInstance().quoteAsString(this._value);
        this._quotedChars = result;
        return result;
    }

    public final byte[] asUnquotedUTF8() {
        byte[] result = this._unquotedUTF8Ref;
        if (result != null) {
            return result;
        }
        result = JsonStringEncoder.getInstance().encodeAsUTF8(this._value);
        this._unquotedUTF8Ref = result;
        return result;
    }

    public final byte[] asQuotedUTF8() {
        byte[] result = this._quotedUTF8Ref;
        if (result != null) {
            return result;
        }
        result = JsonStringEncoder.getInstance().quoteAsUTF8(this._value);
        this._quotedUTF8Ref = result;
        return result;
    }

    public final String toString() {
        return this._value;
    }

    public final int hashCode() {
        return this._value.hashCode();
    }

    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        return this._value.equals(((SerializedString) o)._value);
    }
}
