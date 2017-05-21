package com.anjlab.android.iab.v3;

import android.util.Log;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public class PurchaseInfo {
    private static final String LOG_TAG = "iabv3.purchaseInfo";
    public final String responseData;
    public final String signature;

    public enum PurchaseState {
        PurchasedSuccessfully,
        Canceled,
        Refunded,
        SubscriptionExpired
    }

    public class ResponseData {
        public boolean autoRenewing;
        public String developerPayload;
        public String orderId;
        public String packageName;
        public String productId;
        public PurchaseState purchaseState;
        public Date purchaseTime;
        public String purchaseToken;
    }

    PurchaseInfo(String responseData, String signature) {
        this.responseData = responseData;
        this.signature = signature;
    }

    public static PurchaseState getPurchaseState(int state) {
        switch (state) {
            case 0:
                return PurchaseState.PurchasedSuccessfully;
            case 1:
                return PurchaseState.Canceled;
            case 2:
                return PurchaseState.Refunded;
            case 3:
                return PurchaseState.SubscriptionExpired;
            default:
                return PurchaseState.Canceled;
        }
    }

    public ResponseData parseResponseData() {
        try {
            Date date;
            JSONObject json = new JSONObject(this.responseData);
            ResponseData data = new ResponseData();
            data.orderId = json.optString(Constants.RESPONSE_ORDER_ID);
            data.packageName = json.optString("packageName");
            data.productId = json.optString(Constants.RESPONSE_PRODUCT_ID);
            long purchaseTimeMillis = json.optLong(Constants.RESPONSE_PURCHASE_TIME, 0);
            if (purchaseTimeMillis != 0) {
                date = new Date(purchaseTimeMillis);
            } else {
                date = null;
            }
            data.purchaseTime = date;
            data.purchaseState = getPurchaseState(json.optInt("purchaseState", 1));
            data.developerPayload = json.optString(Constants.RESPONSE_PAYLOAD);
            data.purchaseToken = json.getString(Constants.RESPONSE_PURCHASE_TOKEN);
            data.autoRenewing = json.optBoolean("autoRenewing");
            return data;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Failed to parse response data " + e.toString());
            return null;
        }
    }
}
