package org.codehaus.jackson.map.introspect;

import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import org.codehaus.jackson.map.type.TypeFactory;

public final class AnnotatedParameter extends AnnotatedMember {
    protected final int _index;
    protected final AnnotatedWithParams _owner;
    protected final Type _type;

    public AnnotatedParameter(AnnotatedWithParams owner, Type type, AnnotationMap annotations, int index) {
        super(annotations);
        this._owner = owner;
        this._type = type;
        this._index = index;
    }

    public AnnotatedParameter withAnnotations(AnnotationMap ann) {
        return ann == this._annotations ? this : this._owner.replaceParameterAnnotations(this._index, ann);
    }

    public void addOrOverride(Annotation a) {
        this._annotations.add(a);
    }

    public AnnotatedElement getAnnotated() {
        return null;
    }

    public int getModifiers() {
        return this._owner.getModifiers();
    }

    public String getName() {
        return "";
    }

    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return this._annotations.get(acls);
    }

    public Type getGenericType() {
        return this._type;
    }

    public Class<?> getRawType() {
        if (this._type instanceof Class) {
            return (Class) this._type;
        }
        return TypeFactory.defaultInstance().constructType(this._type).getRawClass();
    }

    public Class<?> getDeclaringClass() {
        return this._owner.getDeclaringClass();
    }

    public Member getMember() {
        return this._owner.getMember();
    }

    public void setValue(Object pojo, Object value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot call setValue() on constructor parameter of " + getDeclaringClass().getName());
    }

    public Type getParameterType() {
        return this._type;
    }

    public AnnotatedWithParams getOwner() {
        return this._owner;
    }

    public int getIndex() {
        return this._index;
    }

    public String toString() {
        return "[parameter #" + getIndex() + ", annotations: " + this._annotations + RequestParameters.RIGHT_BRACKETS;
    }
}
