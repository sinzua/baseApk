package org.codehaus.jackson.map.ser.impl;

import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ser.impl.SerializerCache.TypeKey;

public class JsonSerializerMap {
    private final Bucket[] _buckets;
    private final int _size;

    private static final class Bucket {
        public final TypeKey key;
        public final Bucket next;
        public final JsonSerializer<Object> value;

        public Bucket(Bucket next, TypeKey key, JsonSerializer<Object> value) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }

    public JsonSerializerMap(Map<TypeKey, JsonSerializer<Object>> serializers) {
        int size = findSize(serializers.size());
        this._size = size;
        int hashMask = size - 1;
        Bucket[] buckets = new Bucket[size];
        for (Entry<TypeKey, JsonSerializer<Object>> entry : serializers.entrySet()) {
            TypeKey key = (TypeKey) entry.getKey();
            int index = key.hashCode() & hashMask;
            buckets[index] = new Bucket(buckets[index], key, (JsonSerializer) entry.getValue());
        }
        this._buckets = buckets;
    }

    private static final int findSize(int size) {
        int result = 8;
        while (result < (size <= 64 ? size + size : size + (size >> 2))) {
            result += result;
        }
        return result;
    }

    public int size() {
        return this._size;
    }

    public JsonSerializer<Object> find(TypeKey key) {
        Bucket bucket = this._buckets[key.hashCode() & (this._buckets.length - 1)];
        if (bucket == null) {
            return null;
        }
        if (key.equals(bucket.key)) {
            return bucket.value;
        }
        do {
            bucket = bucket.next;
            if (bucket == null) {
                return null;
            }
        } while (!key.equals(bucket.key));
        return bucket.value;
    }
}
