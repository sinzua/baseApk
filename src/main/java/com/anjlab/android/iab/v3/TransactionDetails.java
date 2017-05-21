package com.anjlab.android.iab.v3;

import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public class TransactionDetails {
    public final String orderId;
    public final String productId;
    public final PurchaseInfo purchaseInfo;
    public final Date purchaseTime;
    public final String purchaseToken;

    public TransactionDetails(PurchaseInfo info) throws JSONException {
        JSONObject source = new JSONObject(info.responseData);
        this.purchaseInfo = info;
        this.productId = source.getString(Constants.RESPONSE_PRODUCT_ID);
        this.orderId = source.optString(Constants.RESPONSE_ORDER_ID);
        this.purchaseToken = source.getString(Constants.RESPONSE_PURCHASE_TOKEN);
        this.purchaseTime = new Date(source.getLong(Constants.RESPONSE_PURCHASE_TIME));
    }

    public String toString() {
        return String.format("%s purchased at %s(%s). Token: %s, Signature: %s", new Object[]{this.productId, this.purchaseTime, this.orderId, this.purchaseToken, this.purchaseInfo.signature});
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransactionDetails details = (TransactionDetails) o;
        if (this.orderId != null) {
            if (this.orderId.equals(details.orderId)) {
                return true;
            }
        } else if (details.orderId == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.orderId != null ? this.orderId.hashCode() : 0;
    }
}
