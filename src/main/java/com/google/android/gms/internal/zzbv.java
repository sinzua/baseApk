package com.google.android.gms.internal;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@zzhb
public class zzbv {
    final Context mContext;
    final String zzsy;
    String zzxj;
    BlockingQueue<zzcb> zzxl;
    ExecutorService zzxm;
    LinkedHashMap<String, String> zzxn = new LinkedHashMap();
    Map<String, zzby> zzxo = new HashMap();
    private AtomicBoolean zzxp;
    private File zzxq;

    public zzbv(Context context, String str, String str2, Map<String, String> map) {
        this.mContext = context;
        this.zzsy = str;
        this.zzxj = str2;
        this.zzxp = new AtomicBoolean(false);
        this.zzxp.set(((Boolean) zzbt.zzwi.get()).booleanValue());
        if (this.zzxp.get()) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            if (externalStorageDirectory != null) {
                this.zzxq = new File(externalStorageDirectory, "sdk_csi_data.txt");
            }
        }
        for (Entry entry : map.entrySet()) {
            this.zzxn.put(entry.getKey(), entry.getValue());
        }
        this.zzxl = new ArrayBlockingQueue(30);
        this.zzxm = Executors.newSingleThreadExecutor();
        this.zzxm.execute(new Runnable(this) {
            final /* synthetic */ zzbv zzxr;

            {
                this.zzxr = r1;
            }

            public void run() {
                this.zzxr.zzdx();
            }
        });
        this.zzxo.put(ParametersKeys.ACTION, zzby.zzxt);
        this.zzxo.put("ad_format", zzby.zzxt);
        this.zzxo.put("e", zzby.zzxu);
    }

    private void zza(File file, String str) {
        FileOutputStream fileOutputStream;
        Throwable e;
        if (file != null) {
            try {
                fileOutputStream = new FileOutputStream(file, true);
                try {
                    fileOutputStream.write(str.getBytes());
                    fileOutputStream.write(10);
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                            return;
                        } catch (Throwable e2) {
                            zzb.zzd("CsiReporter: Cannot close file: sdk_csi_data.txt.", e2);
                            return;
                        }
                    }
                    return;
                } catch (IOException e3) {
                    e2 = e3;
                    try {
                        zzb.zzd("CsiReporter: Cannot write to file: sdk_csi_data.txt.", e2);
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                                return;
                            } catch (Throwable e22) {
                                zzb.zzd("CsiReporter: Cannot close file: sdk_csi_data.txt.", e22);
                                return;
                            }
                        }
                        return;
                    } catch (Throwable th) {
                        e22 = th;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (Throwable e4) {
                                zzb.zzd("CsiReporter: Cannot close file: sdk_csi_data.txt.", e4);
                            }
                        }
                        throw e22;
                    }
                }
            } catch (IOException e5) {
                e22 = e5;
                fileOutputStream = null;
                zzb.zzd("CsiReporter: Cannot write to file: sdk_csi_data.txt.", e22);
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                    return;
                }
                return;
            } catch (Throwable th2) {
                e22 = th2;
                fileOutputStream = null;
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                throw e22;
            }
        }
        zzb.zzaK("CsiReporter: File doesn't exists. Cannot write CSI data to file.");
    }

    private void zzc(Map<String, String> map, String str) {
        String zza = zza(this.zzxj, map, str);
        if (this.zzxp.get()) {
            zza(this.zzxq, zza);
        } else {
            zzr.zzbC().zzc(this.mContext, this.zzsy, zza);
        }
    }

    private void zzdx() {
        while (true) {
            try {
                zzcb com_google_android_gms_internal_zzcb = (zzcb) this.zzxl.take();
                Object zzdD = com_google_android_gms_internal_zzcb.zzdD();
                if (!TextUtils.isEmpty(zzdD)) {
                    zzc(zza(this.zzxn, com_google_android_gms_internal_zzcb.zzn()), zzdD);
                }
            } catch (Throwable e) {
                zzb.zzd("CsiReporter:reporter interrupted", e);
                return;
            }
        }
    }

    public zzby zzL(String str) {
        zzby com_google_android_gms_internal_zzby = (zzby) this.zzxo.get(str);
        return com_google_android_gms_internal_zzby != null ? com_google_android_gms_internal_zzby : zzby.zzxs;
    }

    String zza(String str, Map<String, String> map, @NonNull String str2) {
        Builder buildUpon = Uri.parse(str).buildUpon();
        for (Entry entry : map.entrySet()) {
            buildUpon.appendQueryParameter((String) entry.getKey(), (String) entry.getValue());
        }
        StringBuilder stringBuilder = new StringBuilder(buildUpon.build().toString());
        stringBuilder.append(RequestParameters.AMPERSAND).append("it").append(RequestParameters.EQUAL).append(str2);
        return stringBuilder.toString();
    }

    Map<String, String> zza(Map<String, String> map, @Nullable Map<String, String> map2) {
        Map<String, String> linkedHashMap = new LinkedHashMap(map);
        if (map2 == null) {
            return linkedHashMap;
        }
        for (Entry entry : map2.entrySet()) {
            String str = (String) entry.getKey();
            String str2 = (String) linkedHashMap.get(str);
            linkedHashMap.put(str, zzL(str).zzb(str2, (String) entry.getValue()));
        }
        return linkedHashMap;
    }

    public boolean zza(zzcb com_google_android_gms_internal_zzcb) {
        return this.zzxl.offer(com_google_android_gms_internal_zzcb);
    }

    public void zzb(List<String> list) {
        if (list != null && !list.isEmpty()) {
            this.zzxn.put("e", TextUtils.join(",", list));
        }
    }
}
