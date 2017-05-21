package com.google.android.gms.common.api.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.TransformedResult;
import com.google.android.gms.common.internal.zzq;
import com.google.android.gms.common.internal.zzx;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class zzb<R extends Result> extends PendingResult<R> {
    private boolean zzL;
    private final Object zzagI = new Object();
    protected final zza<R> zzagJ;
    private final WeakReference<GoogleApiClient> zzagK;
    private final ArrayList<com.google.android.gms.common.api.PendingResult.zza> zzagL = new ArrayList();
    private ResultCallback<? super R> zzagM;
    private volatile boolean zzagN;
    private boolean zzagO;
    private boolean zzagP;
    private zzq zzagQ;
    private Integer zzagR;
    private volatile zzx<R> zzagS;
    private volatile R zzagy;
    private final CountDownLatch zzpJ = new CountDownLatch(1);

    public static class zza<R extends Result> extends Handler {
        public zza() {
            this(Looper.getMainLooper());
        }

        public zza(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Pair pair = (Pair) msg.obj;
                    zzb((ResultCallback) pair.first, (Result) pair.second);
                    return;
                case 2:
                    ((zzb) msg.obj).zzx(Status.zzagF);
                    return;
                default:
                    Log.wtf("BasePendingResult", "Don't know how to handle message: " + msg.what, new Exception());
                    return;
            }
        }

        public void zza(ResultCallback<? super R> resultCallback, R r) {
            sendMessage(obtainMessage(1, new Pair(resultCallback, r)));
        }

        public void zza(zzb<R> com_google_android_gms_common_api_internal_zzb_R, long j) {
            sendMessageDelayed(obtainMessage(2, com_google_android_gms_common_api_internal_zzb_R), j);
        }

        protected void zzb(ResultCallback<? super R> resultCallback, R r) {
            try {
                resultCallback.onResult(r);
            } catch (RuntimeException e) {
                zzb.zzc((Result) r);
                throw e;
            }
        }

        public void zzph() {
            removeMessages(2);
        }
    }

    @Deprecated
    protected zzb(Looper looper) {
        this.zzagJ = new zza(looper);
        this.zzagK = new WeakReference(null);
    }

    protected zzb(GoogleApiClient googleApiClient) {
        this.zzagJ = new zza(googleApiClient != null ? googleApiClient.getLooper() : Looper.getMainLooper());
        this.zzagK = new WeakReference(googleApiClient);
    }

    private R get() {
        R r;
        boolean z = true;
        synchronized (this.zzagI) {
            if (this.zzagN) {
                z = false;
            }
            zzx.zza(z, (Object) "Result has already been consumed.");
            zzx.zza(isReady(), (Object) "Result is not ready.");
            r = this.zzagy;
            this.zzagy = null;
            this.zzagM = null;
            this.zzagN = true;
        }
        zzpf();
        return r;
    }

    private void zzb(R r) {
        this.zzagy = r;
        this.zzagQ = null;
        this.zzpJ.countDown();
        Status status = this.zzagy.getStatus();
        if (this.zzagM != null) {
            this.zzagJ.zzph();
            if (!this.zzL) {
                this.zzagJ.zza(this.zzagM, get());
            }
        }
        Iterator it = this.zzagL.iterator();
        while (it.hasNext()) {
            ((com.google.android.gms.common.api.PendingResult.zza) it.next()).zzu(status);
        }
        this.zzagL.clear();
    }

    public static void zzc(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (Throwable e) {
                Log.w("BasePendingResult", "Unable to release " + result, e);
            }
        }
    }

    public final R await() {
        boolean z = true;
        zzx.zza(Looper.myLooper() != Looper.getMainLooper(), (Object) "await must not be called on the UI thread");
        zzx.zza(!this.zzagN, (Object) "Result has already been consumed");
        if (this.zzagS != null) {
            z = false;
        }
        zzx.zza(z, (Object) "Cannot await if then() has been called.");
        try {
            this.zzpJ.await();
        } catch (InterruptedException e) {
            zzx(Status.zzagD);
        }
        zzx.zza(isReady(), (Object) "Result is not ready.");
        return get();
    }

    public final R await(long time, TimeUnit units) {
        boolean z = true;
        boolean z2 = time <= 0 || Looper.myLooper() != Looper.getMainLooper();
        zzx.zza(z2, (Object) "await must not be called on the UI thread when time is greater than zero.");
        zzx.zza(!this.zzagN, (Object) "Result has already been consumed.");
        if (this.zzagS != null) {
            z = false;
        }
        zzx.zza(z, (Object) "Cannot await if then() has been called.");
        try {
            if (!this.zzpJ.await(time, units)) {
                zzx(Status.zzagF);
            }
        } catch (InterruptedException e) {
            zzx(Status.zzagD);
        }
        zzx.zza(isReady(), (Object) "Result is not ready.");
        return get();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void cancel() {
        /*
        r2 = this;
        r1 = r2.zzagI;
        monitor-enter(r1);
        r0 = r2.zzL;	 Catch:{ all -> 0x002c }
        if (r0 != 0) goto L_0x000b;
    L_0x0007:
        r0 = r2.zzagN;	 Catch:{ all -> 0x002c }
        if (r0 == 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r1);	 Catch:{ all -> 0x002c }
    L_0x000c:
        return;
    L_0x000d:
        r0 = r2.zzagQ;	 Catch:{ all -> 0x002c }
        if (r0 == 0) goto L_0x0016;
    L_0x0011:
        r0 = r2.zzagQ;	 Catch:{ RemoteException -> 0x002f }
        r0.cancel();	 Catch:{ RemoteException -> 0x002f }
    L_0x0016:
        r0 = r2.zzagy;	 Catch:{ all -> 0x002c }
        zzc(r0);	 Catch:{ all -> 0x002c }
        r0 = 0;
        r2.zzagM = r0;	 Catch:{ all -> 0x002c }
        r0 = 1;
        r2.zzL = r0;	 Catch:{ all -> 0x002c }
        r0 = com.google.android.gms.common.api.Status.zzagG;	 Catch:{ all -> 0x002c }
        r0 = r2.zzc(r0);	 Catch:{ all -> 0x002c }
        r2.zzb(r0);	 Catch:{ all -> 0x002c }
        monitor-exit(r1);	 Catch:{ all -> 0x002c }
        goto L_0x000c;
    L_0x002c:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x002c }
        throw r0;
    L_0x002f:
        r0 = move-exception;
        goto L_0x0016;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zzb.cancel():void");
    }

    public boolean isCanceled() {
        boolean z;
        synchronized (this.zzagI) {
            z = this.zzL;
        }
        return z;
    }

    public final boolean isReady() {
        return this.zzpJ.getCount() == 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setResultCallback(com.google.android.gms.common.api.ResultCallback<? super R> r5) {
        /*
        r4 = this;
        r1 = 1;
        r2 = 0;
        r0 = r4.zzagN;
        if (r0 != 0) goto L_0x0020;
    L_0x0006:
        r0 = r1;
    L_0x0007:
        r3 = "Result has already been consumed.";
        com.google.android.gms.common.internal.zzx.zza(r0, r3);
        r3 = r4.zzagI;
        monitor-enter(r3);
        r0 = r4.zzagS;	 Catch:{ all -> 0x003b }
        if (r0 != 0) goto L_0x0022;
    L_0x0013:
        r0 = "Cannot set callbacks if then() has been called.";
        com.google.android.gms.common.internal.zzx.zza(r1, r0);	 Catch:{ all -> 0x003b }
        r0 = r4.isCanceled();	 Catch:{ all -> 0x003b }
        if (r0 == 0) goto L_0x0024;
    L_0x001e:
        monitor-exit(r3);	 Catch:{ all -> 0x003b }
    L_0x001f:
        return;
    L_0x0020:
        r0 = r2;
        goto L_0x0007;
    L_0x0022:
        r1 = r2;
        goto L_0x0013;
    L_0x0024:
        r0 = r4.zzagP;	 Catch:{ all -> 0x003b }
        if (r0 == 0) goto L_0x003e;
    L_0x0028:
        r0 = r4.zzagK;	 Catch:{ all -> 0x003b }
        r0 = r0.get();	 Catch:{ all -> 0x003b }
        r0 = (com.google.android.gms.common.api.GoogleApiClient) r0;	 Catch:{ all -> 0x003b }
        if (r0 == 0) goto L_0x0036;
    L_0x0032:
        r0 = r5 instanceof com.google.android.gms.common.api.internal.zzx;	 Catch:{ all -> 0x003b }
        if (r0 != 0) goto L_0x003e;
    L_0x0036:
        r4.cancel();	 Catch:{ all -> 0x003b }
        monitor-exit(r3);	 Catch:{ all -> 0x003b }
        goto L_0x001f;
    L_0x003b:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x003b }
        throw r0;
    L_0x003e:
        r0 = r4.isReady();	 Catch:{ all -> 0x003b }
        if (r0 == 0) goto L_0x004f;
    L_0x0044:
        r0 = r4.zzagJ;	 Catch:{ all -> 0x003b }
        r1 = r4.get();	 Catch:{ all -> 0x003b }
        r0.zza(r5, r1);	 Catch:{ all -> 0x003b }
    L_0x004d:
        monitor-exit(r3);	 Catch:{ all -> 0x003b }
        goto L_0x001f;
    L_0x004f:
        r4.zzagM = r5;	 Catch:{ all -> 0x003b }
        goto L_0x004d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zzb.setResultCallback(com.google.android.gms.common.api.ResultCallback):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setResultCallback(com.google.android.gms.common.api.ResultCallback<? super R> r7, long r8, java.util.concurrent.TimeUnit r10) {
        /*
        r6 = this;
        r1 = 1;
        r2 = 0;
        r0 = r6.zzagN;
        if (r0 != 0) goto L_0x0020;
    L_0x0006:
        r0 = r1;
    L_0x0007:
        r3 = "Result has already been consumed.";
        com.google.android.gms.common.internal.zzx.zza(r0, r3);
        r3 = r6.zzagI;
        monitor-enter(r3);
        r0 = r6.zzagS;	 Catch:{ all -> 0x003b }
        if (r0 != 0) goto L_0x0022;
    L_0x0013:
        r0 = "Cannot set callbacks if then() has been called.";
        com.google.android.gms.common.internal.zzx.zza(r1, r0);	 Catch:{ all -> 0x003b }
        r0 = r6.isCanceled();	 Catch:{ all -> 0x003b }
        if (r0 == 0) goto L_0x0024;
    L_0x001e:
        monitor-exit(r3);	 Catch:{ all -> 0x003b }
    L_0x001f:
        return;
    L_0x0020:
        r0 = r2;
        goto L_0x0007;
    L_0x0022:
        r1 = r2;
        goto L_0x0013;
    L_0x0024:
        r0 = r6.zzagP;	 Catch:{ all -> 0x003b }
        if (r0 == 0) goto L_0x003e;
    L_0x0028:
        r0 = r6.zzagK;	 Catch:{ all -> 0x003b }
        r0 = r0.get();	 Catch:{ all -> 0x003b }
        r0 = (com.google.android.gms.common.api.GoogleApiClient) r0;	 Catch:{ all -> 0x003b }
        if (r0 == 0) goto L_0x0036;
    L_0x0032:
        r0 = r7 instanceof com.google.android.gms.common.api.internal.zzx;	 Catch:{ all -> 0x003b }
        if (r0 != 0) goto L_0x003e;
    L_0x0036:
        r6.cancel();	 Catch:{ all -> 0x003b }
        monitor-exit(r3);	 Catch:{ all -> 0x003b }
        goto L_0x001f;
    L_0x003b:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x003b }
        throw r0;
    L_0x003e:
        r0 = r6.isReady();	 Catch:{ all -> 0x003b }
        if (r0 == 0) goto L_0x004f;
    L_0x0044:
        r0 = r6.zzagJ;	 Catch:{ all -> 0x003b }
        r1 = r6.get();	 Catch:{ all -> 0x003b }
        r0.zza(r7, r1);	 Catch:{ all -> 0x003b }
    L_0x004d:
        monitor-exit(r3);	 Catch:{ all -> 0x003b }
        goto L_0x001f;
    L_0x004f:
        r6.zzagM = r7;	 Catch:{ all -> 0x003b }
        r0 = r6.zzagJ;	 Catch:{ all -> 0x003b }
        r4 = r10.toMillis(r8);	 Catch:{ all -> 0x003b }
        r0.zza(r6, r4);	 Catch:{ all -> 0x003b }
        goto L_0x004d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zzb.setResultCallback(com.google.android.gms.common.api.ResultCallback, long, java.util.concurrent.TimeUnit):void");
    }

    public <S extends Result> TransformedResult<S> then(ResultTransform<? super R, ? extends S> transform) {
        TransformedResult<S> then;
        boolean z = true;
        zzx.zza(!this.zzagN, (Object) "Result has already been consumed.");
        synchronized (this.zzagI) {
            zzx.zza(this.zzagS == null, (Object) "Cannot call then() twice.");
            if (this.zzagM != null) {
                z = false;
            }
            zzx.zza(z, (Object) "Cannot call then() if callbacks are set.");
            this.zzagS = new zzx(this.zzagK);
            then = this.zzagS.then(transform);
            if (isReady()) {
                this.zzagJ.zza(this.zzagS, get());
            } else {
                this.zzagM = this.zzagS;
            }
        }
        return then;
    }

    public final void zza(com.google.android.gms.common.api.PendingResult.zza com_google_android_gms_common_api_PendingResult_zza) {
        boolean z = true;
        zzx.zza(!this.zzagN, (Object) "Result has already been consumed.");
        if (com_google_android_gms_common_api_PendingResult_zza == null) {
            z = false;
        }
        zzx.zzb(z, (Object) "Callback cannot be null.");
        synchronized (this.zzagI) {
            if (isReady()) {
                com_google_android_gms_common_api_PendingResult_zza.zzu(this.zzagy.getStatus());
            } else {
                this.zzagL.add(com_google_android_gms_common_api_PendingResult_zza);
            }
        }
    }

    public final void zza(R r) {
        boolean z = true;
        synchronized (this.zzagI) {
            if (this.zzagO || this.zzL) {
                zzc((Result) r);
                return;
            }
            zzx.zza(!isReady(), (Object) "Results have already been set");
            if (this.zzagN) {
                z = false;
            }
            zzx.zza(z, (Object) "Result has already been consumed");
            zzb(r);
        }
    }

    protected final void zza(zzq com_google_android_gms_common_internal_zzq) {
        synchronized (this.zzagI) {
            this.zzagQ = com_google_android_gms_common_internal_zzq;
        }
    }

    protected abstract R zzc(Status status);

    public Integer zzpa() {
        return this.zzagR;
    }

    protected void zzpf() {
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void zzpg() {
        /*
        r2 = this;
        r1 = r2.zzagI;
        monitor-enter(r1);
        r0 = r2.zzagK;	 Catch:{ all -> 0x0021 }
        r0 = r0.get();	 Catch:{ all -> 0x0021 }
        r0 = (com.google.android.gms.common.api.GoogleApiClient) r0;	 Catch:{ all -> 0x0021 }
        if (r0 != 0) goto L_0x0012;
    L_0x000d:
        r2.cancel();	 Catch:{ all -> 0x0021 }
        monitor-exit(r1);	 Catch:{ all -> 0x0021 }
    L_0x0011:
        return;
    L_0x0012:
        r0 = r2.zzagM;	 Catch:{ all -> 0x0021 }
        if (r0 == 0) goto L_0x001c;
    L_0x0016:
        r0 = r2.zzagM;	 Catch:{ all -> 0x0021 }
        r0 = r0 instanceof com.google.android.gms.common.api.internal.zzx;	 Catch:{ all -> 0x0021 }
        if (r0 == 0) goto L_0x0024;
    L_0x001c:
        r0 = 1;
        r2.zzagP = r0;	 Catch:{ all -> 0x0021 }
    L_0x001f:
        monitor-exit(r1);	 Catch:{ all -> 0x0021 }
        goto L_0x0011;
    L_0x0021:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0021 }
        throw r0;
    L_0x0024:
        r2.cancel();	 Catch:{ all -> 0x0021 }
        goto L_0x001f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zzb.zzpg():void");
    }

    public final void zzx(Status status) {
        synchronized (this.zzagI) {
            if (!isReady()) {
                zza(zzc(status));
                this.zzagO = true;
            }
        }
    }
}
