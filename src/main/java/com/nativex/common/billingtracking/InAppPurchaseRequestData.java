package com.nativex.common.billingtracking;

import com.google.gson.annotations.SerializedName;

public class InAppPurchaseRequestData {
    @SerializedName("AdvertiserSessionId")
    private String advertiserSession;
    @SerializedName("CostPerItem")
    private Float costPerItem;
    @SerializedName("CurrencyLocale")
    private String currencyLocale;
    @SerializedName("ProductTitle")
    private String productTitle;
    @SerializedName("PublisherSessionId")
    private String publisherSession;
    @SerializedName("Quantity")
    private Integer quantity;
    @SerializedName("StoreProductId")
    private String storeProductId;
    @SerializedName("StoreTransactionId")
    private String storeTransactionId;
    @SerializedName("StoreTransactionTimeUtc")
    private String storeTransactionTime;

    public void setPublisherSession(String publisherSession) {
        this.publisherSession = publisherSession;
    }

    public void setCostPerItem(float costPerItem) {
        this.costPerItem = Float.valueOf(costPerItem);
    }

    public void setQuantity(int quantity) {
        this.quantity = Integer.valueOf(quantity);
    }

    public void setStoreProductId(String storeProductId) {
        this.storeProductId = storeProductId;
    }

    public void setStoreTransactionId(String storeTransactionId) {
        this.storeTransactionId = storeTransactionId;
    }

    public void setStoreTransactionTime(String storeTransactionTime) {
        this.storeTransactionTime = storeTransactionTime;
    }

    public void setCurrencyLocale(String currencyLocale) {
        this.currencyLocale = currencyLocale;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
}
