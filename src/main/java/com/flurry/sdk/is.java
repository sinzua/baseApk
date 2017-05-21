package com.flurry.sdk;

import com.nativex.monetization.mraid.objects.ObjectNames.CalendarEntryData;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class is implements lb<hs> {
    private static final String a = is.class.getSimpleName();

    public /* synthetic */ Object b(InputStream inputStream) throws IOException {
        return a(inputStream);
    }

    public void a(OutputStream outputStream, hs hsVar) throws IOException {
        if (outputStream != null && hsVar != null) {
            DataOutputStream anonymousClass1 = new DataOutputStream(this, outputStream) {
                final /* synthetic */ is a;

                public void close() {
                }
            };
            JSONObject jSONObject = new JSONObject();
            try {
                Object obj;
                a(jSONObject, "project_key", hsVar.a);
                a(jSONObject, "bundle_id", hsVar.b);
                a(jSONObject, "app_version", hsVar.c);
                jSONObject.put("sdk_version", hsVar.d);
                jSONObject.put("platform", hsVar.e);
                a(jSONObject, "platform_version", hsVar.f);
                jSONObject.put("limit_ad_tracking", hsVar.g);
                if (hsVar.h == null || hsVar.h.a == null) {
                    obj = null;
                } else {
                    obj = new JSONObject();
                    JSONObject jSONObject2 = new JSONObject();
                    a(jSONObject2, "model", hsVar.h.a.a);
                    a(jSONObject2, "brand", hsVar.h.a.b);
                    a(jSONObject2, CalendarEntryData.ID, hsVar.h.a.c);
                    a(jSONObject2, ParametersKeys.ORIENTATION_DEVICE, hsVar.h.a.d);
                    a(jSONObject2, "product", hsVar.h.a.e);
                    a(jSONObject2, "version_release", hsVar.h.a.f);
                    obj.put("com.flurry.proton.generated.avro.v2.AndroidTags", jSONObject2);
                }
                if (obj != null) {
                    jSONObject.put("device_tags", obj);
                } else {
                    jSONObject.put("device_tags", JSONObject.NULL);
                }
                JSONArray jSONArray = new JSONArray();
                for (hu huVar : hsVar.i) {
                    JSONObject jSONObject3 = new JSONObject();
                    jSONObject3.put("type", huVar.a);
                    a(jSONObject3, CalendarEntryData.ID, huVar.b);
                    jSONArray.put(jSONObject3);
                }
                jSONObject.put("device_ids", jSONArray);
                if (hsVar.j == null || hsVar.j.a == null) {
                    obj = null;
                } else {
                    obj = new JSONObject();
                    JSONObject jSONObject4 = new JSONObject();
                    jSONObject4.putOpt("latitude", Double.valueOf(hsVar.j.a.a));
                    jSONObject4.putOpt("longitude", Double.valueOf(hsVar.j.a.b));
                    jSONObject4.putOpt("accuracy", Float.valueOf(hsVar.j.a.c));
                    obj.put("com.flurry.proton.generated.avro.v2.Geolocation", jSONObject4);
                }
                if (obj != null) {
                    jSONObject.put("geo", obj);
                } else {
                    jSONObject.put("geo", JSONObject.NULL);
                }
                JSONObject jSONObject5 = new JSONObject();
                if (hsVar.k != null) {
                    a(jSONObject5, "string", hsVar.k.a);
                    jSONObject.put("publisher_user_id", jSONObject5);
                } else {
                    jSONObject.put("publisher_user_id", JSONObject.NULL);
                }
                kg.a(5, a, "Proton Request String: " + jSONObject.toString());
                anonymousClass1.write(jSONObject.toString().getBytes());
                anonymousClass1.flush();
                anonymousClass1.close();
            } catch (Throwable e) {
                throw new IOException("Invalid Json", e);
            } catch (Throwable th) {
                anonymousClass1.close();
            }
        }
    }

    private void a(JSONObject jSONObject, String str, String str2) throws IOException, JSONException {
        if (jSONObject == null) {
            throw new IOException("Null Json object");
        } else if (str2 != null) {
            jSONObject.put(str, str2);
        } else {
            jSONObject.put(str, JSONObject.NULL);
        }
    }

    public hs a(InputStream inputStream) throws IOException {
        throw new IOException("Deserialize not supported for request");
    }
}
