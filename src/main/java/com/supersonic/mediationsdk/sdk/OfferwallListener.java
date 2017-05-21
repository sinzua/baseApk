package com.supersonic.mediationsdk.sdk;

import com.supersonic.mediationsdk.logger.SupersonicError;

public interface OfferwallListener {
    void onGetOfferwallCreditsFail(SupersonicError supersonicError);

    boolean onOfferwallAdCredited(int i, int i2, boolean z);

    void onOfferwallClosed();

    void onOfferwallInitFail(SupersonicError supersonicError);

    void onOfferwallInitSuccess();

    void onOfferwallOpened();

    void onOfferwallShowFail(SupersonicError supersonicError);
}
