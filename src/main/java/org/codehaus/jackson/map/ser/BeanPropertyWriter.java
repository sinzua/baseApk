package org.codehaus.jackson.map.ser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap;
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap.SerializerAndMapResult;
import org.codehaus.jackson.map.ser.impl.UnwrappingBeanPropertyWriter;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.type.JavaType;

public class BeanPropertyWriter implements BeanProperty {
    protected final Method _accessorMethod;
    protected final JavaType _cfgSerializationType;
    protected final Annotations _contextAnnotations;
    protected final JavaType _declaredType;
    protected PropertySerializerMap _dynamicSerializers;
    protected final Field _field;
    protected Class<?>[] _includeInViews;
    protected HashMap<Object, Object> _internalSettings;
    protected final AnnotatedMember _member;
    protected final SerializedString _name;
    protected JavaType _nonTrivialBaseType;
    protected final JsonSerializer<Object> _serializer;
    protected final boolean _suppressNulls;
    protected final Object _suppressableValue;
    protected TypeSerializer _typeSerializer;

    public BeanPropertyWriter(AnnotatedMember member, Annotations contextAnnotations, String name, JavaType declaredType, JsonSerializer<Object> ser, TypeSerializer typeSer, JavaType serType, Method m, Field f, boolean suppressNulls, Object suppressableValue) {
        this(member, contextAnnotations, new SerializedString(name), declaredType, (JsonSerializer) ser, typeSer, serType, m, f, suppressNulls, suppressableValue);
    }

    public BeanPropertyWriter(AnnotatedMember member, Annotations contextAnnotations, SerializedString name, JavaType declaredType, JsonSerializer<Object> ser, TypeSerializer typeSer, JavaType serType, Method m, Field f, boolean suppressNulls, Object suppressableValue) {
        this._member = member;
        this._contextAnnotations = contextAnnotations;
        this._name = name;
        this._declaredType = declaredType;
        this._serializer = ser;
        this._dynamicSerializers = ser == null ? PropertySerializerMap.emptyMap() : null;
        this._typeSerializer = typeSer;
        this._cfgSerializationType = serType;
        this._accessorMethod = m;
        this._field = f;
        this._suppressNulls = suppressNulls;
        this._suppressableValue = suppressableValue;
    }

    protected BeanPropertyWriter(BeanPropertyWriter base) {
        this(base, base._serializer);
    }

    protected BeanPropertyWriter(BeanPropertyWriter base, JsonSerializer<Object> ser) {
        this._serializer = ser;
        this._member = base._member;
        this._contextAnnotations = base._contextAnnotations;
        this._declaredType = base._declaredType;
        this._accessorMethod = base._accessorMethod;
        this._field = base._field;
        if (base._internalSettings != null) {
            this._internalSettings = new HashMap(base._internalSettings);
        }
        this._name = base._name;
        this._cfgSerializationType = base._cfgSerializationType;
        this._dynamicSerializers = base._dynamicSerializers;
        this._suppressNulls = base._suppressNulls;
        this._suppressableValue = base._suppressableValue;
        this._includeInViews = base._includeInViews;
        this._typeSerializer = base._typeSerializer;
        this._nonTrivialBaseType = base._nonTrivialBaseType;
    }

    public BeanPropertyWriter withSerializer(JsonSerializer<Object> ser) {
        if (getClass() == BeanPropertyWriter.class) {
            return new BeanPropertyWriter(this, ser);
        }
        throw new IllegalStateException("BeanPropertyWriter sub-class does not override 'withSerializer()'; needs to!");
    }

    public BeanPropertyWriter unwrappingWriter() {
        return new UnwrappingBeanPropertyWriter(this);
    }

    public void setViews(Class<?>[] views) {
        this._includeInViews = views;
    }

    public void setNonTrivialBaseType(JavaType t) {
        this._nonTrivialBaseType = t;
    }

    public String getName() {
        return this._name.getValue();
    }

