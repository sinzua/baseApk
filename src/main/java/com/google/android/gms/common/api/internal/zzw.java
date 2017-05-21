package com.google.android.gms.common.api.internal;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.zzc;
import com.google.android.gms.common.zze;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class zzw extends Fragment implements OnCancelListener {
    private boolean mStarted;
    private int zzaiA = -1;
    private ConnectionResult zzaiB;
    private final Handler zzaiC = new Handler(Looper.getMainLooper());
    protected zzn zzaiD;
    private final SparseArray<zza> zzaiE = new SparseArray();
    private boolean zzaiz;

    private class zzb implements Runnable {
        final /* synthetic */ zzw zzaiI;
        private final int zzaiJ;
        private final ConnectionResult zzaiK;

        public zzb(zzw com_google_android_gms_common_api_internal_zzw, int i, ConnectionResult connectionResult) {
            this.zzaiI = com_google_android_gms_common_api_internal_zzw;
            this.zzaiJ = i;
            this.zzaiK = connectionResult;
        }

        @MainThread
        public void run() {
            if (this.zzaiI.mStarted && !this.zzaiI.zzaiz) {
                this.zzaiI.zzaiz = true;
                this.zzaiI.zzaiA = this.zzaiJ;
                this.zzaiI.zzaiB = this.zzaiK;
                if (this.zzaiK.hasResolution()) {
                    try {
                        this.zzaiK.startResolutionForResult(this.zzaiI.getActivity(), ((this.zzaiI.getActivity().getSupportFragmentManager().getFragments().indexOf(this.zzaiI) + 1) << 16) + 1);
                    } catch (SendIntentException e) {
                        this.zzaiI.zzpP();
                    }
                } else if (this.zzaiI.zzpQ().isUserResolvableError(this.zzaiK.getErrorCode())) {
                    this.zzaiI.zzb(this.zzaiJ, this.zzaiK);
                } else if (this.zzaiK.getErrorCode() == 18) {
                    this.zzaiI.zzc(this.zzaiJ, this.zzaiK);
                } else {
                    this.zzaiI.zza(this.zzaiJ, this.zzaiK);
                }
            }
        }
    }

    private class zza implements OnConnectionFailedListener {
        public final int zzaiF;
        public final GoogleApiClient zzaiG;
        public final OnConnectionFailedListener zzaiH;
        final /* synthetic */ zzw zzaiI;

        public zza(zzw com_google_android_gms_common_api_internal_zzw, int i, GoogleApiClient googleApiClient, OnConnectionFailedListener onConnectionFailedListener) {
            this.zzaiI = com_google_android_gms_common_api_internal_zzw;
            this.zzaiF = i;
            this.zzaiG = googleApiClient;
            this.zzaiH = onConnectionFailedListener;
            googleApiClient.registerConnectionFailedListener(this);
        }

        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            writer.append(prefix).append("GoogleApiClient #").print(this.zzaiF);
            writer.println(":");
            this.zzaiG.dump(prefix + "  ", fd, writer, args);
        }

        public void onConnectionFailed(@NonNull ConnectionResult result) {
            this.zzaiI.zzaiC.post(new zzb(this.zzaiI, this.zzaiF, result));
        }

        public void zzpR() {
            this.zzaiG.unregisterConnectionFailedListener(this);
            this.zzaiG.disconnect();
        }
    }

    @Nullable
    public static zzw zza(FragmentActivity fragmentActivity) {
        zzx.zzcD("Must be called from main thread of process");
        try {
            zzw com_google_android_gms_common_api_internal_zzw = (zzw) fragmentActivity.getSupportFragmentManager().findFragmentByTag("GmsSupportLifecycleFrag");
            return (com_google_android_gms_common_api_internal_zzw == null || com_google_android_gms_common_api_internal_zzw.isRemoving()) ? null : com_google_android_gms_common_api_internal_zzw;
        } catch (Throwable e) {
            throw new IllegalStateException("Fragment with tag GmsSupportLifecycleFrag is not a SupportLifecycleFragment", e);
        }
    }

    private void zza(int i, ConnectionResult connectionResult) {
        Log.w("GmsSupportLifecycleFrag", "Unresolved error while connecting client. Stopping auto-manage.");
        zza com_google_android_gms_common_api_internal_zzw_zza = (zza) this.zzaiE.get(i);
        if (com_google_android_gms_common_api_internal_zzw_zza != null) {
            zzbD(i);
            OnConnectionFailedListener onConnectionFailedListener = com_google_android_gms_common_api_internal_zzw_zza.zzaiH;
            if (onConnectionFailedListener != null) {
                onConnectionFailedListener.onConnectionFailed(connectionResult);
            }
        }
        zzpP();
    }

    public static zzw zzb(FragmentActivity fragmentActivity) {
        zzw zza = zza(fragmentActivity);
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        if (zza == null) {
            zza = zzpO();
            if (zza == null) {
                Log.w("GmsSupportLifecycleFrag", "Unable to find connection error message resources (Did you include play-services-base and the proper proguard rules?); error dialogs may be unavailable.");
                zza = new zzw();
            }
            supportFragmentManager.beginTransaction().add((Fragment) zza, "GmsSupportLifecycleFrag").commitAllowingStateLoss();
            supportFragmentManager.executePendingTransactions();
        }
        return zza;
    }

    private static String zzi(ConnectionResult connectionResult) {
        return connectionResult.getErrorMessage() + " (" + connectionResult.getErrorCode() + ": " + zze.getErrorString(connectionResult.getErrorCode()) + ')';
    }

    @Nullable
    private static zzw zzpO() {
        Class cls;
        Throwable e;
        try {
            cls = Class.forName("com.google.android.gms.common.api.internal.SupportLifecycleFragmentImpl");
        } catch (ClassNotFoundException e2) {
            e = e2;
            if (Log.isLoggable("GmsSupportLifecycleFrag", 3)) {
                Log.d("GmsSupportLifecycleFrag", "Unable to find SupportLifecycleFragmentImpl class", e);
            }
            cls = null;
            if (cls != null) {
                try {
                    return (zzw) cls.newInstance();
                } catch (IllegalAccessException e3) {
                    e = e3;
                } catch (InstantiationException e4) {
                    e = e4;
                } catch (ExceptionInInitializerError e5) {
                    e = e5;
                } catch (RuntimeException e6) {
                    e = e6;
                }
            }
            return null;
        } catch (LinkageError e7) {
            e = e7;
            if (Log.isLoggable("GmsSupportLifecycleFrag", 3)) {
                Log.d("GmsSupportLifecycleFrag", "Unable to find SupportLifecycleFragmentImpl class", e);
            }
            cls = null;
            if (cls != null) {
                return (zzw) cls.newInstance();
            }
            return null;
        } catch (SecurityException e8) {
            e = e8;
            if (Log.isLoggable("GmsSupportLifecycleFrag", 3)) {
                Log.d("GmsSupportLifecycleFrag", "Unable to find SupportLifecycleFragmentImpl class", e);
            }
            cls = null;
            if (cls != null) {
                return (zzw) cls.newInstance();
            }
            return null;
        }
        if (cls != null) {
            return (zzw) cls.newInstance();
        }
        return null;
        if (Log.isLoggable("GmsSupportLifecycleFrag", 3)) {
            Log.d("GmsSupportLifecycleFrag", "Unable to instantiate SupportLifecycleFragmentImpl class", e);
        }
        return null;
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
        for (int i = 0; i < this.zzaiE.size(); i++) {
            ((zza) this.zzaiE.valueAt(i)).dump(prefix, fd, writer, args);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActivityResult(int r5, int r6, android.content.Intent r7) {
        /*
        r4 = this;
        r0 = 1;
        r1 = 0;
        switch(r5) {
            case 1: goto L_0x001b;
            case 2: goto L_0x000c;
            default: goto L_0x0005;
        };
    L_0x0005:
        r0 = r1;
    L_0x0006:
        if (r0 == 0) goto L_0x002b;
    L_0x0008:
        r4.zzpP();
    L_0x000b:
        return;
    L_0x000c:
        r2 = r4.zzpQ();
        r3 = r4.getActivity();
        r2 = r2.isGooglePlayServicesAvailable(r3);
        if (r2 != 0) goto L_0x0005;
    L_0x001a:
        goto L_0x0006;
    L_0x001b:
        r2 = -1;
        if (r6 == r2) goto L_0x0006;
    L_0x001e:
        if (r6 != 0) goto L_0x0005;
    L_0x0020:
        r0 = new com.google.android.gms.common.ConnectionResult;
        r2 = 13;
        r3 = 0;
        r0.<init>(r2, r3);
        r4.zzaiB = r0;
        goto L_0x0005;
    L_0x002b:
        r0 = r4.zzaiA;
        r1 = r4.zzaiB;
        r4.zza(r0, r1);
        goto L_0x000b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zzw.onActivityResult(int, int, android.content.Intent):void");
    }

    public void onCancel(DialogInterface dialogInterface) {
        zza(this.zzaiA, new ConnectionResult(13, null));
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.zzaiz = savedInstanceState.getBoolean("resolving_error", false);
            this.zzaiA = savedInstanceState.getInt("failed_client_id", -1);
            if (this.zzaiA >= 0) {
                this.zzaiB = new ConnectionResult(savedInstanceState.getInt("failed_status"), (PendingIntent) savedInstanceState.getParcelable("failed_resolution"));
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("resolving_error", this.zzaiz);
        if (this.zzaiA >= 0) {
            outState.putInt("failed_client_id", this.zzaiA);
            outState.putInt("failed_status", this.zzaiB.getErrorCode());
            outState.putParcelable("failed_resolution", this.zzaiB.getResolution());
        }
    }

    public void onStart() {
        super.onStart();
        this.mStarted = true;
        if (!this.zzaiz) {
            for (int i = 0; i < this.zzaiE.size(); i++) {
                ((zza) this.zzaiE.valueAt(i)).zzaiG.connect();
            }
        }
    }

    public void onStop() {
        super.onStop();
        this.mStarted = false;
        for (int i = 0; i < this.zzaiE.size(); i++) {
            ((zza) this.zzaiE.valueAt(i)).zzaiG.disconnect();
        }
    }

    public void zza(int i, GoogleApiClient googleApiClient, OnConnectionFailedListener onConnectionFailedListener) {
        zzx.zzb((Object) googleApiClient, (Object) "GoogleApiClient instance cannot be null");
        zzx.zza(this.zzaiE.indexOfKey(i) < 0, "Already managing a GoogleApiClient with id " + i);
        this.zzaiE.put(i, new zza(this, i, googleApiClient, onConnectionFailedListener));
        if (this.mStarted && !this.zzaiz) {
            googleApiClient.connect();
        }
    }

    protected void zzb(int i, ConnectionResult connectionResult) {
        Log.w("GmsSupportLifecycleFrag", "Failed to connect due to user resolvable error " + zzi(connectionResult));
        zza(i, connectionResult);
    }

    public void zzbD(int i) {
        zza com_google_android_gms_common_api_internal_zzw_zza = (zza) this.zzaiE.get(i);
        this.zzaiE.remove(i);
        if (com_google_android_gms_common_api_internal_zzw_zza != null) {
            com_google_android_gms_common_api_internal_zzw_zza.zzpR();
        }
    }

    protected void zzc(int i, ConnectionResult connectionResult) {
        Log.w("GmsSupportLifecycleFrag", "Unable to connect, GooglePlayServices is updating.");
        zza(i, connectionResult);
    }

    protected void zzpP() {
        this.zzaiz = false;
        this.zzaiA = -1;
        this.zzaiB = null;
        if (this.zzaiD != null) {
            this.zzaiD.unregister();
            this.zzaiD = null;
        }
        for (int i = 0; i < this.zzaiE.size(); i++) {
            ((zza) this.zzaiE.valueAt(i)).zzaiG.connect();
        }
    }

    protected zzc zzpQ() {
        return zzc.zzoK();
    }
}
