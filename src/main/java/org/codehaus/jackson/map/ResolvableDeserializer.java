package org.codehaus.jackson.map;

public interface ResolvableDeserializer {
    void resolve(DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider) throws JsonMappingException;
}
