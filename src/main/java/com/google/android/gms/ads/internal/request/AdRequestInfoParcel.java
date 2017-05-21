package com.google.android.gms.ads.internal.request;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Messenger;
import android.os.Parcel;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzhb;
import java.util.Collections;
import java.util.List;

@zzhb
public final class AdRequestInfoParcel implements SafeParcelable {
    public static final zzf CREATOR = new zzf();
    public final ApplicationInfo applicationInfo;
    public final int versionCode;
    public final Bundle zzHA;
    public final boolean zzHB;
    public final Messenger zzHC;
    public final int zzHD;
    public final int zzHE;
    public final float zzHF;
    public final String zzHG;
    public final long zzHH;
    public final String zzHI;
    public final List<String> zzHJ;
    public final List<String> zzHK;
    public final long zzHL;
    public final CapabilityParcel zzHM;
    public final String zzHN;
    public final float zzHO;
    public final int zzHP;
    public final int zzHQ;
    public final Bundle zzHs;
    public final AdRequestParcel zzHt;
    public final PackageInfo zzHu;
    public final String zzHv;
    public final String zzHw;
    public final String zzHx;
    public final Bundle zzHy;
    public final int zzHz;
    public final NativeAdOptionsParcel zzrD;
    public final List<String> zzrH;
    public final String zzri;
    public final String zzrj;
    public final VersionInfoParcel zzrl;
    public final AdSizeParcel zzrp;

    @zzhb
    public static final class zza {
        public final ApplicationInfo applicationInfo;
        public final Bundle zzHA;
        public final boolean zzHB;
        public final Messenger zzHC;
        public final int zzHD;
        public final int zzHE;
        public final float zzHF;
        public final String zzHG;
        public final long zzHH;
        public final String zzHI;
        public final List<String> zzHJ;
        public final List<String> zzHK;
        public final CapabilityParcel zzHM;
        public final String zzHN;
        public final float zzHO;
        public final int zzHP;
        public final int zzHQ;
        public final Bundle zzHs;
        public final AdRequestParcel zzHt;
        public final PackageInfo zzHu;
        public final String zzHw;
        public final String zzHx;
        public final Bundle zzHy;
        public final int zzHz;
        public final NativeAdOptionsParcel zzrD;
        public final List<String> zzrH;
        public final String zzri;
        public final String zzrj;
        public final VersionInfoParcel zzrl;
        public final AdSizeParcel zzrp;

        public zza(Bundle bundle, AdRequestParcel adRequestParcel, AdSizeParcel adSizeParcel, String str, ApplicationInfo applicationInfo, PackageInfo packageInfo, String str2, String str3, VersionInfoParcel versionInfoParcel, Bundle bundle2, List<String> list, List<String> list2, Bundle bundle3, boolean z, Messenger messenger, int i, int i2, float f, String str4, long j, String str5, List<String> list3, String str6, NativeAdOptionsParcel nativeAdOptionsParcel, CapabilityParcel capabilityParcel, String str7, float f2, int i3, int i4) {
            this.zzHs = bundle;
            this.zzHt = adRequestParcel;
            this.zzrp = adSizeParcel;
            this.zzrj = str;
            this.applicationInfo = applicationInfo;
            this.zzHu = packageInfo;
            this.zzHw = str2;
            this.zzHx = str3;
            this.zzrl = versionInfoParcel;
            this.zzHy = bundle2;
            this.zzHB = z;
            this.zzHC = messenger;
            this.zzHD = i;
            this.zzHE = i2;
            this.zzHF = f;
            if (list == null || list.size() <= 0) {
                if (adSizeParcel.zzum) {
                    this.zzHz = 4;
                } else {
                    this.zzHz = 0;
                }
                this.zzrH = null;
                this.zzHK = null;
            } else {
                this.zzHz = 3;
                this.zzrH = list;
                this.zzHK = list2;
            }
            this.zzHA = bundle3;
            this.zzHG = str4;
            this.zzHH = j;
            this.zzHI = str5;
            this.zzHJ = list3;
            this.zzri = str6;
            this.zzrD = nativeAdOptionsParcel;
            this.zzHM = capabilityParcel;
            this.zzHN = str7;
            this.zzHO = f2;
            this.zzHP = i3;
            this.zzHQ = i4;
        }
    }

