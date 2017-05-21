package org.codehaus.jackson.type;

import java.lang.reflect.Modifier;

public abstract class JavaType {
    protected final Class<?> _class;
    protected final int _hashCode;
    protected Object _typeHandler = null;
    protected Object _valueHandler = null;

    protected abstract JavaType _narrow(Class<?> cls);

    public abstract boolean equals(Object obj);

    public abstract StringBuilder getErasedSignature(StringBuilder stringBuilder);

    public abstract StringBuilder getGenericSignature(StringBuilder stringBuilder);

    public abstract boolean isContainerType();

    public abstract JavaType narrowContentsBy(Class<?> cls);

    public abstract String toCanonical();

    public abstract String toString();

    public abstract JavaType widenContentsBy(Class<?> cls);

    public abstract JavaType withContentTypeHandler(Object obj);

    public abstract JavaType withTypeHandler(Object obj);

    protected JavaType(Class<?> raw, int additionalHash) {
        this._class = raw;
        this._hashCode = raw.getName().hashCode() + additionalHash;
    }

    public JavaType withValueHandler(Object h) {
        setValueHandler(h);
        return this;
    }

    public JavaType withContentValueHandler(Object h) {
        getContentType().setValueHandler(h);
        return this;
    }

    @Deprecated
    public void setValueHandler(Object h) {
        if (h == null || this._valueHandler == null) {
            this._valueHandler = h;
            return;
        }
        throw new IllegalStateException("Trying to reset value handler for type [" + toString() + "]; old handler of type " + this._valueHandler.getClass().getName() + ", new handler of type " + h.getClass().getName());
    }

    public JavaType narrowBy(Class<?> subclass) {
        if (subclass == this._class) {
            return this;
        }
        _assertSubclass(subclass, this._class);
        JavaType result = _narrow(subclass);
        if (this._valueHandler != result.getValueHandler()) {
            result = result.withValueHandler(this._valueHandler);
        }
        if (this._typeHandler != result.getTypeHandler()) {
            result = result.withTypeHandler(this._typeHandler);
        }
        return result;
    }

    public JavaType forcedNarrowBy(Class<?> subclass) {
        if (subclass == this._class) {
            return this;
        }
        JavaType result = _narrow(subclass);
        if (this._valueHandler != result.getValueHandler()) {
            result = result.withValueHandler(this._valueHandler);
        }
        if (this._typeHandler != result.getTypeHandler()) {
            result = result.withTypeHandler(this._typeHandler);
        }
        return result;
    }

    public JavaType widenBy(Class<?> superclass) {
        if (superclass == this._class) {
            return this;
        }
        _assertSubclass(this._class, superclass);
        return _widen(superclass);
    }

    protected JavaType _widen(Class<?> superclass) {
        return _narrow(superclass);
    }

    public final Class<?> getRawClass() {
        return this._class;
    }

    public final boolean hasRawClass(Class<?> clz) {
        return this._class == clz;
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(this._class.getModifiers());
    }

    public boolean isConcrete() {
        if ((this._class.getModifiers() & 1536) == 0 || this._class.isPrimitive()) {
            return true;
        }
        return false;
    }

    public boolean isThrowable() {
        return Throwable.class.isAssignableFrom(this._class);
    }

    public boolean isArrayType() {
        return false;
    }

    public final boolean isEnumType() {
        return this._class.isEnum();
    }

    public final boolean isInterface() {
        return this._class.isInterface();
    }

    public final boolean isPrimitive() {
        return this._class.isPrimitive();
    }

    public final boolean isFinal() {
        return Modifier.isFinal(this._class.getModifiers());
    }

    public boolean isCollectionLikeType() {
        return false;
    }

    public boolean isMapLikeType() {
        return false;
    }

    public boolean hasGenericTypes() {
        return containedTypeCount() > 0;
    }

    public JavaType getKeyType() {
        return null;
    }

    public JavaType getContentType() {
        return null;
    }

    public int containedTypeCount() {
        return 0;
    }

    public JavaType containedType(int index) {
        return null;
    }

    public String containedTypeName(int index) {
        return null;
    }

    public <T> T getValueHandler() {
        return this._valueHandler;
    }

    public <T> T getTypeHandler() {
        return this._typeHandler;
    }

    public String getGenericSignature() {
        StringBuilder sb = new StringBuilder(40);
        getGenericSignature(sb);
        return sb.toString();
    }

    public String getErasedSignature() {
        StringBuilder sb = new StringBuilder(40);
        getErasedSignature(sb);
        return sb.toString();
    }

    protected void _assertSubclass(Class<?> subclass, Class<?> cls) {
        if (!this._class.isAssignableFrom(subclass)) {
            throw new IllegalArgumentException("Class " + subclass.getName() + " is not assignable to " + this._class.getName());
        }
    }

    public final int hashCode() {
        return this._hashCode;
    }
}
