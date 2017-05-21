package com.google.android.gms.ads.internal.request;

import android.os.Parcel;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.reward.mediation.client.RewardItemParcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzhb;
import java.util.Collections;
import java.util.List;

@zzhb
public final class AdResponseParcel implements SafeParcelable {
    public static final zzh CREATOR = new zzh();
    public String body;
    public final int errorCode;
    public final int orientation;
    public final int versionCode;
    public final List<String> zzBQ;
    public final List<String> zzBR;
    public final long zzBU;
    private AdRequestInfoParcel zzCu;
    public final String zzEF;
    public final boolean zzHB;
    public final long zzHS;
    public final boolean zzHT;
    public final long zzHU;
    public final List<String> zzHV;
    public final String zzHW;
    public final long zzHX;
    public final String zzHY;
    public final boolean zzHZ;
    public final String zzIa;
    public final String zzIb;
    public final boolean zzIc;
    public final boolean zzId;
    public final boolean zzIe;
    public final int zzIf;
    public LargeParcelTeleporter zzIg;
    public String zzIh;
    public String zzIi;
    @Nullable
    public RewardItemParcel zzIj;
    @Nullable
    public List<String> zzIk;
    @Nullable
    public List<String> zzIl;
    @Nullable
    public boolean zzIm;
    public final boolean zzuk;
    public boolean zzul;
    public boolean zzum;

    public AdResponseParcel(int errorCode) {
        this(16, null, null, null, errorCode, null, -1, false, -1, null, -1, -1, null, -1, null, false, null, null, false, false, false, true, false, 0, null, null, null, false, false, null, null, null, false);
    }

    public AdResponseParcel(int errorCode, long refreshIntervalInMillis) {
        this(16, null, null, null, errorCode, null, -1, false, -1, null, refreshIntervalInMillis, -1, null, -1, null, false, null, null, false, false, false, true, false, 0, null, null, null, false, false, null, null, null, false);
    }

    AdResponseParcel(int versionCode, String baseUrl, String body, List<String> clickUrls, int errorCode, List<String> impressionUrls, long interstitialTimeoutInMillis, boolean isMediation, long mediationConfigCacheTimeInMillis, List<String> manualTrackingUrls, long refreshIntervalInMillis, int orientation, String adSizeString, long fetchTime, String debugDialog, boolean isJavascriptTag, String passbackUrl, String activeViewJSON, boolean isCustomRenderAllowed, boolean isNative, boolean useHTTPS, boolean contentUrlOptedOut, boolean isPrefetched, int panTokenStatus, LargeParcelTeleporter bodyTeleporter, String csiLatencyInfo, String gwsQueryId, boolean isFluid, boolean isNativeExpress, RewardItemParcel rewardItemParcel, List<String> rewardVideoStartUrls, List<String> rewardVideoGrantedUrls, boolean isUsingDisplayedImpression) {
        this.versionCode = versionCode;
        this.zzEF = baseUrl;
        this.body = body;
        this.zzBQ = clickUrls != null ? Collections.unmodifiableList(clickUrls) : null;
        this.errorCode = errorCode;
        this.zzBR = impressionUrls != null ? Collections.unmodifiableList(impressionUrls) : null;
        this.zzHS = interstitialTimeoutInMillis;
        this.zzHT = isMediation;
        this.zzHU = mediationConfigCacheTimeInMillis;
        this.zzHV = manualTrackingUrls != null ? Collections.unmodifiableList(manualTrackingUrls) : null;
        this.zzBU = refreshIntervalInMillis;
        this.orientation = orientation;
        this.zzHW = adSizeString;
        this.zzHX = fetchTime;
        this.zzHY = debugDialog;
        this.zzHZ = isJavascriptTag;
        this.zzIa = passbackUrl;
        this.zzIb = activeViewJSON;
        this.zzIc = isCustomRenderAllowed;
        this.zzuk = isNative;
        this.zzHB = useHTTPS;
        this.zzId = contentUrlOptedOut;
        this.zzIe = isPrefetched;
        this.zzIf = panTokenStatus;
        this.zzIg = bodyTeleporter;
        this.zzIh = csiLatencyInfo;
        this.zzIi = gwsQueryId;
        if (this.body == null && this.zzIg != null) {
            StringParcel stringParcel = (StringParcel) this.zzIg.zza(StringParcel.CREATOR);
            if (!(stringParcel == null || TextUtils.isEmpty(stringParcel.zzgz()))) {
                this.body = stringParcel.zzgz();
            }
        }
        this.zzul = isFluid;
        this.zzum = isNativeExpress;
        this.zzIj = rewardItemParcel;
        this.zzIk = rewardVideoStartUrls;
        this.zzIl = rewardVideoGrantedUrls;
        this.zzIm = isUsingDisplayedImpression;
    }

