package com.lidroid.xutils.cache;

import java.util.concurrent.ConcurrentHashMap;

public class KeyExpiryMap<K, V> extends ConcurrentHashMap<K, Long> {
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    private static final long serialVersionUID = 1;

    public KeyExpiryMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        super(initialCapacity, loadFactor, concurrencyLevel);
    }

    public KeyExpiryMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor, 16);
    }

    public KeyExpiryMap(int initialCapacity) {
        super(initialCapacity);
    }

    public synchronized Long get(Object key) {
        Long l;
        if (containsKey(key)) {
            l = (Long) super.get(key);
        } else {
            l = null;
        }
        return l;
    }

    public synchronized Long put(K key, Long expiryTimestamp) {
        if (containsKey(key)) {
            remove((Object) key);
        }
        return (Long) super.put(key, expiryTimestamp);
    }

    public synchronized boolean containsKey(Object key) {
        boolean result;
        result = false;
        Long expiryTimestamp = (Long) super.get(key);
        if (expiryTimestamp == null || System.currentTimeMillis() >= expiryTimestamp.longValue()) {
            remove(key);
        } else {
            result = true;
        }
        return result;
    }

    public synchronized Long remove(Object key) {
        return (Long) super.remove(key);
    }

    public synchronized void clear() {
        super.clear();
    }
}
