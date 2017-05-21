package org.codehaus.jackson.map.deser.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.SettableBeanProperty;
import org.codehaus.jackson.util.TokenBuffer;

public class ExternalTypeHandler {
    private final HashMap<String, Integer> _nameToPropertyIndex;
    private final ExtTypedProperty[] _properties;
    private final TokenBuffer[] _tokens;
    private final String[] _typeIds;

    public static class Builder {
        private final HashMap<String, Integer> _nameToPropertyIndex = new HashMap();
        private final ArrayList<ExtTypedProperty> _properties = new ArrayList();

        public void addExternal(SettableBeanProperty property, String extPropName) {
            Integer index = Integer.valueOf(this._properties.size());
            this._properties.add(new ExtTypedProperty(property, extPropName));
            this._nameToPropertyIndex.put(property.getName(), index);
            this._nameToPropertyIndex.put(extPropName, index);
        }

        public ExternalTypeHandler build() {
            return new ExternalTypeHandler((ExtTypedProperty[]) this._properties.toArray(new ExtTypedProperty[this._properties.size()]), this._nameToPropertyIndex, null, null);
        }
    }

    private static final class ExtTypedProperty {
        private final SettableBeanProperty _property;
        private final String _typePropertyName;

        public ExtTypedProperty(SettableBeanProperty property, String typePropertyName) {
            this._property = property;
            this._typePropertyName = typePropertyName;
        }

        public boolean hasTypePropertyName(String n) {
            return n.equals(this._typePropertyName);
        }

        public String getTypePropertyName() {
            return this._typePropertyName;
        }

        public SettableBeanProperty getProperty() {
            return this._property;
        }
    }

    protected ExternalTypeHandler(ExtTypedProperty[] properties, HashMap<String, Integer> nameToPropertyIndex, String[] typeIds, TokenBuffer[] tokens) {
        this._properties = properties;
        this._nameToPropertyIndex = nameToPropertyIndex;
        this._typeIds = typeIds;
        this._tokens = tokens;
    }

    protected ExternalTypeHandler(ExternalTypeHandler h) {
        this._properties = h._properties;
        this._nameToPropertyIndex = h._nameToPropertyIndex;
        int len = this._properties.length;
        this._typeIds = new String[len];
        this._tokens = new TokenBuffer[len];
    }

    public ExternalTypeHandler start() {
        return new ExternalTypeHandler(this);
    }

    public boolean handleTypePropertyValue(JsonParser jp, DeserializationContext ctxt, String propName, Object bean) throws IOException, JsonProcessingException {
        Integer I = (Integer) this._nameToPropertyIndex.get(propName);
        if (I == null) {
            return false;
        }
        int index = I.intValue();
        if (!this._properties[index].hasTypePropertyName(propName)) {
            return false;
        }
        boolean canDeserialize;
        this._typeIds[index] = jp.getText();
        if (bean == null || this._tokens[index] == null) {
            canDeserialize = false;
        } else {
            canDeserialize = true;
        }
        if (canDeserialize) {
            _deserialize(jp, ctxt, bean, index);
            this._typeIds[index] = null;
            this._tokens[index] = null;
        }
        return true;
    }

    public boolean handleToken(JsonParser jp, DeserializationContext ctxt, String propName, Object bean) throws IOException, JsonProcessingException {
        boolean canDeserialize = false;
        Integer I = (Integer) this._nameToPropertyIndex.get(propName);
        if (I == null) {
            return false;
        }
        int index = I.intValue();
        if (this._properties[index].hasTypePropertyName(propName)) {
            this._typeIds[index] = jp.getText();
            jp.skipChildren();
            if (!(bean == null || this._tokens[index] == null)) {
                canDeserialize = true;
            }
        } else {
            TokenBuffer tokens = new TokenBuffer(jp.getCodec());
            tokens.copyCurrentStructure(jp);
            this._tokens[index] = tokens;
            if (!(bean == null || this._typeIds[index] == null)) {
                canDeserialize = true;
            }
        }
        if (canDeserialize) {
            _deserialize(jp, ctxt, bean, index);
            this._typeIds[index] = null;
            this._tokens[index] = null;
        }
        return true;
    }

    public Object complete(JsonParser jp, DeserializationContext ctxt, Object bean) throws IOException, JsonProcessingException {
        int len = this._properties.length;
        for (int i = 0; i < len; i++) {
            if (this._typeIds[i] == null) {
                if (this._tokens[i] != null) {
                    throw ctxt.mappingException("Missing external type id property '" + this._properties[i].getTypePropertyName() + "'");
                }
            } else if (this._tokens[i] == null) {
                throw ctxt.mappingException("Missing property '" + this._properties[i].getProperty().getName() + "' for external type id '" + this._properties[i].getTypePropertyName());
            } else {
                _deserialize(jp, ctxt, bean, i);
            }
        }
        return bean;
    }

    protected final void _deserialize(JsonParser jp, DeserializationContext ctxt, Object bean, int index) throws IOException, JsonProcessingException {
        TokenBuffer merged = new TokenBuffer(jp.getCodec());
        merged.writeStartArray();
        merged.writeString(this._typeIds[index]);
        JsonParser p2 = this._tokens[index].asParser(jp);
        p2.nextToken();
        merged.copyCurrentStructure(p2);
        merged.writeEndArray();
        p2 = merged.asParser(jp);
        p2.nextToken();
        this._properties[index].getProperty().deserializeAndSet(p2, ctxt, bean);
    }
}
