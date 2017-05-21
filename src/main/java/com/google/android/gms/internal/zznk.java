package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzx;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class zznk implements ThreadFactory {
    private final int mPriority;
    private final String zzaoq;
    private final AtomicInteger zzaor;
    private final ThreadFactory zzaos;

    public zznk(String str) {
        this(str, 0);
    }

    public zznk(String str, int i) {
        this.zzaor = new AtomicInteger();
        this.zzaos = Executors.defaultThreadFactory();
        this.zzaoq = (String) zzx.zzb((Object) str, (Object) "Name must not be null");
        this.mPriority = i;
    }

    public Thread newThread(Runnable runnable) {
        Thread newThread = this.zzaos.newThread(new zznl(runnable, this.mPriority));
        newThread.setName(this.zzaoq + RequestParameters.LEFT_BRACKETS + this.zzaor.getAndIncrement() + RequestParameters.RIGHT_BRACKETS);
        return newThread;
    }
}
