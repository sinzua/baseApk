package com.nativex.monetization.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import com.nativex.common.Log;
import com.nativex.monetization.activities.InterstitialActivity;
import com.nativex.monetization.mraid.MRAIDContainer;
import com.nativex.monetization.mraid.MRAIDLogger;
import com.nativex.monetization.mraid.MRAIDManager;
import com.nativex.videoplayer.NativeXVideoPlayer;

public class ActivityManager {
    private static int _systemUIVisibility = 0;

    private ActivityManager() {
    }

    public static void setSystemUIVisibility(int vis) {
        _systemUIVisibility = vis;
    }

    public static void startMRAIDVideo(Context context, String url, MRAIDContainer container) {
        try {
            MRAIDManager.getNativeVideoPlayerListener().setVideoResource(url);
            MRAIDManager.getNativeVideoPlayerListener().setMRAIDContainerName(container.getContainerName());
            if (VERSION.SDK_INT >= 19) {
                NativeXVideoPlayer.getInstance().setSystemUiVisibility(_systemUIVisibility);
            }
            if (!NativeXVideoPlayer.getInstance().play(url, container.getVideoOptions(), context, MRAIDManager.getNativeVideoPlayerListener(), Log.isLogging())) {
                correctiveActionForPlayerFailure(container);
            }
        } catch (Exception e) {
            MRAIDLogger.e("VideoPlayer error: Failed to start", e);
            correctiveActionForPlayerFailure(container);
        }
    }

    private static void correctiveActionForPlayerFailure(MRAIDContainer container) {
        if (container.isVideoAutoPlay()) {
            MRAIDManager.stopAlphaAnimationAndDismissAd(container.getContainerName());
        } else {
            container.fireVideoCancelledEvent();
        }
    }

    public static void startMRAIDInterstitial(Context context, String name, boolean userCall) {
        Intent intent = new Intent(context, InterstitialActivity.class);
        intent.putExtra(InterstitialActivity.INTENT_EXTRA_AD_NAME, name);
        intent.putExtra(InterstitialActivity.INTENT_EXTRA_USER_CALL, userCall);
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        if (VERSION.SDK_INT >= 19) {
            intent.putExtra(InterstitialActivity.INTENT_EXTRA_SYSTEM_UI_VISIBILITY, _systemUIVisibility);
        }
        context.startActivity(intent);
    }
}
