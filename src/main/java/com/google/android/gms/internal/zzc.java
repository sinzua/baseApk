package com.google.android.gms.internal;

import android.os.Process;
import com.google.android.gms.internal.zzb.zza;
import java.util.concurrent.BlockingQueue;

public class zzc extends Thread {
    private static final boolean DEBUG = zzs.DEBUG;
    private final BlockingQueue<zzk<?>> zzh;
    private final BlockingQueue<zzk<?>> zzi;
    private final zzb zzj;
    private final zzn zzk;
    private volatile boolean zzl = false;

    public zzc(BlockingQueue<zzk<?>> blockingQueue, BlockingQueue<zzk<?>> blockingQueue2, zzb com_google_android_gms_internal_zzb, zzn com_google_android_gms_internal_zzn) {
        this.zzh = blockingQueue;
        this.zzi = blockingQueue2;
        this.zzj = com_google_android_gms_internal_zzb;
        this.zzk = com_google_android_gms_internal_zzn;
    }

    public void quit() {
        this.zzl = true;
        interrupt();
    }

    public void run() {
        if (DEBUG) {
            zzs.zza("start new dispatcher", new Object[0]);
        }
        Process.setThreadPriority(10);
        this.zzj.zza();
        while (true) {
            try {
                final zzk com_google_android_gms_internal_zzk = (zzk) this.zzh.take();
                com_google_android_gms_internal_zzk.zzc("cache-queue-take");
                if (com_google_android_gms_internal_zzk.isCanceled()) {
                    com_google_android_gms_internal_zzk.zzd("cache-discard-canceled");
                } else {
                    zza zza = this.zzj.zza(com_google_android_gms_internal_zzk.zzh());
                    if (zza == null) {
                        com_google_android_gms_internal_zzk.zzc("cache-miss");
                        this.zzi.put(com_google_android_gms_internal_zzk);
                    } else if (zza.zzb()) {
                        com_google_android_gms_internal_zzk.zzc("cache-hit-expired");
                        com_google_android_gms_internal_zzk.zza(zza);
                        this.zzi.put(com_google_android_gms_internal_zzk);
                    } else {
                        com_google_android_gms_internal_zzk.zzc("cache-hit");
                        zzm zza2 = com_google_android_gms_internal_zzk.zza(new zzi(zza.data, zza.zzg));
                        com_google_android_gms_internal_zzk.zzc("cache-hit-parsed");
                        if (zza.zzc()) {
                            com_google_android_gms_internal_zzk.zzc("cache-hit-refresh-needed");
                            com_google_android_gms_internal_zzk.zza(zza);
                            zza2.zzai = true;
                            this.zzk.zza(com_google_android_gms_internal_zzk, zza2, new Runnable(this) {
                                final /* synthetic */ zzc zzn;

                                public void run() {
                                    try {
                                        this.zzn.zzi.put(com_google_android_gms_internal_zzk);
                                    } catch (InterruptedException e) {
                                    }
                                }
                            });
                        } else {
                            this.zzk.zza(com_google_android_gms_internal_zzk, zza2);
                        }
                    }
                }
            } catch (InterruptedException e) {
                if (this.zzl) {
                    return;
                }
            }
        }
    }
}
