package org.codehaus.jackson.map.deser.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.deser.SettableBeanProperty;
import org.codehaus.jackson.map.deser.ValueInstantiator;
import org.codehaus.jackson.map.util.ClassUtil;

public final class PropertyBasedCreator {
    protected Object[] _defaultValues;
    protected final HashMap<String, SettableBeanProperty> _properties = new HashMap();
    protected final SettableBeanProperty[] _propertiesWithInjectables;
    protected final int _propertyCount;
    protected final ValueInstantiator _valueInstantiator;

    public PropertyBasedCreator(ValueInstantiator valueInstantiator) {
        this._valueInstantiator = valueInstantiator;
        Object[] defValues = null;
        SettableBeanProperty[] creatorProps = valueInstantiator.getFromObjectArguments();
        SettableBeanProperty[] propertiesWithInjectables = null;
        int len = creatorProps.length;
        this._propertyCount = len;
        for (int i = 0; i < len; i++) {
            SettableBeanProperty prop = creatorProps[i];
            this._properties.put(prop.getName(), prop);
            if (prop.getType().isPrimitive()) {
                if (defValues == null) {
                    defValues = new Object[len];
                }
                defValues[i] = ClassUtil.defaultValue(prop.getType().getRawClass());
            }
            if (prop.getInjectableValueId() != null) {
                if (propertiesWithInjectables == null) {
                    propertiesWithInjectables = new SettableBeanProperty[len];
                }
                propertiesWithInjectables[i] = prop;
            }
        }
        this._defaultValues = defValues;
        this._propertiesWithInjectables = propertiesWithInjectables;
    }

    public Collection<SettableBeanProperty> getCreatorProperties() {
        return this._properties.values();
    }

    public SettableBeanProperty findCreatorProperty(String name) {
        return (SettableBeanProperty) this._properties.get(name);
    }

    public void assignDeserializer(SettableBeanProperty prop, JsonDeserializer<Object> deser) {
        prop = prop.withValueDeserializer(deser);
        this._properties.put(prop.getName(), prop);
        Object nullValue = deser.getNullValue();
        if (nullValue != null) {
            if (this._defaultValues == null) {
                this._defaultValues = new Object[this._properties.size()];
            }
            this._defaultValues[prop.getPropertyIndex()] = nullValue;
        }
    }

    public PropertyValueBuffer startBuilding(JsonParser jp, DeserializationContext ctxt) {
        PropertyValueBuffer buffer = new PropertyValueBuffer(jp, ctxt, this._propertyCount);
        if (this._propertiesWithInjectables != null) {
            buffer.inject(this._propertiesWithInjectables);
        }
        return buffer;
    }

    public Object build(PropertyValueBuffer buffer) throws IOException {
        Object bean = this._valueInstantiator.createFromObjectWith(buffer.getParameters(this._defaultValues));
        for (PropertyValue pv = buffer.buffered(); pv != null; pv = pv.next) {
            pv.assign(bean);
        }
        return bean;
    }
}
