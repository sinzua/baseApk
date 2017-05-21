package com.flurry.sdk;

import android.app.Activity;

public class jv extends ka {
    public Activity a;
    public a b;

    public enum a {
        kCreated,
        kDestroyed,
        kPaused,
        kResumed,
        kStarted,
        kStopped,
        kSaveState
    }

    public jv() {
        super("com.flurry.android.sdk.ActivityLifecycleEvent");
    }
}
