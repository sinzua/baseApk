package com.flurry.sdk;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class kq<T> extends WeakReference<T> {
    public kq(T t) {
        super(t);
    }

    public boolean equals(Object obj) {
        Object obj2 = get();
        if (obj instanceof Reference) {
            return obj2.equals(((Reference) obj).get());
        }
        return obj2.equals(obj);
    }

    public int hashCode() {
        Object obj = get();
        if (obj == null) {
            return super.hashCode();
        }
        return obj.hashCode();
    }
}
