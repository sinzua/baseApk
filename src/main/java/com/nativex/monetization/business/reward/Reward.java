package com.nativex.monetization.business.reward;

import com.google.gson.annotations.SerializedName;

public class Reward {
    @SerializedName("Amount")
    private double amount = 0.0d;
    @SerializedName("PayoutId")
    private String payoutId = null;
    @SerializedName("ExternalCurrencyId")
    private String rewardId = null;
    @SerializedName("DisplayName")
    private String rewardName = null;

    public Reward(Reward r) {
        this.amount = r.amount;
        this.rewardName = r.rewardName;
        this.rewardId = r.rewardId;
        this.payoutId = r.payoutId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getRewardName() {
        return this.rewardName;
    }

    public String getRewardId() {
        return this.rewardId;
    }

    public String getPayoutId() {
        return this.payoutId;
    }
}
