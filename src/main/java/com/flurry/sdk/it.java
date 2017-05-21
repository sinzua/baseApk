package com.flurry.sdk;

import android.text.TextUtils;
import bolts.MeasurementEvent;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class it implements lb<ht> {
    private static final String a = it.class.getSimpleName();

    public /* synthetic */ Object b(InputStream inputStream) throws IOException {
        return a(inputStream);
    }

    public void a(OutputStream outputStream, ht htVar) throws IOException {
        throw new IOException("Serialize not supported for response");
    }

    public ht a(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        String str = new String(lt.a(inputStream));
        kg.a(5, a, "Proton response string: " + str);
        ht htVar = new ht();
        try {
            JSONObject jSONObject = new JSONObject(str);
            htVar.a = jSONObject.optLong("issued_at", -1);
            htVar.b = jSONObject.optLong("refresh_ttl", 3600);
            htVar.c = jSONObject.optLong("expiration_ttl", 86400);
            a(htVar, jSONObject);
            b(htVar, jSONObject);
            c(htVar, jSONObject);
            return htVar;
        } catch (Throwable e) {
            throw new IOException("Exception while deserialize: ", e);
        }
    }

    private void a(ht htVar, JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("global_settings");
        htVar.d = new ia();
        if (optJSONObject != null) {
            htVar.d.a = b(optJSONObject.optString("log_level"));
        }
    }

    private void b(ht htVar, JSONObject jSONObject) throws JSONException {
        JSONObject optJSONObject = jSONObject.optJSONObject("pulse");
        hr hrVar = new hr();
        if (optJSONObject != null) {
            a(hrVar, optJSONObject.optJSONArray("callbacks"));
            hrVar.b = optJSONObject.optInt("max_callback_retries", 3);
            hrVar.c = optJSONObject.optInt("max_callback_attempts_per_report", 15);
            hrVar.d = optJSONObject.optInt("max_report_delay_seconds", 600);
            hrVar.e = optJSONObject.optString("agent_report_url", "");
        }
        htVar.e = hrVar;
    }

    private void c(ht htVar, JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("analytics");
        htVar.f = new id();
        if (optJSONObject != null) {
            htVar.f.b = optJSONObject.optBoolean("analytics_enabled", true);
            htVar.f.a = optJSONObject.optInt("max_session_properties", 10);
        }
    }

    private void a(hr hrVar, JSONArray jSONArray) throws JSONException {
        if (jSONArray != null) {
            List arrayList = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject optJSONObject = jSONArray.optJSONObject(i);
                if (optJSONObject != null) {
                    hq hqVar = new hq();
                    hqVar.b = optJSONObject.optString("partner", "");
                    a(hqVar, optJSONObject.optJSONArray(EventEntry.TABLE_NAME));
                    hqVar.d = a(optJSONObject.optString(ParametersKeys.METHOD));
                    hqVar.e = optJSONObject.optString("uri_template", "");
                    JSONObject optJSONObject2 = optJSONObject.optJSONObject("body_template");
                    if (optJSONObject2 != null) {
                        String optString = optJSONObject2.optString("string", "null");
                        if (!optString.equals("null")) {
                            hqVar.f = optString;
                        }
                    }
                    hqVar.g = optJSONObject.optInt("max_redirects", 5);
                    hqVar.h = optJSONObject.optInt("connect_timeout", 20);
                    hqVar.i = optJSONObject.optInt("request_timeout", 20);
                    hqVar.a = optJSONObject.optLong("callback_id", -1);
                    optJSONObject = optJSONObject.optJSONObject("headers");
                    if (optJSONObject != null) {
                        optJSONObject = optJSONObject.optJSONObject("map");
                        if (optJSONObject != null) {
                            hqVar.j = lv.a(optJSONObject);
                        }
                    }
                    arrayList.add(hqVar);
                }
            }
            hrVar.a = arrayList;
        }
    }

    private void a(hq hqVar, JSONArray jSONArray) {
        if (jSONArray != null) {
            List list = null;
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject optJSONObject = jSONArray.optJSONObject(i);
                if (optJSONObject != null) {
                    if (optJSONObject.has("string")) {
                        if (list == null) {
                            list = new ArrayList();
                        }
                        hw hwVar = new hw();
                        hwVar.a = optJSONObject.optString("string", "");
                        list.add(hwVar);
                    } else if (optJSONObject.has("com.flurry.proton.generated.avro.v2.EventParameterCallbackTrigger")) {
                        if (list == null) {
                            list = new ArrayList();
                        }
                        optJSONObject = optJSONObject.optJSONObject("com.flurry.proton.generated.avro.v2.EventParameterCallbackTrigger");
                        if (optJSONObject != null) {
                            String[] strArr;
                            hx hxVar = new hx();
                            hxVar.a = optJSONObject.optString(MeasurementEvent.MEASUREMENT_EVENT_NAME_KEY, "");
                            hxVar.c = optJSONObject.optString("event_parameter_name", "");
                            JSONArray optJSONArray = optJSONObject.optJSONArray("event_parameter_values");
                            if (optJSONArray != null) {
                                String[] strArr2 = new String[optJSONArray.length()];
                                for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                                    strArr2[i2] = optJSONArray.optString(i2, "");
                                }
                                strArr = strArr2;
                            } else {
                                strArr = new String[0];
                            }
                            hxVar.d = strArr;
                            list.add(hxVar);
                        }
                    }
                }
            }
            hqVar.c = list;
        }
    }

    private iq a(String str) {
        iq iqVar = iq.GET;
        try {
            if (TextUtils.isEmpty(str)) {
                return iqVar;
            }
            return (iq) Enum.valueOf(iq.class, str);
        } catch (Exception e) {
            return iqVar;
        }
    }

    private ib b(String str) {
        ib ibVar = ib.OFF;
        try {
            if (TextUtils.isEmpty(str)) {
                return ibVar;
            }
            return (ib) Enum.valueOf(ib.class, str);
        } catch (Exception e) {
            return ibVar;
        }
    }
}
