package com.google.android.gms.ads.internal.purchase;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import com.anjlab.android.iab.v3.Constants;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.zze;
import com.google.android.gms.internal.zzgh;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzim;
import com.google.android.gms.internal.zzin;
import com.google.android.gms.internal.zzir;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@zzhb
public class zzc extends zzim implements ServiceConnection {
    private Context mContext;
    private zzgh zzAK;
    private boolean zzFB;
    private zzb zzFC;
    private zzh zzFD;
    private List<zzf> zzFE;
    private zzk zzFF;
    private final Object zzpV;

    public zzc(Context context, zzgh com_google_android_gms_internal_zzgh, zzk com_google_android_gms_ads_internal_purchase_zzk) {
        this(context, com_google_android_gms_internal_zzgh, com_google_android_gms_ads_internal_purchase_zzk, new zzb(context), zzh.zzy(context.getApplicationContext()));
    }

    zzc(Context context, zzgh com_google_android_gms_internal_zzgh, zzk com_google_android_gms_ads_internal_purchase_zzk, zzb com_google_android_gms_ads_internal_purchase_zzb, zzh com_google_android_gms_ads_internal_purchase_zzh) {
        this.zzpV = new Object();
        this.zzFB = false;
        this.zzFE = null;
        this.mContext = context;
        this.zzAK = com_google_android_gms_internal_zzgh;
        this.zzFF = com_google_android_gms_ads_internal_purchase_zzk;
        this.zzFC = com_google_android_gms_ads_internal_purchase_zzb;
        this.zzFD = com_google_android_gms_ads_internal_purchase_zzh;
        this.zzFE = this.zzFD.zzg(10);
    }

    private void zze(long j) {
        do {
            if (!zzf(j)) {
                zzin.v("Timeout waiting for pending transaction to be processed.");
            }
        } while (!this.zzFB);
    }

    private boolean zzf(long j) {
        long elapsedRealtime = 60000 - (SystemClock.elapsedRealtime() - j);
        if (elapsedRealtime <= 0) {
            return false;
        }
        try {
            this.zzpV.wait(elapsedRealtime);
        } catch (InterruptedException e) {
            zzb.zzaK("waitWithTimeout_lock interrupted");
        }
        return true;
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
        synchronized (this.zzpV) {
            this.zzFC.zzN(service);
            zzfW();
            this.zzFB = true;
            this.zzpV.notify();
        }
    }

    public void onServiceDisconnected(ComponentName name) {
        zzb.zzaJ("In-app billing service disconnected.");
        this.zzFC.destroy();
    }

    public void onStop() {
        synchronized (this.zzpV) {
            com.google.android.gms.common.stats.zzb.zzrP().zza(this.mContext, this);
            this.zzFC.destroy();
        }
    }

    protected void zza(final zzf com_google_android_gms_ads_internal_purchase_zzf, String str, String str2) {
        final Intent intent = new Intent();
        zzr.zzbM();
        intent.putExtra(Constants.RESPONSE_CODE, 0);
        zzr.zzbM();
        intent.putExtra(Constants.INAPP_PURCHASE_DATA, str);
        zzr.zzbM();
        intent.putExtra(Constants.RESPONSE_INAPP_SIGNATURE, str2);
        zzir.zzMc.post(new Runnable(this) {
            final /* synthetic */ zzc zzFH;

            public void run() {
                try {
                    if (this.zzFH.zzFF.zza(com_google_android_gms_ads_internal_purchase_zzf.zzFQ, -1, intent)) {
                        this.zzFH.zzAK.zza(new zzg(this.zzFH.mContext, com_google_android_gms_ads_internal_purchase_zzf.zzFR, true, -1, intent, com_google_android_gms_ads_internal_purchase_zzf));
                    } else {
                        this.zzFH.zzAK.zza(new zzg(this.zzFH.mContext, com_google_android_gms_ads_internal_purchase_zzf.zzFR, false, -1, intent, com_google_android_gms_ads_internal_purchase_zzf));
                    }
                } catch (RemoteException e) {
                    zzb.zzaK("Fail to verify and dispatch pending transaction");
                }
            }
        });
    }

    public void zzbr() {
        synchronized (this.zzpV) {
            Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            intent.setPackage(zze.GOOGLE_PLAY_STORE_PACKAGE);
            com.google.android.gms.common.stats.zzb.zzrP().zza(this.mContext, intent, (ServiceConnection) this, 1);
            zze(SystemClock.elapsedRealtime());
            com.google.android.gms.common.stats.zzb.zzrP().zza(this.mContext, this);
            this.zzFC.destroy();
        }
    }

    protected void zzfW() {
        if (!this.zzFE.isEmpty()) {
            HashMap hashMap = new HashMap();
            for (zzf com_google_android_gms_ads_internal_purchase_zzf : this.zzFE) {
                hashMap.put(com_google_android_gms_ads_internal_purchase_zzf.zzFR, com_google_android_gms_ads_internal_purchase_zzf);
            }
            String str = null;
            while (true) {
                Bundle zzi = this.zzFC.zzi(this.mContext.getPackageName(), str);
                if (zzi == null || zzr.zzbM().zzd(zzi) != 0) {
                    break;
                }
                ArrayList stringArrayList = zzi.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList stringArrayList2 = zzi.getStringArrayList(Constants.INAPP_PURCHASE_DATA_LIST);
                ArrayList stringArrayList3 = zzi.getStringArrayList(Constants.INAPP_DATA_SIGNATURE_LIST);
                String string = zzi.getString("INAPP_CONTINUATION_TOKEN");
                for (int i = 0; i < stringArrayList.size(); i++) {
                    if (hashMap.containsKey(stringArrayList.get(i))) {
                        str = (String) stringArrayList.get(i);
                        String str2 = (String) stringArrayList2.get(i);
                        String str3 = (String) stringArrayList3.get(i);
                        zzf com_google_android_gms_ads_internal_purchase_zzf2 = (zzf) hashMap.get(str);
                        if (com_google_android_gms_ads_internal_purchase_zzf2.zzFQ.equals(zzr.zzbM().zzaq(str2))) {
                            zza(com_google_android_gms_ads_internal_purchase_zzf2, str2, str3);
                            hashMap.remove(str);
                        }
                    }
                }
                if (string == null || hashMap.isEmpty()) {
                    break;
                }
                str = string;
            }
            for (String str4 : hashMap.keySet()) {
                this.zzFD.zza((zzf) hashMap.get(str4));
            }
        }
    }
}
