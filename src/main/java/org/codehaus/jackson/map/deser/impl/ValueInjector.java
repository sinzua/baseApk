package org.codehaus.jackson.map.deser.impl;

import java.io.IOException;
import org.codehaus.jackson.map.BeanProperty.Std;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.type.JavaType;

public class ValueInjector extends Std {
    protected final Object _valueId;

    public ValueInjector(String propertyName, JavaType type, Annotations contextAnnotations, AnnotatedMember mutator, Object valueId) {
        super(propertyName, type, contextAnnotations, mutator);
        this._valueId = valueId;
    }

    public Object findValue(DeserializationContext context, Object beanInstance) {
        return context.findInjectableValue(this._valueId, this, beanInstance);
    }

    public void inject(DeserializationContext context, Object beanInstance) throws IOException {
        this._member.setValue(beanInstance, findValue(context, beanInstance));
    }
}
