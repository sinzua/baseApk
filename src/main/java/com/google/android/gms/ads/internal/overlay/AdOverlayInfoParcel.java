package com.google.android.gms.ads.internal.overlay;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.ads.internal.InterstitialAdParameterParcel;
import com.google.android.gms.ads.internal.client.zza;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzdb;
import com.google.android.gms.internal.zzdh;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzjp;

@zzhb
public final class AdOverlayInfoParcel implements SafeParcelable {
    public static final zzf CREATOR = new zzf();
    public final int orientation;
    public final String url;
    public final int versionCode;
    public final AdLauncherIntentInfoParcel zzEA;
    public final zza zzEB;
    public final zzg zzEC;
    public final zzjp zzED;
    public final zzdb zzEE;
    public final String zzEF;
    public final boolean zzEG;
    public final String zzEH;
    public final zzp zzEI;
    public final int zzEJ;
    public final zzdh zzEK;
    public final String zzEL;
    public final InterstitialAdParameterParcel zzEM;
    public final VersionInfoParcel zzrl;

    AdOverlayInfoParcel(int versionCode, AdLauncherIntentInfoParcel adLauncherIntentInfo, IBinder wrappedAdClickListener, IBinder wrappedAdOverlayListener, IBinder wrappedAdWebView, IBinder wrappedAppEventGmsgListener, String baseUrl, boolean customClose, String html, IBinder wrappedLeaveApplicationListener, int orientation, int overlayType, String url, VersionInfoParcel versionInfo, IBinder wrappedInAppPurchaseGmsgListener, String debugMessage, InterstitialAdParameterParcel interstitialAdParameter) {
        this.versionCode = versionCode;
        this.zzEA = adLauncherIntentInfo;
        this.zzEB = (zza) zze.zzp(zzd.zza.zzbs(wrappedAdClickListener));
        this.zzEC = (zzg) zze.zzp(zzd.zza.zzbs(wrappedAdOverlayListener));
        this.zzED = (zzjp) zze.zzp(zzd.zza.zzbs(wrappedAdWebView));
        this.zzEE = (zzdb) zze.zzp(zzd.zza.zzbs(wrappedAppEventGmsgListener));
        this.zzEF = baseUrl;
        this.zzEG = customClose;
        this.zzEH = html;
        this.zzEI = (zzp) zze.zzp(zzd.zza.zzbs(wrappedLeaveApplicationListener));
        this.orientation = orientation;
        this.zzEJ = overlayType;
        this.url = url;
        this.zzrl = versionInfo;
        this.zzEK = (zzdh) zze.zzp(zzd.zza.zzbs(wrappedInAppPurchaseGmsgListener));
        this.zzEL = debugMessage;
        this.zzEM = interstitialAdParameter;
    }

    public AdOverlayInfoParcel(zza adClickListener, zzg adOverlayListener, zzp leaveApplicationListener, zzjp adWebView, int orientation, VersionInfoParcel versionInfo, String debugMessage, InterstitialAdParameterParcel interstitialAdParameter) {
        this.versionCode = 4;
        this.zzEA = null;
        this.zzEB = adClickListener;
        this.zzEC = adOverlayListener;
        this.zzED = adWebView;
        this.zzEE = null;
        this.zzEF = null;
        this.zzEG = false;
        this.zzEH = null;
        this.zzEI = leaveApplicationListener;
        this.orientation = orientation;
        this.zzEJ = 1;
        this.url = null;
        this.zzrl = versionInfo;
        this.zzEK = null;
        this.zzEL = debugMessage;
        this.zzEM = interstitialAdParameter;
    }

    public AdOverlayInfoParcel(zza adClickListener, zzg adOverlayListener, zzp leaveApplicationListener, zzjp adWebView, boolean customClose, int orientation, VersionInfoParcel versionInfo) {
        this.versionCode = 4;
        this.zzEA = null;
        this.zzEB = adClickListener;
        this.zzEC = adOverlayListener;
        this.zzED = adWebView;
        this.zzEE = null;
        this.zzEF = null;
        this.zzEG = customClose;
        this.zzEH = null;
        this.zzEI = leaveApplicationListener;
        this.orientation = orientation;
        this.zzEJ = 2;
        this.url = null;
        this.zzrl = versionInfo;
        this.zzEK = null;
        this.zzEL = null;
        this.zzEM = null;
    }

