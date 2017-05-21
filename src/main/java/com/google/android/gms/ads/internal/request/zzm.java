package com.google.android.gms.ads.internal.request;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.internal.zzbm;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzdf;
import com.google.android.gms.internal.zzdg;
import com.google.android.gms.internal.zzdk;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzeg;
import com.google.android.gms.internal.zzeg.zzd;
import com.google.android.gms.internal.zzeh;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzhe;
import com.google.android.gms.internal.zzim;
import com.google.android.gms.internal.zzjp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public class zzm extends zzim {
    private static zzdk zzIA = null;
    private static zzdf zzIB = null;
    static final long zzIw = TimeUnit.SECONDS.toMillis(10);
    private static boolean zzIx = false;
    private static zzeg zzIy = null;
    private static zzdg zzIz = null;
    private static final Object zzqy = new Object();
    private final Context mContext;
    private final Object zzGg = new Object();
    private final com.google.android.gms.ads.internal.request.zza.zza zzHg;
    private final com.google.android.gms.ads.internal.request.AdRequestInfoParcel.zza zzHh;
    private zzd zzIC;

    public static class zza implements com.google.android.gms.internal.zzeg.zzb<zzed> {
        public void zza(zzed com_google_android_gms_internal_zzed) {
            zzm.zzd(com_google_android_gms_internal_zzed);
        }

        public /* synthetic */ void zze(Object obj) {
            zza((zzed) obj);
        }
    }

    public static class zzb implements com.google.android.gms.internal.zzeg.zzb<zzed> {
        public void zza(zzed com_google_android_gms_internal_zzed) {
            zzm.zzc(com_google_android_gms_internal_zzed);
        }

        public /* synthetic */ void zze(Object obj) {
            zza((zzed) obj);
        }
    }

    public static class zzc implements zzdf {
        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            String str = (String) map.get("request_id");
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Invalid request: " + ((String) map.get("errors")));
            zzm.zzIA.zzS(str);
        }
    }

    public zzm(Context context, com.google.android.gms.ads.internal.request.AdRequestInfoParcel.zza com_google_android_gms_ads_internal_request_AdRequestInfoParcel_zza, com.google.android.gms.ads.internal.request.zza.zza com_google_android_gms_ads_internal_request_zza_zza) {
        super(true);
        this.zzHg = com_google_android_gms_ads_internal_request_zza_zza;
        this.mContext = context;
        this.zzHh = com_google_android_gms_ads_internal_request_AdRequestInfoParcel_zza;
        synchronized (zzqy) {
            if (!zzIx) {
                zzIA = new zzdk();
                zzIz = new zzdg(context.getApplicationContext(), com_google_android_gms_ads_internal_request_AdRequestInfoParcel_zza.zzrl);
                zzIB = new zzc();
                zzIy = new zzeg(this.mContext.getApplicationContext(), this.zzHh.zzrl, (String) zzbt.zzvB.get(), new zzb(), new zza());
                zzIx = true;
            }
        }
    }

    private JSONObject zza(AdRequestInfoParcel adRequestInfoParcel, String str) {
        Info advertisingIdInfo;
        Throwable e;
        Object obj;
        JSONObject jSONObject = null;
        Bundle bundle = adRequestInfoParcel.zzHt.extras.getBundle("sdk_less_server_data");
        String string = adRequestInfoParcel.zzHt.extras.getString("sdk_less_network_id");
        if (bundle != null) {
            JSONObject zza = zzhe.zza(this.mContext, adRequestInfoParcel, zzr.zzbI().zzE(this.mContext), jSONObject, jSONObject, new zzbm((String) zzbt.zzvB.get()), jSONObject, jSONObject, new ArrayList(), jSONObject);
            if (zza != null) {
                Map hashMap;
                try {
                    advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.mContext);
                } catch (IOException e2) {
                    e = e2;
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Cannot get advertising id info", e);
                    obj = jSONObject;
                    hashMap = new HashMap();
                    hashMap.put("request_id", str);
                    hashMap.put("network_id", string);
                    hashMap.put("request_param", zza);
                    hashMap.put("data", bundle);
                    if (advertisingIdInfo != null) {
                        hashMap.put("adid", advertisingIdInfo.getId());
                        hashMap.put("lat", Integer.valueOf(advertisingIdInfo.isLimitAdTrackingEnabled() ? 0 : 1));
                    }
                    jSONObject = zzr.zzbC().zzG(hashMap);
                    return jSONObject;
                } catch (IllegalStateException e3) {
                    e = e3;
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Cannot get advertising id info", e);
                    obj = jSONObject;
                    hashMap = new HashMap();
                    hashMap.put("request_id", str);
                    hashMap.put("network_id", string);
                    hashMap.put("request_param", zza);
                    hashMap.put("data", bundle);
                    if (advertisingIdInfo != null) {
                        hashMap.put("adid", advertisingIdInfo.getId());
                        if (advertisingIdInfo.isLimitAdTrackingEnabled()) {
                        }
                        hashMap.put("lat", Integer.valueOf(advertisingIdInfo.isLimitAdTrackingEnabled() ? 0 : 1));
                    }
                    jSONObject = zzr.zzbC().zzG(hashMap);
                    return jSONObject;
                } catch (GooglePlayServicesNotAvailableException e4) {
                    e = e4;
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Cannot get advertising id info", e);
                    obj = jSONObject;
                    hashMap = new HashMap();
                    hashMap.put("request_id", str);
                    hashMap.put("network_id", string);
                    hashMap.put("request_param", zza);
                    hashMap.put("data", bundle);
                    if (advertisingIdInfo != null) {
                        hashMap.put("adid", advertisingIdInfo.getId());
                        if (advertisingIdInfo.isLimitAdTrackingEnabled()) {
                        }
                        hashMap.put("lat", Integer.valueOf(advertisingIdInfo.isLimitAdTrackingEnabled() ? 0 : 1));
                    }
                    jSONObject = zzr.zzbC().zzG(hashMap);
                    return jSONObject;
                } catch (GooglePlayServicesRepairableException e5) {
                    e = e5;
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Cannot get advertising id info", e);
                    obj = jSONObject;
                    hashMap = new HashMap();
                    hashMap.put("request_id", str);
                    hashMap.put("network_id", string);
                    hashMap.put("request_param", zza);
                    hashMap.put("data", bundle);
                    if (advertisingIdInfo != null) {
                        hashMap.put("adid", advertisingIdInfo.getId());
                        if (advertisingIdInfo.isLimitAdTrackingEnabled()) {
                        }
                        hashMap.put("lat", Integer.valueOf(advertisingIdInfo.isLimitAdTrackingEnabled() ? 0 : 1));
                    }
                    jSONObject = zzr.zzbC().zzG(hashMap);
                    return jSONObject;
                }
                hashMap = new HashMap();
                hashMap.put("request_id", str);
                hashMap.put("network_id", string);
                hashMap.put("request_param", zza);
                hashMap.put("data", bundle);
                if (advertisingIdInfo != null) {
                    hashMap.put("adid", advertisingIdInfo.getId());
                    if (advertisingIdInfo.isLimitAdTrackingEnabled()) {
                    }
                    hashMap.put("lat", Integer.valueOf(advertisingIdInfo.isLimitAdTrackingEnabled() ? 0 : 1));
                }
                try {
                    jSONObject = zzr.zzbC().zzG(hashMap);
                } catch (JSONException e6) {
                }
            }
        }
        return jSONObject;
    }

    protected static void zzc(zzed com_google_android_gms_internal_zzed) {
        com_google_android_gms_internal_zzed.zza("/loadAd", zzIA);
        com_google_android_gms_internal_zzed.zza("/fetchHttpRequest", zzIz);
        com_google_android_gms_internal_zzed.zza("/invalidRequest", zzIB);
    }

    protected static void zzd(zzed com_google_android_gms_internal_zzed) {
        com_google_android_gms_internal_zzed.zzb("/loadAd", zzIA);
        com_google_android_gms_internal_zzed.zzb("/fetchHttpRequest", zzIz);
        com_google_android_gms_internal_zzed.zzb("/invalidRequest", zzIB);
    }

    private AdResponseParcel zze(AdRequestInfoParcel adRequestInfoParcel) {
        final String uuid = UUID.randomUUID().toString();
        final JSONObject zza = zza(adRequestInfoParcel, uuid);
        if (zza == null) {
            return new AdResponseParcel(0);
        }
        long elapsedRealtime = zzr.zzbG().elapsedRealtime();
        Future zzR = zzIA.zzR(uuid);
        com.google.android.gms.ads.internal.util.client.zza.zzMS.post(new Runnable(this) {
            final /* synthetic */ zzm zzID;

            public void run() {
                this.zzID.zzIC = zzm.zzIy.zzer();
                this.zzID.zzIC.zza(new com.google.android.gms.internal.zzji.zzc<zzeh>(this) {
                    final /* synthetic */ AnonymousClass2 zzIG;

                    {
                        this.zzIG = r1;
                    }

                    public void zzd(zzeh com_google_android_gms_internal_zzeh) {
                        try {
                            com_google_android_gms_internal_zzeh.zza("AFMA_getAdapterLessMediationAd", zza);
                        } catch (Throwable e) {
                            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error requesting an ad url", e);
                            zzm.zzIA.zzS(uuid);
                        }
                    }

                    public /* synthetic */ void zze(Object obj) {
                        zzd((zzeh) obj);
                    }
                }, new com.google.android.gms.internal.zzji.zza(this) {
                    final /* synthetic */ AnonymousClass2 zzIG;

                    {
                        this.zzIG = r1;
                    }

                    public void run() {
                        zzm.zzIA.zzS(uuid);
                    }
                });
            }
        });
        try {
            JSONObject jSONObject = (JSONObject) zzR.get(zzIw - (zzr.zzbG().elapsedRealtime() - elapsedRealtime), TimeUnit.MILLISECONDS);
            if (jSONObject == null) {
                return new AdResponseParcel(-1);
            }
            AdResponseParcel zza2 = zzhe.zza(this.mContext, adRequestInfoParcel, jSONObject.toString());
            return (zza2.errorCode == -3 || !TextUtils.isEmpty(zza2.body)) ? zza2 : new AdResponseParcel(3);
        } catch (CancellationException e) {
            return new AdResponseParcel(-1);
        } catch (InterruptedException e2) {
            return new AdResponseParcel(-1);
        } catch (TimeoutException e3) {
            return new AdResponseParcel(2);
        } catch (ExecutionException e4) {
            return new AdResponseParcel(0);
        }
    }

    public void onStop() {
        synchronized (this.zzGg) {
            com.google.android.gms.ads.internal.util.client.zza.zzMS.post(new Runnable(this) {
                final /* synthetic */ zzm zzID;

                {
                    this.zzID = r1;
                }

                public void run() {
                    if (this.zzID.zzIC != null) {
                        this.zzID.zzIC.release();
                        this.zzID.zzIC = null;
                    }
                }
            });
        }
    }

    public void zzbr() {
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("SdkLessAdLoaderBackgroundTask started.");
        AdRequestInfoParcel adRequestInfoParcel = new AdRequestInfoParcel(this.zzHh, null, -1);
        AdResponseParcel zze = zze(adRequestInfoParcel);
        AdSizeParcel adSizeParcel = null;
        final com.google.android.gms.internal.zzif.zza com_google_android_gms_internal_zzif_zza = new com.google.android.gms.internal.zzif.zza(adRequestInfoParcel, zze, null, adSizeParcel, zze.errorCode, zzr.zzbG().elapsedRealtime(), zze.zzHX, null);
        com.google.android.gms.ads.internal.util.client.zza.zzMS.post(new Runnable(this) {
            final /* synthetic */ zzm zzID;

            public void run() {
                this.zzID.zzHg.zza(com_google_android_gms_internal_zzif_zza);
                if (this.zzID.zzIC != null) {
                    this.zzID.zzIC.release();
                    this.zzID.zzIC = null;
                }
            }
        });
    }
}
