package com.supersonicads.sdk;

import android.app.Activity;
import com.supersonicads.sdk.agent.SupersonicAdsAdvertiserAgent;
import com.supersonicads.sdk.agent.SupersonicAdsPublisherAgent;
import com.supersonicads.sdk.data.SSAEnums.DebugMode;

public class SSAFactory {
    public static SSAPublisher getPublisherInstance(Activity activity) {
        return SupersonicAdsPublisherAgent.getInstance(activity);
    }

    public static SSAPublisher getPublisherTestInstance(Activity activity) {
        return SupersonicAdsPublisherAgent.getInstance(activity, DebugMode.MODE_2.getValue());
    }

    public static SSAPublisher getPublisherTestInstance(Activity activity, int debugMode) {
        return SupersonicAdsPublisherAgent.getInstance(activity, debugMode);
    }

    public static SSAAdvertiser getAdvertiserInstance() {
        return SupersonicAdsAdvertiserAgent.getInstance();
    }

    public static SSAAdvertiserTest getAdvertiserTestInstance() {
        return SupersonicAdsAdvertiserAgent.getInstance();
    }
}
