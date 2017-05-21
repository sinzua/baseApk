package org.codehaus.jackson.map.type;

import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.lang.reflect.Array;
import org.codehaus.jackson.type.JavaType;

public final class ArrayType extends TypeBase {
    protected final JavaType _componentType;
    protected final Object _emptyArray;

    private ArrayType(JavaType componentType, Object emptyInstance, Object valueHandler, Object typeHandler) {
        super(emptyInstance.getClass(), componentType.hashCode(), valueHandler, typeHandler);
        this._componentType = componentType;
        this._emptyArray = emptyInstance;
    }

    @Deprecated
    public static ArrayType construct(JavaType componentType) {
        return construct(componentType, null, null);
    }

    public static ArrayType construct(JavaType componentType, Object valueHandler, Object typeHandler) {
        return new ArrayType(componentType, Array.newInstance(componentType.getRawClass(), 0), null, null);
    }

    public ArrayType withTypeHandler(Object h) {
        return h == this._typeHandler ? this : new ArrayType(this._componentType, this._emptyArray, this._valueHandler, h);
    }

    public ArrayType withContentTypeHandler(Object h) {
        return h == this._componentType.getTypeHandler() ? this : new ArrayType(this._componentType.withTypeHandler(h), this._emptyArray, this._valueHandler, this._typeHandler);
    }

    public ArrayType withValueHandler(Object h) {
        return h == this._valueHandler ? this : new ArrayType(this._componentType, this._emptyArray, h, this._typeHandler);
    }

    public ArrayType withContentValueHandler(Object h) {
        return h == this._componentType.getValueHandler() ? this : new ArrayType(this._componentType.withValueHandler(h), this._emptyArray, this._valueHandler, this._typeHandler);
    }

    protected String buildCanonicalName() {
        return this._class.getName();
    }

    protected JavaType _narrow(Class<?> subclass) {
        if (subclass.isArray()) {
            return construct(TypeFactory.defaultInstance().constructType(subclass.getComponentType()), this._valueHandler, this._typeHandler);
        }
        throw new IllegalArgumentException("Incompatible narrowing operation: trying to narrow " + toString() + " to class " + subclass.getName());
    }

    public JavaType narrowContentsBy(Class<?> contentClass) {
        return contentClass == this._componentType.getRawClass() ? this : construct(this._componentType.narrowBy(contentClass), this._valueHandler, this._typeHandler);
    }

    public JavaType widenContentsBy(Class<?> contentClass) {
        return contentClass == this._componentType.getRawClass() ? this : construct(this._componentType.widenBy(contentClass), this._valueHandler, this._typeHandler);
    }

    public boolean isArrayType() {
        return true;
    }

    public boolean isAbstract() {
        return false;
    }

    public boolean isConcrete() {
        return true;
    }

    public boolean hasGenericTypes() {
        return this._componentType.hasGenericTypes();
    }

    public String containedTypeName(int index) {
        if (index == 0) {
            return "E";
        }
        return null;
    }

    public boolean isContainerType() {
        return true;
    }

    public JavaType getContentType() {
        return this._componentType;
    }

    public int containedTypeCount() {
        return 1;
    }

    public JavaType containedType(int index) {
        return index == 0 ? this._componentType : null;
    }

    public StringBuilder getGenericSignature(StringBuilder sb) {
        sb.append('[');
        return this._componentType.getGenericSignature(sb);
    }

    public StringBuilder getErasedSignature(StringBuilder sb) {
        sb.append('[');
        return this._componentType.getErasedSignature(sb);
    }

    public String toString() {
        return "[array type, component type: " + this._componentType + RequestParameters.RIGHT_BRACKETS;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        return this._componentType.equals(((ArrayType) o)._componentType);
    }
}
