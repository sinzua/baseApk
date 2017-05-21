package com.amazon.device.iap.internal.a;

import com.amazon.device.iap.internal.a;
import com.amazon.device.iap.internal.b;
import com.amazon.device.iap.internal.c;
import java.util.HashMap;
import java.util.Map;

/* compiled from: SandboxImplementationRegistry */
public final class d implements b {
    private static final Map<Class, Class> a = new HashMap();

    static {
        a.put(c.class, c.class);
        a.put(a.class, a.class);
    }

    public <T> Class<T> a(Class<T> cls) {
        return (Class) a.get(cls);
    }
}
