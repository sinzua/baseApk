package org.codehaus.jackson.map.deser;

import java.util.HashMap;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializerFactory;
import org.codehaus.jackson.map.DeserializerFactory.Config;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.type.JavaType;

@Deprecated
public class CustomDeserializerFactory extends BeanDeserializerFactory {
    protected HashMap<ClassKey, JsonDeserializer<Object>> _directClassMappings;
    protected HashMap<ClassKey, Class<?>> _mixInAnnotations;

    public CustomDeserializerFactory() {
        this(null);
    }

    protected CustomDeserializerFactory(Config config) {
        super(config);
        this._directClassMappings = null;
    }

    public DeserializerFactory withConfig(Config config) {
        if (getClass() == CustomDeserializerFactory.class) {
            return new CustomDeserializerFactory(config);
        }
        throw new IllegalStateException("Subtype of CustomDeserializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalDeserializers': can not instantiate subtype with " + "additional deserializer definitions");
    }

    public <T> void addSpecificMapping(Class<T> forClass, JsonDeserializer<? extends T> deser) {
        ClassKey key = new ClassKey(forClass);
        if (this._directClassMappings == null) {
            this._directClassMappings = new HashMap();
        }
        this._directClassMappings.put(key, deser);
    }

    public void addMixInAnnotationMapping(Class<?> destinationClass, Class<?> classWithMixIns) {
        if (this._mixInAnnotations == null) {
            this._mixInAnnotations = new HashMap();
        }
        this._mixInAnnotations.put(new ClassKey(destinationClass), classWithMixIns);
    }

    public JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType type, BeanProperty property) throws JsonMappingException {
        ClassKey key = new ClassKey(type.getRawClass());
        if (this._directClassMappings != null) {
            JsonDeserializer<Object> deser = (JsonDeserializer) this._directClassMappings.get(key);
            if (deser != null) {
                return deser;
            }
        }
        return super.createBeanDeserializer(config, p, type, property);
    }

    public JsonDeserializer<?> createArrayDeserializer(DeserializationConfig config, DeserializerProvider p, ArrayType type, BeanProperty property) throws JsonMappingException {
        ClassKey key = new ClassKey(type.getRawClass());
        if (this._directClassMappings != null) {
            JsonDeserializer<Object> deser = (JsonDeserializer) this._directClassMappings.get(key);
            if (deser != null) {
                return deser;
            }
        }
        return super.createArrayDeserializer(config, p, type, property);
    }

    public JsonDeserializer<?> createEnumDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType enumType, BeanProperty property) throws JsonMappingException {
        if (this._directClassMappings != null) {
            JsonDeserializer<?> deser = (JsonDeserializer) this._directClassMappings.get(new ClassKey(enumType.getRawClass()));
            if (deser != null) {
                return deser;
            }
        }
        return super.createEnumDeserializer(config, p, enumType, property);
    }
}
