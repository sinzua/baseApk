package com.amazon.device.iap.internal.a;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.amazon.device.iap.PurchasingListener;
import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.internal.d;
import com.amazon.device.iap.internal.model.ProductBuilder;
import com.amazon.device.iap.internal.model.ProductDataResponseBuilder;
import com.amazon.device.iap.internal.model.PurchaseResponseBuilder;
import com.amazon.device.iap.internal.model.PurchaseUpdatesResponseBuilder;
import com.amazon.device.iap.internal.model.ReceiptBuilder;
import com.amazon.device.iap.internal.model.UserDataBuilder;
import com.amazon.device.iap.internal.model.UserDataResponseBuilder;
import com.amazon.device.iap.internal.util.b;
import com.amazon.device.iap.internal.util.e;
import com.amazon.device.iap.model.FulfillmentResult;
import com.amazon.device.iap.model.Product;
import com.amazon.device.iap.model.ProductDataResponse;
import com.amazon.device.iap.model.ProductType;
import com.amazon.device.iap.model.PurchaseResponse;
import com.amazon.device.iap.model.PurchaseUpdatesResponse;
import com.amazon.device.iap.model.PurchaseUpdatesResponse.RequestStatus;
import com.amazon.device.iap.model.Receipt;
import com.amazon.device.iap.model.RequestId;
import com.amazon.device.iap.model.UserData;
import com.amazon.device.iap.model.UserDataResponse;
import com.nativex.common.JsonRequestConstants.UnityRewardData;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: SandboxRequestHandler */
public final class c implements com.amazon.device.iap.internal.c {
    private static final String a = c.class.getSimpleName();

    public void a(RequestId requestId) {
        e.a(a, "sendGetUserDataRequest");
        a(requestId.toString(), false, false);
    }

    private void a(String str, boolean z, boolean z2) {
        try {
            Context b = d.d().b();
            Bundle bundle = new Bundle();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("requestId", str);
            jSONObject.put("packageName", b.getPackageName());
            jSONObject.put("sdkVersion", PurchasingService.SDK_VERSION);
            jSONObject.put("isPurchaseUpdates", z);
            jSONObject.put("reset", z2);
            bundle.putString("userInput", jSONObject.toString());
            Intent a = a("com.amazon.testclient.iap.appUserId");
            a.addFlags(268435456);
            a.putExtras(bundle);
            b.startService(a);
        } catch (JSONException e) {
            e.b(a, "Error in sendGetUserDataRequest.");
        }
    }

    public void a(RequestId requestId, String str) {
        e.a(a, "sendPurchaseRequest");
        try {
            Context b = d.d().b();
            Bundle bundle = new Bundle();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("sku", str);
            jSONObject.put("requestId", requestId.toString());
            jSONObject.put("packageName", b.getPackageName());
            jSONObject.put("sdkVersion", PurchasingService.SDK_VERSION);
            bundle.putString("purchaseInput", jSONObject.toString());
            Intent a = a("com.amazon.testclient.iap.purchase");
            a.addFlags(268435456);
            a.putExtras(bundle);
            b.startService(a);
        } catch (JSONException e) {
            e.b(a, "Error in sendPurchaseRequest.");
        }
    }

    public void a(RequestId requestId, Set<String> set) {
        e.a(a, "sendItemDataRequest");
        try {
            Context b = d.d().b();
            Bundle bundle = new Bundle();
            JSONObject jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray(set);
            jSONObject.put("requestId", requestId.toString());
            jSONObject.put("packageName", b.getPackageName());
            jSONObject.put("skus", jSONArray);
            jSONObject.put("sdkVersion", PurchasingService.SDK_VERSION);
            bundle.putString("itemDataInput", jSONObject.toString());
            Intent a = a("com.amazon.testclient.iap.itemData");
            a.addFlags(268435456);
            a.putExtras(bundle);
            b.startService(a);
        } catch (JSONException e) {
            e.b(a, "Error in sendItemDataRequest.");
        }
    }

