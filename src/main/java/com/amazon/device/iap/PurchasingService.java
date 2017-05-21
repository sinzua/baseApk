package com.amazon.device.iap;

import android.content.Context;
import android.util.Log;
import com.amazon.device.iap.internal.d;
import com.amazon.device.iap.internal.e;
import com.amazon.device.iap.model.FulfillmentResult;
import com.amazon.device.iap.model.RequestId;
import java.util.Set;

public final class PurchasingService {
    public static final boolean IS_SANDBOX_MODE = e.a();
    public static final String SDK_VERSION = "2.0.61.0";
    private static final String TAG = PurchasingService.class.getSimpleName();

    private PurchasingService() {
        Log.i(TAG, "In-App Purchasing SDK initializing. SDK Version 2.0.61.0, IS_SANDBOX_MODE: " + IS_SANDBOX_MODE);
    }

    public static void registerListener(Context context, PurchasingListener purchasingListener) {
        d.d().a(context, purchasingListener);
    }

    public static RequestId getUserData() {
        return d.d().c();
    }

    public static RequestId purchase(String str) {
        return d.d().a(str);
    }

    public static RequestId getProductData(Set<String> set) {
        return d.d().a((Set) set);
    }

    public static RequestId getPurchaseUpdates(boolean z) {
        return d.d().a(z);
    }

    public static void notifyFulfillment(String str, FulfillmentResult fulfillmentResult) {
        d.d().a(str, fulfillmentResult);
    }
}
