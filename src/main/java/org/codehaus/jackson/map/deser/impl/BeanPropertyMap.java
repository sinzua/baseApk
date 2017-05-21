package org.codehaus.jackson.map.deser.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.codehaus.jackson.map.deser.SettableBeanProperty;

public final class BeanPropertyMap {
    private final Bucket[] _buckets;
    private final int _hashMask;
    private final int _size;

    private static final class Bucket {
        public final String key;
        public final Bucket next;
        public final SettableBeanProperty value;

        public Bucket(Bucket next, String key, SettableBeanProperty value) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }

    private static final class IteratorImpl implements Iterator<SettableBeanProperty> {
        private final Bucket[] _buckets;
        private Bucket _currentBucket;
        private int _nextBucketIndex;

        public IteratorImpl(Bucket[] buckets) {
            int i;
            this._buckets = buckets;
            int len = this._buckets.length;
            int i2 = 0;
            while (i2 < len) {
                i = i2 + 1;
                Bucket b = this._buckets[i2];
                if (b != null) {
                    this._currentBucket = b;
                    break;
                }
                i2 = i;
            }
            i = i2;
            this._nextBucketIndex = i;
        }

        public boolean hasNext() {
            return this._currentBucket != null;
        }

        public SettableBeanProperty next() {
            Bucket curr = this._currentBucket;
            if (curr == null) {
                throw new NoSuchElementException();
            }
            Bucket b = curr.next;
            while (b == null && this._nextBucketIndex < this._buckets.length) {
                Bucket[] bucketArr = this._buckets;
                int i = this._nextBucketIndex;
                this._nextBucketIndex = i + 1;
                b = bucketArr[i];
            }
            this._currentBucket = b;
            return curr.value;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public BeanPropertyMap(Collection<SettableBeanProperty> properties) {
        this._size = properties.size();
        int bucketCount = findSize(this._size);
        this._hashMask = bucketCount - 1;
        Bucket[] buckets = new Bucket[bucketCount];
        for (SettableBeanProperty property : properties) {
            String key = property.getName();
            int index = key.hashCode() & this._hashMask;
            buckets[index] = new Bucket(buckets[index], key, property);
        }
        this._buckets = buckets;
    }

    public void assignIndexes() {
        int index = 0;
        Bucket[] arr$ = this._buckets;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Bucket bucket = arr$[i$];
            int index2 = index;
            while (bucket != null) {
                index = index2 + 1;
                bucket.value.assignIndex(index2);
                bucket = bucket.next;
                index2 = index;
            }
            i$++;
            index = index2;
        }
    }

    private static final int findSize(int size) {
        int result = 2;
        while (result < (size <= 32 ? size + size : size + (size >> 2))) {
            result += result;
        }
        return result;
    }

    public int size() {
        return this._size;
    }

    public Iterator<SettableBeanProperty> allProperties() {
        return new IteratorImpl(this._buckets);
    }

    public SettableBeanProperty find(String key) {
        int index = key.hashCode() & this._hashMask;
        Bucket bucket = this._buckets[index];
        if (bucket == null) {
            return null;
        }
        if (bucket.key == key) {
            return bucket.value;
        }
        do {
            bucket = bucket.next;
            if (bucket == null) {
                return _findWithEquals(key, index);
            }
        } while (bucket.key != key);
        return bucket.value;
    }

    public void replace(SettableBeanProperty property) {
        String name = property.getName();
        int index = name.hashCode() & (this._buckets.length - 1);
        boolean found = false;
        Bucket bucket = this._buckets[index];
        Bucket tail = null;
        while (bucket != null) {
            Bucket tail2;
            if (found || !bucket.key.equals(name)) {
                tail2 = new Bucket(tail, bucket.key, bucket.value);
            } else {
                found = true;
                tail2 = tail;
            }
            bucket = bucket.next;
            tail = tail2;
        }
        if (found) {
            this._buckets[index] = new Bucket(tail, name, property);
            return;
        }
        throw new NoSuchElementException("No entry '" + property + "' found, can't replace");
    }

    public void remove(SettableBeanProperty property) {
        String name = property.getName();
        int index = name.hashCode() & (this._buckets.length - 1);
        boolean found = false;
        Bucket bucket = this._buckets[index];
        Bucket tail = null;
        while (bucket != null) {
            Bucket tail2;
            if (found || !bucket.key.equals(name)) {
                tail2 = new Bucket(tail, bucket.key, bucket.value);
            } else {
                found = true;
                tail2 = tail;
            }
            bucket = bucket.next;
            tail = tail2;
        }
        if (found) {
            this._buckets[index] = tail;
            return;
        }
        throw new NoSuchElementException("No entry '" + property + "' found, can't remove");
    }

    private SettableBeanProperty _findWithEquals(String key, int index) {
        for (Bucket bucket = this._buckets[index]; bucket != null; bucket = bucket.next) {
            if (key.equals(bucket.key)) {
                return bucket.value;
            }
        }
        return null;
    }
}
