package com.supersonic.mediationsdk.sdk;

import android.content.Context;
import com.supersonic.mediationsdk.logger.LoggingApi;
import com.supersonic.mediationsdk.model.Placement;

public interface Supersonic extends RewardedVideoApi, InterstitialApi, OfferwallApi, LoggingApi {
    String getAdvertiserId(Context context);

    Placement getPlacementInfo(String str);

    void removeInterstitialListener();

    void removeOfferwallListener();

    void removeRewardedVideoListener();

    void shouldTrackNetworkState(boolean z);
}
