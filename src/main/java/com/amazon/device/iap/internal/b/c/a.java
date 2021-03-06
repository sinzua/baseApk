package com.amazon.device.iap.internal.b.c;

import android.os.RemoteException;
import com.amazon.android.framework.exception.KiwiException;
import com.amazon.device.iap.internal.b.e;
import com.amazon.device.iap.internal.model.ProductBuilder;
import com.amazon.device.iap.internal.model.ProductDataResponseBuilder;
import com.amazon.device.iap.internal.util.MetricsHelper;
import com.amazon.device.iap.internal.util.d;
import com.amazon.device.iap.model.Product;
import com.amazon.device.iap.model.ProductDataResponse.RequestStatus;
import com.amazon.device.iap.model.ProductType;
import com.amazon.venezia.command.SuccessResult;
import com.anjlab.android.iab.v3.Constants;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: GetItemDataCommandV2 */
public final class a extends c {
    private static final String b = a.class.getSimpleName();

    public a(e eVar, Set<String> set) {
        super(eVar, "2.0", set);
    }

    protected boolean a(SuccessResult successResult) throws RemoteException, KiwiException {
        Map data = successResult.getData();
        com.amazon.device.iap.internal.util.e.a(b, "data: " + data);
        Set linkedHashSet = new LinkedHashSet();
        Map hashMap = new HashMap();
        for (String str : this.a) {
            if (data.containsKey(str)) {
                try {
                    hashMap.put(str, a(str, data));
                } catch (IllegalArgumentException e) {
                    IllegalArgumentException illegalArgumentException = e;
                    linkedHashSet.add(str);
                    MetricsHelper.submitJsonParsingExceptionMetrics(c(), (String) data.get(str), b + ".onResult()");
                    com.amazon.device.iap.internal.util.e.b(b, "Error parsing JSON for SKU " + str + ": " + illegalArgumentException.getMessage());
                }
            } else {
                linkedHashSet.add(str);
            }
        }
        e b = b();
        b.d().a(new ProductDataResponseBuilder().setRequestId(b.c()).setRequestStatus(RequestStatus.SUCCESSFUL).setUnavailableSkus(linkedHashSet).setProductData(hashMap).build());
        return true;
    }

    private Product a(String str, Map map) throws IllegalArgumentException {
        String str2 = (String) map.get(str);
        try {
            JSONObject jSONObject = new JSONObject(str2);
            ProductType valueOf = ProductType.valueOf(jSONObject.getString("itemType").toUpperCase());
            String string = jSONObject.getString("description");
            String optString = jSONObject.optString(Constants.RESPONSE_PRICE, null);
            if (d.a(optString)) {
                JSONObject optJSONObject = jSONObject.optJSONObject("priceJson");
                if (optJSONObject != null) {
                    Currency instance = Currency.getInstance(optJSONObject.getString("currency"));
                    optString = instance.getSymbol() + new BigDecimal(optJSONObject.getString(ParametersKeys.VALUE));
                }
            }
            return new ProductBuilder().setSku(str).setProductType(valueOf).setDescription(string).setPrice(optString).setSmallIconUrl(jSONObject.getString("iconUrl")).setTitle(jSONObject.getString("title")).build();
        } catch (JSONException e) {
            throw new IllegalArgumentException("error in parsing json string" + str2);
        }
    }
}
