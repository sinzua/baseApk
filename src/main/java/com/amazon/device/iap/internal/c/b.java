package com.amazon.device.iap.internal.c;

import com.amazon.device.iap.internal.util.d;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/* compiled from: PurchaseRequestTracker */
public class b {
    private static final b b = new b();
    private final Set<String> a = new ConcurrentSkipListSet();

    public boolean a(String str) {
        if (d.a(str)) {
            return false;
        }
        return this.a.remove(str);
    }

    public void b(String str) {
        if (!d.a(str)) {
            this.a.add(str);
        }
    }

    public static b a() {
        return b;
    }
}
