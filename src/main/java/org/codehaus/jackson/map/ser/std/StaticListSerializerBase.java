package org.codehaus.jackson.map.ser.std;

import java.lang.reflect.Type;
import java.util.Collection;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.node.ObjectNode;

public abstract class StaticListSerializerBase<T extends Collection<?>> extends SerializerBase<T> {
    protected final BeanProperty _property;

    protected abstract JsonNode contentSchema();

    protected StaticListSerializerBase(Class<?> cls, BeanProperty property) {
        super(cls, false);
        this._property = property;
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        ObjectNode o = createSchemaNode("array", true);
        o.put("items", contentSchema());
        return o;
    }
}
