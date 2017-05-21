package org.codehaus.jackson.map;

import java.io.IOException;
import org.codehaus.jackson.JsonProcessingException;

public abstract class DeserializationProblemHandler {
    public boolean handleUnknownProperty(DeserializationContext ctxt, JsonDeserializer<?> jsonDeserializer, Object beanOrClass, String propertyName) throws IOException, JsonProcessingException {
        return false;
    }
}
