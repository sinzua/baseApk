package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.BinderThread;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public abstract class zzj<T extends IInterface> implements com.google.android.gms.common.api.Api.zzb, com.google.android.gms.common.internal.zzk.zza {
    public static final String[] zzalJ = new String[]{"service_esmobile", "service_googleme"};
    private final Context mContext;
    final Handler mHandler;
    private final Account zzTI;
    private final Set<Scope> zzXf;
    private final Looper zzagr;
    private final com.google.android.gms.common.zzc zzags;
    private final zzf zzahz;
    private com.google.android.gms.common.api.GoogleApiClient.zza zzalA;
    private T zzalB;
    private final ArrayList<zzc<?>> zzalC;
    private zze zzalD;
    private int zzalE;
    private final ConnectionCallbacks zzalF;
    private final OnConnectionFailedListener zzalG;
    private final int zzalH;
    protected AtomicInteger zzalI;
    private int zzals;
    private long zzalt;
    private long zzalu;
    private int zzalv;
    private long zzalw;
    private final zzl zzalx;
    private final Object zzaly;
    private zzs zzalz;
    private final Object zzpV;

    final class zzb extends Handler {
        final /* synthetic */ zzj zzalL;

        public zzb(zzj com_google_android_gms_common_internal_zzj, Looper looper) {
            this.zzalL = com_google_android_gms_common_internal_zzj;
            super(looper);
        }

        private void zza(Message message) {
            zzc com_google_android_gms_common_internal_zzj_zzc = (zzc) message.obj;
            com_google_android_gms_common_internal_zzj_zzc.zzqM();
            com_google_android_gms_common_internal_zzj_zzc.unregister();
        }

        private boolean zzb(Message message) {
            return message.what == 2 || message.what == 1 || message.what == 5;
        }

        public void handleMessage(Message msg) {
            if (this.zzalL.zzalI.get() != msg.arg1) {
                if (zzb(msg)) {
                    zza(msg);
                }
            } else if ((msg.what == 1 || msg.what == 5) && !this.zzalL.isConnecting()) {
                zza(msg);
            } else if (msg.what == 3) {
                ConnectionResult connectionResult = new ConnectionResult(msg.arg2, null);
                this.zzalL.zzalA.zza(connectionResult);
                this.zzalL.onConnectionFailed(connectionResult);
            } else if (msg.what == 4) {
                this.zzalL.zzb(4, null);
                if (this.zzalL.zzalF != null) {
                    this.zzalL.zzalF.onConnectionSuspended(msg.arg2);
                }
                this.zzalL.onConnectionSuspended(msg.arg2);
                this.zzalL.zza(4, 1, null);
            } else if (msg.what == 2 && !this.zzalL.isConnected()) {
                zza(msg);
            } else if (zzb(msg)) {
                ((zzc) msg.obj).zzqN();
            } else {
                Log.wtf("GmsClient", "Don't know how to handle message: " + msg.what, new Exception());
            }
        }
    }

    protected abstract class zzc<TListener> {
        private TListener mListener;
        final /* synthetic */ zzj zzalL;
        private boolean zzalM = false;

        public zzc(zzj com_google_android_gms_common_internal_zzj, TListener tListener) {
            this.zzalL = com_google_android_gms_common_internal_zzj;
            this.mListener = tListener;
        }

        public void unregister() {
            zzqO();
            synchronized (this.zzalL.zzalC) {
                this.zzalL.zzalC.remove(this);
            }
        }

        protected abstract void zzqM();

        public void zzqN() {
            synchronized (this) {
                Object obj = this.mListener;
                if (this.zzalM) {
                    Log.w("GmsClient", "Callback proxy " + this + " being reused. This is not safe.");
                }
            }
            if (obj != null) {
                try {
                    zzw(obj);
                } catch (RuntimeException e) {
                    zzqM();
                    throw e;
                }
            }
            zzqM();
            synchronized (this) {
                this.zzalM = true;
            }
            unregister();
        }

        public void zzqO() {
            synchronized (this) {
                this.mListener = null;
            }
        }

        protected abstract void zzw(TListener tListener);
    }

    public final class zze implements ServiceConnection {
        final /* synthetic */ zzj zzalL;
        private final int zzalO;

        public zze(zzj com_google_android_gms_common_internal_zzj, int i) {
            this.zzalL = com_google_android_gms_common_internal_zzj;
            this.zzalO = i;
        }

        public void onServiceConnected(ComponentName component, IBinder binder) {
            zzx.zzb((Object) binder, (Object) "Expecting a valid IBinder");
            synchronized (this.zzalL.zzaly) {
                this.zzalL.zzalz = com.google.android.gms.common.internal.zzs.zza.zzaS(binder);
            }
            this.zzalL.zzm(0, this.zzalO);
        }

        public void onServiceDisconnected(ComponentName component) {
            synchronized (this.zzalL.zzaly) {
                this.zzalL.zzalz = null;
            }
            this.zzalL.mHandler.sendMessage(this.zzalL.mHandler.obtainMessage(4, this.zzalO, 1));
        }
    }

    private abstract class zza extends zzc<Boolean> {
        public final int statusCode;
        public final Bundle zzalK;
        final /* synthetic */ zzj zzalL;

        @BinderThread
        protected zza(zzj com_google_android_gms_common_internal_zzj, int i, Bundle bundle) {
            this.zzalL = com_google_android_gms_common_internal_zzj;
            super(com_google_android_gms_common_internal_zzj, Boolean.valueOf(true));
            this.statusCode = i;
            this.zzalK = bundle;
        }

        protected void zzc(Boolean bool) {
            PendingIntent pendingIntent = null;
            if (bool == null) {
                this.zzalL.zzb(1, null);
                return;
            }
            switch (this.statusCode) {
                case 0:
                    if (!zzqL()) {
                        this.zzalL.zzb(1, null);
                        zzj(new ConnectionResult(8, null));
                        return;
                    }
                    return;
                case 10:
                    this.zzalL.zzb(1, null);
                    throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
                default:
                    this.zzalL.zzb(1, null);
                    if (this.zzalK != null) {
                        pendingIntent = (PendingIntent) this.zzalK.getParcelable("pendingIntent");
                    }
                    zzj(new ConnectionResult(this.statusCode, pendingIntent));
                    return;
            }
        }

        protected abstract void zzj(ConnectionResult connectionResult);

        protected abstract boolean zzqL();

        protected void zzqM() {
        }

        protected /* synthetic */ void zzw(Object obj) {
            zzc((Boolean) obj);
        }
    }

    protected class zzf implements com.google.android.gms.common.api.GoogleApiClient.zza {
        final /* synthetic */ zzj zzalL;

        public zzf(zzj com_google_android_gms_common_internal_zzj) {
            this.zzalL = com_google_android_gms_common_internal_zzj;
        }

        public void zza(@NonNull ConnectionResult connectionResult) {
            if (connectionResult.isSuccess()) {
                this.zzalL.zza(null, this.zzalL.zzXf);
            } else if (this.zzalL.zzalG != null) {
                this.zzalL.zzalG.onConnectionFailed(connectionResult);
            }
        }
    }

    public static final class zzd extends com.google.android.gms.common.internal.zzr.zza {
        private zzj zzalN;
        private final int zzalO;

        public zzd(@NonNull zzj com_google_android_gms_common_internal_zzj, int i) {
            this.zzalN = com_google_android_gms_common_internal_zzj;
            this.zzalO = i;
        }

        private void zzqP() {
            this.zzalN = null;
        }

        @BinderThread
        public void zza(int i, @NonNull IBinder iBinder, @Nullable Bundle bundle) {
            zzx.zzb(this.zzalN, (Object) "onPostInitComplete can be called only once per call to getRemoteService");
            this.zzalN.zza(i, iBinder, bundle, this.zzalO);
            zzqP();
        }

        @BinderThread
        public void zzb(int i, @Nullable Bundle bundle) {
            Log.wtf("GmsClient", "received deprecated onAccountValidationComplete callback, ignoring", new Exception());
        }
    }

    protected final class zzg extends zza {
        final /* synthetic */ zzj zzalL;
        public final IBinder zzalP;

        @BinderThread
        public zzg(zzj com_google_android_gms_common_internal_zzj, int i, IBinder iBinder, Bundle bundle) {
            this.zzalL = com_google_android_gms_common_internal_zzj;
            super(com_google_android_gms_common_internal_zzj, i, bundle);
            this.zzalP = iBinder;
        }

        protected void zzj(ConnectionResult connectionResult) {
            if (this.zzalL.zzalG != null) {
                this.zzalL.zzalG.onConnectionFailed(connectionResult);
            }
            this.zzalL.onConnectionFailed(connectionResult);
        }

        protected boolean zzqL() {
            try {
                String interfaceDescriptor = this.zzalP.getInterfaceDescriptor();
                if (this.zzalL.zzgv().equals(interfaceDescriptor)) {
                    IInterface zzW = this.zzalL.zzW(this.zzalP);
                    if (zzW == null || !this.zzalL.zza(2, 3, zzW)) {
                        return false;
                    }
                    Bundle zzoi = this.zzalL.zzoi();
                    if (this.zzalL.zzalF != null) {
                        this.zzalL.zzalF.onConnected(zzoi);
                    }
                    return true;
                }
                Log.e("GmsClient", "service descriptor mismatch: " + this.zzalL.zzgv() + " vs. " + interfaceDescriptor);
                return false;
            } catch (RemoteException e) {
                Log.w("GmsClient", "service probably died");
                return false;
            }
        }
    }

    protected final class zzh extends zza {
        final /* synthetic */ zzj zzalL;

        @BinderThread
        public zzh(zzj com_google_android_gms_common_internal_zzj, int i) {
            this.zzalL = com_google_android_gms_common_internal_zzj;
            super(com_google_android_gms_common_internal_zzj, i, null);
        }

        protected void zzj(ConnectionResult connectionResult) {
            this.zzalL.zzalA.zza(connectionResult);
            this.zzalL.onConnectionFailed(connectionResult);
        }

        protected boolean zzqL() {
            this.zzalL.zzalA.zza(ConnectionResult.zzafB);
            return true;
        }
    }

    protected zzj(Context context, Looper looper, int i, zzf com_google_android_gms_common_internal_zzf, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, zzl.zzau(context), com.google.android.gms.common.zzc.zzoK(), i, com_google_android_gms_common_internal_zzf, (ConnectionCallbacks) zzx.zzz(connectionCallbacks), (OnConnectionFailedListener) zzx.zzz(onConnectionFailedListener));
    }

    protected zzj(Context context, Looper looper, zzl com_google_android_gms_common_internal_zzl, com.google.android.gms.common.zzc com_google_android_gms_common_zzc, int i, zzf com_google_android_gms_common_internal_zzf, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        this.zzpV = new Object();
        this.zzaly = new Object();
        this.zzalA = new zzf(this);
        this.zzalC = new ArrayList();
        this.zzalE = 1;
        this.zzalI = new AtomicInteger(0);
        this.mContext = (Context) zzx.zzb((Object) context, (Object) "Context must not be null");
        this.zzagr = (Looper) zzx.zzb((Object) looper, (Object) "Looper must not be null");
        this.zzalx = (zzl) zzx.zzb((Object) com_google_android_gms_common_internal_zzl, (Object) "Supervisor must not be null");
        this.zzags = (com.google.android.gms.common.zzc) zzx.zzb((Object) com_google_android_gms_common_zzc, (Object) "API availability must not be null");
        this.mHandler = new zzb(this, looper);
        this.zzalH = i;
        this.zzahz = (zzf) zzx.zzz(com_google_android_gms_common_internal_zzf);
        this.zzTI = com_google_android_gms_common_internal_zzf.getAccount();
        this.zzXf = zza(com_google_android_gms_common_internal_zzf.zzqt());
        this.zzalF = connectionCallbacks;
        this.zzalG = onConnectionFailedListener;
    }

    private Set<Scope> zza(Set<Scope> set) {
        Set<Scope> zzb = zzb((Set) set);
        if (zzb == null) {
            return zzb;
        }
        for (Scope contains : zzb) {
            if (!set.contains(contains)) {
                throw new IllegalStateException("Expanding scopes is not permitted, use implied scopes instead");
            }
        }
        return zzb;
    }

    private boolean zza(int i, int i2, T t) {
        boolean z;
        synchronized (this.zzpV) {
            if (this.zzalE != i) {
                z = false;
            } else {
                zzb(i2, t);
                z = true;
            }
        }
        return z;
    }

    private void zzb(int i, T t) {
        boolean z = true;
        if ((i == 3) != (t != null)) {
            z = false;
        }
        zzx.zzac(z);
        synchronized (this.zzpV) {
            this.zzalE = i;
            this.zzalB = t;
            zzc(i, t);
            switch (i) {
                case 1:
                    zzqF();
                    break;
                case 2:
                    zzqE();
                    break;
                case 3:
                    zza((IInterface) t);
                    break;
            }
        }
    }

    private void zzqE() {
        if (this.zzalD != null) {
            Log.e("GmsClient", "Calling connect() while still connected, missing disconnect() for " + zzgu());
            this.zzalx.zzb(zzgu(), this.zzalD, zzqD());
            this.zzalI.incrementAndGet();
        }
        this.zzalD = new zze(this, this.zzalI.get());
        if (!this.zzalx.zza(zzgu(), this.zzalD, zzqD())) {
            Log.e("GmsClient", "unable to connect to service: " + zzgu());
            zzm(8, this.zzalI.get());
        }
    }

    private void zzqF() {
        if (this.zzalD != null) {
            this.zzalx.zzb(zzgu(), this.zzalD, zzqD());
            this.zzalD = null;
        }
    }

    public void disconnect() {
        this.zzalI.incrementAndGet();
        synchronized (this.zzalC) {
            int size = this.zzalC.size();
            for (int i = 0; i < size; i++) {
                ((zzc) this.zzalC.get(i)).zzqO();
            }
            this.zzalC.clear();
        }
        synchronized (this.zzaly) {
            this.zzalz = null;
        }
        zzb(1, null);
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        synchronized (this.zzpV) {
            int i = this.zzalE;
            IInterface iInterface = this.zzalB;
        }
        writer.append(prefix).append("mConnectState=");
        switch (i) {
            case 1:
                writer.print("DISCONNECTED");
                break;
            case 2:
                writer.print("CONNECTING");
                break;
            case 3:
                writer.print("CONNECTED");
                break;
            case 4:
                writer.print("DISCONNECTING");
                break;
            default:
                writer.print("UNKNOWN");
                break;
        }
        writer.append(" mService=");
        if (iInterface == null) {
            writer.println("null");
        } else {
            writer.append(zzgv()).append("@").println(Integer.toHexString(System.identityHashCode(iInterface.asBinder())));
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        if (this.zzalu > 0) {
            writer.append(prefix).append("lastConnectedTime=").println(this.zzalu + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + simpleDateFormat.format(new Date(this.zzalu)));
        }
        if (this.zzalt > 0) {
            writer.append(prefix).append("lastSuspendedCause=");
            switch (this.zzals) {
                case 1:
                    writer.append("CAUSE_SERVICE_DISCONNECTED");
                    break;
                case 2:
                    writer.append("CAUSE_NETWORK_LOST");
                    break;
                default:
                    writer.append(String.valueOf(this.zzals));
                    break;
            }
            writer.append(" lastSuspendedTime=").println(this.zzalt + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + simpleDateFormat.format(new Date(this.zzalt)));
        }
        if (this.zzalw > 0) {
            writer.append(prefix).append("lastFailedStatus=").append(CommonStatusCodes.getStatusCodeString(this.zzalv));
            writer.append(" lastFailedTime=").println(this.zzalw + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + simpleDateFormat.format(new Date(this.zzalw)));
        }
    }

    public final Context getContext() {
        return this.mContext;
    }

    public final Looper getLooper() {
        return this.zzagr;
    }

    public boolean isConnected() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzalE == 3;
        }
        return z;
    }

    public boolean isConnecting() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzalE == 2;
        }
        return z;
    }

    @CallSuper
    protected void onConnectionFailed(ConnectionResult result) {
        this.zzalv = result.getErrorCode();
        this.zzalw = System.currentTimeMillis();
    }

    @CallSuper
    protected void onConnectionSuspended(int cause) {
        this.zzals = cause;
        this.zzalt = System.currentTimeMillis();
    }

    @Nullable
    protected abstract T zzW(IBinder iBinder);

    @BinderThread
    protected void zza(int i, IBinder iBinder, Bundle bundle, int i2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, i2, -1, new zzg(this, i, iBinder, bundle)));
    }

    @CallSuper
    protected void zza(@NonNull T t) {
        this.zzalu = System.currentTimeMillis();
    }

    public void zza(@NonNull com.google.android.gms.common.api.GoogleApiClient.zza com_google_android_gms_common_api_GoogleApiClient_zza) {
        this.zzalA = (com.google.android.gms.common.api.GoogleApiClient.zza) zzx.zzb((Object) com_google_android_gms_common_api_GoogleApiClient_zza, (Object) "Connection progress callbacks cannot be null.");
        zzb(2, null);
    }

    @WorkerThread
    public void zza(zzp com_google_android_gms_common_internal_zzp, Set<Scope> set) {
        try {
            GetServiceRequest zzj = new GetServiceRequest(this.zzalH).zzcG(this.mContext.getPackageName()).zzj(zzml());
            if (set != null) {
                zzj.zzd(set);
            }
            if (zzmE()) {
                zzj.zzc(zzqq()).zzb(com_google_android_gms_common_internal_zzp);
            } else if (zzqK()) {
                zzj.zzc(this.zzTI);
            }
            synchronized (this.zzaly) {
                if (this.zzalz != null) {
                    this.zzalz.zza(new zzd(this, this.zzalI.get()), zzj);
                } else {
                    Log.w("GmsClient", "mServiceBroker is null, client disconnected");
                }
            }
        } catch (DeadObjectException e) {
            Log.w("GmsClient", "service died");
            zzbS(1);
        } catch (Throwable e2) {
            Log.w("GmsClient", "Remote exception occurred", e2);
        }
    }

    @NonNull
    protected Set<Scope> zzb(@NonNull Set<Scope> set) {
        return set;
    }

    public void zzbS(int i) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(4, this.zzalI.get(), i));
    }

    void zzc(int i, T t) {
    }

    @NonNull
    protected abstract String zzgu();

    @NonNull
    protected abstract String zzgv();

    protected void zzm(int i, int i2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(5, i2, -1, new zzh(this, i)));
    }

    public boolean zzmE() {
        return false;
    }

    protected Bundle zzml() {
        return new Bundle();
    }

    public boolean zznb() {
        return false;
    }

    public Intent zznc() {
        throw new UnsupportedOperationException("Not a sign in API");
    }

    @Nullable
    public IBinder zzoT() {
        IBinder iBinder;
        synchronized (this.zzaly) {
            if (this.zzalz == null) {
                iBinder = null;
            } else {
                iBinder = this.zzalz.asBinder();
            }
        }
        return iBinder;
    }

    public Bundle zzoi() {
        return null;
    }

    @Nullable
    protected final String zzqD() {
        return this.zzahz.zzqw();
    }

    public void zzqG() {
        int isGooglePlayServicesAvailable = this.zzags.isGooglePlayServicesAvailable(this.mContext);
        if (isGooglePlayServicesAvailable != 0) {
            zzb(1, null);
            this.zzalA = new zzf(this);
            this.mHandler.sendMessage(this.mHandler.obtainMessage(3, this.zzalI.get(), isGooglePlayServicesAvailable));
            return;
        }
        zza(new zzf(this));
    }

    protected final zzf zzqH() {
        return this.zzahz;
    }

    protected final void zzqI() {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }

    public final T zzqJ() throws DeadObjectException {
        T t;
        synchronized (this.zzpV) {
            if (this.zzalE == 4) {
                throw new DeadObjectException();
            }
            zzqI();
            zzx.zza(this.zzalB != null, (Object) "Client is connected but service is null");
            t = this.zzalB;
        }
        return t;
    }

    public boolean zzqK() {
        return false;
    }

    public final Account zzqq() {
        return this.zzTI != null ? this.zzTI : new Account("<<default account>>", com.google.android.gms.auth.zzd.GOOGLE_ACCOUNT_TYPE);
    }
}
