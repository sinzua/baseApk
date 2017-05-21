package com.google.android.gms.internal;

import java.util.concurrent.TimeUnit;

@zzhb
public class zzje<T> implements zzjg<T> {
    private final T zzNc;
    private final zzjh zzNe = new zzjh();

    public zzje(T t) {
        this.zzNc = t;
        this.zzNe.zzhK();
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public T get() {
        return this.zzNc;
    }

    public T get(long timeout, TimeUnit unit) {
        return this.zzNc;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return true;
    }

    public void zzb(Runnable runnable) {
        this.zzNe.zzb(runnable);
    }
}
