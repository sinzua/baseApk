package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.internal.reward.client.RewardedVideoAdRequestParcel;
import com.google.android.gms.ads.search.SearchAdRequest;
import com.google.android.gms.internal.zzhb;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@zzhb
public class zzh {
    public static final zzh zzug = new zzh();

    protected zzh() {
    }

    public static zzh zzcO() {
        return zzug;
    }

    public AdRequestParcel zza(Context context, zzaa com_google_android_gms_ads_internal_client_zzaa) {
        Date birthday = com_google_android_gms_ads_internal_client_zzaa.getBirthday();
        long time = birthday != null ? birthday.getTime() : -1;
        String contentUrl = com_google_android_gms_ads_internal_client_zzaa.getContentUrl();
        int gender = com_google_android_gms_ads_internal_client_zzaa.getGender();
        Collection keywords = com_google_android_gms_ads_internal_client_zzaa.getKeywords();
        List unmodifiableList = !keywords.isEmpty() ? Collections.unmodifiableList(new ArrayList(keywords)) : null;
        boolean isTestDevice = com_google_android_gms_ads_internal_client_zzaa.isTestDevice(context);
        int zzdd = com_google_android_gms_ads_internal_client_zzaa.zzdd();
        Location location = com_google_android_gms_ads_internal_client_zzaa.getLocation();
        Bundle networkExtrasBundle = com_google_android_gms_ads_internal_client_zzaa.getNetworkExtrasBundle(AdMobAdapter.class);
        boolean manualImpressionsEnabled = com_google_android_gms_ads_internal_client_zzaa.getManualImpressionsEnabled();
        String publisherProvidedId = com_google_android_gms_ads_internal_client_zzaa.getPublisherProvidedId();
        SearchAdRequest zzda = com_google_android_gms_ads_internal_client_zzaa.zzda();
        SearchAdRequestParcel searchAdRequestParcel = zzda != null ? new SearchAdRequestParcel(zzda) : null;
        String str = null;
        Context applicationContext = context.getApplicationContext();
        if (applicationContext != null) {
            str = zzn.zzcS().zza(Thread.currentThread().getStackTrace(), applicationContext.getPackageName());
        }
        return new AdRequestParcel(7, time, networkExtrasBundle, gender, unmodifiableList, isTestDevice, zzdd, manualImpressionsEnabled, publisherProvidedId, searchAdRequestParcel, location, contentUrl, com_google_android_gms_ads_internal_client_zzaa.zzdc(), com_google_android_gms_ads_internal_client_zzaa.getCustomTargeting(), Collections.unmodifiableList(new ArrayList(com_google_android_gms_ads_internal_client_zzaa.zzde())), com_google_android_gms_ads_internal_client_zzaa.zzcZ(), str, com_google_android_gms_ads_internal_client_zzaa.isDesignedForFamilies());
    }

    public RewardedVideoAdRequestParcel zza(Context context, zzaa com_google_android_gms_ads_internal_client_zzaa, String str) {
        return new RewardedVideoAdRequestParcel(zza(context, com_google_android_gms_ads_internal_client_zzaa), str);
    }
}
