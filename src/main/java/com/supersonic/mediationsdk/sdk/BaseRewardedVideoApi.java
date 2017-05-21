package com.supersonic.mediationsdk.sdk;

import android.app.Activity;

public interface BaseRewardedVideoApi extends BaseApi {
    void initRewardedVideo(Activity activity, String str, String str2);

    boolean isRewardedVideoAvailable();

    void showRewardedVideo();

    void showRewardedVideo(String str);
}