    public void a(RequestId requestId, boolean z) {
        if (requestId == null) {
            requestId = new RequestId();
        }
        e.a(a, "sendPurchaseUpdatesRequest/sendGetUserData first:" + requestId);
        a(requestId.toString(), true, z);
    }

    public void a(RequestId requestId, String str, FulfillmentResult fulfillmentResult) {
        e.a(a, "sendNotifyPurchaseFulfilled");
        try {
            Context b = d.d().b();
            Bundle bundle = new Bundle();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("requestId", requestId.toString());
            jSONObject.put("packageName", b.getPackageName());
            jSONObject.put("receiptId", str);
            jSONObject.put("fulfillmentResult", fulfillmentResult);
            jSONObject.put("sdkVersion", PurchasingService.SDK_VERSION);
            bundle.putString("purchaseFulfilledInput", jSONObject.toString());
            Intent a = a("com.amazon.testclient.iap.purchaseFulfilled");
            a.addFlags(268435456);
            a.putExtras(bundle);
            b.startService(a);
        } catch (JSONException e) {
            e.b(a, "Error in sendNotifyPurchaseFulfilled.");
        }
    }

    public void a(Context context, Intent intent) {
        e.a(a, "handleResponse");
        intent.setComponent(new ComponentName("com.amazon.sdktestclient", "com.amazon.sdktestclient.command.CommandBroker"));
        try {
            String string = intent.getExtras().getString("responseType");
            if (string.equalsIgnoreCase("com.amazon.testclient.iap.purchase")) {
                g(intent);
            } else if (string.equalsIgnoreCase("com.amazon.testclient.iap.appUserId")) {
                e(intent);
            } else if (string.equalsIgnoreCase("com.amazon.testclient.iap.itemData")) {
                c(intent);
            } else if (string.equalsIgnoreCase("com.amazon.testclient.iap.purchaseUpdates")) {
                a(intent);
            }
        } catch (Throwable e) {
            Log.e(a, "Error handling response.", e);
        }
    }

    private Intent a(String str) {
        Intent intent = new Intent(str);
        intent.setComponent(new ComponentName("com.amazon.sdktestclient", "com.amazon.sdktestclient.command.CommandBroker"));
        return intent;
    }

    protected void a(final Object obj) {
        com.amazon.device.iap.internal.util.d.a(obj, "response");
        Context b = d.d().b();
        final PurchasingListener a = d.d().a();
        if (b == null || a == null) {
            e.a(a, "PurchasingListener is not set. Dropping response: " + obj);
            return;
        }
        new Handler(b.getMainLooper()).post(new Runnable(this) {
            final /* synthetic */ c c;

            public void run() {
                try {
                    if (obj instanceof ProductDataResponse) {
                        a.onProductDataResponse((ProductDataResponse) obj);
                    } else if (obj instanceof UserDataResponse) {
                        a.onUserDataResponse((UserDataResponse) obj);
                    } else if (obj instanceof PurchaseUpdatesResponse) {
                        a.onPurchaseUpdatesResponse((PurchaseUpdatesResponse) obj);
                    } else if (obj instanceof PurchaseResponse) {
                        a.onPurchaseResponse((PurchaseResponse) obj);
                    } else {
                        e.b(c.a, "Unknown response type:" + obj.getClass().getName());
                    }
                } catch (Exception e) {
                    e.b(c.a, "Error in sendResponse: " + e);
                }
            }
        });
    }

    private void a(Intent intent) throws JSONException {
        Object b = b(intent);
        if (b.getRequestStatus() == RequestStatus.SUCCESSFUL) {
            String optString = new JSONObject(intent.getStringExtra("purchaseUpdatesOutput")).optString("offset");
            Log.i(a, "Offset for PurchaseUpdatesResponse:" + optString);
            b.a(b.getUserData().getUserId(), optString);
        }
        a(b);
    }

