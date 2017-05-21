package com.nativex.common.billingtracking;

import com.nativex.common.JsonRequestConstants.URLS;
import com.nativex.common.JsonRequestManager;
import com.nativex.common.Log;
import com.nativex.common.ServerConfig;
import com.nativex.network.volley.Response.ErrorListener;
import com.nativex.network.volley.Response.Listener;
import com.nativex.network.volley.VolleyError;
import com.nativex.volleytoolbox.GsonRequest;
import com.nativex.volleytoolbox.NativeXVolley;

public class BillingCallback {
    public static void trackPurchase(String storeProductId, String storeTransactionId, String storeTransactionTimeUTC, float costPerItem, String currencyLocale, int quantity, String productTitle) {
        String requestBody = JsonRequestManager.getInAppBillingBody(storeProductId, storeTransactionId, storeTransactionTimeUTC, costPerItem, currencyLocale, quantity, productTitle);
        if (requestBody != null) {
            try {
                GsonRequest<InAppPurchaseResponseData> request = new GsonRequest(1, ServerConfig.applyConfiguration(URLS.IN_APP_PURCHASE, false), InAppPurchaseResponseData.class, requestBody, new Listener<InAppPurchaseResponseData>() {
                    public void onResponse(InAppPurchaseResponseData responseData) {
                        if (responseData == null) {
                            Log.d("InAppBilling response failed.");
                        } else if (responseData.isSuccessful().booleanValue()) {
                            Log.d("InAppBilling response successful.");
                        }
                    }
                }, new ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ServerRequestManager: Error obtained during InAppBilling request. " + error.networkResponse.getMessage());
                    }
                });
                request.setRequestName("InAppBillingRequest");
                NativeXVolley.getInstance().getRequestQueue().add(request);
            } catch (Exception e) {
                Log.d("ServerRequestManager: Unexpected exception caught while executing InAppBilling request.", e);
            }
        }
    }
}
