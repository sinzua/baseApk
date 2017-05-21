package com.google.android.gms.internal;

import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.clearcut.LogEventParcelable;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class zzlv implements com.google.android.gms.clearcut.zzc {
    private static final Object zzafn = new Object();
    private static final zze zzafo = new zze();
    private static final long zzafp = TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES);
    private GoogleApiClient zzaaj;
    private final zza zzafq;
    private final Object zzafr;
    private long zzafs;
    private final long zzaft;
    private ScheduledFuture<?> zzafu;
    private final Runnable zzafv;
    private final zzmq zzqW;

    public interface zza {
    }

    private static final class zze {
        private int mSize;

        private zze() {
            this.mSize = 0;
        }

        public boolean zza(long j, TimeUnit timeUnit) throws InterruptedException {
            boolean z;
            long currentTimeMillis = System.currentTimeMillis();
            long convert = TimeUnit.MILLISECONDS.convert(j, timeUnit);
            synchronized (this) {
                while (this.mSize != 0) {
                    if (convert <= 0) {
                        z = false;
                        break;
                    }
                    wait(convert);
                    convert -= System.currentTimeMillis() - currentTimeMillis;
                }
                z = true;
            }
            return z;
        }

        public synchronized void zzoH() {
            this.mSize++;
        }

        public synchronized void zzoI() {
            if (this.mSize == 0) {
                throw new RuntimeException("too many decrements");
            }
            this.mSize--;
            if (this.mSize == 0) {
                notifyAll();
            }
        }
    }

    public static class zzb implements zza {
    }

    static abstract class zzc<R extends Result> extends com.google.android.gms.common.api.internal.zza.zza<R, zzlw> {
        public zzc(GoogleApiClient googleApiClient) {
            super(com.google.android.gms.clearcut.zzb.zzUI, googleApiClient);
        }
    }

    final class zzd extends zzc<Status> {
        final /* synthetic */ zzlv zzafw;
        private final LogEventParcelable zzafx;

        zzd(zzlv com_google_android_gms_internal_zzlv, LogEventParcelable logEventParcelable, GoogleApiClient googleApiClient) {
            this.zzafw = com_google_android_gms_internal_zzlv;
            super(googleApiClient);
            this.zzafx = logEventParcelable;
        }

        public boolean equals(Object rhs) {
            if (!(rhs instanceof zzd)) {
                return false;
            }
            return this.zzafx.equals(((zzd) rhs).zzafx);
        }

        public String toString() {
            return "MethodImpl(" + this.zzafx + ")";
        }

        protected void zza(zzlw com_google_android_gms_internal_zzlw) throws RemoteException {
            zzlx anonymousClass1 = new com.google.android.gms.internal.zzlx.zza(this) {
                final /* synthetic */ zzd zzafy;

                {
                    this.zzafy = r1;
                }

                public void zzv(Status status) {
                    this.zzafy.zza((Result) status);
                }
            };
            try {
                zzlv.zza(this.zzafx);
                com_google_android_gms_internal_zzlw.zza(anonymousClass1, this.zzafx);
            } catch (Throwable th) {
                Log.e("ClearcutLoggerApiImpl", "MessageNanoProducer " + this.zzafx.zzafl.toString() + " threw: " + th.toString());
            }
        }

        protected Status zzb(Status status) {
            return status;
        }

        protected /* synthetic */ Result zzc(Status status) {
            return zzb(status);
        }
    }

    public zzlv() {
        this(new zzmt(), zzafp, new zzb());
    }

    public zzlv(zzmq com_google_android_gms_internal_zzmq, long j, zza com_google_android_gms_internal_zzlv_zza) {
        this.zzafr = new Object();
        this.zzafs = 0;
        this.zzafu = null;
        this.zzaaj = null;
        this.zzafv = new Runnable(this) {
            final /* synthetic */ zzlv zzafw;

            {
                this.zzafw = r1;
            }

            public void run() {
                synchronized (this.zzafw.zzafr) {
                    if (this.zzafw.zzafs <= this.zzafw.zzqW.elapsedRealtime() && this.zzafw.zzaaj != null) {
                        Log.i("ClearcutLoggerApiImpl", "disconnect managed GoogleApiClient");
                        this.zzafw.zzaaj.disconnect();
                        this.zzafw.zzaaj = null;
                    }
                }
            }
        };
        this.zzqW = com_google_android_gms_internal_zzmq;
        this.zzaft = j;
        this.zzafq = com_google_android_gms_internal_zzlv_zza;
    }

    private static void zza(LogEventParcelable logEventParcelable) {
        if (logEventParcelable.zzafl != null && logEventParcelable.zzafk.zzbuY.length == 0) {
            logEventParcelable.zzafk.zzbuY = logEventParcelable.zzafl.zzoF();
        }
        if (logEventParcelable.zzafm != null && logEventParcelable.zzafk.zzbvf.length == 0) {
            logEventParcelable.zzafk.zzbvf = logEventParcelable.zzafm.zzoF();
        }
        logEventParcelable.zzafi = zzsu.toByteArray(logEventParcelable.zzafk);
    }

    private zzd zzb(GoogleApiClient googleApiClient, LogEventParcelable logEventParcelable) {
        zzafo.zzoH();
        zzd com_google_android_gms_internal_zzlv_zzd = new zzd(this, logEventParcelable, googleApiClient);
        com_google_android_gms_internal_zzlv_zzd.zza(new com.google.android.gms.common.api.PendingResult.zza(this) {
            final /* synthetic */ zzlv zzafw;

            {
                this.zzafw = r1;
            }

            public void zzu(Status status) {
                zzlv.zzafo.zzoI();
            }
        });
        return com_google_android_gms_internal_zzlv_zzd;
    }

    public PendingResult<Status> zza(GoogleApiClient googleApiClient, LogEventParcelable logEventParcelable) {
        zza(logEventParcelable);
        return googleApiClient.zza(zzb(googleApiClient, logEventParcelable));
    }

    public boolean zza(GoogleApiClient googleApiClient, long j, TimeUnit timeUnit) {
        try {
            return zzafo.zza(j, timeUnit);
        } catch (InterruptedException e) {
            Log.e("ClearcutLoggerApiImpl", "flush interrupted");
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