    private PurchaseUpdatesResponse b(Intent intent) {
        UserData build;
        RequestId requestId;
        Throwable th;
        List list;
        UserData userData;
        RequestStatus requestStatus;
        boolean z;
        Throwable th2;
        int i = 0;
        RequestStatus requestStatus2 = RequestStatus.FAILED;
        try {
            JSONObject jSONObject = new JSONObject(intent.getStringExtra("purchaseUpdatesOutput"));
            RequestId fromString = RequestId.fromString(jSONObject.optString("requestId"));
            try {
                requestStatus2 = RequestStatus.valueOf(jSONObject.optString("status"));
                boolean optBoolean = jSONObject.optBoolean("isMore");
                try {
                    String optString = jSONObject.optString("userId");
                    build = new UserDataBuilder().setUserId(optString).setMarketplace(jSONObject.optString("marketplace")).build();
                } catch (Throwable e) {
                    requestId = fromString;
                    th = e;
                    list = null;
                    boolean z2 = optBoolean;
                    userData = null;
                    requestStatus = requestStatus2;
                    z = z2;
                    Log.e(a, "Error parsing purchase updates output", th);
                    return new PurchaseUpdatesResponseBuilder().setRequestId(requestId).setRequestStatus(requestStatus).setUserData(userData).setReceipts(list).setHasMore(z).build();
                }
                try {
                    if (requestStatus2 == RequestStatus.SUCCESSFUL) {
                        list = new ArrayList();
                        try {
                            JSONArray optJSONArray = jSONObject.optJSONArray(UnityRewardData.RECEIPTS);
                            if (optJSONArray != null) {
                                while (i < optJSONArray.length()) {
                                    jSONObject = optJSONArray.optJSONObject(i);
                                    try {
                                        list.add(a(jSONObject));
                                    } catch (Exception e2) {
                                        Log.e(a, "Failed to parse receipt from json:" + jSONObject);
                                    }
                                    i++;
                                }
                            }
                        } catch (Throwable e3) {
                            th2 = e3;
                            requestStatus = requestStatus2;
                            z = optBoolean;
                            userData = build;
                            requestId = fromString;
                            th = th2;
                        }
                    } else {
                        list = null;
                    }
                    requestStatus = requestStatus2;
                    z = optBoolean;
                    userData = build;
                    requestId = fromString;
                } catch (Throwable e4) {
                    th2 = e4;
                    list = null;
                    requestStatus = requestStatus2;
                    z = optBoolean;
                    userData = build;
                    requestId = fromString;
                    th = th2;
                    Log.e(a, "Error parsing purchase updates output", th);
                    return new PurchaseUpdatesResponseBuilder().setRequestId(requestId).setRequestStatus(requestStatus).setUserData(userData).setReceipts(list).setHasMore(z).build();
                }
            } catch (Throwable e42) {
                userData = null;
                requestId = fromString;
                th = e42;
                list = null;
                requestStatus = requestStatus2;
                z = false;
                Log.e(a, "Error parsing purchase updates output", th);
                return new PurchaseUpdatesResponseBuilder().setRequestId(requestId).setRequestStatus(requestStatus).setUserData(userData).setReceipts(list).setHasMore(z).build();
            }
        } catch (Throwable e422) {
            th = e422;
            userData = null;
            requestId = null;
            list = null;
            requestStatus = requestStatus2;
            z = false;
            Log.e(a, "Error parsing purchase updates output", th);
            return new PurchaseUpdatesResponseBuilder().setRequestId(requestId).setRequestStatus(requestStatus).setUserData(userData).setReceipts(list).setHasMore(z).build();
        }
        return new PurchaseUpdatesResponseBuilder().setRequestId(requestId).setRequestStatus(requestStatus).setUserData(userData).setReceipts(list).setHasMore(z).build();
    }

    private void c(Intent intent) {
        a(d(intent));
    }