    public AdOverlayInfoParcel(zza adClickListener, zzg adOverlayListener, zzdb appEventGmsgListener, zzp leaveApplicationListener, zzjp adWebView, boolean customClose, int orientation, String url, VersionInfoParcel versionInfo, zzdh inAppPurchaseGmsgListener) {
        this.versionCode = 4;
        this.zzEA = null;
        this.zzEB = adClickListener;
        this.zzEC = adOverlayListener;
        this.zzED = adWebView;
        this.zzEE = appEventGmsgListener;
        this.zzEF = null;
        this.zzEG = customClose;
        this.zzEH = null;
        this.zzEI = leaveApplicationListener;
        this.orientation = orientation;
        this.zzEJ = 3;
        this.url = url;
        this.zzrl = versionInfo;
        this.zzEK = inAppPurchaseGmsgListener;
        this.zzEL = null;
        this.zzEM = null;
    }

    public AdOverlayInfoParcel(zza adClickListener, zzg adOverlayListener, zzdb appEventGmsgListener, zzp leaveApplicationListener, zzjp adWebView, boolean customClose, int orientation, String html, String baseUrl, VersionInfoParcel versionInfo, zzdh inAppPurchaseGmsgListener) {
        this.versionCode = 4;
        this.zzEA = null;
        this.zzEB = adClickListener;
        this.zzEC = adOverlayListener;
        this.zzED = adWebView;
        this.zzEE = appEventGmsgListener;
        this.zzEF = baseUrl;
        this.zzEG = customClose;
        this.zzEH = html;
        this.zzEI = leaveApplicationListener;
        this.orientation = orientation;
        this.zzEJ = 3;
        this.url = null;
        this.zzrl = versionInfo;
        this.zzEK = inAppPurchaseGmsgListener;
        this.zzEL = null;
        this.zzEM = null;
    }

    public AdOverlayInfoParcel(AdLauncherIntentInfoParcel adLauncherIntentInfo, zza adClickListener, zzg adOverlayListener, zzp leaveApplicationListener, VersionInfoParcel versionInfo) {
        this.versionCode = 4;
        this.zzEA = adLauncherIntentInfo;
        this.zzEB = adClickListener;
        this.zzEC = adOverlayListener;
        this.zzED = null;
        this.zzEE = null;
        this.zzEF = null;
        this.zzEG = false;
        this.zzEH = null;
        this.zzEI = leaveApplicationListener;
        this.orientation = -1;
        this.zzEJ = 4;
        this.url = null;
        this.zzrl = versionInfo;
        this.zzEK = null;
        this.zzEL = null;
        this.zzEM = null;
    }

    public static void zza(Intent intent, AdOverlayInfoParcel adOverlayInfoParcel) {
        Bundle bundle = new Bundle(1);
        bundle.putParcelable("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo", adOverlayInfoParcel);
        intent.putExtra("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo", bundle);
    }

    public static AdOverlayInfoParcel zzb(Intent intent) {
        try {
            Bundle bundleExtra = intent.getBundleExtra("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo");
            bundleExtra.setClassLoader(AdOverlayInfoParcel.class.getClassLoader());
            return (AdOverlayInfoParcel) bundleExtra.getParcelable("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo");
        } catch (Exception e) {
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzf.zza(this, out, flags);
    }

    IBinder zzfs() {
        return zze.zzC(this.zzEB).asBinder();
    }

    IBinder zzft() {
        return zze.zzC(this.zzEC).asBinder();
    }

    IBinder zzfu() {
        return zze.zzC(this.zzED).asBinder();
    }

    IBinder zzfv() {
        return zze.zzC(this.zzEE).asBinder();
    }

    IBinder zzfw() {
        return zze.zzC(this.zzEK).asBinder();
    }

    IBinder zzfx() {
        return zze.zzC(this.zzEI).asBinder();
    }
}
