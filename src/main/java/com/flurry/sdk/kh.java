package com.flurry.sdk;

import android.os.Build.VERSION;

public final class kh {
    private final Class<? extends kj> a;
    private final int b;

    public kh(Class<? extends kj> cls, int i) {
        this.a = cls;
        this.b = i;
    }

    public Class<? extends kj> a() {
        return this.a;
    }

    public boolean b() {
        return this.a != null && VERSION.SDK_INT >= this.b;
    }
}
