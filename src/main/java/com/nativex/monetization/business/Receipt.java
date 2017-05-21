package com.nativex.monetization.business;

import com.google.gson.annotations.SerializedName;

public class Receipt {
    @SerializedName("Amount")
    private double amount;
    @SerializedName("DisplayName")
    private String displayName;
    @SerializedName("ExternalCurrencyId")
    private String externalCurrencyId;
    @SerializedName("Id")
    private Long id;
    @SerializedName("PayoutId")
    private String payoutId;
    @SerializedName("ReceiptId")
    private String receiptId;

    public String getPayoutId() {
        return this.payoutId;
    }

    public String getReceiptId() {
        return this.receiptId;
    }
}