    private ProductDataResponse d(Intent intent) {
        Set linkedHashSet;
        Throwable th;
        ProductDataResponse.RequestStatus requestStatus;
        RequestId requestId;
        Throwable th2;
        Map map = null;
        ProductDataResponse.RequestStatus requestStatus2 = ProductDataResponse.RequestStatus.FAILED;
        try {
            JSONObject jSONObject = new JSONObject(intent.getStringExtra("itemDataOutput"));
            RequestId fromString = RequestId.fromString(jSONObject.optString("requestId"));
            try {
                Set set;
                requestStatus2 = ProductDataResponse.RequestStatus.valueOf(jSONObject.optString("status"));
                if (requestStatus2 != ProductDataResponse.RequestStatus.FAILED) {
                    linkedHashSet = new LinkedHashSet();
                    try {
                        Map hashMap = new HashMap();
                        try {
                            JSONArray optJSONArray = jSONObject.optJSONArray("unavailableSkus");
                            if (optJSONArray != null) {
                                for (int i = 0; i < optJSONArray.length(); i++) {
                                    linkedHashSet.add(optJSONArray.getString(i));
                                }
                            }
                            jSONObject = jSONObject.optJSONObject("items");
                            if (jSONObject != null) {
                                Iterator keys = jSONObject.keys();
                                while (keys.hasNext()) {
                                    String str = (String) keys.next();
                                    hashMap.put(str, a(str, jSONObject.optJSONObject(str)));
                                }
                            }
                            map = hashMap;
                            set = linkedHashSet;
                        } catch (Throwable e) {
                            th = e;
                            map = hashMap;
                            requestStatus = requestStatus2;
                            requestId = fromString;
                            th2 = th;
                            Log.e(a, "Error parsing item data output", th2);
                            return new ProductDataResponseBuilder().setRequestId(requestId).setRequestStatus(requestStatus).setProductData(map).setUnavailableSkus(linkedHashSet).build();
                        }
                    } catch (Throwable e2) {
                        th = e2;
                        requestStatus = requestStatus2;
                        requestId = fromString;
                        th2 = th;
                        Log.e(a, "Error parsing item data output", th2);
                        return new ProductDataResponseBuilder().setRequestId(requestId).setRequestStatus(requestStatus).setProductData(map).setUnavailableSkus(linkedHashSet).build();
                    }
                }
                set = null;
                linkedHashSet = set;
                requestStatus = requestStatus2;
                requestId = fromString;
            } catch (Throwable e22) {
                linkedHashSet = null;
                ProductDataResponse.RequestStatus requestStatus3 = requestStatus2;
                requestId = fromString;
                th2 = e22;
                requestStatus = requestStatus3;
                Log.e(a, "Error parsing item data output", th2);
                return new ProductDataResponseBuilder().setRequestId(requestId).setRequestStatus(requestStatus).setProductData(map).setUnavailableSkus(linkedHashSet).build();
            }
        } catch (Throwable e222) {
            th2 = e222;
            linkedHashSet = null;
            requestStatus = requestStatus2;
            requestId = null;
            Log.e(a, "Error parsing item data output", th2);
            return new ProductDataResponseBuilder().setRequestId(requestId).setRequestStatus(requestStatus).setProductData(map).setUnavailableSkus(linkedHashSet).build();
        }
        return new ProductDataResponseBuilder().setRequestId(requestId).setRequestStatus(requestStatus).setProductData(map).setUnavailableSkus(linkedHashSet).build();
    }

    private Product a(String str, JSONObject jSONObject) throws JSONException {
        ProductType valueOf = ProductType.valueOf(jSONObject.optString("itemType"));
        JSONObject jSONObject2 = jSONObject.getJSONObject("priceJson");
        Currency instance = Currency.getInstance(jSONObject2.optString("currency"));
        String str2 = instance.getSymbol() + new BigDecimal(jSONObject2.optString(ParametersKeys.VALUE));
        String optString = jSONObject.optString("title");
        String optString2 = jSONObject.optString("description");
        return new ProductBuilder().setSku(str).setProductType(valueOf).setDescription(optString2).setPrice(str2).setSmallIconUrl(jSONObject.optString("smallIconUrl")).setTitle(optString).build();
    }

