package com.nativex.monetization;

import android.graphics.drawable.Drawable;

class DialogInputs {
    private int currencyAmount;
    private String currencyName;
    private Drawable icon;

    DialogInputs() {
    }

    public String getCurrencyName() {
        return this.currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getCurrencyAmount() {
        return this.currencyAmount;
    }

    public void setCurrencyAmount(int currencyAmount) {
        this.currencyAmount = currencyAmount;
    }
}
