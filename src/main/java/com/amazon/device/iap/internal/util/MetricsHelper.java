package com.amazon.device.iap.internal.util;

import com.amazon.device.iap.internal.b.e;
import com.amazon.device.iap.internal.b.h.a;
import com.amazon.device.iap.model.RequestId;
import org.json.JSONObject;

public class MetricsHelper {
    private static final String DESCRIPTION = "description";
    private static final String EXCEPTION_MESSAGE = "exceptionMessage";
    private static final String EXCEPTION_METRIC = "GenericException";
    private static final String JSON_PARSING_EXCEPTION_METRIC = "JsonParsingFailed";
    private static final String JSON_STRING = "jsonString";
    private static final String RECEIPT_VERIFICATION_FAILED_METRIC = "IapReceiptVerificationFailed";
    private static final String SIGNATURE = "signature";
    private static final String STRING_TO_SIGN = "stringToSign";
    private static final String TAG = MetricsHelper.class.getSimpleName();

    protected static void submitMetric(String str, String str2, JSONObject jSONObject) {
        new a(new e(RequestId.fromString(str)), str2, jSONObject.toString()).a_();
    }

    public static void submitReceiptVerificationFailureMetrics(String str, String str2, String str3) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(STRING_TO_SIGN, str2);
            jSONObject.put(SIGNATURE, str3);
            submitMetric(str, RECEIPT_VERIFICATION_FAILED_METRIC, jSONObject);
        } catch (Exception e) {
            e.b(TAG, "error calling submitMetric: " + e);
        }
    }

    public static void submitJsonParsingExceptionMetrics(String str, String str2, String str3) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(JSON_STRING, str2);
            jSONObject.put("description", str3);
            submitMetric(str, JSON_PARSING_EXCEPTION_METRIC, jSONObject);
        } catch (Exception e) {
            e.b(TAG, "error calling submitMetric: " + e);
        }
    }

    public static void submitExceptionMetrics(String str, String str2, Exception exception) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(EXCEPTION_MESSAGE, exception.getMessage());
            jSONObject.put("description", str2);
            submitMetric(str, EXCEPTION_METRIC, jSONObject);
        } catch (Exception e) {
            e.b(TAG, "error calling submitMetric: " + e);
        }
    }
}