    AdRequestInfoParcel(int versionCode, Bundle adPositionBundle, AdRequestParcel adRequest, AdSizeParcel adSize, String adUnitId, ApplicationInfo applicationInfo, PackageInfo packageInfo, String querySpamSignals, String sequenceNumber, String sessionId, VersionInfoParcel versionInfo, Bundle stats, int nativeVersion, List<String> nativeTemplates, Bundle contentInfo, boolean useHTTPS, Messenger prefetchMessenger, int screenWidth, int screenHeight, float screenDensity, String viewHierarchy, long correlationId, String requestId, List<String> experimentIds, String slotId, NativeAdOptionsParcel nativeAdOptions, List<String> nativeCustomTemplateIds, long connectionStartTime, CapabilityParcel capabilityParcel, String anchorStatus, float appVolume, int targetApi, int adapterViewPosition) {
        this.versionCode = versionCode;
        this.zzHs = adPositionBundle;
        this.zzHt = adRequest;
        this.zzrp = adSize;
        this.zzrj = adUnitId;
        this.applicationInfo = applicationInfo;
        this.zzHu = packageInfo;
        this.zzHv = querySpamSignals;
        this.zzHw = sequenceNumber;
        this.zzHx = sessionId;
        this.zzrl = versionInfo;
        this.zzHy = stats;
        this.zzHz = nativeVersion;
        this.zzrH = nativeTemplates;
        this.zzHK = nativeCustomTemplateIds == null ? Collections.emptyList() : Collections.unmodifiableList(nativeCustomTemplateIds);
        this.zzHA = contentInfo;
        this.zzHB = useHTTPS;
        this.zzHC = prefetchMessenger;
        this.zzHD = screenWidth;
        this.zzHE = screenHeight;
        this.zzHF = screenDensity;
        this.zzHG = viewHierarchy;
        this.zzHH = correlationId;
        this.zzHI = requestId;
        this.zzHJ = experimentIds == null ? Collections.emptyList() : Collections.unmodifiableList(experimentIds);
        this.zzri = slotId;
        this.zzrD = nativeAdOptions;
        this.zzHL = connectionStartTime;
        this.zzHM = capabilityParcel;
        this.zzHN = anchorStatus;
        this.zzHO = appVolume;
        this.zzHP = targetApi;
        this.zzHQ = adapterViewPosition;
    }

    public AdRequestInfoParcel(Bundle adPositionBundle, AdRequestParcel adRequest, AdSizeParcel adSize, String adUnitId, ApplicationInfo applicationInfo, PackageInfo packageInfo, String querySpamSignals, String sequenceNumber, String sessionId, VersionInfoParcel versionInfo, Bundle stats, int nativeVersion, List<String> nativeTemplates, List<String> nativeCustomTemplateIds, Bundle contentInfo, boolean useHTTPS, Messenger prefetchMessenger, int screenWidth, int screenHeight, float screenDensity, String viewHierarchy, long correlationId, String requestId, List<String> experimentIds, String slotId, NativeAdOptionsParcel nativeAdOptionsParcel, long connectionStartTime, CapabilityParcel capabilityParcel, String anchorStatus, float appVolume, int targetApi, int adapterViewPosition) {
        this(15, adPositionBundle, adRequest, adSize, adUnitId, applicationInfo, packageInfo, querySpamSignals, sequenceNumber, sessionId, versionInfo, stats, nativeVersion, nativeTemplates, contentInfo, useHTTPS, prefetchMessenger, screenWidth, screenHeight, screenDensity, viewHierarchy, correlationId, requestId, experimentIds, slotId, nativeAdOptionsParcel, nativeCustomTemplateIds, connectionStartTime, capabilityParcel, anchorStatus, appVolume, targetApi, adapterViewPosition);
    }

    public AdRequestInfoParcel(zza partialAdRequestInfo, String querySpamSignals, long connectionStartTime) {
        this(partialAdRequestInfo.zzHs, partialAdRequestInfo.zzHt, partialAdRequestInfo.zzrp, partialAdRequestInfo.zzrj, partialAdRequestInfo.applicationInfo, partialAdRequestInfo.zzHu, querySpamSignals, partialAdRequestInfo.zzHw, partialAdRequestInfo.zzHx, partialAdRequestInfo.zzrl, partialAdRequestInfo.zzHy, partialAdRequestInfo.zzHz, partialAdRequestInfo.zzrH, partialAdRequestInfo.zzHK, partialAdRequestInfo.zzHA, partialAdRequestInfo.zzHB, partialAdRequestInfo.zzHC, partialAdRequestInfo.zzHD, partialAdRequestInfo.zzHE, partialAdRequestInfo.zzHF, partialAdRequestInfo.zzHG, partialAdRequestInfo.zzHH, partialAdRequestInfo.zzHI, partialAdRequestInfo.zzHJ, partialAdRequestInfo.zzri, partialAdRequestInfo.zzrD, connectionStartTime, partialAdRequestInfo.zzHM, partialAdRequestInfo.zzHN, partialAdRequestInfo.zzHO, partialAdRequestInfo.zzHP, partialAdRequestInfo.zzHQ);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzf.zza(this, out, flags);
    }
}
