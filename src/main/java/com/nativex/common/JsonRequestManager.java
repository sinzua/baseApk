package com.nativex.common;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.nativex.common.JsonRequestConstants.UDIDs;
import com.nativex.common.billingtracking.InAppPurchaseRequestData;
import com.nativex.monetization.manager.MonetizationSharedDataManager;
import com.nativex.monetization.manager.SessionManager;

public class JsonRequestManager {
    private final Device device;

    protected JsonRequestManager(Device device) {
        this.device = device;
    }

    protected Device getDevice() {
        return this.device;
    }

    protected UDIDs generateUdidJsonCollection() {
        try {
            UDIDs udids = new UDIDs();
            String advertiserId = MonetizationSharedDataManager.getAdvertiserId();
            if (advertiserId == null || advertiserId.trim().length() <= 0) {
                udids.addUDID(UDIDs.ANDROID_DEVICE_ID, this.device.getAndroidDeviceID());
                udids.addUDID(UDIDs.ANDROID_ID, this.device.getAndroidID());
                return udids;
            }
            udids.addUDID(UDIDs.ANDROID_ADVERTISER_ID, advertiserId);
            return udids;
        } catch (Exception e) {
            Log.e("AdvertiserJsonRequestManager: Unexpected exception caught in generateUdidJsonCollection().", e);
            return null;
        }
    }

    public static String getInAppBillingBody(String storeProductId, String storeTransactionId, String storeTransactionTimeUTC, float costPerItem, String currencyLocale, int quantity, String productTitle) {
        String publisherSession = SessionManager.getSessionId();
        if (TextUtils.isEmpty(publisherSession)) {
            Log.d("InAppBilling: No session");
            return null;
        }
        if (!storeTransactionTimeUTC.contains("Date")) {
            storeTransactionTimeUTC = "/Date(" + storeTransactionTimeUTC + ")/";
        }
        Object request = new InAppPurchaseRequestData();
        request.setPublisherSession(publisherSession);
        request.setStoreProductId(storeProductId);
        request.setStoreTransactionId(storeTransactionId);
        request.setStoreTransactionTime(storeTransactionTimeUTC);
        request.setCostPerItem(costPerItem);
        request.setCurrencyLocale(currencyLocale);
        request.setQuantity(quantity);
        request.setProductTitle(productTitle);
        String requestBody = new Gson().toJson(request);
        Log.d("BillingBody: " + requestBody);
        return requestBody;
    }
}
