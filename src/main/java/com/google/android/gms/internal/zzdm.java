package com.google.android.gms.internal;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.overlay.AdLauncherIntentInfoParcel;
import com.google.android.gms.ads.internal.zze;
import com.google.android.gms.ads.internal.zzr;
import com.nativex.monetization.mraid.objects.ObjectNames.CalendarEntryData;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

@zzhb
public final class zzdm implements zzdf {
    private final zze zzzA;
    private final zzfn zzzB;
    private final zzdh zzzD;

    public static class zzb {
        private final zzjp zzpD;

        public zzb(zzjp com_google_android_gms_internal_zzjp) {
            this.zzpD = com_google_android_gms_internal_zzjp;
        }

        public Intent zza(Context context, Map<String, String> map) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            String str = (String) map.get("u");
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            if (this.zzpD != null) {
                str = zzr.zzbC().zza(this.zzpD, str);
            }
            Uri parse = Uri.parse(str);
            boolean parseBoolean = Boolean.parseBoolean((String) map.get("use_first_package"));
            boolean parseBoolean2 = Boolean.parseBoolean((String) map.get("use_running_process"));
            Uri build = "http".equalsIgnoreCase(parse.getScheme()) ? parse.buildUpon().scheme("https").build() : "https".equalsIgnoreCase(parse.getScheme()) ? parse.buildUpon().scheme("http").build() : null;
            ArrayList arrayList = new ArrayList();
            Intent zzd = zzd(parse);
            Intent zzd2 = zzd(build);
            ResolveInfo zza = zza(context, zzd, arrayList);
            if (zza != null) {
                return zza(zzd, zza);
            }
            if (zzd2 != null) {
                ResolveInfo zza2 = zza(context, zzd2);
                if (zza2 != null) {
                    Intent zza3 = zza(zzd, zza2);
                    if (zza(context, zza3) != null) {
                        return zza3;
                    }
                }
            }
            if (arrayList.size() == 0) {
                return zzd;
            }
            if (parseBoolean2 && activityManager != null) {
                List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
                if (runningAppProcesses != null) {
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        ResolveInfo resolveInfo = (ResolveInfo) it.next();
                        for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                            if (runningAppProcessInfo.processName.equals(resolveInfo.activityInfo.packageName)) {
                                return zza(zzd, resolveInfo);
                            }
                        }
                    }
                }
            }
            return parseBoolean ? zza(zzd, (ResolveInfo) arrayList.get(0)) : zzd;
        }

        public Intent zza(Intent intent, ResolveInfo resolveInfo) {
            Intent intent2 = new Intent(intent);
            intent2.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
            return intent2;
        }

        public ResolveInfo zza(Context context, Intent intent) {
            return zza(context, intent, new ArrayList());
        }

        public ResolveInfo zza(Context context, Intent intent, ArrayList<ResolveInfo> arrayList) {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return null;
            }
            ResolveInfo resolveInfo;
            Collection queryIntentActivities = packageManager.queryIntentActivities(intent, 65536);
            ResolveInfo resolveActivity = packageManager.resolveActivity(intent, 65536);
            if (!(queryIntentActivities == null || resolveActivity == null)) {
                for (int i = 0; i < queryIntentActivities.size(); i++) {
                    resolveInfo = (ResolveInfo) queryIntentActivities.get(i);
                    if (resolveActivity != null && resolveActivity.activityInfo.name.equals(resolveInfo.activityInfo.name)) {
                        resolveInfo = resolveActivity;
                        break;
                    }
                }
            }
            resolveInfo = null;
            arrayList.addAll(queryIntentActivities);
            return resolveInfo;
        }

        public Intent zzd(Uri uri) {
            if (uri == null) {
                return null;
            }
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(268435456);
            intent.setData(uri);
            intent.setAction("android.intent.action.VIEW");
            return intent;
        }
    }

    public static class zza extends zzim {
        private final String zzF;
        private final zzjp zzpD;
        private final String zzzE = "play.google.com";
        private final String zzzF = "market";
        private final int zzzG = 10;

        public zza(zzjp com_google_android_gms_internal_zzjp, String str) {
            this.zzpD = com_google_android_gms_internal_zzjp;
            this.zzF = str;
        }

        public void onStop() {
        }

        public Intent zzT(String str) {
            Uri parse = Uri.parse(str);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(268435456);
            intent.setData(parse);
            return intent;
        }

        public void zzbr() {
            HttpURLConnection httpURLConnection;
            String str;
            Throwable th;
            CharSequence charSequence;
            Throwable th2;
            int i = 0;
            String str2 = this.zzF;
            while (i < 10) {
                int i2 = i + 1;
                try {
                    URL url = new URL(str2);
                    if (!"play.google.com".equalsIgnoreCase(url.getHost())) {
                        if (!"market".equalsIgnoreCase(url.getProtocol())) {
                            CharSequence charSequence2;
                            CharSequence charSequence3;
                            httpURLConnection = (HttpURLConnection) url.openConnection();
                            zzr.zzbC().zza(this.zzpD.getContext(), this.zzpD.zzhX().afmaVersion, false, httpURLConnection);
                            int responseCode = httpURLConnection.getResponseCode();
                            Map headerFields = httpURLConnection.getHeaderFields();
                            String str3 = "";
                            if (responseCode >= 300 && responseCode <= 399) {
                                List list = null;
                                if (headerFields.containsKey("Location")) {
                                    list = (List) headerFields.get("Location");
                                } else if (headerFields.containsKey(CalendarEntryData.LOCATION)) {
                                    list = (List) headerFields.get(CalendarEntryData.LOCATION);
                                }
                                if (list != null && list.size() > 0) {
                                    charSequence2 = (String) list.get(0);
                                    if (TextUtils.isEmpty(charSequence2)) {
                                        com.google.android.gms.ads.internal.util.client.zzb.zzaK("Arrived at landing page, this ideally should not happen. Will open it in browser.");
                                        httpURLConnection.disconnect();
                                        str = str2;
                                        break;
                                    }
                                    try {
                                        httpURLConnection.disconnect();
                                        i = i2;
                                        charSequence3 = charSequence2;
                                    } catch (Throwable e) {
                                        th = e;
                                        charSequence = charSequence2;
                                        th2 = th;
                                    } catch (Throwable e2) {
                                        th = e2;
                                        charSequence = charSequence2;
                                        th2 = th;
                                    } catch (Throwable e22) {
                                        th = e22;
                                        charSequence = charSequence2;
                                        th2 = th;
                                    }
                                }
                            }
                            Object obj = str3;
                            if (TextUtils.isEmpty(charSequence2)) {
                                com.google.android.gms.ads.internal.util.client.zzb.zzaK("Arrived at landing page, this ideally should not happen. Will open it in browser.");
                                httpURLConnection.disconnect();
                                str = str2;
                                break;
                            }
                            httpURLConnection.disconnect();
                            i = i2;
                            charSequence3 = charSequence2;
                        } else {
                            str = str2;
                            break;
                        }
                    }
                    str = str2;
                    break;
                } catch (Throwable e222) {
                    th2 = e222;
                    str = str2;
                } catch (Throwable e2222) {
                    th2 = e2222;
                    str = str2;
                } catch (Throwable e22222) {
                    th2 = e22222;
                    str = str2;
                } catch (Throwable th3) {
                    httpURLConnection.disconnect();
                }
            }
            str = str2;
            zzr.zzbC().zzb(this.zzpD.getContext(), zzT(str));
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Error while pinging URL: " + str, th2);
            zzr.zzbC().zzb(this.zzpD.getContext(), zzT(str));
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Error while parsing ping URL: " + str, th2);
            zzr.zzbC().zzb(this.zzpD.getContext(), zzT(str));
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Error while pinging URL: " + str, th2);
            zzr.zzbC().zzb(this.zzpD.getContext(), zzT(str));
        }
    }

    public zzdm(zzdh com_google_android_gms_internal_zzdh, zze com_google_android_gms_ads_internal_zze, zzfn com_google_android_gms_internal_zzfn) {
        this.zzzD = com_google_android_gms_internal_zzdh;
        this.zzzA = com_google_android_gms_ads_internal_zze;
        this.zzzB = com_google_android_gms_internal_zzfn;
    }

    private static boolean zzc(Map<String, String> map) {
        return "1".equals(map.get("custom_close"));
    }

    private static int zzd(Map<String, String> map) {
        String str = (String) map.get("o");
        if (str != null) {
            if ("p".equalsIgnoreCase(str)) {
                return zzr.zzbE().zzhw();
            }
            if ("l".equalsIgnoreCase(str)) {
                return zzr.zzbE().zzhv();
            }
            if ("c".equalsIgnoreCase(str)) {
                return zzr.zzbE().zzhx();
            }
        }
        return -1;
    }

    private static void zze(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        String str = (String) map.get("u");
        if (TextUtils.isEmpty(str)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Destination url cannot be empty.");
        } else {
            new zza(com_google_android_gms_internal_zzjp, str).zzhn();
        }
    }

    private static void zzf(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        Context context = com_google_android_gms_internal_zzjp.getContext();
        if (TextUtils.isEmpty((String) map.get("u"))) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Destination url cannot be empty.");
            return;
        }
        try {
            com_google_android_gms_internal_zzjp.zzhU().zza(new AdLauncherIntentInfoParcel(new zzb(com_google_android_gms_internal_zzjp).zza(context, (Map) map)));
        } catch (ActivityNotFoundException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK(e.getMessage());
        }
    }

    private void zzo(boolean z) {
        if (this.zzzB != null) {
            this.zzzB.zzp(z);
        }
    }

    public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        String str = (String) map.get("a");
        if (str == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Action missing from an open GMSG.");
        } else if (this.zzzA == null || this.zzzA.zzbh()) {
            zzjq zzhU = com_google_android_gms_internal_zzjp.zzhU();
            if ("expand".equalsIgnoreCase(str)) {
                if (com_google_android_gms_internal_zzjp.zzhY()) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaK("Cannot expand WebView that is already expanded.");
                    return;
                }
                zzo(false);
                zzhU.zza(zzc(map), zzd(map));
            } else if ("webapp".equalsIgnoreCase(str)) {
                str = (String) map.get("u");
                zzo(false);
                if (str != null) {
                    zzhU.zza(zzc(map), zzd(map), str);
                } else {
                    zzhU.zza(zzc(map), zzd(map), (String) map.get("html"), (String) map.get("baseurl"));
                }
            } else if ("in_app_purchase".equalsIgnoreCase(str)) {
                str = (String) map.get("product_id");
                String str2 = (String) map.get("report_urls");
                if (this.zzzD == null) {
                    return;
                }
                if (str2 == null || str2.isEmpty()) {
                    this.zzzD.zza(str, new ArrayList());
                } else {
                    this.zzzD.zza(str, new ArrayList(Arrays.asList(str2.split(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR))));
                }
            } else if ("app".equalsIgnoreCase(str) && "true".equalsIgnoreCase((String) map.get("play_store"))) {
                zze(com_google_android_gms_internal_zzjp, map);
            } else if ("app".equalsIgnoreCase(str) && "true".equalsIgnoreCase((String) map.get("system_browser"))) {
                zzo(true);
                zzf(com_google_android_gms_internal_zzjp, map);
            } else {
                zzo(true);
                str = (String) map.get("u");
                zzhU.zza(new AdLauncherIntentInfoParcel((String) map.get("i"), !TextUtils.isEmpty(str) ? zzr.zzbC().zza(com_google_android_gms_internal_zzjp, str) : str, (String) map.get("m"), (String) map.get("p"), (String) map.get("c"), (String) map.get("f"), (String) map.get("e")));
            }
        } else {
            this.zzzA.zzq((String) map.get("u"));
        }
    }
}
