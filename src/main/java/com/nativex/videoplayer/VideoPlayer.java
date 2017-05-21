package com.nativex.videoplayer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import org.codehaus.jackson.util.BufferRecycler;

public interface VideoPlayer {

    public static final class Options {
        public boolean allowMute = false;
        public int allowSkipAfterMilliseconds = -1;
        public int allowSkipAfterVideoStuckForMilliseconds = BufferRecycler.DEFAULT_WRITE_CONCAT_BUFFER_LEN;
        public int controlIconMaxDimensionInDensityIndependentPixels = 30;
        public int controlsAlpha = 255;
        public int controlsDistanceFromScreenEdgeInDensityIndependentPixels = 10;
        public int countdownAfterMilliseconds = -1;
        public String countdownMessageFormat = "Video ends in %d seconds";
        public int countdownMessageTextColor = -1;
        public String errorMessageToast = "Sorry, something went wrong";
        public Drawable mutedButtonIcon;
        public Drawable notMutedButtonIcon;
        public int orientation = -1;
        public Drawable skipButtonIcon;
        public String specialSkipCountdownMessageFormat = "Skip in %d seconds";
        public boolean startMuted = false;
    }

    void cancel();

    boolean play(String str, Options options, Context context, VideoListener videoListener, boolean z);

    boolean prepare(String str, boolean z);
}
