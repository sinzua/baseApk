package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.ResolveAccountResponse;
import com.google.android.gms.common.internal.zzp;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzrn;
import com.google.android.gms.internal.zzro;
import com.google.android.gms.signin.internal.SignInResponse;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;

public class zzh implements zzk {
    private final Context mContext;
    private final Lock zzXG;
    private final com.google.android.gms.common.zzc zzags;
    private final com.google.android.gms.common.api.Api.zza<? extends zzrn, zzro> zzagt;
    private final Map<Api<?>, Integer> zzahA;
    private ArrayList<Future<?>> zzahB = new ArrayList();
    private final zzl zzahj;
    private ConnectionResult zzahm;
    private int zzahn;
    private int zzaho = 0;
    private int zzahp;
    private final Bundle zzahq = new Bundle();
    private final Set<com.google.android.gms.common.api.Api.zzc> zzahr = new HashSet();
    private zzrn zzahs;
    private int zzaht;
    private boolean zzahu;
    private boolean zzahv;
    private zzp zzahw;
    private boolean zzahx;
    private boolean zzahy;
    private final com.google.android.gms.common.internal.zzf zzahz;

    private abstract class zzf implements Runnable {
        final /* synthetic */ zzh zzahC;

        private zzf(zzh com_google_android_gms_common_api_internal_zzh) {
            this.zzahC = com_google_android_gms_common_api_internal_zzh;
        }

        @WorkerThread
        public void run() {
            this.zzahC.zzXG.lock();
            try {
                if (!Thread.interrupted()) {
                    zzpt();
                    this.zzahC.zzXG.unlock();
                }
            } catch (RuntimeException e) {
                this.zzahC.zzahj.zza(e);
            } finally {
                this.zzahC.zzXG.unlock();
            }
        }

        @WorkerThread
        protected abstract void zzpt();
    }

    private static class zza implements com.google.android.gms.common.api.GoogleApiClient.zza {
        private final Api<?> zzagT;
        private final int zzagU;
        private final WeakReference<zzh> zzahD;

        public zza(zzh com_google_android_gms_common_api_internal_zzh, Api<?> api, int i) {
            this.zzahD = new WeakReference(com_google_android_gms_common_api_internal_zzh);
            this.zzagT = api;
            this.zzagU = i;
        }

        public void zza(@NonNull ConnectionResult connectionResult) {
            boolean z = false;
            zzh com_google_android_gms_common_api_internal_zzh = (zzh) this.zzahD.get();
            if (com_google_android_gms_common_api_internal_zzh != null) {
                if (Looper.myLooper() == com_google_android_gms_common_api_internal_zzh.zzahj.zzagW.getLooper()) {
                    z = true;
                }
                zzx.zza(z, (Object) "onReportServiceBinding must be called on the GoogleApiClient handler thread");
                com_google_android_gms_common_api_internal_zzh.zzXG.lock();
                try {
                    if (com_google_android_gms_common_api_internal_zzh.zzbz(0)) {
                        if (!connectionResult.isSuccess()) {
                            com_google_android_gms_common_api_internal_zzh.zzb(connectionResult, this.zzagT, this.zzagU);
                        }
                        if (com_google_android_gms_common_api_internal_zzh.zzpu()) {
                            com_google_android_gms_common_api_internal_zzh.zzpv();
                        }
                        com_google_android_gms_common_api_internal_zzh.zzXG.unlock();
                    }
                } finally {
                    com_google_android_gms_common_api_internal_zzh.zzXG.unlock();
                }
            }
        }
    }

    private class zzb extends zzf {
        final /* synthetic */ zzh zzahC;
        private final Map<com.google.android.gms.common.api.Api.zzb, com.google.android.gms.common.api.GoogleApiClient.zza> zzahE;

        public zzb(zzh com_google_android_gms_common_api_internal_zzh, Map<com.google.android.gms.common.api.Api.zzb, com.google.android.gms.common.api.GoogleApiClient.zza> map) {
            this.zzahC = com_google_android_gms_common_api_internal_zzh;
            super();
            this.zzahE = map;
        }

        @WorkerThread
        public void zzpt() {
            int isGooglePlayServicesAvailable = this.zzahC.zzags.isGooglePlayServicesAvailable(this.zzahC.mContext);
            if (isGooglePlayServicesAvailable != 0) {
                final ConnectionResult connectionResult = new ConnectionResult(isGooglePlayServicesAvailable, null);
                this.zzahC.zzahj.zza(new zza(this, this.zzahC) {
                    final /* synthetic */ zzb zzahG;

                    public void zzpt() {
                        this.zzahG.zzahC.zzg(connectionResult);
                    }
                });
                return;
            }
            if (this.zzahC.zzahu) {
                this.zzahC.zzahs.connect();
            }
            for (com.google.android.gms.common.api.Api.zzb com_google_android_gms_common_api_Api_zzb : this.zzahE.keySet()) {
                com_google_android_gms_common_api_Api_zzb.zza((com.google.android.gms.common.api.GoogleApiClient.zza) this.zzahE.get(com_google_android_gms_common_api_Api_zzb));
            }
        }
    }

