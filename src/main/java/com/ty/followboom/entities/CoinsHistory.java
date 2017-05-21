package com.ty.followboom.entities;

public class CoinsHistory {
    private String changeAmount;
    private String changeReason;
    private String createtime;

    public String getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getChangeAmount() {
        return this.changeAmount;
    }

    public void setChangeAmount(String changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getChangeReason() {
        return this.changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }
}
