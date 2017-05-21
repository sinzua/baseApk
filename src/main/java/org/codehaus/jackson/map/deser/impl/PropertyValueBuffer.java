package org.codehaus.jackson.map.deser.impl;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.SettableAnyProperty;
import org.codehaus.jackson.map.deser.SettableBeanProperty;

public final class PropertyValueBuffer {
    private PropertyValue _buffered;
    final DeserializationContext _context;
    final Object[] _creatorParameters;
    private int _paramsNeeded;
    final JsonParser _parser;

    public PropertyValueBuffer(JsonParser jp, DeserializationContext ctxt, int paramCount) {
        this._parser = jp;
        this._context = ctxt;
        this._paramsNeeded = paramCount;
        this._creatorParameters = new Object[paramCount];
    }

    public void inject(SettableBeanProperty[] injectableProperties) {
        int len = injectableProperties.length;
        for (int i = 0; i < len; i++) {
            SettableBeanProperty prop = injectableProperties[i];
            if (prop != null) {
                this._creatorParameters[i] = this._context.findInjectableValue(prop.getInjectableValueId(), prop, null);
            }
        }
    }

    protected final Object[] getParameters(Object[] defaults) {
        if (defaults != null) {
            int len = this._creatorParameters.length;
            for (int i = 0; i < len; i++) {
                if (this._creatorParameters[i] == null) {
                    Object value = defaults[i];
                    if (value != null) {
                        this._creatorParameters[i] = value;
                    }
                }
            }
        }
        return this._creatorParameters;
    }

    protected PropertyValue buffered() {
        return this._buffered;
    }

    public boolean assignParameter(int index, Object value) {
        this._creatorParameters[index] = value;
        int i = this._paramsNeeded - 1;
        this._paramsNeeded = i;
        return i <= 0;
    }

    public void bufferProperty(SettableBeanProperty prop, Object value) {
        this._buffered = new Regular(this._buffered, value, prop);
    }

    public void bufferAnyProperty(SettableAnyProperty prop, String propName, Object value) {
        this._buffered = new Any(this._buffered, value, prop, propName);
    }

    public void bufferMapProperty(Object key, Object value) {
        this._buffered = new Map(this._buffered, value, key);
    }
}
