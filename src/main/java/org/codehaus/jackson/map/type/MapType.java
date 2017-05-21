package org.codehaus.jackson.map.type;

import com.supersonicads.sdk.utils.Constants.RequestParameters;
import org.codehaus.jackson.type.JavaType;

public final class MapType extends MapLikeType {
    @Deprecated
    private MapType(Class<?> mapType, JavaType keyT, JavaType valueT) {
        this(mapType, keyT, valueT, null, null);
    }

    private MapType(Class<?> mapType, JavaType keyT, JavaType valueT, Object valueHandler, Object typeHandler) {
        super(mapType, keyT, valueT, valueHandler, typeHandler);
    }

    public static MapType construct(Class<?> rawType, JavaType keyT, JavaType valueT) {
        return new MapType(rawType, keyT, valueT, null, null);
    }

    protected JavaType _narrow(Class<?> subclass) {
        return new MapType(subclass, this._keyType, this._valueType, this._valueHandler, this._typeHandler);
    }

    public JavaType narrowContentsBy(Class<?> contentClass) {
        return contentClass == this._valueType.getRawClass() ? this : new MapType(this._class, this._keyType, this._valueType.narrowBy(contentClass), this._valueHandler, this._typeHandler);
    }

    public JavaType widenContentsBy(Class<?> contentClass) {
        return contentClass == this._valueType.getRawClass() ? this : new MapType(this._class, this._keyType, this._valueType.widenBy(contentClass), this._valueHandler, this._typeHandler);
    }

    public JavaType narrowKey(Class<?> keySubclass) {
        return keySubclass == this._keyType.getRawClass() ? this : new MapType(this._class, this._keyType.narrowBy(keySubclass), this._valueType, this._valueHandler, this._typeHandler);
    }

    public JavaType widenKey(Class<?> keySubclass) {
        return keySubclass == this._keyType.getRawClass() ? this : new MapType(this._class, this._keyType.widenBy(keySubclass), this._valueType, this._valueHandler, this._typeHandler);
    }

    public MapType withTypeHandler(Object h) {
        return new MapType(this._class, this._keyType, this._valueType, this._valueHandler, h);
    }

    public MapType withContentTypeHandler(Object h) {
        return new MapType(this._class, this._keyType, this._valueType.withTypeHandler(h), this._valueHandler, this._typeHandler);
    }

    public MapType withValueHandler(Object h) {
        return new MapType(this._class, this._keyType, this._valueType, h, this._typeHandler);
    }

    public MapType withContentValueHandler(Object h) {
        return new MapType(this._class, this._keyType, this._valueType.withValueHandler(h), this._valueHandler, this._typeHandler);
    }

    public MapType withKeyTypeHandler(Object h) {
        return new MapType(this._class, this._keyType.withTypeHandler(h), this._valueType, this._valueHandler, this._typeHandler);
    }

    public MapType withKeyValueHandler(Object h) {
        return new MapType(this._class, this._keyType.withValueHandler(h), this._valueType, this._valueHandler, this._typeHandler);
    }

    public String toString() {
        return "[map type; class " + this._class.getName() + ", " + this._keyType + " -> " + this._valueType + RequestParameters.RIGHT_BRACKETS;
    }
}
