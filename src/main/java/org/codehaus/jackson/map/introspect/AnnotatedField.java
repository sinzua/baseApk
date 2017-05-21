package org.codehaus.jackson.map.introspect;

import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

public final class AnnotatedField extends AnnotatedMember {
    protected final Field _field;

    public AnnotatedField(Field field, AnnotationMap annMap) {
        super(annMap);
        this._field = field;
    }

    public AnnotatedField withAnnotations(AnnotationMap ann) {
        return new AnnotatedField(this._field, ann);
    }

    public void addOrOverride(Annotation a) {
        this._annotations.add(a);
    }

    public Field getAnnotated() {
        return this._field;
    }

    public int getModifiers() {
        return this._field.getModifiers();
    }

    public String getName() {
        return this._field.getName();
    }

    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return this._annotations.get(acls);
    }

    public Type getGenericType() {
        return this._field.getGenericType();
    }

    public Class<?> getRawType() {
        return this._field.getType();
    }

    public Class<?> getDeclaringClass() {
        return this._field.getDeclaringClass();
    }

    public Member getMember() {
        return this._field;
    }

    public void setValue(Object pojo, Object value) throws IllegalArgumentException {
        try {
            this._field.set(pojo, value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Failed to setValue() for field " + getFullName() + ": " + e.getMessage(), e);
        }
    }

    public String getFullName() {
        return getDeclaringClass().getName() + "#" + getName();
    }

    public int getAnnotationCount() {
        return this._annotations.size();
    }

    public String toString() {
        return "[field " + getName() + ", annotations: " + this._annotations + RequestParameters.RIGHT_BRACKETS;
    }
}
