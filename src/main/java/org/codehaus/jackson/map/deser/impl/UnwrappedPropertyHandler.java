package org.codehaus.jackson.map.deser.impl;

import java.io.IOException;
import java.util.ArrayList;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.SettableBeanProperty;
import org.codehaus.jackson.util.TokenBuffer;

public class UnwrappedPropertyHandler {
    protected final ArrayList<SettableBeanProperty> _properties = new ArrayList();

    public void addProperty(SettableBeanProperty property) {
        this._properties.add(property);
    }

    public Object processUnwrapped(JsonParser originalParser, DeserializationContext ctxt, Object bean, TokenBuffer buffered) throws IOException, JsonProcessingException {
        int len = this._properties.size();
        for (int i = 0; i < len; i++) {
            SettableBeanProperty prop = (SettableBeanProperty) this._properties.get(i);
            JsonParser jp = buffered.asParser();
            jp.nextToken();
            prop.deserializeAndSet(jp, ctxt, bean);
        }
        return bean;
    }
}
