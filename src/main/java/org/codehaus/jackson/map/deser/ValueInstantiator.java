package org.codehaus.jackson.map.deser;

import java.io.IOException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.introspect.AnnotatedWithParams;
import org.codehaus.jackson.type.JavaType;

public abstract class ValueInstantiator {
    public abstract String getValueTypeDesc();

    public boolean canInstantiate() {
        return canCreateUsingDefault() || canCreateUsingDelegate() || canCreateFromObjectWith() || canCreateFromString() || canCreateFromInt() || canCreateFromLong() || canCreateFromDouble() || canCreateFromBoolean();
    }

    public boolean canCreateFromString() {
        return false;
    }

    public boolean canCreateFromInt() {
        return false;
    }

    public boolean canCreateFromLong() {
        return false;
    }

    public boolean canCreateFromDouble() {
        return false;
    }

    public boolean canCreateFromBoolean() {
        return false;
    }

    public boolean canCreateUsingDefault() {
        return getDefaultCreator() != null;
    }

    public boolean canCreateUsingDelegate() {
        return getDelegateType() != null;
    }

    public boolean canCreateFromObjectWith() {
        return false;
    }

    public SettableBeanProperty[] getFromObjectArguments() {
        return null;
    }

    public JavaType getDelegateType() {
        return null;
    }

    public Object createUsingDefault() throws IOException, JsonProcessingException {
        throw new JsonMappingException("Can not instantiate value of type " + getValueTypeDesc() + "; no default creator found");
    }

    public Object createFromObjectWith(Object[] args) throws IOException, JsonProcessingException {
        throw new JsonMappingException("Can not instantiate value of type " + getValueTypeDesc() + " with arguments");
    }

    public Object createUsingDelegate(Object delegate) throws IOException, JsonProcessingException {
        throw new JsonMappingException("Can not instantiate value of type " + getValueTypeDesc() + " using delegate");
    }

    public Object createFromString(String value) throws IOException, JsonProcessingException {
        throw new JsonMappingException("Can not instantiate value of type " + getValueTypeDesc() + " from JSON String");
    }

    public Object createFromInt(int value) throws IOException, JsonProcessingException {
        throw new JsonMappingException("Can not instantiate value of type " + getValueTypeDesc() + " from JSON int number");
    }

    public Object createFromLong(long value) throws IOException, JsonProcessingException {
        throw new JsonMappingException("Can not instantiate value of type " + getValueTypeDesc() + " from JSON long number");
    }

    public Object createFromDouble(double value) throws IOException, JsonProcessingException {
        throw new JsonMappingException("Can not instantiate value of type " + getValueTypeDesc() + " from JSON floating-point number");
    }

    public Object createFromBoolean(boolean value) throws IOException, JsonProcessingException {
        throw new JsonMappingException("Can not instantiate value of type " + getValueTypeDesc() + " from JSON boolean value");
    }

    public AnnotatedWithParams getDefaultCreator() {
        return null;
    }

    public AnnotatedWithParams getDelegateCreator() {
        return null;
    }

    public AnnotatedWithParams getWithArgsCreator() {
        return null;
    }
}
