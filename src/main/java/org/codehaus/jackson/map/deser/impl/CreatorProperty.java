package org.codehaus.jackson.map.deser.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.deser.SettableBeanProperty;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedParameter;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.type.JavaType;

public class CreatorProperty extends SettableBeanProperty {
    protected final AnnotatedParameter _annotated;
    protected final Object _injectableValueId;

    public CreatorProperty(String name, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedParameter param, int index, Object injectableValueId) {
        super(name, type, typeDeser, contextAnnotations);
        this._annotated = param;
        this._propertyIndex = index;
        this._injectableValueId = injectableValueId;
    }

    protected CreatorProperty(CreatorProperty src, JsonDeserializer<Object> deser) {
        super(src, deser);
        this._annotated = src._annotated;
        this._injectableValueId = src._injectableValueId;
    }

    public CreatorProperty withValueDeserializer(JsonDeserializer<Object> deser) {
        return new CreatorProperty(this, deser);
    }

    public Object findInjectableValue(DeserializationContext context, Object beanInstance) {
        if (this._injectableValueId != null) {
            return context.findInjectableValue(this._injectableValueId, this, beanInstance);
        }
        throw new IllegalStateException("Property '" + getName() + "' (type " + getClass().getName() + ") has no injectable value id configured");
    }

    public void inject(DeserializationContext context, Object beanInstance) throws IOException {
        set(beanInstance, findInjectableValue(context, beanInstance));
    }

    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        if (this._annotated == null) {
            return null;
        }
        return this._annotated.getAnnotation(acls);
    }

    public AnnotatedMember getMember() {
        return this._annotated;
    }

    public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance) throws IOException, JsonProcessingException {
        set(instance, deserialize(jp, ctxt));
    }

    public void set(Object instance, Object value) throws IOException {
    }

    public Object getInjectableValueId() {
        return this._injectableValueId;
    }
}
