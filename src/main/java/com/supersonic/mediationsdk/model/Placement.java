package com.supersonic.mediationsdk.model;

public class Placement {
    private int mId;
    private String mPlacementName;
    private int mRewardAmount;
    private String mRewardName;

    public Placement(int id, String placementName, String rewardName, int rewardAmount) {
        this.mId = id;
        this.mPlacementName = placementName;
        this.mRewardName = rewardName;
        this.mRewardAmount = rewardAmount;
    }

    public int getId() {
        return this.mId;
    }

    public String getPlacementName() {
        return this.mPlacementName;
    }

    public String getRewardName() {
        return this.mRewardName;
    }

    public int getRewardAmount() {
        return this.mRewardAmount;
    }

    public String toString() {
        return "placement name: " + this.mPlacementName + ", reward name: " + this.mRewardName + " , amount:" + this.mRewardAmount;
    }
}