    private class zzc extends zzf {
        final /* synthetic */ zzh zzahC;
        private final ArrayList<com.google.android.gms.common.api.Api.zzb> zzahH;

        public zzc(zzh com_google_android_gms_common_api_internal_zzh, ArrayList<com.google.android.gms.common.api.Api.zzb> arrayList) {
            this.zzahC = com_google_android_gms_common_api_internal_zzh;
            super();
            this.zzahH = arrayList;
        }

        @WorkerThread
        public void zzpt() {
            this.zzahC.zzahj.zzagW.zzahU = this.zzahC.zzpA();
            Iterator it = this.zzahH.iterator();
            while (it.hasNext()) {
                ((com.google.android.gms.common.api.Api.zzb) it.next()).zza(this.zzahC.zzahw, this.zzahC.zzahj.zzagW.zzahU);
            }
        }
    }

    private class zze implements ConnectionCallbacks, OnConnectionFailedListener {
        final /* synthetic */ zzh zzahC;

        private zze(zzh com_google_android_gms_common_api_internal_zzh) {
            this.zzahC = com_google_android_gms_common_api_internal_zzh;
        }

        public void onConnected(Bundle connectionHint) {
            this.zzahC.zzahs.zza(new zzd(this.zzahC));
        }

        public void onConnectionFailed(@NonNull ConnectionResult result) {
            this.zzahC.zzXG.lock();
            try {
                if (this.zzahC.zzf(result)) {
                    this.zzahC.zzpy();
                    this.zzahC.zzpv();
                } else {
                    this.zzahC.zzg(result);
                }
                this.zzahC.zzXG.unlock();
            } catch (Throwable th) {
                this.zzahC.zzXG.unlock();
            }
        }

        public void onConnectionSuspended(int cause) {
        }
    }

    private static class zzd extends com.google.android.gms.signin.internal.zzb {
        private final WeakReference<zzh> zzahD;

        zzd(zzh com_google_android_gms_common_api_internal_zzh) {
            this.zzahD = new WeakReference(com_google_android_gms_common_api_internal_zzh);
        }

        @BinderThread
        public void zzb(final SignInResponse signInResponse) {
            final zzh com_google_android_gms_common_api_internal_zzh = (zzh) this.zzahD.get();
            if (com_google_android_gms_common_api_internal_zzh != null) {
                com_google_android_gms_common_api_internal_zzh.zzahj.zza(new zza(this, com_google_android_gms_common_api_internal_zzh) {
                    final /* synthetic */ zzd zzahK;

                    public void zzpt() {
                        com_google_android_gms_common_api_internal_zzh.zza(signInResponse);
                    }
                });
            }
        }
    }

    public zzh(zzl com_google_android_gms_common_api_internal_zzl, com.google.android.gms.common.internal.zzf com_google_android_gms_common_internal_zzf, Map<Api<?>, Integer> map, com.google.android.gms.common.zzc com_google_android_gms_common_zzc, com.google.android.gms.common.api.Api.zza<? extends zzrn, zzro> com_google_android_gms_common_api_Api_zza__extends_com_google_android_gms_internal_zzrn__com_google_android_gms_internal_zzro, Lock lock, Context context) {
        this.zzahj = com_google_android_gms_common_api_internal_zzl;
        this.zzahz = com_google_android_gms_common_internal_zzf;
        this.zzahA = map;
        this.zzags = com_google_android_gms_common_zzc;
        this.zzagt = com_google_android_gms_common_api_Api_zza__extends_com_google_android_gms_internal_zzrn__com_google_android_gms_internal_zzro;
        this.zzXG = lock;
        this.mContext = context;
    }

    private void zzZ(boolean z) {
        if (this.zzahs != null) {
            if (this.zzahs.isConnected() && z) {
                this.zzahs.zzFG();
            }
            this.zzahs.disconnect();
            this.zzahw = null;
        }
    }

