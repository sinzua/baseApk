package org.codehaus.jackson.map;

import org.codehaus.jackson.type.JavaType;

public interface KeyDeserializers {
    KeyDeserializer findKeyDeserializer(JavaType javaType, DeserializationConfig deserializationConfig, BeanDescription beanDescription, BeanProperty beanProperty) throws JsonMappingException;
}
