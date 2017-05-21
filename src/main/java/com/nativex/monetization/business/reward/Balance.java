package com.nativex.monetization.business.reward;

import com.google.gson.annotations.SerializedName;

@Deprecated
public class Balance {
    @SerializedName("Amount")
    private String amount = null;
    @SerializedName("DisplayName")
    private String displayName = null;
    @SerializedName("ExternalCurrencyId")
    private String externalCurrencyId = null;
    @SerializedName("PayoutId")
    private String payoutId = null;

    public Balance(Reward r) {
        this.displayName = r.getRewardName();
        this.externalCurrencyId = r.getRewardId();
        this.amount = Double.toString(r.getAmount());
        this.payoutId = r.getPayoutId();
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getExternalCurrencyId() {
        return this.externalCurrencyId;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getPayoutId() {
        return this.payoutId;
    }
}
