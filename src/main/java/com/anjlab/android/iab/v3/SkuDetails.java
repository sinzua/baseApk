package com.anjlab.android.iab.v3;

import org.json.JSONException;
import org.json.JSONObject;

public class SkuDetails {
    public final String currency;
    public final String description;
    public final boolean isSubscription;
    public final String priceText;
    public final Double priceValue;
    public final String productId;
    public final String title;

    public SkuDetails(String productId, String title, String description, boolean isSubscription, String currency, Double priceValue, String priceText) {
        this.productId = productId;
        this.title = title;
        this.description = description;
        this.isSubscription = isSubscription;
        this.currency = currency;
        this.priceValue = priceValue;
        this.priceText = priceText;
    }

    public SkuDetails(JSONObject source) throws JSONException {
        String responseType = source.optString("type");
        if (responseType == null) {
            responseType = Constants.PRODUCT_TYPE_MANAGED;
        }
        this.productId = source.optString(Constants.RESPONSE_PRODUCT_ID);
        this.title = source.optString("title");
        this.description = source.optString("description");
        this.isSubscription = responseType.equalsIgnoreCase(Constants.PRODUCT_TYPE_SUBSCRIPTION);
        this.currency = source.optString(Constants.RESPONSE_PRICE_CURRENCY);
        this.priceValue = Double.valueOf(source.optDouble(Constants.RESPONSE_PRICE_MICROS) / 1000000.0d);
        this.priceText = source.optString(Constants.RESPONSE_PRICE);
    }

    public String toString() {
        return String.format("%s: %s(%s) %f in %s (%s)", new Object[]{this.productId, this.title, this.description, this.priceValue, this.currency, this.priceText});
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SkuDetails that = (SkuDetails) o;
        if (this.isSubscription != that.isSubscription) {
            return false;
        }
        if (this.productId != null) {
            if (this.productId.equals(that.productId)) {
                return true;
            }
        } else if (that.productId == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.productId != null) {
            result = this.productId.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.isSubscription) {
            i = 1;
        }
        return i2 + i;
    }
}
