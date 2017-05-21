package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import org.json.JSONObject;

@zzhb
public class zzga extends Handler {
    private final zzfz zzFq;

    public zzga(Context context) {
        if (context.getApplicationContext() != null) {
            context = context.getApplicationContext();
        }
        this(new zzgb(context));
    }

    public zzga(zzfz com_google_android_gms_internal_zzfz) {
        this.zzFq = com_google_android_gms_internal_zzfz;
    }

    private void zzd(JSONObject jSONObject) {
        try {
            this.zzFq.zza(jSONObject.getString("request_id"), jSONObject.getString("base_url"), jSONObject.getString("html"));
        } catch (Exception e) {
        }
    }

    public void handleMessage(Message msg) {
        try {
            Bundle data = msg.getData();
            if (data != null) {
                JSONObject jSONObject = new JSONObject(data.getString("data"));
                if ("fetch_html".equals(jSONObject.getString("message_name"))) {
                    zzd(jSONObject);
                }
            }
        } catch (Exception e) {
        }
    }
}
