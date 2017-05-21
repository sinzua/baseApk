package com.flurry.sdk;

import android.text.TextUtils;

public abstract class ka {
    protected String g = "com.flurry.android.sdk.ReplaceMeWithAProperEventName";

    public ka(String str) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Event must have a name!");
        }
        this.g = str;
    }

    public String a() {
        return this.g;
    }

    public void b() {
        kc.a().a(this);
    }
}
