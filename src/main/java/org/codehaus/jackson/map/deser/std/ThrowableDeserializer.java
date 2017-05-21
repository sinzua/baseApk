package org.codehaus.jackson.map.deser.std;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.deser.BeanDeserializer;
import org.codehaus.jackson.map.deser.SettableBeanProperty;

public class ThrowableDeserializer extends BeanDeserializer {
    protected static final String PROP_NAME_MESSAGE = "message";

    public ThrowableDeserializer(BeanDeserializer baseDeserializer) {
        super(baseDeserializer);
    }

    protected ThrowableDeserializer(BeanDeserializer src, boolean ignoreAllUnknown) {
        super(src, ignoreAllUnknown);
    }

    public JsonDeserializer<Object> unwrappingDeserializer() {
        return getClass() != ThrowableDeserializer.class ? this : new ThrowableDeserializer(this, true);
    }

    public Object deserializeFromObject(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (this._propertyBasedCreator != null) {
            return _deserializeUsingPropertyBased(jp, ctxt);
        }
        if (this._delegateDeserializer != null) {
            return this._valueInstantiator.createUsingDelegate(this._delegateDeserializer.deserialize(jp, ctxt));
        }
        if (this._beanType.isAbstract()) {
            throw JsonMappingException.from(jp, "Can not instantiate abstract type " + this._beanType + " (need to add/enable type information?)");
        }
        boolean hasStringCreator = this._valueInstantiator.canCreateFromString();
        boolean hasDefaultCtor = this._valueInstantiator.canCreateUsingDefault();
        if (hasStringCreator || hasDefaultCtor) {
            int len;
            int i;
            Object throwable = null;
            Object[] pending = null;
            int pendingIx = 0;
            while (jp.getCurrentToken() != JsonToken.END_OBJECT) {
                String propName = jp.getCurrentName();
                SettableBeanProperty prop = this._beanProperties.find(propName);
                jp.nextToken();
                if (prop != null) {
                    if (throwable != null) {
                        prop.deserializeAndSet(jp, ctxt, throwable);
                    } else {
                        if (pending == null) {
                            len = this._beanProperties.size();
                            pending = new Object[(len + len)];
                        }
                        int i2 = pendingIx + 1;
                        pending[pendingIx] = prop;
                        pendingIx = i2 + 1;
                        pending[i2] = prop.deserialize(jp, ctxt);
                    }
                } else if (PROP_NAME_MESSAGE.equals(propName) && hasStringCreator) {
                    throwable = this._valueInstantiator.createFromString(jp.getText());
                    if (pending != null) {
                        len = pendingIx;
                        for (i = 0; i < len; i += 2) {
                            pending[i].set(throwable, pending[i + 1]);
                        }
                        pending = null;
                    }
                } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
                    jp.skipChildren();
                } else if (this._anySetter != null) {
                    this._anySetter.deserializeAndSet(jp, ctxt, throwable, propName);
                } else {
                    handleUnknownProperty(jp, ctxt, throwable, propName);
                }
                jp.nextToken();
            }
            if (throwable != null) {
                return throwable;
            }
            if (hasStringCreator) {
                throwable = this._valueInstantiator.createFromString(null);
            } else {
                throwable = this._valueInstantiator.createUsingDefault();
            }
            if (pending == null) {
                return throwable;
            }
            len = pendingIx;
            for (i = 0; i < len; i += 2) {
                ((SettableBeanProperty) pending[i]).set(throwable, pending[i + 1]);
            }
            return throwable;
        }
        throw new JsonMappingException("Can not deserialize Throwable of type " + this._beanType + " without having a default contructor, a single-String-arg constructor; or explicit @JsonCreator");
    }
}
