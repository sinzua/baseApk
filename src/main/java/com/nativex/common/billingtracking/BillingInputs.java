package com.nativex.common.billingtracking;

public class BillingInputs {
    private float costPerItem;
    private String currencyLocale;
    private String productTitle;
    private int quantity;
    private String storeProductId;
    private String storeTransactionId;
    private String storeTransactionTimeUTC;

    public String getStoreProductId() {
        return this.storeProductId;
    }

    public void setStoreProductId(String storeProductId) {
        this.storeProductId = storeProductId;
    }

    public String getStoreTransactionId() {
        return this.storeTransactionId;
    }

    public void setStoreTransactionId(String storeTransactionId) {
        this.storeTransactionId = storeTransactionId;
    }

    public String getStoreTransactionTimeUTC() {
        return this.storeTransactionTimeUTC;
    }

    public void setStoreTransactionTimeUTC(String storeTransactionTimeUTC) {
        this.storeTransactionTimeUTC = storeTransactionTimeUTC;
    }

    public float getCostPerItem() {
        return this.costPerItem;
    }

    public void setCostPerItem(float costPerItem) {
        this.costPerItem = costPerItem;
    }

    public String getCurrencyLocale() {
        return this.currencyLocale;
    }

    public void setCurrencyLocale(String currencyLocale) {
        this.currencyLocale = currencyLocale;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductTitle() {
        return this.productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
}
