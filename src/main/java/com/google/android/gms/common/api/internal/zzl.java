package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.zzc;
import com.google.android.gms.internal.zzrn;
import com.google.android.gms.internal.zzro;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class zzl implements zzp {
    private final Context mContext;
    private final Lock zzXG;
    final zzj zzagW;
    private final zzc zzags;
    final com.google.android.gms.common.api.Api.zza<? extends zzrn, zzro> zzagt;
    final Map<Api<?>, Integer> zzahA;
    final Map<Api.zzc<?>, com.google.android.gms.common.api.Api.zzb> zzahT;
    final zzf zzahz;
    private final Condition zzaim;
    private final zzb zzain;
    final Map<Api.zzc<?>, ConnectionResult> zzaio = new HashMap();
    private volatile zzk zzaip;
    private ConnectionResult zzaiq = null;
    int zzair;
    final com.google.android.gms.common.api.internal.zzp.zza zzais;

    static abstract class zza {
        private final zzk zzait;

        protected zza(zzk com_google_android_gms_common_api_internal_zzk) {
            this.zzait = com_google_android_gms_common_api_internal_zzk;
        }

        public final void zzd(zzl com_google_android_gms_common_api_internal_zzl) {
            com_google_android_gms_common_api_internal_zzl.zzXG.lock();
            try {
                if (com_google_android_gms_common_api_internal_zzl.zzaip == this.zzait) {
                    zzpt();
                    com_google_android_gms_common_api_internal_zzl.zzXG.unlock();
                }
            } finally {
                com_google_android_gms_common_api_internal_zzl.zzXG.unlock();
            }
        }

        protected abstract void zzpt();
    }

    final class zzb extends Handler {
        final /* synthetic */ zzl zzaiu;

        zzb(zzl com_google_android_gms_common_api_internal_zzl, Looper looper) {
            this.zzaiu = com_google_android_gms_common_api_internal_zzl;
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ((zza) msg.obj).zzd(this.zzaiu);
                    return;
                case 2:
                    throw ((RuntimeException) msg.obj);
                default:
                    Log.w("GACStateManager", "Unknown message id: " + msg.what);
                    return;
            }
        }
    }

    public zzl(Context context, zzj com_google_android_gms_common_api_internal_zzj, Lock lock, Looper looper, zzc com_google_android_gms_common_zzc, Map<Api.zzc<?>, com.google.android.gms.common.api.Api.zzb> map, zzf com_google_android_gms_common_internal_zzf, Map<Api<?>, Integer> map2, com.google.android.gms.common.api.Api.zza<? extends zzrn, zzro> com_google_android_gms_common_api_Api_zza__extends_com_google_android_gms_internal_zzrn__com_google_android_gms_internal_zzro, ArrayList<zzc> arrayList, com.google.android.gms.common.api.internal.zzp.zza com_google_android_gms_common_api_internal_zzp_zza) {
        this.mContext = context;
        this.zzXG = lock;
        this.zzags = com_google_android_gms_common_zzc;
        this.zzahT = map;
        this.zzahz = com_google_android_gms_common_internal_zzf;
        this.zzahA = map2;
        this.zzagt = com_google_android_gms_common_api_Api_zza__extends_com_google_android_gms_internal_zzrn__com_google_android_gms_internal_zzro;
        this.zzagW = com_google_android_gms_common_api_internal_zzj;
        this.zzais = com_google_android_gms_common_api_internal_zzp_zza;
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((zzc) it.next()).zza(this);
        }
        this.zzain = new zzb(this, looper);
        this.zzaim = lock.newCondition();
        this.zzaip = new zzi(this);
    }

    public ConnectionResult blockingConnect() {
        connect();
        while (isConnecting()) {
            try {
                this.zzaim.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new ConnectionResult(15, null);
            }
        }
        return isConnected() ? ConnectionResult.zzafB : this.zzaiq != null ? this.zzaiq : new ConnectionResult(13, null);
    }

    public ConnectionResult blockingConnect(long timeout, TimeUnit unit) {
        connect();
        long toNanos = unit.toNanos(timeout);
        while (isConnecting()) {
            if (toNanos <= 0) {
                try {
                    disconnect();
                    return new ConnectionResult(14, null);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return new ConnectionResult(15, null);
                }
            }
            toNanos = this.zzaim.awaitNanos(toNanos);
        }
        if (isConnected()) {
            return ConnectionResult.zzafB;
        }
        return this.zzaiq != null ? this.zzaiq : new ConnectionResult(13, null);
    }

    public void connect() {
        this.zzaip.connect();
    }

    public boolean disconnect() {
        boolean disconnect = this.zzaip.disconnect();
        if (disconnect) {
            this.zzaio.clear();
        }
        return disconnect;
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        String str = prefix + "  ";
        for (Api api : this.zzahA.keySet()) {
            writer.append(prefix).append(api.getName()).println(":");
            ((com.google.android.gms.common.api.Api.zzb) this.zzahT.get(api.zzoR())).dump(str, fd, writer, args);
        }
    }

    @Nullable
    public ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        Api.zzc zzoR = api.zzoR();
        if (this.zzahT.containsKey(zzoR)) {
            if (((com.google.android.gms.common.api.Api.zzb) this.zzahT.get(zzoR)).isConnected()) {
                return ConnectionResult.zzafB;
            }
            if (this.zzaio.containsKey(zzoR)) {
                return (ConnectionResult) this.zzaio.get(zzoR);
            }
        }
        return null;
    }

    public boolean isConnected() {
        return this.zzaip instanceof zzg;
    }

    public boolean isConnecting() {
        return this.zzaip instanceof zzh;
    }

    public void onConnected(@Nullable Bundle connectionHint) {
        this.zzXG.lock();
        try {
            this.zzaip.onConnected(connectionHint);
        } finally {
            this.zzXG.unlock();
        }
    }

    public void onConnectionSuspended(int cause) {
        this.zzXG.lock();
        try {
            this.zzaip.onConnectionSuspended(cause);
        } finally {
            this.zzXG.unlock();
        }
    }

    public <A extends com.google.android.gms.common.api.Api.zzb, R extends Result, T extends com.google.android.gms.common.api.internal.zza.zza<R, A>> T zza(@NonNull T t) {
        return this.zzaip.zza(t);
    }

    public void zza(@NonNull ConnectionResult connectionResult, @NonNull Api<?> api, int i) {
        this.zzXG.lock();
        try {
            this.zzaip.zza(connectionResult, api, i);
        } finally {
            this.zzXG.unlock();
        }
    }

    void zza(zza com_google_android_gms_common_api_internal_zzl_zza) {
        this.zzain.sendMessage(this.zzain.obtainMessage(1, com_google_android_gms_common_api_internal_zzl_zza));
    }

    void zza(RuntimeException runtimeException) {
        this.zzain.sendMessage(this.zzain.obtainMessage(2, runtimeException));
    }

    public boolean zza(zzu com_google_android_gms_common_api_internal_zzu) {
        return false;
    }

    public <A extends com.google.android.gms.common.api.Api.zzb, T extends com.google.android.gms.common.api.internal.zza.zza<? extends Result, A>> T zzb(@NonNull T t) {
        return this.zzaip.zzb(t);
    }

    void zzh(ConnectionResult connectionResult) {
        this.zzXG.lock();
        try {
            this.zzaiq = connectionResult;
            this.zzaip = new zzi(this);
            this.zzaip.begin();
            this.zzaim.signalAll();
        } finally {
            this.zzXG.unlock();
        }
    }

    public void zzoW() {
    }

    void zzpK() {
        this.zzXG.lock();
        try {
            this.zzaip = new zzh(this, this.zzahz, this.zzahA, this.zzags, this.zzagt, this.zzXG, this.mContext);
            this.zzaip.begin();
            this.zzaim.signalAll();
        } finally {
            this.zzXG.unlock();
        }
    }

    void zzpL() {
        this.zzXG.lock();
        try {
            this.zzagW.zzpF();
            this.zzaip = new zzg(this);
            this.zzaip.begin();
            this.zzaim.signalAll();
        } finally {
            this.zzXG.unlock();
        }
    }

    void zzpM() {
        for (com.google.android.gms.common.api.Api.zzb disconnect : this.zzahT.values()) {
            disconnect.disconnect();
        }
    }

    public void zzpj() {
        if (isConnected()) {
            ((zzg) this.zzaip).zzps();
        }
    }
}
