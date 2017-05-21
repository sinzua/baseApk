package org.codehaus.jackson.node;

import java.io.IOException;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.Base64Variants;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.NumberInput;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.util.CharTypes;

public final class TextNode extends ValueNode {
    static final TextNode EMPTY_STRING_NODE = new TextNode("");
    static final int INT_SPACE = 32;
    final String _value;

    public TextNode(String v) {
        this._value = v;
    }

    public static TextNode valueOf(String v) {
        if (v == null) {
            return null;
        }
        if (v.length() == 0) {
            return EMPTY_STRING_NODE;
        }
        return new TextNode(v);
    }

    public JsonToken asToken() {
        return JsonToken.VALUE_STRING;
    }

    public boolean isTextual() {
        return true;
    }

    public String getTextValue() {
        return this._value;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] getBinaryValue(org.codehaus.jackson.Base64Variant r13) throws java.io.IOException {
        /*
        r12 = this;
        r11 = 3;
        r10 = -2;
        r1 = new org.codehaus.jackson.util.ByteArrayBuilder;
        r8 = 100;
        r1.<init>(r8);
        r7 = r12._value;
        r5 = 0;
        r4 = r7.length();
    L_0x0010:
        if (r5 >= r4) goto L_0x001b;
    L_0x0012:
        r6 = r5 + 1;
        r2 = r7.charAt(r5);
        if (r6 < r4) goto L_0x0020;
    L_0x001a:
        r5 = r6;
    L_0x001b:
        r8 = r1.toByteArray();
        return r8;
    L_0x0020:
        r8 = 32;
        if (r2 <= r8) goto L_0x00db;
    L_0x0024:
        r0 = r13.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x002e;
    L_0x002a:
        r8 = 0;
        r12._reportInvalidBase64(r13, r2, r8);
    L_0x002e:
        r3 = r0;
        if (r6 < r4) goto L_0x0034;
    L_0x0031:
        r12._reportBase64EOF();
    L_0x0034:
        r5 = r6 + 1;
        r2 = r7.charAt(r6);
        r0 = r13.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x0044;
    L_0x0040:
        r8 = 1;
        r12._reportInvalidBase64(r13, r2, r8);
    L_0x0044:
        r8 = r3 << 6;
        r3 = r8 | r0;
        if (r5 < r4) goto L_0x0059;
    L_0x004a:
        r8 = r13.usesPadding();
        if (r8 != 0) goto L_0x0056;
    L_0x0050:
        r3 = r3 >> 4;
        r1.append(r3);
        goto L_0x001b;
    L_0x0056:
        r12._reportBase64EOF();
    L_0x0059:
        r6 = r5 + 1;
        r2 = r7.charAt(r5);
        r0 = r13.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x00a3;
    L_0x0065:
        if (r0 == r10) goto L_0x006b;
    L_0x0067:
        r8 = 2;
        r12._reportInvalidBase64(r13, r2, r8);
    L_0x006b:
        if (r6 < r4) goto L_0x0070;
    L_0x006d:
        r12._reportBase64EOF();
    L_0x0070:
        r5 = r6 + 1;
        r2 = r7.charAt(r6);
        r8 = r13.usesPaddingChar(r2);
        if (r8 != 0) goto L_0x009c;
    L_0x007c:
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "expected padding character '";
        r8 = r8.append(r9);
        r9 = r13.getPaddingChar();
        r8 = r8.append(r9);
        r9 = "'";
        r8 = r8.append(r9);
        r8 = r8.toString();
        r12._reportInvalidBase64(r13, r2, r11, r8);
    L_0x009c:
        r3 = r3 >> 4;
        r1.append(r3);
        goto L_0x0010;
    L_0x00a3:
        r8 = r3 << 6;
        r3 = r8 | r0;
        if (r6 < r4) goto L_0x00ba;
    L_0x00a9:
        r8 = r13.usesPadding();
        if (r8 != 0) goto L_0x00b7;
    L_0x00af:
        r3 = r3 >> 2;
        r1.appendTwoBytes(r3);
        r5 = r6;
        goto L_0x001b;
    L_0x00b7:
        r12._reportBase64EOF();
    L_0x00ba:
        r5 = r6 + 1;
        r2 = r7.charAt(r6);
        r0 = r13.decodeBase64Char(r2);
        if (r0 >= 0) goto L_0x00d2;
    L_0x00c6:
        if (r0 == r10) goto L_0x00cb;
    L_0x00c8:
        r12._reportInvalidBase64(r13, r2, r11);
    L_0x00cb:
        r3 = r3 >> 2;
        r1.appendTwoBytes(r3);
        goto L_0x0010;
    L_0x00d2:
        r8 = r3 << 6;
        r3 = r8 | r0;
        r1.appendThreeBytes(r3);
        goto L_0x0010;
    L_0x00db:
        r5 = r6;
        goto L_0x0012;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.node.TextNode.getBinaryValue(org.codehaus.jackson.Base64Variant):byte[]");
    }

    public byte[] getBinaryValue() throws IOException {
        return getBinaryValue(Base64Variants.getDefaultVariant());
    }

    public String asText() {
        return this._value;
    }

    public boolean asBoolean(boolean defaultValue) {
        if (this._value == null || !"true".equals(this._value.trim())) {
            return defaultValue;
        }
        return true;
    }

    public int asInt(int defaultValue) {
        return NumberInput.parseAsInt(this._value, defaultValue);
    }

    public long asLong(long defaultValue) {
        return NumberInput.parseAsLong(this._value, defaultValue);
    }

    public double asDouble(double defaultValue) {
        return NumberInput.parseAsDouble(this._value, defaultValue);
    }

    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
        if (this._value == null) {
            jg.writeNull();
        } else {
            jg.writeString(this._value);
        }
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        return ((TextNode) o)._value.equals(this._value);
    }

    public int hashCode() {
        return this._value.hashCode();
    }

    public String toString() {
        int len = this._value.length();
        StringBuilder sb = new StringBuilder((len + 2) + (len >> 4));
        appendQuoted(sb, this._value);
        return sb.toString();
    }

    protected static void appendQuoted(StringBuilder sb, String content) {
        sb.append('\"');
        CharTypes.appendQuoted(sb, content);
        sb.append('\"');
    }

    protected void _reportInvalidBase64(Base64Variant b64variant, char ch, int bindex) throws JsonParseException {
        _reportInvalidBase64(b64variant, ch, bindex, null);
    }

    protected void _reportInvalidBase64(Base64Variant b64variant, char ch, int bindex, String msg) throws JsonParseException {
        String base;
        if (ch <= ' ') {
            base = "Illegal white space character (code 0x" + Integer.toHexString(ch) + ") as character #" + (bindex + 1) + " of 4-char base64 unit: can only used between units";
        } else if (b64variant.usesPaddingChar(ch)) {
            base = "Unexpected padding character ('" + b64variant.getPaddingChar() + "') as character #" + (bindex + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        } else if (!Character.isDefined(ch) || Character.isISOControl(ch)) {
            base = "Illegal character (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        } else {
            base = "Illegal character '" + ch + "' (code 0x" + Integer.toHexString(ch) + ") in base64 content";
        }
        if (msg != null) {
            base = base + ": " + msg;
        }
        throw new JsonParseException(base, JsonLocation.NA);
    }

    protected void _reportBase64EOF() throws JsonParseException {
        throw new JsonParseException("Unexpected end-of-String when base64 content", JsonLocation.NA);
    }
}
