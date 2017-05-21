package com.google.android.gms.ads.internal.purchase;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.anjlab.android.iab.v3.Constants;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.internal.zzgc;
import com.google.android.gms.internal.zzge.zza;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zze extends zza implements ServiceConnection {
    private final Activity mActivity;
    private zzb zzFC;
    zzh zzFD;
    private zzk zzFF;
    private Context zzFK;
    private zzgc zzFL;
    private zzf zzFM;
    private zzj zzFN;
    private String zzFO = null;

    public zze(Activity activity) {
        this.mActivity = activity;
        this.zzFD = zzh.zzy(this.mActivity.getApplicationContext());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            boolean z = false;
            try {
                int zzd = zzr.zzbM().zzd(data);
                if (resultCode == -1) {
                    zzr.zzbM();
                    if (zzd == 0) {
                        if (this.zzFF.zza(this.zzFO, resultCode, data)) {
                            z = true;
                        }
                        this.zzFL.recordPlayBillingResolution(zzd);
                        this.mActivity.finish();
                        zza(this.zzFL.getProductId(), z, resultCode, data);
                    }
                }
                this.zzFD.zza(this.zzFM);
                this.zzFL.recordPlayBillingResolution(zzd);
                this.mActivity.finish();
                zza(this.zzFL.getProductId(), z, resultCode, data);
            } catch (RemoteException e) {
                zzb.zzaK("Fail to process purchase result.");
                this.mActivity.finish();
            } finally {
                this.zzFO = null;
            }
        }
    }

    public void onCreate() {
        GInAppPurchaseManagerInfoParcel zzc = GInAppPurchaseManagerInfoParcel.zzc(this.mActivity.getIntent());
        this.zzFN = zzc.zzFy;
        this.zzFF = zzc.zzrI;
        this.zzFL = zzc.zzFw;
        this.zzFC = new zzb(this.mActivity.getApplicationContext());
        this.zzFK = zzc.zzFx;
        if (this.mActivity.getResources().getConfiguration().orientation == 2) {
            this.mActivity.setRequestedOrientation(zzr.zzbE().zzhv());
        } else {
            this.mActivity.setRequestedOrientation(zzr.zzbE().zzhw());
        }
        Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        intent.setPackage(com.google.android.gms.common.zze.GOOGLE_PLAY_STORE_PACKAGE);
        this.mActivity.bindService(intent, this, 1);
    }

    public void onDestroy() {
        this.mActivity.unbindService(this);
        this.zzFC.destroy();
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
        Throwable e;
        this.zzFC.zzN(service);
        try {
            this.zzFO = this.zzFF.zzfZ();
            Bundle zzb = this.zzFC.zzb(this.mActivity.getPackageName(), this.zzFL.getProductId(), this.zzFO);
            PendingIntent pendingIntent = (PendingIntent) zzb.getParcelable(Constants.BUY_INTENT);
            if (pendingIntent == null) {
                int zzd = zzr.zzbM().zzd(zzb);
                this.zzFL.recordPlayBillingResolution(zzd);
                zza(this.zzFL.getProductId(), false, zzd, null);
                this.mActivity.finish();
                return;
            }
            this.zzFM = new zzf(this.zzFL.getProductId(), this.zzFO);
            this.zzFD.zzb(this.zzFM);
            this.mActivity.startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(), Integer.valueOf(0).intValue(), Integer.valueOf(0).intValue(), Integer.valueOf(0).intValue());
        } catch (RemoteException e2) {
            e = e2;
            zzb.zzd("Error when connecting in-app billing service", e);
            this.mActivity.finish();
        } catch (SendIntentException e3) {
            e = e3;
            zzb.zzd("Error when connecting in-app billing service", e);
            this.mActivity.finish();
        }
    }

    public void onServiceDisconnected(ComponentName name) {
        zzb.zzaJ("In-app billing service disconnected.");
        this.zzFC.destroy();
    }

    protected void zza(String str, boolean z, int i, Intent intent) {
        if (this.zzFN != null) {
            this.zzFN.zza(str, z, i, intent, this.zzFM);
        }
    }
}
