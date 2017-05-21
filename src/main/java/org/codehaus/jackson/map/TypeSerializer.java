package org.codehaus.jackson.map;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;

public abstract class TypeSerializer {
    public abstract String getPropertyName();

    public abstract TypeIdResolver getTypeIdResolver();

    public abstract As getTypeInclusion();

    public abstract void writeTypePrefixForArray(Object obj, JsonGenerator jsonGenerator) throws IOException, JsonProcessingException;

    public abstract void writeTypePrefixForObject(Object obj, JsonGenerator jsonGenerator) throws IOException, JsonProcessingException;

    public abstract void writeTypePrefixForScalar(Object obj, JsonGenerator jsonGenerator) throws IOException, JsonProcessingException;

    public abstract void writeTypeSuffixForArray(Object obj, JsonGenerator jsonGenerator) throws IOException, JsonProcessingException;

    public abstract void writeTypeSuffixForObject(Object obj, JsonGenerator jsonGenerator) throws IOException, JsonProcessingException;

    public abstract void writeTypeSuffixForScalar(Object obj, JsonGenerator jsonGenerator) throws IOException, JsonProcessingException;

    public void writeTypePrefixForScalar(Object value, JsonGenerator jgen, Class<?> cls) throws IOException, JsonProcessingException {
        writeTypePrefixForScalar(value, jgen);
    }

    public void writeTypePrefixForObject(Object value, JsonGenerator jgen, Class<?> cls) throws IOException, JsonProcessingException {
        writeTypePrefixForObject(value, jgen);
    }

    public void writeTypePrefixForArray(Object value, JsonGenerator jgen, Class<?> cls) throws IOException, JsonProcessingException {
        writeTypePrefixForArray(value, jgen);
    }
}
