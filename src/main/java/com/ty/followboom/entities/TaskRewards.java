package com.ty.followboom.entities;

public class TaskRewards {
    private int rewardCoin;
    private String rewardName;
    private int rewardTypeId;

    public int getRewardTypeId() {
        return this.rewardTypeId;
    }

    public void setRewardTypeId(int rewardTypeId) {
        this.rewardTypeId = rewardTypeId;
    }

    public String getRewardName() {
        return this.rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public int getRewardCoin() {
        return this.rewardCoin;
    }

    public void setRewardCoin(int rewardCoin) {
        this.rewardCoin = rewardCoin;
    }
}