    private void e(Intent intent) {
        JSONObject jSONObject;
        Object f = f(intent);
        RequestId requestId = f.getRequestId();
        String stringExtra = intent.getStringExtra("userInput");
        try {
            jSONObject = new JSONObject(stringExtra);
        } catch (Throwable e) {
            Log.e(a, "Unable to parse request data: " + stringExtra, e);
            jSONObject = null;
        }
        if (requestId == null || jSONObject == null) {
            a(f);
        } else if (!jSONObject.optBoolean("isPurchaseUpdates", false)) {
            a(f);
        } else if (f.getUserData() == null || com.amazon.device.iap.internal.util.d.a(f.getUserData().getUserId())) {
            Log.e(a, "No Userid found in userDataResponse" + f);
            a(new PurchaseUpdatesResponseBuilder().setRequestId(requestId).setRequestStatus(RequestStatus.FAILED).setUserData(f.getUserData()).setReceipts(new ArrayList()).setHasMore(false).build());
        } else {
            Log.i(a, "sendGetPurchaseUpdates with user id" + f.getUserData().getUserId());
            a(requestId.toString(), f.getUserData().getUserId(), jSONObject.optBoolean("reset", true));
        }
    }

    private void a(String str, String str2, boolean z) {
        try {
            Context b = d.d().b();
            Object a = b.a(str2);
            Log.i(a, "send PurchaseUpdates with user id:" + str2 + ";reset flag:" + z + ", local cursor:" + a + ", parsed from old requestId:" + str);
            Bundle bundle = new Bundle();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("requestId", str.toString());
            String str3 = "offset";
            if (z) {
                a = null;
            }
            jSONObject.put(str3, a);
            jSONObject.put("sdkVersion", PurchasingService.SDK_VERSION);
            jSONObject.put("packageName", b.getPackageName());
            bundle.putString("purchaseUpdatesInput", jSONObject.toString());
            Intent a2 = a("com.amazon.testclient.iap.purchaseUpdates");
            a2.addFlags(268435456);
            a2.putExtras(bundle);
            b.startService(a2);
        } catch (JSONException e) {
            e.b(a, "Error in sendPurchaseUpdatesRequest.");
        }
    }

    private UserDataResponse f(Intent intent) {
        RequestId fromString;
        UserDataResponse.RequestStatus valueOf;
        Throwable th;
        UserDataResponse.RequestStatus requestStatus;
        UserData userData;
        UserDataResponse.RequestStatus requestStatus2;
        UserData userData2 = null;
        UserDataResponse.RequestStatus requestStatus3 = UserDataResponse.RequestStatus.FAILED;
        try {
            JSONObject jSONObject = new JSONObject(intent.getStringExtra("userOutput"));
            fromString = RequestId.fromString(jSONObject.optString("requestId"));
            try {
                valueOf = UserDataResponse.RequestStatus.valueOf(jSONObject.optString("status"));
            } catch (Throwable e) {
                Throwable th2 = e;
                valueOf = requestStatus3;
                th = th2;
                Log.e(a, "Error parsing userid output", th);
                requestStatus = valueOf;
                userData = null;
                requestStatus2 = requestStatus;
                return new UserDataResponseBuilder().setRequestId(fromString).setRequestStatus(requestStatus2).setUserData(userData).build();
            }
            try {
                if (valueOf == UserDataResponse.RequestStatus.SUCCESSFUL) {
                    String optString = jSONObject.optString("userId");
                    userData2 = new UserDataBuilder().setUserId(optString).setMarketplace(jSONObject.optString("marketplace")).build();
                }
                requestStatus = valueOf;
                userData = userData2;
                requestStatus2 = requestStatus;
            } catch (Exception e2) {
                th = e2;
                Log.e(a, "Error parsing userid output", th);
                requestStatus = valueOf;
                userData = null;
                requestStatus2 = requestStatus;
                return new UserDataResponseBuilder().setRequestId(fromString).setRequestStatus(requestStatus2).setUserData(userData).build();
            }
        } catch (Throwable e3) {
            fromString = null;
            requestStatus = requestStatus3;
            th = e3;
            valueOf = requestStatus;
            Log.e(a, "Error parsing userid output", th);
            requestStatus = valueOf;
            userData = null;
            requestStatus2 = requestStatus;
            return new UserDataResponseBuilder().setRequestId(fromString).setRequestStatus(requestStatus2).setUserData(userData).build();
        }
        return new UserDataResponseBuilder().setRequestId(fromString).setRequestStatus(requestStatus2).setUserData(userData).build();
    }

