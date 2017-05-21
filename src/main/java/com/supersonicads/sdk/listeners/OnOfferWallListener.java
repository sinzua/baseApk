package com.supersonicads.sdk.listeners;

public interface OnOfferWallListener {
    void onGetOWCreditsFailed(String str);

    void onOWAdClosed();

    boolean onOWAdCredited(int i, int i2, boolean z);

    void onOWGeneric(String str, String str2);

    void onOWShowFail(String str);

    void onOWShowSuccess();

    void onOfferwallInitFail(String str);

    void onOfferwallInitSuccess();
}
