package org.codehaus.jackson.map.ser.impl;

import java.util.HashMap;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.type.JavaType;

public final class SerializerCache {
    private ReadOnlyClassToSerializerMap _readOnlyMap = null;
    private HashMap<TypeKey, JsonSerializer<Object>> _sharedMap = new HashMap(64);

    public static final class TypeKey {
        protected Class<?> _class;
        protected int _hashCode;
        protected boolean _isTyped;
        protected JavaType _type;

        public TypeKey(Class<?> key, boolean typed) {
            this._class = key;
            this._type = null;
            this._isTyped = typed;
            this._hashCode = hash((Class) key, typed);
        }

        public TypeKey(JavaType key, boolean typed) {
            this._type = key;
            this._class = null;
            this._isTyped = typed;
            this._hashCode = hash(key, typed);
        }

        private static final int hash(Class<?> cls, boolean typed) {
            int hash = cls.getName().hashCode();
            if (typed) {
                return hash + 1;
            }
            return hash;
        }

        private static final int hash(JavaType type, boolean typed) {
            int hash = type.hashCode() - 1;
            if (typed) {
                return hash - 1;
            }
            return hash;
        }

        public void resetTyped(Class<?> cls) {
            this._type = null;
            this._class = cls;
            this._isTyped = true;
            this._hashCode = hash((Class) cls, true);
        }

        public void resetUntyped(Class<?> cls) {
            this._type = null;
            this._class = cls;
            this._isTyped = false;
            this._hashCode = hash((Class) cls, false);
        }

        public void resetTyped(JavaType type) {
            this._type = type;
            this._class = null;
            this._isTyped = true;
            this._hashCode = hash(type, true);
        }

        public void resetUntyped(JavaType type) {
            this._type = type;
            this._class = null;
            this._isTyped = false;
            this._hashCode = hash(type, false);
        }

        public final int hashCode() {
            return this._hashCode;
        }

        public final String toString() {
            if (this._class != null) {
                return "{class: " + this._class.getName() + ", typed? " + this._isTyped + "}";
            }
            return "{type: " + this._type + ", typed? " + this._isTyped + "}";
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            TypeKey other = (TypeKey) o;
            if (other._isTyped != this._isTyped) {
                return false;
            }
            if (this._class == null) {
                return this._type.equals(other._type);
            }
            if (other._class != this._class) {
                return false;
            }
            return true;
        }
    }

    public ReadOnlyClassToSerializerMap getReadOnlyLookupMap() {
        ReadOnlyClassToSerializerMap m;
        synchronized (this) {
            m = this._readOnlyMap;
            if (m == null) {
                m = ReadOnlyClassToSerializerMap.from(this._sharedMap);
                this._readOnlyMap = m;
            }
        }
        return m.instance();
    }

    public synchronized int size() {
        return this._sharedMap.size();
    }

    public JsonSerializer<Object> untypedValueSerializer(Class<?> type) {
        JsonSerializer<Object> jsonSerializer;
        synchronized (this) {
            jsonSerializer = (JsonSerializer) this._sharedMap.get(new TypeKey((Class) type, false));
        }
        return jsonSerializer;
    }

    public JsonSerializer<Object> untypedValueSerializer(JavaType type) {
        JsonSerializer<Object> jsonSerializer;
        synchronized (this) {
            jsonSerializer = (JsonSerializer) this._sharedMap.get(new TypeKey(type, false));
        }
        return jsonSerializer;
    }

    public JsonSerializer<Object> typedValueSerializer(JavaType type) {
        JsonSerializer<Object> jsonSerializer;
        synchronized (this) {
            jsonSerializer = (JsonSerializer) this._sharedMap.get(new TypeKey(type, true));
        }
        return jsonSerializer;
    }

    public JsonSerializer<Object> typedValueSerializer(Class<?> cls) {
        JsonSerializer<Object> jsonSerializer;
        synchronized (this) {
            jsonSerializer = (JsonSerializer) this._sharedMap.get(new TypeKey((Class) cls, true));
        }
        return jsonSerializer;
    }

    public void addTypedSerializer(JavaType type, JsonSerializer<Object> ser) {
        synchronized (this) {
            if (this._sharedMap.put(new TypeKey(type, true), ser) == null) {
                this._readOnlyMap = null;
            }
        }
    }

    public void addTypedSerializer(Class<?> cls, JsonSerializer<Object> ser) {
        synchronized (this) {
            if (this._sharedMap.put(new TypeKey((Class) cls, true), ser) == null) {
                this._readOnlyMap = null;
            }
        }
    }

    public void addAndResolveNonTypedSerializer(Class<?> type, JsonSerializer<Object> ser, SerializerProvider provider) throws JsonMappingException {
        synchronized (this) {
            if (this._sharedMap.put(new TypeKey((Class) type, false), ser) == null) {
                this._readOnlyMap = null;
            }
            if (ser instanceof ResolvableSerializer) {
                ((ResolvableSerializer) ser).resolve(provider);
            }
        }
    }

    public void addAndResolveNonTypedSerializer(JavaType type, JsonSerializer<Object> ser, SerializerProvider provider) throws JsonMappingException {
        synchronized (this) {
            if (this._sharedMap.put(new TypeKey(type, false), ser) == null) {
                this._readOnlyMap = null;
            }
            if (ser instanceof ResolvableSerializer) {
                ((ResolvableSerializer) ser).resolve(provider);
            }
        }
    }

    public synchronized void flush() {
        this._sharedMap.clear();
    }
}