    public AdResponseParcel(AdRequestInfoParcel adRequestInfo, String baseUrl, String body, List<String> clickUrls, List<String> impressionUrls, long interstitialTimeoutInMillis, boolean isMediation, long mediationConfigCacheTimeInMillis, List<String> manualTrackingUrls, long refreshIntervalInMillis, int orientation, String adSizeString, long fetchTime, String debugDialog, String activeViewJSON, boolean isCustomRenderAllowed, boolean isNative, boolean useHTTPS, boolean contentUrlOptedOut, boolean isPrefetched, int panTokenStatus, String gwsQueryId, boolean isFluid, boolean isNativeExpress, RewardItemParcel rewardItemParcel, List<String> rewardVideoStartUrls, List<String> rewardGrantedUrls, boolean isUsingDisplayedImpression) {
        this(16, baseUrl, body, clickUrls, -2, impressionUrls, interstitialTimeoutInMillis, isMediation, mediationConfigCacheTimeInMillis, manualTrackingUrls, refreshIntervalInMillis, orientation, adSizeString, fetchTime, debugDialog, false, null, activeViewJSON, isCustomRenderAllowed, isNative, useHTTPS, contentUrlOptedOut, isPrefetched, panTokenStatus, null, null, gwsQueryId, isFluid, isNativeExpress, rewardItemParcel, rewardVideoStartUrls, rewardGrantedUrls, isUsingDisplayedImpression);
        this.zzCu = adRequestInfo;
    }

    public AdResponseParcel(AdRequestInfoParcel adRequestInfo, String baseUrl, String body, List<String> clickUrls, List<String> impressionUrls, long interstitialTimeoutInMillis, boolean isMediation, long mediationConfigCacheTimeInMillis, List<String> manualTrackingUrls, long refreshIntervalInMillis, int orientation, String adSizeString, long fetchTime, String debugDialog, boolean isJavascriptTag, String passbackUrl, String activeViewJSON, boolean isCustomRenderAllowed, boolean isNative, boolean useHTTPS, boolean contentUrlOptedOut, boolean isPrefetched, int panTokenStatus, String gwsQueryId, boolean isFluid, boolean isNativeExpress, RewardItemParcel rewardItemParcel, List<String> rewardVideoStartUrls, List<String> rewardGrantedUrls, boolean isUsingDisplayedImpression) {
        this(16, baseUrl, body, clickUrls, -2, impressionUrls, interstitialTimeoutInMillis, isMediation, mediationConfigCacheTimeInMillis, manualTrackingUrls, refreshIntervalInMillis, orientation, adSizeString, fetchTime, debugDialog, isJavascriptTag, passbackUrl, activeViewJSON, isCustomRenderAllowed, isNative, useHTTPS, contentUrlOptedOut, isPrefetched, panTokenStatus, null, null, gwsQueryId, isFluid, isNativeExpress, rewardItemParcel, rewardVideoStartUrls, rewardGrantedUrls, isUsingDisplayedImpression);
        this.zzCu = adRequestInfo;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        if (!(this.zzCu == null || this.zzCu.versionCode < 9 || TextUtils.isEmpty(this.body))) {
            this.zzIg = new LargeParcelTeleporter(new StringParcel(this.body));
            this.body = null;
        }
        zzh.zza(this, out, flags);
    }
}
