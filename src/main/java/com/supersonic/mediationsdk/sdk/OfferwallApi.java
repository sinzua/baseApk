package com.supersonic.mediationsdk.sdk;

import android.app.Activity;

public interface OfferwallApi extends BaseApi {
    void getOfferwallCredits();

    void initOfferwall(Activity activity, String str, String str2);

    boolean isOfferwallAvailable();

    void setOfferwallListener(OfferwallListener offerwallListener);

    void showOfferwall();
}
