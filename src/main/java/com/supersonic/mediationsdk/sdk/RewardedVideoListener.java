package com.supersonic.mediationsdk.sdk;

import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.model.Placement;

public interface RewardedVideoListener {
    void onRewardedVideoAdClosed();

    void onRewardedVideoAdOpened();

    void onRewardedVideoAdRewarded(Placement placement);

    void onRewardedVideoInitFail(SupersonicError supersonicError);

    void onRewardedVideoInitSuccess();

    void onRewardedVideoShowFail(SupersonicError supersonicError);

    void onVideoAvailabilityChanged(boolean z);

    void onVideoEnd();

    void onVideoStart();
}
