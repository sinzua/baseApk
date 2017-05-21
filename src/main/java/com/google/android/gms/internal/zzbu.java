package com.google.android.gms.internal;

import android.content.Context;
import android.os.Build.VERSION;
import com.google.android.gms.ads.internal.zzr;
import com.nativex.common.JsonRequestConstants.UDIDs;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.util.LinkedHashMap;
import java.util.Map;

@zzhb
public class zzbu {
    private Context mContext = null;
    private String zzsy = null;
    private boolean zzxi;
    private String zzxj;
    private Map<String, String> zzxk;

    public zzbu(Context context, String str) {
        this.mContext = context;
        this.zzsy = str;
        this.zzxi = ((Boolean) zzbt.zzwg.get()).booleanValue();
        this.zzxj = (String) zzbt.zzwh.get();
        this.zzxk = new LinkedHashMap();
        this.zzxk.put("s", "gmob_sdk");
        this.zzxk.put("v", UDIDs.ANDROID_DEVICE_ID);
        this.zzxk.put("os", VERSION.RELEASE);
        this.zzxk.put("sdk", VERSION.SDK);
        this.zzxk.put(ParametersKeys.ORIENTATION_DEVICE, zzr.zzbC().zzht());
        this.zzxk.put("app", context.getApplicationContext() != null ? context.getApplicationContext().getPackageName() : context.getPackageName());
        zzhj zzE = zzr.zzbI().zzE(this.mContext);
        this.zzxk.put("network_coarse", Integer.toString(zzE.zzKc));
        this.zzxk.put("network_fine", Integer.toString(zzE.zzKd));
    }

    Context getContext() {
        return this.mContext;
    }

    String zzcs() {
        return this.zzsy;
    }

    boolean zzdu() {
        return this.zzxi;
    }

    String zzdv() {
        return this.zzxj;
    }

    Map<String, String> zzdw() {
        return this.zzxk;
    }
}
