package com.google.android.gms.internal;

import android.os.Process;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@zzhb
public final class zziq {
    private static final ExecutorService zzLU = Executors.newFixedThreadPool(10, zzaB("Default"));
    private static final ExecutorService zzLV = Executors.newFixedThreadPool(5, zzaB("Loader"));

    public static zzjg<Void> zza(int i, final Runnable runnable) {
        return i == 1 ? zza(zzLV, new Callable<Void>() {
            public /* synthetic */ Object call() throws Exception {
                return zzdt();
            }

            public Void zzdt() {
                runnable.run();
                return null;
            }
        }) : zza(zzLU, new Callable<Void>() {
            public /* synthetic */ Object call() throws Exception {
                return zzdt();
            }

            public Void zzdt() {
                runnable.run();
                return null;
            }
        });
    }

    public static zzjg<Void> zza(Runnable runnable) {
        return zza(0, runnable);
    }

    public static <T> zzjg<T> zza(Callable<T> callable) {
        return zza(zzLU, (Callable) callable);
    }

    public static <T> zzjg<T> zza(ExecutorService executorService, final Callable<T> callable) {
        final Object com_google_android_gms_internal_zzjd = new zzjd();
        try {
            final Future submit = executorService.submit(new Runnable() {
                public void run() {
                    try {
                        Process.setThreadPriority(10);
                        com_google_android_gms_internal_zzjd.zzg(callable.call());
                    } catch (Throwable e) {
                        zzr.zzbF().zzb(e, true);
                        com_google_android_gms_internal_zzjd.cancel(true);
                    }
                }
            });
            com_google_android_gms_internal_zzjd.zzc(new Runnable() {
                public void run() {
                    if (com_google_android_gms_internal_zzjd.isCancelled()) {
                        submit.cancel(true);
                    }
                }
            });
        } catch (Throwable e) {
            zzb.zzd("Thread execution is rejected.", e);
            com_google_android_gms_internal_zzjd.cancel(true);
        }
        return com_google_android_gms_internal_zzjd;
    }

    private static ThreadFactory zzaB(final String str) {
        return new ThreadFactory() {
            private final AtomicInteger zzMa = new AtomicInteger(1);

            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "AdWorker(" + str + ") #" + this.zzMa.getAndIncrement());
            }
        };
    }
}
