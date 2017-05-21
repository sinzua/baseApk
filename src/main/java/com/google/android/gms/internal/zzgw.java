package com.google.android.gms.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.formats.zzc;
import com.google.android.gms.ads.internal.formats.zzf;
import com.google.android.gms.ads.internal.formats.zzi;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zze;
import com.nativex.common.JsonRequestConstants.UDIDs;
import com.supersonicads.sdk.utils.Constants.ControllerParameters;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public class zzgw implements Callable<zzif> {
    private static final long zzGF = TimeUnit.SECONDS.toMillis(60);
    private final Context mContext;
    private final zziw zzGG;
    private final zzp zzGH;
    private final zzee zzGI;
    private boolean zzGJ;
    private List<String> zzGK;
    private JSONObject zzGL;
    private final com.google.android.gms.internal.zzif.zza zzGd;
    private int zzGu;
    private final Object zzpV = new Object();
    private final zzan zzyt;

    public interface zza<T extends com.google.android.gms.ads.internal.formats.zzh.zza> {
        T zza(zzgw com_google_android_gms_internal_zzgw, JSONObject jSONObject) throws JSONException, InterruptedException, ExecutionException;
    }

    class zzb {
        final /* synthetic */ zzgw zzGP;
        public zzdf zzHb;

        zzb(zzgw com_google_android_gms_internal_zzgw) {
            this.zzGP = com_google_android_gms_internal_zzgw;
        }
    }

    public zzgw(Context context, zzp com_google_android_gms_ads_internal_zzp, zzee com_google_android_gms_internal_zzee, zziw com_google_android_gms_internal_zziw, zzan com_google_android_gms_internal_zzan, com.google.android.gms.internal.zzif.zza com_google_android_gms_internal_zzif_zza) {
        this.mContext = context;
        this.zzGH = com_google_android_gms_ads_internal_zzp;
        this.zzGG = com_google_android_gms_internal_zziw;
        this.zzGI = com_google_android_gms_internal_zzee;
        this.zzGd = com_google_android_gms_internal_zzif_zza;
        this.zzyt = com_google_android_gms_internal_zzan;
        this.zzGJ = false;
        this.zzGu = -2;
        this.zzGK = null;
    }

    private com.google.android.gms.ads.internal.formats.zzh.zza zza(zzed com_google_android_gms_internal_zzed, zza com_google_android_gms_internal_zzgw_zza, JSONObject jSONObject) throws ExecutionException, InterruptedException, JSONException {
        if (zzgn()) {
            return null;
        }
        JSONObject jSONObject2 = jSONObject.getJSONObject("tracking_urls_and_actions");
        String[] zzc = zzc(jSONObject2, "impression_tracking_urls");
        this.zzGK = zzc == null ? null : Arrays.asList(zzc);
        this.zzGL = jSONObject2.optJSONObject("active_view");
        com.google.android.gms.ads.internal.formats.zzh.zza zza = com_google_android_gms_internal_zzgw_zza.zza(this, jSONObject);
        if (zza == null) {
            com.google.android.gms.ads.internal.util.client.zzb.e("Failed to retrieve ad assets.");
            return null;
        }
        zza.zzb(new zzi(this.mContext, this.zzGH, com_google_android_gms_internal_zzed, this.zzyt, jSONObject, zza, this.zzGd.zzLd.zzrl));
        return zza;
    }

    private zzif zza(com.google.android.gms.ads.internal.formats.zzh.zza com_google_android_gms_ads_internal_formats_zzh_zza) {
        int i;
        synchronized (this.zzpV) {
            i = this.zzGu;
            if (com_google_android_gms_ads_internal_formats_zzh_zza == null && this.zzGu == -2) {
                i = 0;
            }
        }
        return new zzif(this.zzGd.zzLd.zzHt, null, this.zzGd.zzLe.zzBQ, i, this.zzGd.zzLe.zzBR, this.zzGK, this.zzGd.zzLe.orientation, this.zzGd.zzLe.zzBU, this.zzGd.zzLd.zzHw, false, null, null, null, null, null, 0, this.zzGd.zzrp, this.zzGd.zzLe.zzHS, this.zzGd.zzKY, this.zzGd.zzKZ, this.zzGd.zzLe.zzHY, this.zzGL, i != -2 ? null : com_google_android_gms_ads_internal_formats_zzh_zza, null, null, null, this.zzGd.zzLe.zzIm);
    }

    private zzjg<zzc> zza(JSONObject jSONObject, boolean z, boolean z2) throws JSONException {
        final String string = z ? jSONObject.getString(ParametersKeys.URL) : jSONObject.optString(ParametersKeys.URL);
        final double optDouble = jSONObject.optDouble("scale", 1.0d);
        if (TextUtils.isEmpty(string)) {
            zza(0, z);
            return new zzje(null);
        } else if (z2) {
            return new zzje(new zzc(null, Uri.parse(string), optDouble));
        } else {
            final boolean z3 = z;
            return this.zzGG.zza(string, new com.google.android.gms.internal.zziw.zza<zzc>(this) {
                final /* synthetic */ zzgw zzGP;

                public zzc zzg(InputStream inputStream) {
                    byte[] zzk;
                    try {
                        zzk = zzna.zzk(inputStream);
                    } catch (IOException e) {
                        zzk = null;
                    }
                    if (zzk == null) {
                        this.zzGP.zza(2, z3);
                        return null;
                    }
                    Bitmap decodeByteArray = BitmapFactory.decodeByteArray(zzk, 0, zzk.length);
                    if (decodeByteArray == null) {
                        this.zzGP.zza(2, z3);
                        return null;
                    }
                    decodeByteArray.setDensity((int) (160.0d * optDouble));
                    return new zzc(new BitmapDrawable(Resources.getSystem(), decodeByteArray), Uri.parse(string), optDouble);
                }

                public zzc zzgo() {
                    this.zzGP.zza(2, z3);
                    return null;
                }

                public /* synthetic */ Object zzgp() {
                    return zzgo();
                }

                public /* synthetic */ Object zzh(InputStream inputStream) {
                    return zzg(inputStream);
                }
            });
        }
    }

    private void zza(com.google.android.gms.ads.internal.formats.zzh.zza com_google_android_gms_ads_internal_formats_zzh_zza, zzed com_google_android_gms_internal_zzed) {
        if (com_google_android_gms_ads_internal_formats_zzh_zza instanceof zzf) {
            final zzf com_google_android_gms_ads_internal_formats_zzf = (zzf) com_google_android_gms_ads_internal_formats_zzh_zza;
            zzb com_google_android_gms_internal_zzgw_zzb = new zzb(this);
            zzdf anonymousClass3 = new zzdf(this) {
                final /* synthetic */ zzgw zzGP;

                public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
                    this.zzGP.zzb(com_google_android_gms_ads_internal_formats_zzf, (String) map.get("asset"));
                }
            };
            com_google_android_gms_internal_zzgw_zzb.zzHb = anonymousClass3;
            com_google_android_gms_internal_zzed.zza("/nativeAdCustomClick", anonymousClass3);
        }
    }

    private Integer zzb(JSONObject jSONObject, String str) {
        try {
            JSONObject jSONObject2 = jSONObject.getJSONObject(str);
            return Integer.valueOf(Color.rgb(jSONObject2.getInt("r"), jSONObject2.getInt("g"), jSONObject2.getInt("b")));
        } catch (JSONException e) {
            return null;
        }
    }

    private JSONObject zzb(final zzed com_google_android_gms_internal_zzed) throws TimeoutException, JSONException {
        if (zzgn()) {
            return null;
        }
        final zzjd com_google_android_gms_internal_zzjd = new zzjd();
        final zzb com_google_android_gms_internal_zzgw_zzb = new zzb(this);
        zzdf anonymousClass1 = new zzdf(this) {
            final /* synthetic */ zzgw zzGP;

            public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
                com_google_android_gms_internal_zzed.zzb("/nativeAdPreProcess", com_google_android_gms_internal_zzgw_zzb.zzHb);
                try {
                    String str = (String) map.get("success");
                    if (!TextUtils.isEmpty(str)) {
                        com_google_android_gms_internal_zzjd.zzg(new JSONObject(str).getJSONArray("ads").getJSONObject(0));
                        return;
                    }
                } catch (Throwable e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzb("Malformed native JSON response.", e);
                }
                this.zzGP.zzF(0);
                zzx.zza(this.zzGP.zzgn(), (Object) "Unable to set the ad state error!");
                com_google_android_gms_internal_zzjd.zzg(null);
            }
        };
        com_google_android_gms_internal_zzgw_zzb.zzHb = anonymousClass1;
        com_google_android_gms_internal_zzed.zza("/nativeAdPreProcess", anonymousClass1);
        com_google_android_gms_internal_zzed.zza("google.afma.nativeAds.preProcessJsonGmsg", new JSONObject(this.zzGd.zzLe.body));
        return (JSONObject) com_google_android_gms_internal_zzjd.get(zzGF, TimeUnit.MILLISECONDS);
    }

    private void zzb(zzcp com_google_android_gms_internal_zzcp, String str) {
        try {
            zzct zzs = this.zzGH.zzs(com_google_android_gms_internal_zzcp.getCustomTemplateId());
            if (zzs != null) {
                zzs.zza(com_google_android_gms_internal_zzcp, str);
            }
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Failed to call onCustomClick for asset " + str + ".", e);
        }
    }

    private String[] zzc(JSONObject jSONObject, String str) throws JSONException {
        JSONArray optJSONArray = jSONObject.optJSONArray(str);
        if (optJSONArray == null) {
            return null;
        }
        String[] strArr = new String[optJSONArray.length()];
        for (int i = 0; i < optJSONArray.length(); i++) {
            strArr[i] = optJSONArray.getString(i);
        }
        return strArr;
    }

    private static List<Drawable> zzf(List<zzc> list) throws RemoteException {
        List<Drawable> arrayList = new ArrayList();
        for (zzc zzdJ : list) {
            arrayList.add((Drawable) zze.zzp(zzdJ.zzdJ()));
        }
        return arrayList;
    }

    private zzed zzgm() throws CancellationException, ExecutionException, InterruptedException, TimeoutException {
        if (zzgn()) {
            return null;
        }
        zzed com_google_android_gms_internal_zzed = (zzed) this.zzGI.zza(this.mContext, this.zzGd.zzLd.zzrl, (this.zzGd.zzLe.zzEF.indexOf("https") == 0 ? "https:" : "http:") + ((String) zzbt.zzwC.get()), this.zzyt).get(zzGF, TimeUnit.MILLISECONDS);
        com_google_android_gms_internal_zzed.zza(this.zzGH, this.zzGH, this.zzGH, this.zzGH, false, null, null, null, null);
        return com_google_android_gms_internal_zzed;
    }

    public /* synthetic */ Object call() throws Exception {
        return zzgl();
    }

    public void zzF(int i) {
        synchronized (this.zzpV) {
            this.zzGJ = true;
            this.zzGu = i;
        }
    }

    public zzjg<zzc> zza(JSONObject jSONObject, String str, boolean z, boolean z2) throws JSONException {
        JSONObject jSONObject2 = z ? jSONObject.getJSONObject(str) : jSONObject.optJSONObject(str);
        if (jSONObject2 == null) {
            jSONObject2 = new JSONObject();
        }
        return zza(jSONObject2, z, z2);
    }

    public List<zzjg<zzc>> zza(JSONObject jSONObject, String str, boolean z, boolean z2, boolean z3) throws JSONException {
        JSONArray jSONArray = z ? jSONObject.getJSONArray(str) : jSONObject.optJSONArray(str);
        List<zzjg<zzc>> arrayList = new ArrayList();
        if (jSONArray == null || jSONArray.length() == 0) {
            zza(0, z);
            return arrayList;
        }
        int length = z3 ? jSONArray.length() : 1;
        for (int i = 0; i < length; i++) {
            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
            if (jSONObject2 == null) {
                jSONObject2 = new JSONObject();
            }
            arrayList.add(zza(jSONObject2, z, z2));
        }
        return arrayList;
    }

    public Future<zzc> zza(JSONObject jSONObject, String str, boolean z) throws JSONException {
        JSONObject jSONObject2 = jSONObject.getJSONObject(str);
        boolean optBoolean = jSONObject2.optBoolean("require", true);
        if (jSONObject2 == null) {
            jSONObject2 = new JSONObject();
        }
        return zza(jSONObject2, optBoolean, z);
    }

    public void zza(int i, boolean z) {
        if (z) {
            zzF(i);
        }
    }

    protected zza zze(JSONObject jSONObject) throws JSONException, TimeoutException {
        if (zzgn()) {
            return null;
        }
        String string = jSONObject.getString("template_id");
        boolean z = this.zzGd.zzLd.zzrD != null ? this.zzGd.zzLd.zzrD.zzyA : false;
        boolean z2 = this.zzGd.zzLd.zzrD != null ? this.zzGd.zzLd.zzrD.zzyC : false;
        if ("2".equals(string)) {
            return new zzgx(z, z2);
        }
        if ("1".equals(string)) {
            return new zzgy(z, z2);
        }
        if (UDIDs.ANDROID_DEVICE_ID.equals(string)) {
            final String string2 = jSONObject.getString("custom_template_id");
            final zzjd com_google_android_gms_internal_zzjd = new zzjd();
            zzir.zzMc.post(new Runnable(this) {
                final /* synthetic */ zzgw zzGP;

                public void run() {
                    com_google_android_gms_internal_zzjd.zzg(this.zzGP.zzGH.zzbv().get(string2));
                }
            });
            if (com_google_android_gms_internal_zzjd.get(zzGF, TimeUnit.MILLISECONDS) != null) {
                return new zzgz(z);
            }
            com.google.android.gms.ads.internal.util.client.zzb.e("No handler for custom template: " + jSONObject.getString("custom_template_id"));
        } else {
            zzF(0);
        }
        return null;
    }

    public zzjg<com.google.android.gms.ads.internal.formats.zza> zzf(JSONObject jSONObject) throws JSONException {
        JSONObject optJSONObject = jSONObject.optJSONObject("attribution");
        if (optJSONObject == null) {
            return new zzje(null);
        }
        String optString = optJSONObject.optString("text");
        int optInt = optJSONObject.optInt("text_size", -1);
        Integer zzb = zzb(optJSONObject, "text_color");
        Integer zzb2 = zzb(optJSONObject, "bg_color");
        final int optInt2 = optJSONObject.optInt("animation_ms", ControllerParameters.SECOND);
        final int optInt3 = optJSONObject.optInt("presentation_ms", 4000);
        List arrayList = new ArrayList();
        if (optJSONObject.optJSONArray("images") != null) {
            arrayList = zza(optJSONObject, "images", false, false, true);
        } else {
            arrayList.add(zza(optJSONObject, "image", false, false));
        }
        final String str = optString;
        final Integer num = zzb2;
        final Integer num2 = zzb;
        final int i = optInt;
        return zzjf.zza(zzjf.zzl(arrayList), new com.google.android.gms.internal.zzjf.zza<List<zzc>, com.google.android.gms.ads.internal.formats.zza>(this) {
            final /* synthetic */ zzgw zzGP;

            public /* synthetic */ Object zzf(Object obj) {
                return zzh((List) obj);
            }

            public com.google.android.gms.ads.internal.formats.zza zzh(List<zzc> list) {
                com.google.android.gms.ads.internal.formats.zza com_google_android_gms_ads_internal_formats_zza;
                if (list != null) {
                    try {
                        if (!list.isEmpty()) {
                            com_google_android_gms_ads_internal_formats_zza = new com.google.android.gms.ads.internal.formats.zza(str, zzgw.zzf((List) list), num, num2, i > 0 ? Integer.valueOf(i) : null, optInt3 + optInt2);
                            return com_google_android_gms_ads_internal_formats_zza;
                        }
                    } catch (Throwable e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzb("Could not get attribution icon", e);
                        return null;
                    }
                }
                com_google_android_gms_ads_internal_formats_zza = null;
                return com_google_android_gms_ads_internal_formats_zza;
            }
        });
    }

    public zzif zzgl() {
        try {
            zzed zzgm = zzgm();
            JSONObject zzb = zzb(zzgm);
            com.google.android.gms.ads.internal.formats.zzh.zza zza = zza(zzgm, zze(zzb), zzb);
            zza(zza, zzgm);
            return zza(zza);
        } catch (CancellationException e) {
            if (!this.zzGJ) {
                zzF(0);
            }
            return zza(null);
        } catch (ExecutionException e2) {
            if (this.zzGJ) {
                zzF(0);
            }
            return zza(null);
        } catch (InterruptedException e3) {
            if (this.zzGJ) {
                zzF(0);
            }
            return zza(null);
        } catch (Throwable e4) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Malformed native JSON response.", e4);
            if (this.zzGJ) {
                zzF(0);
            }
            return zza(null);
        } catch (Throwable e42) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Timeout when loading native ad.", e42);
            if (this.zzGJ) {
                zzF(0);
            }
            return zza(null);
        }
    }

    public boolean zzgn() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzGJ;
        }
        return z;
    }
}
