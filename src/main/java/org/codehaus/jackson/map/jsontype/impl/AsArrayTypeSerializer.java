package org.codehaus.jackson.map.jsontype.impl;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;

public class AsArrayTypeSerializer extends TypeSerializerBase {
    public AsArrayTypeSerializer(TypeIdResolver idRes, BeanProperty property) {
        super(idRes, property);
    }

    public As getTypeInclusion() {
        return As.WRAPPER_ARRAY;
    }

    public void writeTypePrefixForObject(Object value, JsonGenerator jgen) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        jgen.writeString(this._idResolver.idFromValue(value));
        jgen.writeStartObject();
    }

    public void writeTypePrefixForObject(Object value, JsonGenerator jgen, Class<?> type) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        jgen.writeString(this._idResolver.idFromValueAndType(value, type));
        jgen.writeStartObject();
    }

    public void writeTypePrefixForArray(Object value, JsonGenerator jgen) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        jgen.writeString(this._idResolver.idFromValue(value));
        jgen.writeStartArray();
    }

    public void writeTypePrefixForArray(Object value, JsonGenerator jgen, Class<?> type) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        jgen.writeString(this._idResolver.idFromValueAndType(value, type));
        jgen.writeStartArray();
    }

    public void writeTypePrefixForScalar(Object value, JsonGenerator jgen) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        jgen.writeString(this._idResolver.idFromValue(value));
    }

    public void writeTypePrefixForScalar(Object value, JsonGenerator jgen, Class<?> type) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        jgen.writeString(this._idResolver.idFromValueAndType(value, type));
    }

    public void writeTypeSuffixForObject(Object value, JsonGenerator jgen) throws IOException, JsonProcessingException {
        jgen.writeEndObject();
        jgen.writeEndArray();
    }

    public void writeTypeSuffixForArray(Object value, JsonGenerator jgen) throws IOException, JsonProcessingException {
        jgen.writeEndArray();
        jgen.writeEndArray();
    }

    public void writeTypeSuffixForScalar(Object value, JsonGenerator jgen) throws IOException, JsonProcessingException {
        jgen.writeEndArray();
    }
}
