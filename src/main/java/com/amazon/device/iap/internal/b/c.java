package com.amazon.device.iap.internal.b;

import android.content.Context;
import android.content.Intent;
import com.amazon.device.iap.internal.b.b.d;
import com.amazon.device.iap.internal.b.e.a;
import com.amazon.device.iap.internal.b.g.b;
import com.amazon.device.iap.internal.util.e;
import com.amazon.device.iap.model.FulfillmentResult;
import com.amazon.device.iap.model.RequestId;
import java.util.Set;

/* compiled from: KiwiRequestHandler */
public final class c implements com.amazon.device.iap.internal.c {
    private static final String a = c.class.getSimpleName();

    public void a(RequestId requestId) {
        e.a(a, "sendGetUserData");
        new a(requestId).e();
    }

    public void a(RequestId requestId, String str) {
        e.a(a, "sendPurchaseRequest");
        new d(requestId, str).e();
    }

    public void a(RequestId requestId, Set<String> set) {
        e.a(a, "sendGetProductDataRequest");
        new com.amazon.device.iap.internal.b.c.d(requestId, set).e();
    }

    public void a(RequestId requestId, boolean z) {
        e.a(a, "sendGetPurchaseUpdates");
        new com.amazon.device.iap.internal.b.d.a(requestId, z).e();
    }

    public void a(RequestId requestId, String str, FulfillmentResult fulfillmentResult) {
        e.a(a, "sendNotifyFulfillment");
        new b(requestId, str, fulfillmentResult).e();
    }

    public void a(Context context, Intent intent) {
        e.a(a, "handleResponse");
        String stringExtra = intent.getStringExtra("response_type");
        if (stringExtra == null) {
            e.a(a, "Invalid response type: null");
            return;
        }
        e.a(a, "Found response type: " + stringExtra);
        if ("purchase_response".equals(stringExtra)) {
            new com.amazon.device.iap.internal.b.a.d(RequestId.fromString(intent.getStringExtra("requestId"))).e();
        }
    }
}
