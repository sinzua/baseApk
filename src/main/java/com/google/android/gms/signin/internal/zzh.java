package com.google.android.gms.signin.internal;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.internal.zzq;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.ResolveAccountRequest;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.common.internal.zzp;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzrn;
import com.google.android.gms.internal.zzro;
import com.google.android.gms.signin.internal.zze.zza;

public class zzh extends zzj<zze> implements zzrn {
    private final zzf zzahz;
    private Integer zzale;
    private final Bundle zzbgU;
    private final boolean zzbhi;

    public zzh(Context context, Looper looper, boolean z, zzf com_google_android_gms_common_internal_zzf, Bundle bundle, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 44, com_google_android_gms_common_internal_zzf, connectionCallbacks, onConnectionFailedListener);
        this.zzbhi = z;
        this.zzahz = com_google_android_gms_common_internal_zzf;
        this.zzbgU = bundle;
        this.zzale = com_google_android_gms_common_internal_zzf.zzqz();
    }

    public zzh(Context context, Looper looper, boolean z, zzf com_google_android_gms_common_internal_zzf, zzro com_google_android_gms_internal_zzro, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, z, com_google_android_gms_common_internal_zzf, zza(com_google_android_gms_common_internal_zzf), connectionCallbacks, onConnectionFailedListener);
    }

    private ResolveAccountRequest zzFN() {
        Account zzqq = this.zzahz.zzqq();
        GoogleSignInAccount googleSignInAccount = null;
        if ("<<default account>>".equals(zzqq.name)) {
            googleSignInAccount = zzq.zzaf(getContext()).zzno();
        }
        return new ResolveAccountRequest(zzqq, this.zzale.intValue(), googleSignInAccount);
    }

    public static Bundle zza(zzf com_google_android_gms_common_internal_zzf) {
        zzro zzqy = com_google_android_gms_common_internal_zzf.zzqy();
        Integer zzqz = com_google_android_gms_common_internal_zzf.zzqz();
        Bundle bundle = new Bundle();
        bundle.putParcelable("com.google.android.gms.signin.internal.clientRequestedAccount", com_google_android_gms_common_internal_zzf.getAccount());
        if (zzqz != null) {
            bundle.putInt("com.google.android.gms.common.internal.ClientSettings.sessionId", zzqz.intValue());
        }
        if (zzqy != null) {
            bundle.putBoolean("com.google.android.gms.signin.internal.offlineAccessRequested", zzqy.zzFH());
            bundle.putBoolean("com.google.android.gms.signin.internal.idTokenRequested", zzqy.zzmO());
            bundle.putString("com.google.android.gms.signin.internal.serverClientId", zzqy.zzmR());
            bundle.putBoolean("com.google.android.gms.signin.internal.usePromptModeForAuthCode", true);
            bundle.putBoolean("com.google.android.gms.signin.internal.forceCodeForRefreshToken", zzqy.zzmQ());
            bundle.putString("com.google.android.gms.signin.internal.hostedDomain", zzqy.zzmS());
            bundle.putBoolean("com.google.android.gms.signin.internal.waitForAccessTokenRefresh", zzqy.zzFI());
        }
        return bundle;
    }

    public void connect() {
        zza(new zzf(this));
    }

    public void zzFG() {
        try {
            ((zze) zzqJ()).zzka(this.zzale.intValue());
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when clearAccountFromSessionStore is called");
        }
    }

    protected /* synthetic */ IInterface zzW(IBinder iBinder) {
        return zzec(iBinder);
    }

    public void zza(zzp com_google_android_gms_common_internal_zzp, boolean z) {
        try {
            ((zze) zzqJ()).zza(com_google_android_gms_common_internal_zzp, this.zzale.intValue(), z);
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when saveDefaultAccount is called");
        }
    }

    public void zza(zzd com_google_android_gms_signin_internal_zzd) {
        zzx.zzb((Object) com_google_android_gms_signin_internal_zzd, (Object) "Expecting a valid ISignInCallbacks");
        try {
            ((zze) zzqJ()).zza(new SignInRequest(zzFN()), com_google_android_gms_signin_internal_zzd);
        } catch (Throwable e) {
            Log.w("SignInClientImpl", "Remote service probably died when signIn is called");
            try {
                com_google_android_gms_signin_internal_zzd.zzb(new SignInResponse(8));
            } catch (RemoteException e2) {
                Log.wtf("SignInClientImpl", "ISignInCallbacks#onSignInComplete should be executed from the same process, unexpected RemoteException.", e);
            }
        }
    }

    protected zze zzec(IBinder iBinder) {
        return zza.zzeb(iBinder);
    }

    protected String zzgu() {
        return "com.google.android.gms.signin.service.START";
    }

    protected String zzgv() {
        return "com.google.android.gms.signin.internal.ISignInService";
    }

    public boolean zzmE() {
        return this.zzbhi;
    }

    protected Bundle zzml() {
        if (!getContext().getPackageName().equals(this.zzahz.zzqv())) {
            this.zzbgU.putString("com.google.android.gms.signin.internal.realClientPackageName", this.zzahz.zzqv());
        }
        return this.zzbgU;
    }
}
