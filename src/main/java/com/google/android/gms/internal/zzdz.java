package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.nativex.monetization.mraid.objects.ObjectNames.CalendarEntryData;
import com.supersonic.adapters.supersonicads.SupersonicConfig;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@zzhb
class zzdz {
    private final Object[] mParams;
    private boolean zzAA;

    zzdz(AdRequestParcel adRequestParcel, String str, int i) {
        this.mParams = zza(adRequestParcel, str, i);
    }

    private static Object[] zza(AdRequestParcel adRequestParcel, String str, int i) {
        Set hashSet = new HashSet(Arrays.asList(((String) zzbt.zzwF.get()).split(",")));
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        if (hashSet.contains("networkType")) {
            arrayList.add(Integer.valueOf(i));
        }
        if (hashSet.contains("birthday")) {
            arrayList.add(Long.valueOf(adRequestParcel.zztC));
        }
        if (hashSet.contains("extras")) {
            arrayList.add(zzc(adRequestParcel.extras));
        }
        if (hashSet.contains(SupersonicConfig.GENDER)) {
            arrayList.add(Integer.valueOf(adRequestParcel.zztD));
        }
        if (hashSet.contains("keywords")) {
            if (adRequestParcel.zztE != null) {
                arrayList.add(adRequestParcel.zztE.toString());
            } else {
                arrayList.add(null);
            }
        }
        if (hashSet.contains("isTestDevice")) {
            arrayList.add(Boolean.valueOf(adRequestParcel.zztF));
        }
        if (hashSet.contains("tagForChildDirectedTreatment")) {
            arrayList.add(Integer.valueOf(adRequestParcel.zztG));
        }
        if (hashSet.contains("manualImpressionsEnabled")) {
            arrayList.add(Boolean.valueOf(adRequestParcel.zztH));
        }
        if (hashSet.contains("publisherProvidedId")) {
            arrayList.add(adRequestParcel.zztI);
        }
        if (hashSet.contains(CalendarEntryData.LOCATION)) {
            if (adRequestParcel.zztK != null) {
                arrayList.add(adRequestParcel.zztK.toString());
            } else {
                arrayList.add(null);
            }
        }
        if (hashSet.contains("contentUrl")) {
            arrayList.add(adRequestParcel.zztL);
        }
        if (hashSet.contains("networkExtras")) {
            arrayList.add(zzc(adRequestParcel.zztM));
        }
        if (hashSet.contains("customTargeting")) {
            arrayList.add(zzc(adRequestParcel.zztN));
        }
        if (hashSet.contains("categoryExclusions")) {
            if (adRequestParcel.zztO != null) {
                arrayList.add(adRequestParcel.zztO.toString());
            } else {
                arrayList.add(null);
            }
        }
        if (hashSet.contains("requestAgent")) {
            arrayList.add(adRequestParcel.zztP);
        }
        if (hashSet.contains("requestPackage")) {
            arrayList.add(adRequestParcel.zztQ);
        }
        return arrayList.toArray();
    }

    private static String zzc(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Collections.sort(new ArrayList(bundle.keySet()));
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            String str2 = obj == null ? "null" : obj instanceof Bundle ? zzc((Bundle) obj) : obj.toString();
            stringBuilder.append(str2);
        }
        return stringBuilder.toString();
    }

    public boolean equals(Object o) {
        if (!(o instanceof zzdz)) {
            return false;
        }
        return Arrays.equals(this.mParams, ((zzdz) o).mParams);
    }

    public int hashCode() {
        return Arrays.hashCode(this.mParams);
    }

    public String toString() {
        return "[InterstitialAdPoolKey " + Arrays.toString(this.mParams) + RequestParameters.RIGHT_BRACKETS;
    }

    void zzeg() {
        this.zzAA = true;
    }

    boolean zzeh() {
        return this.zzAA;
    }
}
