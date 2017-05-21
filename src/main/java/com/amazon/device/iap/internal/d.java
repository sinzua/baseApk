package com.amazon.device.iap.internal;

import android.content.Context;
import android.content.Intent;
import com.amazon.device.iap.PurchasingListener;
import com.amazon.device.iap.internal.util.e;
import com.amazon.device.iap.model.FulfillmentResult;
import com.amazon.device.iap.model.RequestId;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/* compiled from: PurchasingManager */
public class d {
    private static String a = d.class.getSimpleName();
    private static String b = "sku";
    private static d c = new d();
    private final c d = e.b();
    private Context e;
    private PurchasingListener f;

    private d() {
    }

    public PurchasingListener a() {
        return this.f;
    }

    public Context b() {
        return this.e;
    }

    private void e() {
        if (this.f == null) {
            throw new IllegalStateException("You must register a PurchasingListener before invoking this operation");
        }
    }

    public void a(Context context, PurchasingListener purchasingListener) {
        e.a(a, "PurchasingListener registered: " + purchasingListener);
        e.a(a, "PurchasingListener Context: " + context);
        if (purchasingListener == null || context == null) {
            throw new IllegalArgumentException("Neither PurchasingListener or its Context can be null");
        }
        this.e = context.getApplicationContext();
        this.f = purchasingListener;
    }

    public RequestId c() {
        e();
        RequestId requestId = new RequestId();
        this.d.a(requestId);
        return requestId;
    }

    public RequestId a(String str) {
        com.amazon.device.iap.internal.util.d.a((Object) str, b);
        e();
        RequestId requestId = new RequestId();
        this.d.a(requestId, str);
        return requestId;
    }

    public RequestId a(Set<String> set) {
        com.amazon.device.iap.internal.util.d.a((Object) set, "skus");
        com.amazon.device.iap.internal.util.d.a((Collection) set, "skus");
        for (String trim : set) {
            if (trim.trim().length() == 0) {
                throw new IllegalArgumentException("Empty SKU values are not allowed");
            }
        }
        if (set.size() > 100) {
            throw new IllegalArgumentException(set.size() + " SKUs were provided, but no more than " + 100 + " SKUs are allowed");
        }
        e();
        RequestId requestId = new RequestId();
        this.d.a(requestId, new LinkedHashSet(set));
        return requestId;
    }

    public RequestId a(boolean z) {
        e();
        RequestId requestId = new RequestId();
        this.d.a(requestId, z);
        return requestId;
    }

    public void a(String str, FulfillmentResult fulfillmentResult) {
        if (com.amazon.device.iap.internal.util.d.a(str)) {
            throw new IllegalArgumentException("Empty receiptId is not allowed");
        }
        com.amazon.device.iap.internal.util.d.a((Object) fulfillmentResult, "fulfillmentResult");
        e();
        this.d.a(new RequestId(), str, fulfillmentResult);
    }

    public void a(Context context, Intent intent) {
        try {
            this.d.a(context, intent);
        } catch (Exception e) {
            e.b(a, "Error in onReceive: " + e);
        }
    }

    public static d d() {
        return c;
    }
}
