package com.lidroid.xutils.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LruMemoryCache<K, V> {
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private KeyExpiryMap<K, Long> keyExpiryMap;
    private final LinkedHashMap<K, V> map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;

    public LruMemoryCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap(0, 0.75f, true);
        this.keyExpiryMap = new KeyExpiryMap(0, 0.75f);
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        trimToSize(maxSize);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final V get(K r5) {
        /*
        r4 = this;
        r2 = 0;
        if (r5 != 0) goto L_0x000b;
    L_0x0003:
        r2 = new java.lang.NullPointerException;
        r3 = "key == null";
        r2.<init>(r3);
        throw r2;
    L_0x000b:
        monitor-enter(r4);
        r3 = r4.keyExpiryMap;	 Catch:{ all -> 0x002a }
        r3 = r3.containsKey(r5);	 Catch:{ all -> 0x002a }
        if (r3 != 0) goto L_0x001a;
    L_0x0014:
        r4.remove(r5);	 Catch:{ all -> 0x002a }
        monitor-exit(r4);	 Catch:{ all -> 0x002a }
        r1 = r2;
    L_0x0019:
        return r1;
    L_0x001a:
        r3 = r4.map;	 Catch:{ all -> 0x002a }
        r1 = r3.get(r5);	 Catch:{ all -> 0x002a }
        if (r1 == 0) goto L_0x002d;
    L_0x0022:
        r2 = r4.hitCount;	 Catch:{ all -> 0x002a }
        r2 = r2 + 1;
        r4.hitCount = r2;	 Catch:{ all -> 0x002a }
        monitor-exit(r4);	 Catch:{ all -> 0x002a }
        goto L_0x0019;
    L_0x002a:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x002a }
        throw r2;
    L_0x002d:
        r3 = r4.missCount;	 Catch:{ all -> 0x002a }
        r3 = r3 + 1;
        r4.missCount = r3;	 Catch:{ all -> 0x002a }
        monitor-exit(r4);	 Catch:{ all -> 0x002a }
        r0 = r4.create(r5);
        if (r0 != 0) goto L_0x003c;
    L_0x003a:
        r1 = r2;
        goto L_0x0019;
    L_0x003c:
        monitor-enter(r4);
        r2 = r4.createCount;	 Catch:{ all -> 0x0062 }
        r2 = r2 + 1;
        r4.createCount = r2;	 Catch:{ all -> 0x0062 }
        r2 = r4.map;	 Catch:{ all -> 0x0062 }
        r1 = r2.put(r5, r0);	 Catch:{ all -> 0x0062 }
        if (r1 == 0) goto L_0x0058;
    L_0x004b:
        r2 = r4.map;	 Catch:{ all -> 0x0062 }
        r2.put(r5, r1);	 Catch:{ all -> 0x0062 }
    L_0x0050:
        monitor-exit(r4);	 Catch:{ all -> 0x0062 }
        if (r1 == 0) goto L_0x0065;
    L_0x0053:
        r2 = 0;
        r4.entryRemoved(r2, r5, r0, r1);
        goto L_0x0019;
    L_0x0058:
        r2 = r4.size;	 Catch:{ all -> 0x0062 }
        r3 = r4.safeSizeOf(r5, r0);	 Catch:{ all -> 0x0062 }
        r2 = r2 + r3;
        r4.size = r2;	 Catch:{ all -> 0x0062 }
        goto L_0x0050;
    L_0x0062:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0062 }
        throw r2;
    L_0x0065:
        r2 = r4.maxSize;
        r4.trimToSize(r2);
        r1 = r0;
        goto L_0x0019;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.lidroid.xutils.cache.LruMemoryCache.get(java.lang.Object):V");
    }

    public final V put(K key, V value) {
        return put(key, value, Long.MAX_VALUE);
    }

    public final V put(K key, V value, long expiryTimestamp) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }
        V previous;
        synchronized (this) {
            this.putCount++;
            this.size += safeSizeOf(key, value);
            previous = this.map.put(key, value);
            this.keyExpiryMap.put((Object) key, Long.valueOf(expiryTimestamp));
            if (previous != null) {
                this.size -= safeSizeOf(key, previous);
            }
        }
        if (previous != null) {
            entryRemoved(false, key, previous, value);
        }
        trimToSize(this.maxSize);
        return previous;
    }

    private void trimToSize(int maxSize) {
        while (true) {
            Object key;
            V value;
            synchronized (this) {
                if (this.size <= maxSize || this.map.isEmpty()) {
                } else {
                    Entry<K, V> toEvict = (Entry) this.map.entrySet().iterator().next();
                    key = toEvict.getKey();
                    value = toEvict.getValue();
                    this.map.remove(key);
                    this.keyExpiryMap.remove(key);
                    this.size -= safeSizeOf(key, value);
                    this.evictionCount++;
                }
            }
            entryRemoved(true, key, value, null);
        }
    }

    public final V remove(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        V previous;
        synchronized (this) {
            previous = this.map.remove(key);
            this.keyExpiryMap.remove((Object) key);
            if (previous != null) {
                this.size -= safeSizeOf(key, previous);
            }
        }
        if (previous != null) {
            entryRemoved(false, key, previous, null);
        }
        return previous;
    }

    public final boolean containsKey(K key) {
        return this.map.containsKey(key);
    }

    protected void entryRemoved(boolean evicted, K k, V v, V v2) {
    }

    protected V create(K k) {
        return null;
    }

    private int safeSizeOf(K key, V value) {
        int result = sizeOf(key, value);
        if (result <= 0) {
            this.size = 0;
            for (Entry<K, V> entry : this.map.entrySet()) {
                this.size += sizeOf(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    protected int sizeOf(K k, V v) {
        return 1;
    }

    public final void evictAll() {
        trimToSize(-1);
        this.keyExpiryMap.clear();
    }

    public final synchronized int size() {
        return this.size;
    }

    public final synchronized int maxSize() {
        return this.maxSize;
    }

    public final synchronized int hitCount() {
        return this.hitCount;
    }

    public final synchronized int missCount() {
        return this.missCount;
    }

    public final synchronized int createCount() {
        return this.createCount;
    }

    public final synchronized int putCount() {
        return this.putCount;
    }

    public final synchronized int evictionCount() {
        return this.evictionCount;
    }

    public final synchronized Map<K, V> snapshot() {
        return new LinkedHashMap(this.map);
    }

    public final synchronized String toString() {
        String format;
        int hitPercent = 0;
        synchronized (this) {
            int accesses = this.hitCount + this.missCount;
            if (accesses != 0) {
                hitPercent = (this.hitCount * 100) / accesses;
            }
            format = String.format("LruMemoryCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", new Object[]{Integer.valueOf(this.maxSize), Integer.valueOf(this.hitCount), Integer.valueOf(this.missCount), Integer.valueOf(hitPercent)});
        }
        return format;
    }
}