    private void zza(SignInResponse signInResponse) {
        if (zzbz(0)) {
            ConnectionResult zzqY = signInResponse.zzqY();
            if (zzqY.isSuccess()) {
                ResolveAccountResponse zzFP = signInResponse.zzFP();
                ConnectionResult zzqY2 = zzFP.zzqY();
                if (zzqY2.isSuccess()) {
                    this.zzahv = true;
                    this.zzahw = zzFP.zzqX();
                    this.zzahx = zzFP.zzqZ();
                    this.zzahy = zzFP.zzra();
                    zzpv();
                    return;
                }
                Log.wtf("GoogleApiClientConnecting", "Sign-in succeeded with resolve account failure: " + zzqY2, new Exception());
                zzg(zzqY2);
            } else if (zzf(zzqY)) {
                zzpy();
                zzpv();
            } else {
                zzg(zzqY);
            }
        }
    }

    private boolean zza(int i, int i2, ConnectionResult connectionResult) {
        return (i2 != 1 || zze(connectionResult)) ? this.zzahm == null || i < this.zzahn : false;
    }

    private void zzb(ConnectionResult connectionResult, Api<?> api, int i) {
        if (i != 2) {
            int priority = api.zzoP().getPriority();
            if (zza(priority, i, connectionResult)) {
                this.zzahm = connectionResult;
                this.zzahn = priority;
            }
        }
        this.zzahj.zzaio.put(api.zzoR(), connectionResult);
    }

    private String zzbA(int i) {
        switch (i) {
            case 0:
                return "STEP_SERVICE_BINDINGS_AND_SIGN_IN";
            case 1:
                return "STEP_GETTING_REMOTE_SERVICE";
            default:
                return "UNKNOWN";
        }
    }

    private boolean zzbz(int i) {
        if (this.zzaho == i) {
            return true;
        }
        Log.i("GoogleApiClientConnecting", this.zzahj.zzagW.zzpH());
        Log.wtf("GoogleApiClientConnecting", "GoogleApiClient connecting is in step " + zzbA(this.zzaho) + " but received callback for step " + zzbA(i), new Exception());
        zzg(new ConnectionResult(8, null));
        return false;
    }

    private boolean zze(ConnectionResult connectionResult) {
        return connectionResult.hasResolution() || this.zzags.zzbu(connectionResult.getErrorCode()) != null;
    }

    private boolean zzf(ConnectionResult connectionResult) {
        return this.zzaht != 2 ? this.zzaht == 1 && !connectionResult.hasResolution() : true;
    }

    private void zzg(ConnectionResult connectionResult) {
        zzpz();
        zzZ(!connectionResult.hasResolution());
        this.zzahj.zzh(connectionResult);
        this.zzahj.zzais.zzd(connectionResult);
    }

    private Set<Scope> zzpA() {
        if (this.zzahz == null) {
            return Collections.emptySet();
        }
        Set<Scope> hashSet = new HashSet(this.zzahz.zzqs());
        Map zzqu = this.zzahz.zzqu();
        for (Api api : zzqu.keySet()) {
            if (!this.zzahj.zzaio.containsKey(api.zzoR())) {
                hashSet.addAll(((com.google.android.gms.common.internal.zzf.zza) zzqu.get(api)).zzXf);
            }
        }
        return hashSet;
    }

    private boolean zzpu() {
        this.zzahp--;
        if (this.zzahp > 0) {
            return false;
        }
        if (this.zzahp < 0) {
            Log.i("GoogleApiClientConnecting", this.zzahj.zzagW.zzpH());
            Log.wtf("GoogleApiClientConnecting", "GoogleApiClient received too many callbacks for the given step. Clients may be in an unexpected state; GoogleApiClient will now disconnect.", new Exception());
            zzg(new ConnectionResult(8, null));
            return false;
        } else if (this.zzahm == null) {
            return true;
        } else {
            this.zzahj.zzair = this.zzahn;
            zzg(this.zzahm);
            return false;
        }
    }

    private void zzpv() {
        if (this.zzahp == 0) {
            if (!this.zzahu || this.zzahv) {
                zzpw();
            }
        }
    }

    private void zzpw() {
        ArrayList arrayList = new ArrayList();
        this.zzaho = 1;
        this.zzahp = this.zzahj.zzahT.size();
        for (com.google.android.gms.common.api.Api.zzc com_google_android_gms_common_api_Api_zzc : this.zzahj.zzahT.keySet()) {
            if (!this.zzahj.zzaio.containsKey(com_google_android_gms_common_api_Api_zzc)) {
                arrayList.add(this.zzahj.zzahT.get(com_google_android_gms_common_api_Api_zzc));
            } else if (zzpu()) {
                zzpx();
            }
        }
        if (!arrayList.isEmpty()) {
            this.zzahB.add(zzm.zzpN().submit(new zzc(this, arrayList)));
        }
    }

