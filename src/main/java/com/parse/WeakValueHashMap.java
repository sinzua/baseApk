package com.parse;

import java.lang.ref.WeakReference;
import java.util.HashMap;

class WeakValueHashMap<K, V> {
    private HashMap<K, WeakReference<V>> map = new HashMap();

    public void put(K key, V value) {
        this.map.put(key, new WeakReference(value));
    }

    public V get(K key) {
        WeakReference<V> reference = (WeakReference) this.map.get(key);
        if (reference == null) {
            return null;
        }
        V value = reference.get();
        if (value != null) {
            return value;
        }
        this.map.remove(key);
        return value;
    }

    public void remove(K key) {
        this.map.remove(key);
    }

    public void clear() {
        this.map.clear();
    }
}
