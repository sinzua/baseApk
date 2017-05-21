package com.flurry.sdk;

import android.content.Context;
import java.lang.ref.WeakReference;

public class lg extends ka {
    public WeakReference<Context> a;
    public lf b;
    public a c;
    public long d;

    public enum a {
        CREATE,
        SESSION_ID_CREATED,
        START,
        END,
        FINALIZE
    }

    public lg() {
        super("com.flurry.android.sdk.FlurrySessionEvent");
    }
}
