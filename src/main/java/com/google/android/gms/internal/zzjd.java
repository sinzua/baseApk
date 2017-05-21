package com.google.android.gms.internal;

import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@zzhb
public class zzjd<T> implements zzjg<T> {
    private boolean zzCy = false;
    private T zzNc = null;
    private boolean zzNd = false;
    private final zzjh zzNe = new zzjh();
    private final Object zzpV = new Object();

    public boolean cancel(boolean mayInterruptIfRunning) {
        if (!mayInterruptIfRunning) {
            return false;
        }
        synchronized (this.zzpV) {
            if (this.zzNd) {
                return false;
            }
            this.zzCy = true;
            this.zzNd = true;
            this.zzpV.notifyAll();
            this.zzNe.zzhK();
            return true;
        }
    }

    public T get() {
        T t;
        synchronized (this.zzpV) {
            if (!this.zzNd) {
                try {
                    this.zzpV.wait();
                } catch (InterruptedException e) {
                }
            }
            if (this.zzCy) {
                throw new CancellationException("CallbackFuture was cancelled.");
            }
            t = this.zzNc;
        }
        return t;
    }

    public T get(long timeout, TimeUnit unit) throws TimeoutException {
        T t;
        synchronized (this.zzpV) {
            if (!this.zzNd) {
                try {
                    long toMillis = unit.toMillis(timeout);
                    if (toMillis != 0) {
                        this.zzpV.wait(toMillis);
                    }
                } catch (InterruptedException e) {
                }
            }
            if (!this.zzNd) {
                throw new TimeoutException("CallbackFuture timed out.");
            } else if (this.zzCy) {
                throw new CancellationException("CallbackFuture was cancelled.");
            } else {
                t = this.zzNc;
            }
        }
        return t;
    }

    public boolean isCancelled() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzCy;
        }
        return z;
    }

    public boolean isDone() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzNd;
        }
        return z;
    }

    public void zzb(Runnable runnable) {
        this.zzNe.zzb(runnable);
    }

    public void zzc(Runnable runnable) {
        this.zzNe.zzc(runnable);
    }

    public void zzg(T t) {
        synchronized (this.zzpV) {
            if (this.zzCy) {
            } else if (this.zzNd) {
                throw new IllegalStateException("Provided CallbackFuture with multiple values.");
            } else {
                this.zzNd = true;
                this.zzNc = t;
                this.zzpV.notifyAll();
                this.zzNe.zzhK();
            }
        }
    }
}
