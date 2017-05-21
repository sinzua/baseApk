package com.amazon.device.iap.internal.b;

import java.util.HashMap;
import java.util.Map;

/* compiled from: KiwiRequestContext */
public class h {
    static final /* synthetic */ boolean b = (!h.class.desiredAssertionStatus());
    public final Map<String, Object> a = new HashMap();

    public Object a(String str) {
        return this.a.get(str);
    }

    public void a(String str, Object obj) {
        this.a.put(str, obj);
    }

    public void a(Object obj) {
        if (b || obj != null) {
            this.a.put("RESPONSE", obj);
            return;
        }
        throw new AssertionError();
    }

    public Object a() {
        return this.a.get("RESPONSE");
    }

    public void b() {
        this.a.remove("RESPONSE");
    }
}