    private void g(Intent intent) {
        a(h(intent));
    }

    private PurchaseResponse h(Intent intent) {
        RequestId fromString;
        UserData build;
        PurchaseResponse.RequestStatus safeValueOf;
        Throwable th;
        Throwable th2;
        Receipt receipt = null;
        PurchaseResponse.RequestStatus requestStatus = PurchaseResponse.RequestStatus.FAILED;
        try {
            JSONObject jSONObject = new JSONObject(intent.getStringExtra("purchaseOutput"));
            fromString = RequestId.fromString(jSONObject.optString("requestId"));
            try {
                String optString = jSONObject.optString("userId");
                build = new UserDataBuilder().setUserId(optString).setMarketplace(jSONObject.optString("marketplace")).build();
                try {
                    safeValueOf = PurchaseResponse.RequestStatus.safeValueOf(jSONObject.optString("purchaseStatus"));
                } catch (Throwable e) {
                    th = e;
                    safeValueOf = requestStatus;
                    th2 = th;
                    Log.e(a, "Error parsing purchase output", th2);
                    return new PurchaseResponseBuilder().setRequestId(fromString).setRequestStatus(safeValueOf).setUserData(build).setReceipt(receipt).build();
                }
            } catch (Throwable e2) {
                build = null;
                PurchaseResponse.RequestStatus requestStatus2 = requestStatus;
                th2 = e2;
                safeValueOf = requestStatus2;
                Log.e(a, "Error parsing purchase output", th2);
                return new PurchaseResponseBuilder().setRequestId(fromString).setRequestStatus(safeValueOf).setUserData(build).setReceipt(receipt).build();
            }
            try {
                JSONObject optJSONObject = jSONObject.optJSONObject("receipt");
                if (optJSONObject != null) {
                    receipt = a(optJSONObject);
                }
            } catch (Exception e3) {
                th2 = e3;
                Log.e(a, "Error parsing purchase output", th2);
                return new PurchaseResponseBuilder().setRequestId(fromString).setRequestStatus(safeValueOf).setUserData(build).setReceipt(receipt).build();
            }
        } catch (Throwable e22) {
            build = null;
            fromString = null;
            th = e22;
            safeValueOf = requestStatus;
            th2 = th;
            Log.e(a, "Error parsing purchase output", th2);
            return new PurchaseResponseBuilder().setRequestId(fromString).setRequestStatus(safeValueOf).setUserData(build).setReceipt(receipt).build();
        }
        return new PurchaseResponseBuilder().setRequestId(fromString).setRequestStatus(safeValueOf).setUserData(build).setReceipt(receipt).build();
    }

    private Receipt a(JSONObject jSONObject) throws ParseException {
        String optString = jSONObject.optString("receiptId");
        String optString2 = jSONObject.optString("sku");
        ProductType valueOf = ProductType.valueOf(jSONObject.optString("itemType"));
        Date parse = b.a.parse(jSONObject.optString("purchaseDate"));
        String optString3 = jSONObject.optString("cancelDate");
        Date parse2 = (optString3 == null || optString3.length() == 0) ? null : b.a.parse(optString3);
        return new ReceiptBuilder().setReceiptId(optString).setSku(optString2).setProductType(valueOf).setPurchaseDate(parse).setCancelDate(parse2).build();
    }
}
