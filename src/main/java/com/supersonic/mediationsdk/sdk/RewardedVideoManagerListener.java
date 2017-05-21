package com.supersonic.mediationsdk.sdk;

import com.supersonic.mediationsdk.AbstractAdapter;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.model.Placement;

public interface RewardedVideoManagerListener {
    void onRewardedVideoAdClosed(AbstractAdapter abstractAdapter);

    void onRewardedVideoAdOpened(AbstractAdapter abstractAdapter);

    void onRewardedVideoAdRewarded(Placement placement, AbstractAdapter abstractAdapter);

    void onRewardedVideoInitFail(SupersonicError supersonicError, AbstractAdapter abstractAdapter);

    void onRewardedVideoInitSuccess(AbstractAdapter abstractAdapter);

    void onRewardedVideoShowFail(SupersonicError supersonicError, AbstractAdapter abstractAdapter);

    void onVideoAvailabilityChanged(boolean z, AbstractAdapter abstractAdapter);

    void onVideoEnd(AbstractAdapter abstractAdapter);

    void onVideoStart(AbstractAdapter abstractAdapter);
}
