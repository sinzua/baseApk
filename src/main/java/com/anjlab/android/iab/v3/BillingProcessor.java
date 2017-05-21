package com.anjlab.android.iab.v3;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import com.android.vending.billing.IInAppBillingService;
import com.android.vending.billing.IInAppBillingService.Stub;
import com.google.android.gms.common.zze;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public class BillingProcessor extends BillingBase {
    private static final String LOG_TAG = "iabv3";
    private static final String MANAGED_PRODUCTS_CACHE_KEY = ".products.cache.v2_6";
    private static final int PURCHASE_FLOW_REQUEST_CODE = 2061984;
    private static final String PURCHASE_PAYLOAD_CACHE_KEY = ".purchase.last.v2_6";
    private static final String RESTORE_KEY = ".products.restored.v2_6";
    private static final String SETTINGS_VERSION = ".v2_6";
    private static final String SUBSCRIPTIONS_CACHE_KEY = ".subscriptions.cache.v2_6";
    private static final Date dateMerchantLimit1 = new Date(2012, 12, 5);
    private static final Date dateMerchantLimit2 = new Date(2015, 7, 20);
    private IInAppBillingService billingService;
    private BillingCache cachedProducts;
    private BillingCache cachedSubscriptions;
    private String contextPackageName;
    private String developerMerchantId;
    private IBillingHandler eventHandler;
    private ServiceConnection serviceConnection;
    private String signatureBase64;

    public interface IBillingHandler {
        void onBillingError(int i, Throwable th);

        void onBillingInitialized();

        void onProductPurchased(String str, TransactionDetails transactionDetails);

        void onPurchaseHistoryRestored();
    }

    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public BillingProcessor(Context context, String licenseKey, IBillingHandler handler) {
        this(context, licenseKey, null, handler);
    }

    public BillingProcessor(Context context, String licenseKey, String merchantId, IBillingHandler handler) {
        super(context);
        this.serviceConnection = new ServiceConnection() {
            public void onServiceDisconnected(ComponentName name) {
                BillingProcessor.this.billingService = null;
            }

            public void onServiceConnected(ComponentName name, IBinder service) {
                BillingProcessor.this.billingService = Stub.asInterface(service);
                if (!BillingProcessor.this.isPurchaseHistoryRestored() && BillingProcessor.this.loadOwnedPurchasesFromGoogle()) {
                    BillingProcessor.this.setPurchaseHistoryRestored();
                    if (BillingProcessor.this.eventHandler != null) {
                        BillingProcessor.this.eventHandler.onPurchaseHistoryRestored();
                    }
                }
                if (BillingProcessor.this.eventHandler != null) {
                    BillingProcessor.this.eventHandler.onBillingInitialized();
                }
            }
        };
        this.signatureBase64 = licenseKey;
        this.eventHandler = handler;
        this.contextPackageName = context.getApplicationContext().getPackageName();
        this.cachedProducts = new BillingCache(context, MANAGED_PRODUCTS_CACHE_KEY);
        this.cachedSubscriptions = new BillingCache(context, SUBSCRIPTIONS_CACHE_KEY);
        this.developerMerchantId = merchantId;
        bindPlayServices();
    }

    private void bindPlayServices() {
        try {
            Intent iapIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            iapIntent.setPackage(zze.GOOGLE_PLAY_STORE_PACKAGE);
            getContext().bindService(iapIntent, this.serviceConnection, 1);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    public void release() {
        if (!(this.serviceConnection == null || getContext() == null)) {
            try {
                getContext().unbindService(this.serviceConnection);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.toString());
            }
            this.billingService = null;
        }
        this.cachedProducts.release();
        super.release();
    }

    public boolean isInitialized() {
        return this.billingService != null;
    }

    public boolean isPurchased(String productId) {
        return this.cachedProducts.includesProduct(productId);
    }

    public boolean isSubscribed(String productId) {
        return this.cachedSubscriptions.includesProduct(productId);
    }

    public List<String> listOwnedProducts() {
        return this.cachedProducts.getContents();
    }

    public List<String> listOwnedSubscriptions() {
        return this.cachedSubscriptions.getContents();
    }

    private boolean loadPurchasesByType(String type, BillingCache cacheStorage) {
        if (!isInitialized()) {
            return false;
        }
        try {
            Bundle bundle = this.billingService.getPurchases(3, this.contextPackageName, type, null);
            if (bundle.getInt(Constants.RESPONSE_CODE) == 0) {
                cacheStorage.clear();
                ArrayList<String> purchaseList = bundle.getStringArrayList(Constants.INAPP_PURCHASE_DATA_LIST);
                ArrayList<String> signatureList = bundle.getStringArrayList(Constants.INAPP_DATA_SIGNATURE_LIST);
                if (purchaseList != null) {
                    int i = 0;
                    while (i < purchaseList.size()) {
                        String jsonData = (String) purchaseList.get(i);
                        JSONObject purchase = new JSONObject(jsonData);
                        String signature = (signatureList == null || signatureList.size() <= i) ? null : (String) signatureList.get(i);
                        cacheStorage.put(purchase.getString(Constants.RESPONSE_PRODUCT_ID), jsonData, signature);
                        i++;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            if (this.eventHandler != null) {
                this.eventHandler.onBillingError(100, e);
            }
            Log.e(LOG_TAG, e.toString());
            return false;
        }
    }

    public boolean loadOwnedPurchasesFromGoogle() {
        return isInitialized() && loadPurchasesByType(Constants.PRODUCT_TYPE_MANAGED, this.cachedProducts) && loadPurchasesByType(Constants.PRODUCT_TYPE_SUBSCRIPTION, this.cachedSubscriptions);
    }

    public boolean purchase(Activity activity, String productId) {
        return purchase(activity, productId, Constants.PRODUCT_TYPE_MANAGED);
    }

    public boolean subscribe(Activity activity, String productId) {
        return purchase(activity, productId, Constants.PRODUCT_TYPE_SUBSCRIPTION);
    }

    private boolean purchase(Activity activity, String productId, String purchaseType) {
        if (!isInitialized() || TextUtils.isEmpty(productId) || TextUtils.isEmpty(purchaseType)) {
            return false;
        }
        try {
            String purchasePayload = purchaseType + ":" + UUID.randomUUID().toString();
            savePurchasePayload(purchasePayload);
            Bundle bundle = this.billingService.getBuyIntent(3, this.contextPackageName, productId, purchaseType, purchasePayload);
            if (bundle != null) {
                int response = bundle.getInt(Constants.RESPONSE_CODE);
                if (response == 0) {
                    PendingIntent pendingIntent = (PendingIntent) bundle.getParcelable(Constants.BUY_INTENT);
                    if (activity != null && pendingIntent != null) {
                        activity.startIntentSenderForResult(pendingIntent.getIntentSender(), PURCHASE_FLOW_REQUEST_CODE, new Intent(), 0, 0, 0);
                    } else if (this.eventHandler != null) {
                        this.eventHandler.onBillingError(103, null);
                    }
                } else if (response == 7) {
                    if (!(isPurchased(productId) || isSubscribed(productId))) {
                        loadOwnedPurchasesFromGoogle();
                    }
                    TransactionDetails details = getPurchaseTransactionDetails(productId);
                    if (!checkMerchant(details)) {
                        Log.i(LOG_TAG, "Invalid or tampered merchant id!");
                        if (this.eventHandler != null) {
                            this.eventHandler.onBillingError(104, null);
                        }
                        return false;
                    } else if (this.eventHandler != null) {
                        if (details == null) {
                            details = getSubscriptionTransactionDetails(productId);
                        }
                        this.eventHandler.onProductPurchased(productId, details);
                    }
                } else if (this.eventHandler != null) {
                    this.eventHandler.onBillingError(101, null);
                }
            }
            return true;
        } catch (Throwable e) {
            Log.e(LOG_TAG, e.toString());
            if (this.eventHandler != null) {
                this.eventHandler.onBillingError(Constants.BILLING_ERROR_OTHER_ERROR, e);
            }
            return false;
        }
    }

    private boolean checkMerchant(TransactionDetails details) {
        if (this.developerMerchantId == null || details.purchaseTime.before(dateMerchantLimit1) || details.purchaseTime.after(dateMerchantLimit2)) {
            return true;
        }
        if (details.orderId == null || details.orderId.trim().length() == 0) {
            return false;
        }
        int index = details.orderId.indexOf(46);
        if (index <= 0) {
            return false;
        }
        if (details.orderId.substring(0, index).compareTo(this.developerMerchantId) != 0) {
            return false;
        }
        return true;
    }

    private TransactionDetails getPurchaseTransactionDetails(String productId, BillingCache cache) {
        PurchaseInfo details = cache.getDetails(productId);
        if (!(details == null || TextUtils.isEmpty(details.responseData))) {
            try {
                return new TransactionDetails(details);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Failed to load saved purchase details for " + productId);
            }
        }
        return null;
    }

    public boolean consumePurchase(String productId) {
        if (!isInitialized()) {
            return false;
        }
        try {
            TransactionDetails transactionDetails = getPurchaseTransactionDetails(productId, this.cachedProducts);
            if (transactionDetails == null || TextUtils.isEmpty(transactionDetails.purchaseToken)) {
                return false;
            }
            int response = this.billingService.consumePurchase(3, this.contextPackageName, transactionDetails.purchaseToken);
            if (response == 0) {
                this.cachedProducts.remove(productId);
                Log.d(LOG_TAG, "Successfully consumed " + productId + " purchase.");
                return true;
            }
            if (this.eventHandler != null) {
                this.eventHandler.onBillingError(response, null);
            }
            Log.e(LOG_TAG, String.format("Failed to consume %s: error %d", new Object[]{productId, Integer.valueOf(response)}));
            return false;
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
            if (this.eventHandler == null) {
                return false;
            }
            this.eventHandler.onBillingError(111, e);
            return false;
        }
    }

    private SkuDetails getSkuDetails(String productId, String purchaseType) {
        ArrayList productIdList = new ArrayList();
        productIdList.add(productId);
        List<SkuDetails> skuDetailsList = getSkuDetails(productIdList, purchaseType);
        if (skuDetailsList == null || skuDetailsList.size() <= 0) {
            return null;
        }
        return (SkuDetails) skuDetailsList.get(0);
    }

    private List<SkuDetails> getSkuDetails(ArrayList<String> productIdList, String purchaseType) {
        if (!(this.billingService == null || productIdList == null || productIdList.size() <= 0)) {
            try {
                Bundle products = new Bundle();
                products.putStringArrayList(Constants.PRODUCTS_LIST, productIdList);
                Bundle skuDetails = this.billingService.getSkuDetails(3, this.contextPackageName, purchaseType, products);
                if (skuDetails.getInt(Constants.RESPONSE_CODE) == 0) {
                    List<SkuDetails> arrayList = new ArrayList();
                    List<String> detailsList = skuDetails.getStringArrayList(Constants.DETAILS_LIST);
                    if (detailsList == null) {
                        return arrayList;
                    }
                    for (String responseLine : detailsList) {
                        arrayList.add(new SkuDetails(new JSONObject(responseLine)));
                    }
                    return arrayList;
                }
                Log.e(LOG_TAG, String.format("Failed to retrieve info for %d products, %d", new Object[]{Integer.valueOf(productIdList.size()), Integer.valueOf(response)}));
            } catch (Exception e) {
                Log.e(LOG_TAG, String.format("Failed to call getSkuDetails %s", new Object[]{e.toString()}));
                if (this.eventHandler != null) {
                    this.eventHandler.onBillingError(112, e);
                }
            }
        }
        return null;
    }

    public SkuDetails getPurchaseListingDetails(String productId) {
        return getSkuDetails(productId, Constants.PRODUCT_TYPE_MANAGED);
    }

    public SkuDetails getSubscriptionListingDetails(String productId) {
        return getSkuDetails(productId, Constants.PRODUCT_TYPE_SUBSCRIPTION);
    }

    public List<SkuDetails> getPurchaseListingDetails(ArrayList<String> productIdList) {
        return getSkuDetails((ArrayList) productIdList, Constants.PRODUCT_TYPE_MANAGED);
    }

    public List<SkuDetails> getSubscriptionListingDetails(ArrayList<String> productIdList) {
        return getSkuDetails((ArrayList) productIdList, Constants.PRODUCT_TYPE_SUBSCRIPTION);
    }

    public TransactionDetails getPurchaseTransactionDetails(String productId) {
        return getPurchaseTransactionDetails(productId, this.cachedProducts);
    }

    public TransactionDetails getSubscriptionTransactionDetails(String productId) {
        return getPurchaseTransactionDetails(productId, this.cachedSubscriptions);
    }

    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != PURCHASE_FLOW_REQUEST_CODE) {
            return false;
        }
        if (data == null) {
            Log.e(LOG_TAG, "handleActivityResult: data is null!");
            return false;
        }
        int responseCode = data.getIntExtra(Constants.RESPONSE_CODE, 0);
        Log.d(LOG_TAG, String.format("resultCode = %d, responseCode = %d", new Object[]{Integer.valueOf(resultCode), Integer.valueOf(responseCode)}));
        String purchasePayload = getPurchasePayload();
        if (resultCode == -1 && responseCode == 0 && !TextUtils.isEmpty(purchasePayload)) {
            String purchaseData = data.getStringExtra(Constants.INAPP_PURCHASE_DATA);
            String dataSignature = data.getStringExtra(Constants.RESPONSE_INAPP_SIGNATURE);
            try {
                JSONObject purchase = new JSONObject(purchaseData);
                String productId = purchase.getString(Constants.RESPONSE_PRODUCT_ID);
                String developerPayload = purchase.getString(Constants.RESPONSE_PAYLOAD);
                if (developerPayload == null) {
                    developerPayload = "";
                }
                boolean purchasedSubscription = purchasePayload.startsWith(Constants.PRODUCT_TYPE_SUBSCRIPTION);
                if (!purchasePayload.equals(developerPayload)) {
                    Log.e(LOG_TAG, String.format("Payload mismatch: %s != %s", new Object[]{purchasePayload, developerPayload}));
                    if (this.eventHandler != null) {
                        this.eventHandler.onBillingError(102, null);
                    }
                } else if (verifyPurchaseSignature(productId, purchaseData, dataSignature)) {
                    (purchasedSubscription ? this.cachedSubscriptions : this.cachedProducts).put(productId, purchaseData, dataSignature);
                    if (this.eventHandler != null) {
                        this.eventHandler.onProductPurchased(productId, new TransactionDetails(new PurchaseInfo(purchaseData, dataSignature)));
                    }
                } else {
                    Log.e(LOG_TAG, "Public key signature doesn't match!");
                    if (this.eventHandler != null) {
                        this.eventHandler.onBillingError(102, null);
                    }
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, e.toString());
                if (this.eventHandler != null) {
                    this.eventHandler.onBillingError(Constants.BILLING_ERROR_OTHER_ERROR, e);
                }
            }
        } else if (this.eventHandler != null) {
            this.eventHandler.onBillingError(responseCode, null);
        }
        return true;
    }

    private boolean verifyPurchaseSignature(String productId, String purchaseData, String dataSignature) {
        try {
            return TextUtils.isEmpty(this.signatureBase64) || Security.verifyPurchase(productId, this.signatureBase64, purchaseData, dataSignature);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValid(TransactionDetails transactionDetails) {
        return verifyPurchaseSignature(transactionDetails.productId, transactionDetails.purchaseInfo.responseData, transactionDetails.purchaseInfo.signature) && checkMerchant(transactionDetails);
    }

    private boolean isPurchaseHistoryRestored() {
        return loadBoolean(getPreferencesBaseKey() + RESTORE_KEY, false);
    }

    public void setPurchaseHistoryRestored() {
        saveBoolean(getPreferencesBaseKey() + RESTORE_KEY, Boolean.valueOf(true));
    }

    private void savePurchasePayload(String value) {
        saveString(getPreferencesBaseKey() + PURCHASE_PAYLOAD_CACHE_KEY, value);
    }

    private String getPurchasePayload() {
        return loadString(getPreferencesBaseKey() + PURCHASE_PAYLOAD_CACHE_KEY, null);
    }

    public static boolean isIabServiceAvailable(Context context) {
        if (context.getPackageManager().queryIntentServices(new Intent("com.android.vending.billing.InAppBillingService.BIND"), 0).size() > 0) {
            return true;
        }
        return false;
    }
}
