package org.codehaus.jackson.map.ser;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerFactory;
import org.codehaus.jackson.map.SerializerFactory.Config;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.type.JavaType;

public class CustomSerializerFactory extends BeanSerializerFactory {
    protected HashMap<ClassKey, JsonSerializer<?>> _directClassMappings;
    protected JsonSerializer<?> _enumSerializerOverride;
    protected HashMap<ClassKey, JsonSerializer<?>> _interfaceMappings;
    protected HashMap<ClassKey, JsonSerializer<?>> _transitiveClassMappings;

    public CustomSerializerFactory() {
        this(null);
    }

    public CustomSerializerFactory(Config config) {
        super(config);
        this._directClassMappings = null;
        this._transitiveClassMappings = null;
        this._interfaceMappings = null;
    }

    public SerializerFactory withConfig(Config config) {
        if (getClass() == CustomSerializerFactory.class) {
            return new CustomSerializerFactory(config);
        }
        throw new IllegalStateException("Subtype of CustomSerializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalSerializers': can not instantiate subtype with " + "additional serializer definitions");
    }

    public <T> void addGenericMapping(Class<? extends T> type, JsonSerializer<T> ser) {
        ClassKey key = new ClassKey(type);
        if (type.isInterface()) {
            if (this._interfaceMappings == null) {
                this._interfaceMappings = new HashMap();
            }
            this._interfaceMappings.put(key, ser);
            return;
        }
        if (this._transitiveClassMappings == null) {
            this._transitiveClassMappings = new HashMap();
        }
        this._transitiveClassMappings.put(key, ser);
    }

    public <T> void addSpecificMapping(Class<? extends T> forClass, JsonSerializer<T> ser) {
        ClassKey key = new ClassKey(forClass);
        if (forClass.isInterface()) {
            throw new IllegalArgumentException("Can not add specific mapping for an interface (" + forClass.getName() + ")");
        } else if (Modifier.isAbstract(forClass.getModifiers())) {
            throw new IllegalArgumentException("Can not add specific mapping for an abstract class (" + forClass.getName() + ")");
        } else {
            if (this._directClassMappings == null) {
                this._directClassMappings = new HashMap();
            }
            this._directClassMappings.put(key, ser);
        }
    }

    public void setEnumSerializer(JsonSerializer<?> enumSer) {
        this._enumSerializerOverride = enumSer;
    }

    public JsonSerializer<Object> createSerializer(SerializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException {
        JsonSerializer<?> ser = findCustomSerializer(type.getRawClass(), config);
        return ser != null ? ser : super.createSerializer(config, type, property);
    }

    protected JsonSerializer<?> findCustomSerializer(Class<?> type, SerializationConfig config) {
        JsonSerializer<?> ser;
        ClassKey key = new ClassKey(type);
        if (this._directClassMappings != null) {
            ser = (JsonSerializer) this._directClassMappings.get(key);
            if (ser != null) {
                return ser;
            }
        }
        if (type.isEnum() && this._enumSerializerOverride != null) {
            return this._enumSerializerOverride;
        }
        Class<?> curr;
        if (this._transitiveClassMappings != null) {
            for (curr = type; curr != null; curr = curr.getSuperclass()) {
                key.reset(curr);
                ser = (JsonSerializer) this._transitiveClassMappings.get(key);
                if (ser != null) {
                    return ser;
                }
            }
        }
        if (this._interfaceMappings != null) {
            key.reset(type);
            ser = (JsonSerializer) this._interfaceMappings.get(key);
            if (ser != null) {
                return ser;
            }
            for (curr = type; curr != null; curr = curr.getSuperclass()) {
                ser = _findInterfaceMapping(curr, key);
                if (ser != null) {
                    return ser;
                }
            }
        }
        return null;
    }

    protected JsonSerializer<?> _findInterfaceMapping(Class<?> cls, ClassKey key) {
        for (Class<?> iface : cls.getInterfaces()) {
            key.reset(iface);
            JsonSerializer<?> ser = (JsonSerializer) this._interfaceMappings.get(key);
            if (ser != null) {
                return ser;
            }
            ser = _findInterfaceMapping(iface, key);
            if (ser != null) {
                return ser;
            }
        }
        return null;
    }
}
