package org.codehaus.jackson.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.FormatSchema;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.io.SerializedString;

public class JsonGeneratorDelegate extends JsonGenerator {
    protected JsonGenerator delegate;

    public JsonGeneratorDelegate(JsonGenerator d) {
        this.delegate = d;
    }

    public void close() throws IOException {
        this.delegate.close();
    }

    public void copyCurrentEvent(JsonParser jp) throws IOException, JsonProcessingException {
        this.delegate.copyCurrentEvent(jp);
    }

    public void copyCurrentStructure(JsonParser jp) throws IOException, JsonProcessingException {
        this.delegate.copyCurrentStructure(jp);
    }

    public JsonGenerator disable(Feature f) {
        return this.delegate.disable(f);
    }

    public JsonGenerator enable(Feature f) {
        return this.delegate.enable(f);
    }

    public void flush() throws IOException {
        this.delegate.flush();
    }

    public ObjectCodec getCodec() {
        return this.delegate.getCodec();
    }

    public JsonStreamContext getOutputContext() {
        return this.delegate.getOutputContext();
    }

    public void setSchema(FormatSchema schema) {
        this.delegate.setSchema(schema);
    }

    public boolean canUseSchema(FormatSchema schema) {
        return this.delegate.canUseSchema(schema);
    }

    public Version version() {
        return this.delegate.version();
    }

    public Object getOutputTarget() {
        return this.delegate.getOutputTarget();
    }

    public boolean isClosed() {
        return this.delegate.isClosed();
    }

    public boolean isEnabled(Feature f) {
        return this.delegate.isEnabled(f);
    }

    public JsonGenerator setCodec(ObjectCodec oc) {
        this.delegate.setCodec(oc);
        return this;
    }

    public JsonGenerator useDefaultPrettyPrinter() {
        this.delegate.useDefaultPrettyPrinter();
        return this;
    }

    public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException, JsonGenerationException {
        this.delegate.writeBinary(b64variant, data, offset, len);
    }

    public void writeBoolean(boolean state) throws IOException, JsonGenerationException {
        this.delegate.writeBoolean(state);
    }

    public void writeEndArray() throws IOException, JsonGenerationException {
        this.delegate.writeEndArray();
    }

    public void writeEndObject() throws IOException, JsonGenerationException {
        this.delegate.writeEndObject();
    }

    public void writeFieldName(String name) throws IOException, JsonGenerationException {
        this.delegate.writeFieldName(name);
    }

    public void writeFieldName(SerializedString name) throws IOException, JsonGenerationException {
        this.delegate.writeFieldName(name);
    }

    public void writeFieldName(SerializableString name) throws IOException, JsonGenerationException {
        this.delegate.writeFieldName(name);
    }

    public void writeNull() throws IOException, JsonGenerationException {
        this.delegate.writeNull();
    }

    public void writeNumber(int v) throws IOException, JsonGenerationException {
        this.delegate.writeNumber(v);
    }

    public void writeNumber(long v) throws IOException, JsonGenerationException {
        this.delegate.writeNumber(v);
    }

    public void writeNumber(BigInteger v) throws IOException, JsonGenerationException {
        this.delegate.writeNumber(v);
    }

    public void writeNumber(double v) throws IOException, JsonGenerationException {
        this.delegate.writeNumber(v);
    }

    public void writeNumber(float v) throws IOException, JsonGenerationException {
        this.delegate.writeNumber(v);
    }

    public void writeNumber(BigDecimal v) throws IOException, JsonGenerationException {
        this.delegate.writeNumber(v);
    }

    public void writeNumber(String encodedValue) throws IOException, JsonGenerationException, UnsupportedOperationException {
        this.delegate.writeNumber(encodedValue);
    }

    public void writeObject(Object pojo) throws IOException, JsonProcessingException {
        this.delegate.writeObject(pojo);
    }

    public void writeRaw(String text) throws IOException, JsonGenerationException {
        this.delegate.writeRaw(text);
    }

    public void writeRaw(String text, int offset, int len) throws IOException, JsonGenerationException {
        this.delegate.writeRaw(text, offset, len);
    }

    public void writeRaw(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        this.delegate.writeRaw(text, offset, len);
    }

    public void writeRaw(char c) throws IOException, JsonGenerationException {
        this.delegate.writeRaw(c);
    }

    public void writeRawValue(String text) throws IOException, JsonGenerationException {
        this.delegate.writeRawValue(text);
    }

    public void writeRawValue(String text, int offset, int len) throws IOException, JsonGenerationException {
        this.delegate.writeRawValue(text, offset, len);
    }

    public void writeRawValue(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        this.delegate.writeRawValue(text, offset, len);
    }

    public void writeStartArray() throws IOException, JsonGenerationException {
        this.delegate.writeStartArray();
    }

    public void writeStartObject() throws IOException, JsonGenerationException {
        this.delegate.writeStartObject();
    }

    public void writeString(String text) throws IOException, JsonGenerationException {
        this.delegate.writeString(text);
    }

    public void writeString(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        this.delegate.writeString(text, offset, len);
    }

    public void writeString(SerializableString text) throws IOException, JsonGenerationException {
        this.delegate.writeString(text);
    }

    public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException, JsonGenerationException {
        this.delegate.writeRawUTF8String(text, offset, length);
    }

    public void writeUTF8String(byte[] text, int offset, int length) throws IOException, JsonGenerationException {
        this.delegate.writeUTF8String(text, offset, length);
    }

    public void writeTree(JsonNode rootNode) throws IOException, JsonProcessingException {
        this.delegate.writeTree(rootNode);
    }
}