    private void zzpx() {
        this.zzahj.zzpL();
        zzm.zzpN().execute(new Runnable(this) {
            final /* synthetic */ zzh zzahC;

            {
                this.zzahC = r1;
            }

            public void run() {
                this.zzahC.zzags.zzal(this.zzahC.mContext);
            }
        });
        if (this.zzahs != null) {
            if (this.zzahx) {
                this.zzahs.zza(this.zzahw, this.zzahy);
            }
            zzZ(false);
        }
        for (com.google.android.gms.common.api.Api.zzc com_google_android_gms_common_api_Api_zzc : this.zzahj.zzaio.keySet()) {
            ((com.google.android.gms.common.api.Api.zzb) this.zzahj.zzahT.get(com_google_android_gms_common_api_Api_zzc)).disconnect();
        }
        this.zzahj.zzais.zzi(this.zzahq.isEmpty() ? null : this.zzahq);
    }

    private void zzpy() {
        this.zzahu = false;
        this.zzahj.zzagW.zzahU = Collections.emptySet();
        for (com.google.android.gms.common.api.Api.zzc com_google_android_gms_common_api_Api_zzc : this.zzahr) {
            if (!this.zzahj.zzaio.containsKey(com_google_android_gms_common_api_Api_zzc)) {
                this.zzahj.zzaio.put(com_google_android_gms_common_api_Api_zzc, new ConnectionResult(17, null));
            }
        }
    }

    private void zzpz() {
        Iterator it = this.zzahB.iterator();
        while (it.hasNext()) {
            ((Future) it.next()).cancel(true);
        }
        this.zzahB.clear();
    }

    public void begin() {
        this.zzahj.zzaio.clear();
        this.zzahu = false;
        this.zzahm = null;
        this.zzaho = 0;
        this.zzaht = 2;
        this.zzahv = false;
        this.zzahx = false;
        Map hashMap = new HashMap();
        int i = 0;
        for (Api api : this.zzahA.keySet()) {
            com.google.android.gms.common.api.Api.zzb com_google_android_gms_common_api_Api_zzb = (com.google.android.gms.common.api.Api.zzb) this.zzahj.zzahT.get(api.zzoR());
            int intValue = ((Integer) this.zzahA.get(api)).intValue();
            int i2 = (api.zzoP().getPriority() == 1 ? 1 : 0) | i;
            if (com_google_android_gms_common_api_Api_zzb.zzmE()) {
                this.zzahu = true;
                if (intValue < this.zzaht) {
                    this.zzaht = intValue;
                }
                if (intValue != 0) {
                    this.zzahr.add(api.zzoR());
                }
            }
            hashMap.put(com_google_android_gms_common_api_Api_zzb, new zza(this, api, intValue));
            i = i2;
        }
        if (i != 0) {
            this.zzahu = false;
        }
        if (this.zzahu) {
            this.zzahz.zza(Integer.valueOf(this.zzahj.zzagW.getSessionId()));
            ConnectionCallbacks com_google_android_gms_common_api_internal_zzh_zze = new zze();
            this.zzahs = (zzrn) this.zzagt.zza(this.mContext, this.zzahj.zzagW.getLooper(), this.zzahz, this.zzahz.zzqy(), com_google_android_gms_common_api_internal_zzh_zze, com_google_android_gms_common_api_internal_zzh_zze);
        }
        this.zzahp = this.zzahj.zzahT.size();
        this.zzahB.add(zzm.zzpN().submit(new zzb(this, hashMap)));
    }

    public void connect() {
    }

    public boolean disconnect() {
        zzpz();
        zzZ(true);
        this.zzahj.zzh(null);
        return true;
    }

    public void onConnected(Bundle connectionHint) {
        if (zzbz(1)) {
            if (connectionHint != null) {
                this.zzahq.putAll(connectionHint);
            }
            if (zzpu()) {
                zzpx();
            }
        }
    }

    public void onConnectionSuspended(int cause) {
        zzg(new ConnectionResult(8, null));
    }

    public <A extends com.google.android.gms.common.api.Api.zzb, R extends Result, T extends com.google.android.gms.common.api.internal.zza.zza<R, A>> T zza(T t) {
        this.zzahj.zzagW.zzahN.add(t);
        return t;
    }

    public void zza(ConnectionResult connectionResult, Api<?> api, int i) {
        if (zzbz(1)) {
            zzb(connectionResult, api, i);
            if (zzpu()) {
                zzpx();
            }
        }
    }

    public <A extends com.google.android.gms.common.api.Api.zzb, T extends com.google.android.gms.common.api.internal.zza.zza<? extends Result, A>> T zzb(T t) {
        throw new IllegalStateException("GoogleApiClient is not connected yet.");
    }
}