    public JavaType getType() {
        return this._declaredType;
    }

    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return this._member.getAnnotation(acls);
    }

    public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
        return this._contextAnnotations.get(acls);
    }

    public AnnotatedMember getMember() {
        return this._member;
    }

    public Object getInternalSetting(Object key) {
        if (this._internalSettings == null) {
            return null;
        }
        return this._internalSettings.get(key);
    }

    public Object setInternalSetting(Object key, Object value) {
        if (this._internalSettings == null) {
            this._internalSettings = new HashMap();
        }
        return this._internalSettings.put(key, value);
    }

    public Object removeInternalSetting(Object key) {
        Object removed = null;
        if (this._internalSettings != null) {
            removed = this._internalSettings.remove(key);
            if (this._internalSettings.size() == 0) {
                this._internalSettings = null;
            }
        }
        return removed;
    }

    public SerializedString getSerializedName() {
        return this._name;
    }

    public boolean hasSerializer() {
        return this._serializer != null;
    }

    public JsonSerializer<Object> getSerializer() {
        return this._serializer;
    }

    public JavaType getSerializationType() {
        return this._cfgSerializationType;
    }

    public Class<?> getRawSerializationType() {
        return this._cfgSerializationType == null ? null : this._cfgSerializationType.getRawClass();
    }

    public Class<?> getPropertyType() {
        if (this._accessorMethod != null) {
            return this._accessorMethod.getReturnType();
        }
        return this._field.getType();
    }

    public Type getGenericPropertyType() {
        if (this._accessorMethod != null) {
            return this._accessorMethod.getGenericReturnType();
        }
        return this._field.getGenericType();
    }

    public Class<?>[] getViews() {
        return this._includeInViews;
    }

    public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov) throws Exception {
        Object value = get(bean);
        if (value != null) {
            if (value == bean) {
                _reportSelfReference(bean);
            }
            if (this._suppressableValue == null || !this._suppressableValue.equals(value)) {
                JsonSerializer<Object> ser = this._serializer;
                if (ser == null) {
                    Class<?> cls = value.getClass();
                    PropertySerializerMap map = this._dynamicSerializers;
                    ser = map.serializerFor(cls);
                    if (ser == null) {
                        ser = _findAndAddDynamic(map, cls, prov);
                    }
                }
                jgen.writeFieldName(this._name);
                if (this._typeSerializer == null) {
                    ser.serialize(value, jgen, prov);
                } else {
                    ser.serializeWithType(value, jgen, prov, this._typeSerializer);
                }
            }
        } else if (!this._suppressNulls) {
            jgen.writeFieldName(this._name);
            prov.defaultSerializeNull(jgen);
        }
    }

    protected JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider) throws JsonMappingException {
        SerializerAndMapResult result;
        if (this._nonTrivialBaseType != null) {
            result = map.findAndAddSerializer(provider.constructSpecializedType(this._nonTrivialBaseType, type), provider, (BeanProperty) this);
        } else {
            result = map.findAndAddSerializer((Class) type, provider, (BeanProperty) this);
        }
        if (map != result.map) {
            this._dynamicSerializers = result.map;
        }
        return result.serializer;
    }

    public final Object get(Object bean) throws Exception {
        if (this._accessorMethod != null) {
            return this._accessorMethod.invoke(bean, new Object[0]);
        }
        return this._field.get(bean);
    }

    protected void _reportSelfReference(Object bean) throws JsonMappingException {
        throw new JsonMappingException("Direct self-reference leading to cycle");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(40);
        sb.append("property '").append(getName()).append("' (");
        if (this._accessorMethod != null) {
            sb.append("via method ").append(this._accessorMethod.getDeclaringClass().getName()).append("#").append(this._accessorMethod.getName());
        } else {
            sb.append("field \"").append(this._field.getDeclaringClass().getName()).append("#").append(this._field.getName());
        }
        if (this._serializer == null) {
            sb.append(", no static serializer");
        } else {
            sb.append(", static serializer of type " + this._serializer.getClass().getName());
        }
        sb.append(')');
        return sb.toString();
    }
}
