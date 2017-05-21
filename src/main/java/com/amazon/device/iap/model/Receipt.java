package com.amazon.device.iap.model;

import com.amazon.device.iap.internal.model.ReceiptBuilder;
import com.amazon.device.iap.internal.util.d;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public final class Receipt {
    private static final String CANCEL_DATE = "endDate";
    private static final Date DATE_CANCELED = new Date(1);
    private static final String PRODUCT_TYPE = "itemType";
    private static final String PURCHASE_DATE = "purchaseDate";
    private static final String RECEIPT_ID = "receiptId";
    private static final String SKU = "sku";
    private final Date cancelDate;
    private final ProductType productType;
    private final Date purchaseDate;
    private final String receiptId;
    private final String sku;

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.receiptId == null ? 0 : this.receiptId.hashCode()) + (((this.purchaseDate == null ? 0 : this.purchaseDate.hashCode()) + (((this.productType == null ? 0 : this.productType.hashCode()) + (((this.cancelDate == null ? 0 : this.cancelDate.hashCode()) + 31) * 31)) * 31)) * 31)) * 31;
        if (this.sku != null) {
            i = this.sku.hashCode();
        }
        return hashCode + i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Receipt receipt = (Receipt) obj;
        if (this.cancelDate == null) {
            if (receipt.cancelDate != null) {
                return false;
            }
        } else if (!this.cancelDate.equals(receipt.cancelDate)) {
            return false;
        }
        if (this.productType != receipt.productType) {
            return false;
        }
        if (this.purchaseDate == null) {
            if (receipt.purchaseDate != null) {
                return false;
            }
        } else if (!this.purchaseDate.equals(receipt.purchaseDate)) {
            return false;
        }
        if (this.receiptId == null) {
            if (receipt.receiptId != null) {
                return false;
            }
        } else if (!this.receiptId.equals(receipt.receiptId)) {
            return false;
        }
        if (this.sku == null) {
            if (receipt.sku != null) {
                return false;
            }
            return true;
        } else if (this.sku.equals(receipt.sku)) {
            return true;
        } else {
            return false;
        }
    }

    public Receipt(ReceiptBuilder receiptBuilder) {
        d.a(receiptBuilder.getSku(), SKU);
        d.a(receiptBuilder.getProductType(), ParametersKeys.PRODUCT_TYPE);
        if (ProductType.SUBSCRIPTION == receiptBuilder.getProductType()) {
            d.a(receiptBuilder.getPurchaseDate(), PURCHASE_DATE);
        }
        this.receiptId = receiptBuilder.getReceiptId();
        this.sku = receiptBuilder.getSku();
        this.productType = receiptBuilder.getProductType();
        this.purchaseDate = receiptBuilder.getPurchaseDate();
        this.cancelDate = receiptBuilder.getCancelDate();
    }

    public String getReceiptId() {
        return this.receiptId;
    }

    public String getSku() {
        return this.sku;
    }

    public ProductType getProductType() {
        return this.productType;
    }

    public Date getPurchaseDate() {
        return this.purchaseDate;
    }

    public Date getCancelDate() {
        return this.cancelDate;
    }

    public JSONObject toJSON() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(RECEIPT_ID, this.receiptId);
            jSONObject.put(SKU, this.sku);
            jSONObject.put(PRODUCT_TYPE, this.productType);
            jSONObject.put(PURCHASE_DATE, this.purchaseDate);
            jSONObject.put(CANCEL_DATE, this.cancelDate);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public String toString() {
        String str = null;
        try {
            str = toJSON().toString(4);
        } catch (JSONException e) {
        }
        return str;
    }

    public boolean isCanceled() {
        return this.cancelDate != null;
    }
}
