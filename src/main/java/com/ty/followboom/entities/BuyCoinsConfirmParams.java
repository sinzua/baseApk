package com.ty.followboom.entities;

public class BuyCoinsConfirmParams {
    private String amazonUserId;
    private String androidSignature;
    private String androidSignedData;
    private String originalReceipt;
    private String packageName;
    private String productId;
    private String token;

    public BuyCoinsConfirmParams(String packageName, String productId, String token, String androidSignedData, String androidSignature) {
        this.packageName = packageName;
        this.productId = productId;
        this.token = token;
        this.androidSignedData = androidSignedData;
        this.androidSignature = androidSignature;
    }

    public BuyCoinsConfirmParams(String packageName, String productId, String originalReceipt, String amazonUserId) {
        this.packageName = packageName;
        this.productId = productId;
        this.originalReceipt = originalReceipt;
        this.amazonUserId = amazonUserId;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAndroidSignedData() {
        return this.androidSignedData;
    }

    public void setAndroidSignedData(String androidSignedData) {
        this.androidSignedData = androidSignedData;
    }

    public String getAndroidSignature() {
        return this.androidSignature;
    }

    public void setAndroidSignature(String androidSignature) {
        this.androidSignature = androidSignature;
    }

    public String getOriginalReceipt() {
        return this.originalReceipt;
    }

    public void setOriginalReceipt(String originalReceipt) {
        this.originalReceipt = originalReceipt;
    }

    public String getAmazonUserId() {
        return this.amazonUserId;
    }

    public void setAmazonUserId(String amazonUserId) {
        this.amazonUserId = amazonUserId;
    }
}
