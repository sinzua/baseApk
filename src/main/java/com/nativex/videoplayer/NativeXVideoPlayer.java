package com.nativex.videoplayer;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.webkit.URLUtil;
import android.widget.Toast;
import com.nativex.network.volley.DefaultRetryPolicy;
import com.nativex.videoplayer.VideoPlayer.Options;
import com.supersonicads.sdk.utils.Constants.ControllerParameters;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.IllegalFormatException;

public final class NativeXVideoPlayer implements VideoPlayer {
    private static final String LOGGING_TAG = "NativeXVideoPlayer";
    private static NativeXVideoPlayer sInstance;
    private int _systemUiVisibility = 0;
    private Context mContext;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mIsLoggingEnabled = false;
    private boolean mIsPlayInProgress = false;
    private volatile boolean mIsSkipRequestedButNotAvailable = false;
    private volatile boolean mIsVideoBeingDisplayed = false;
    private final Object mLock = new Object();
    private MediaPlayer mMediaPlayer;
    private final MediaPlayerListener mMediaPlayerListener = new MediaPlayerListener();
    private Options mOptions;
    private VideoPreparationState mPreparationState = VideoPreparationState.NONE;
    private VideoDisplayer mVideoDisplayer;
    private int mVideoDurationMilliseconds = -1;
    private VideoListener mVideoListener;
    private String mVideoUri;
    private float mVideoWidthHeightProportion = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;

    private class MediaPlayerListener implements OnPreparedListener, OnCompletionListener, OnErrorListener {
        private MediaPlayerListener() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onPrepared(android.media.MediaPlayer r5) {
            /*
            r4 = this;
            r0 = com.nativex.videoplayer.NativeXVideoPlayer.this;
            r1 = "listener.onPrepared called";
            r0.log(r1);
            r0 = com.nativex.videoplayer.NativeXVideoPlayer.this;
            r1 = r0.mLock;
            monitor-enter(r1);
            r0 = com.nativex.videoplayer.NativeXVideoPlayer.this;	 Catch:{ all -> 0x0093 }
            r0 = r0.mMediaPlayer;	 Catch:{ all -> 0x0093 }
            if (r0 == 0) goto L_0x0028;
        L_0x0016:
            r0 = com.nativex.videoplayer.NativeXVideoPlayer.this;	 Catch:{ all -> 0x0093 }
            r0 = r0.mMediaPlayer;	 Catch:{ all -> 0x0093 }
            if (r5 != r0) goto L_0x0028;
        L_0x001e:
            r0 = com.nativex.videoplayer.NativeXVideoPlayer.this;	 Catch:{ all -> 0x0093 }
            r0 = r0.mPreparationState;	 Catch:{ all -> 0x0093 }
            r2 = com.nativex.videoplayer.NativeXVideoPlayer.VideoPreparationState.PREPARING;	 Catch:{ all -> 0x0093 }
            if (r0 == r2) goto L_0x0031;
        L_0x0028:
            r0 = com.nativex.videoplayer.NativeXVideoPlayer.this;	 Catch:{ all -> 0x0093 }
            r2 = "mediaPlayer null, or not the same media player, or not preparing.. exiting..";
            r0.log(r2);	 Catch:{ all -> 0x0093 }
            monitor-exit(r1);	 Catch:{ all -> 0x0093 }
        L_0x0030:
            return;
        L_0x0031:
            r0 = com.nativex.videoplayer.NativeXVideoPlayer.this;	 Catch:{ all -> 0x0093 }
            r2 = com.nativex.videoplayer.NativeXVideoPlayer.VideoPreparationState.PREPARED;	 Catch:{ all -> 0x0093 }
            r0.mPreparationState = r2;	 Catch:{ all -> 0x0093 }
            r0 = com.nativex.videoplayer.NativeXVideoPlayer.this;	 Catch:{ all -> 0x0093 }
            r2 = com.nativex.videoplayer.NativeXVideoPlayer.this;	 Catch:{ all -> 0x0093 }
            r2 = r2.mMediaPlayer;	 Catch:{ all -> 0x0093 }
            r2 = r2.getVideoWidth();	 Catch:{ all -> 0x0093 }
            r2 = (float) r2;	 Catch:{ all -> 0x0093 }
            r3 = com.nativex.videoplayer.NativeXVideoPlayer.this;	 Catch:{ all -> 0x0093 }
            r3 = r3.mMediaPlayer;	 Catch:{ all -> 0x0093 }
            r3 = r3.getVideoHeight();	 Catch:{ all -> 0x0093 }
            r3 = (float) r3;	 Catch:{ all -> 0x0093 }
            r2 = r2 / r3;
            r0.mVideoWidthHeightProportion = r2;	 Catch:{ all -> 0x0093 }
            r0 = com.nativex.videoplayer.NativeXVideoPlayer.this;	 Catch:{ all -> 0x0093 }
            r2 = com.nativex.videoplayer.NativeXVideoPlayer.this;	 Catch:{ all -> 0x0093 }
            r2 = r2.mMediaPlayer;	 Catch:{ all -> 0x0093 }
            r2 = r2.getDuration();	 Catch:{ all -> 0x0093 }
            r0.mVideoDurationMilliseconds = r2;	 Catch:{ all -> 0x0093 }
            r0 = com.nativex.videoplayer.NativeXVideoPlayer.this;	 Catch:{ all -> 0x0093 }
            r0 = r0.mIsVideoBeingDisplayed;	 Catch:{ all -> 0x0093 }
            if (r0 == 0) goto L_0x008a;
        L_0x006b:
            r0 = com.nativex.videoplayer.NativeXVideoPlayer.this;	 Catch:{ all -> 0x0093 }
            r2 = "video is being displayed; initializing and starting video...";
            r0.log(r2);	 Catch:{ all -> 0x0093 }
            r0 = com.nativex.videoplayer.NativeXVideoPlayer.this;	 Catch:{ all -> 0x0093 }
            r0 = r0.mVideoDisplayer;	 Catch:{ all -> 0x0093 }
            r2 = com.nativex.videoplayer.NativeXVideoPlayer.this;	 Catch:{ all -> 0x0093 }
            r2 = r2.mVideoWidthHeightProportion;	 Catch:{ all -> 0x0093 }
            r0.initializeVideo(r2);	 Catch:{ all -> 0x0093 }
            r0 = com.nativex.videoplayer.NativeXVideoPlayer.this;	 Catch:{ all -> 0x0093 }
            r0 = r0.mMediaPlayer;	 Catch:{ all -> 0x0093 }
            r0.start();	 Catch:{ all -> 0x0093 }
        L_0x008a:
            monitor-exit(r1);	 Catch:{ all -> 0x0093 }
            r0 = com.nativex.videoplayer.NativeXVideoPlayer.this;
            r1 = "onPrepared finished";
            r0.log(r1);
            goto L_0x0030;
        L_0x0093:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0093 }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.nativex.videoplayer.NativeXVideoPlayer.MediaPlayerListener.onPrepared(android.media.MediaPlayer):void");
        }

        public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
            synchronized (NativeXVideoPlayer.this.mLock) {
                if (NativeXVideoPlayer.this.mMediaPlayer == null || mediaPlayer != NativeXVideoPlayer.this.mMediaPlayer) {
                } else {
                    NativeXVideoPlayer.this.log(String.format("MediaPlayer %d: %d", new Object[]{Integer.valueOf(what), Integer.valueOf(extra)}));
                    if (NativeXVideoPlayer.this.mIsPlayInProgress) {
                        final VideoListener videoListener = NativeXVideoPlayer.this.mVideoListener;
                        if (videoListener != null) {
                            NativeXVideoPlayer.this.mHandler.post(new Runnable() {
                                public void run() {
                                    try {
                                        videoListener.onClosedEarly();
                                    } catch (Exception e) {
                                        NativeXVideoPlayer.this.log(e);
                                    }
                                }
                            });
                        }
                        if (NativeXVideoPlayer.this.mOptions.errorMessageToast != null && NativeXVideoPlayer.this.mOptions.errorMessageToast.length() > 0) {
                            Toast.makeText(NativeXVideoPlayer.this.mContext, NativeXVideoPlayer.this.mOptions.errorMessageToast, 0).show();
                        }
                    }
                    NativeXVideoPlayer.this.resetToIdle();
                }
            }
            return true;
        }

        public void onCompletion(MediaPlayer mediaPlayer) {
            NativeXVideoPlayer.this.log("onCompletion called");
            synchronized (NativeXVideoPlayer.this.mLock) {
                if (NativeXVideoPlayer.this.mMediaPlayer == null || mediaPlayer != NativeXVideoPlayer.this.mMediaPlayer) {
                    NativeXVideoPlayer.this.log("mediaPlayer null, and mediaPlayer is not current instance");
                    return;
                }
                final VideoListener videoListener = NativeXVideoPlayer.this.mVideoListener;
                if (videoListener != null) {
                    NativeXVideoPlayer.this.mHandler.post(new Runnable() {
                        public void run() {
                            try {
                                videoListener.onCompleted();
                            } catch (Exception e) {
                                NativeXVideoPlayer.this.log(e);
                            }
                        }
                    });
                }
                NativeXVideoPlayer.this.resetToIdle();
                NativeXVideoPlayer.this.log("onCompletion finished");
            }
        }
    }

    private enum VideoPreparationState {
        NONE,
        PREPARING,
        PREPARED
    }

    private class VideoProgressListener implements Runnable {
        private static final int SLEEP_INTERVAL_MILLISECONDS = 250;
        boolean mForceDisplayOfCountdownTimer = false;
        boolean mIsSkipEnabledDueToBeingStuck = false;
        boolean mIsSkipPointPassed = false;
        boolean mIsVideoProgressStuck = false;
        int mLastMilestoneCompleted;
        final MediaPlayer mMediaPlayerOfProgressListener;
        int mPercentCompleted;
        int mPositionMilliseconds = 0;
        float mPreviousLastMilestoneCompleted = 0.0f;
        int mPreviousPositionMilliseconds = 0;
        int mSecondsRemainingLastDisplayed = -1;
        boolean mShowSkipCountdownTimer = false;
        long mStuckSinceTimeMilliseconds = -1;
        boolean mWasVideoProgressStuck = false;

        public VideoProgressListener(MediaPlayer mediaPlayer) {
            this.mMediaPlayerOfProgressListener = mediaPlayer;
        }

        public void run() {
            try {
                runInternal();
            } catch (Exception e) {
                NativeXVideoPlayer.this.log(e);
            }
        }

        private void runInternal() {
            while (true) {
                try {
                    Thread.sleep(250);
                    synchronized (NativeXVideoPlayer.this.mLock) {
                        if (NativeXVideoPlayer.this.mIsVideoBeingDisplayed && this.mMediaPlayerOfProgressListener == NativeXVideoPlayer.this.mMediaPlayer) {
                            getVideoProgressStatus();
                            detectAndHandleVideoStart();
                            detectAndHandleGettingStuckOrUnstuck();
                            detectAndHandleVideoProgressMilestones();
                            detectAndHandleSkipRequestedButNotAvailable();
                            detectAndHandleSkipPointReached();
                            maintainSkippabilityForGettingStuckOrUnstuck();
                            maintainSkipCountdownTimer();
                            maintainCompletionCountdownTimer();
                        } else {
                            NativeXVideoPlayer.this.log("VideoProgressListener: exiting due to target video no longer being displayed");
                        }
                    }
                    this.mPreviousPositionMilliseconds = this.mPositionMilliseconds;
                    this.mWasVideoProgressStuck = this.mIsVideoProgressStuck;
                    this.mPreviousLastMilestoneCompleted = (float) this.mLastMilestoneCompleted;
                } catch (InterruptedException e) {
                    return;
                }
            }
            NativeXVideoPlayer.this.log("VideoProgressListener: exiting due to target video no longer being displayed");
        }

        private void getVideoProgressStatus() {
            if (NativeXVideoPlayer.this.mMediaPlayer.isPlaying()) {
                this.mPositionMilliseconds = NativeXVideoPlayer.this.mMediaPlayer.getCurrentPosition();
            }
            this.mPercentCompleted = (int) Math.floor((double) ((this.mPositionMilliseconds * 100) / NativeXVideoPlayer.this.mVideoDurationMilliseconds));
        }

        private void detectAndHandleVideoStart() {
            if (this.mPreviousPositionMilliseconds == 0 && this.mPositionMilliseconds > 0) {
                NativeXVideoPlayer.this.log("VideoProgressListener: video started");
                NativeXVideoPlayer.this.mHandler.post(new Runnable() {
                    public void run() {
                        try {
                            synchronized (NativeXVideoPlayer.this.mLock) {
                                if (!(NativeXVideoPlayer.this.mVideoListener == null || NativeXVideoPlayer.this.mMediaPlayer == null || VideoProgressListener.this.mMediaPlayerOfProgressListener != NativeXVideoPlayer.this.mMediaPlayer)) {
                                    NativeXVideoPlayer.this.mVideoListener.onStarted();
                                }
                            }
                        } catch (Exception e) {
                            NativeXVideoPlayer.this.log(e);
                        }
                    }
                });
            }
        }

        private void detectAndHandleGettingStuckOrUnstuck() {
            boolean z = true;
            this.mIsVideoProgressStuck = this.mPositionMilliseconds - this.mPreviousPositionMilliseconds == 0;
            if (this.mStuckSinceTimeMilliseconds == -1) {
                z = false;
            }
            this.mWasVideoProgressStuck = z;
            if (this.mIsVideoProgressStuck != this.mWasVideoProgressStuck) {
                long currentTimeMillis;
                NativeXVideoPlayer.this.log("VideoProgressListener: " + (this.mIsVideoProgressStuck ? "video is not progressing" : "video is now progressing"));
                NativeXVideoPlayer.this.mVideoDisplayer.isStuck(this.mIsVideoProgressStuck);
                if (this.mIsVideoProgressStuck) {
                    currentTimeMillis = System.currentTimeMillis();
                } else {
                    currentTimeMillis = -1;
                }
                this.mStuckSinceTimeMilliseconds = currentTimeMillis;
                this.mIsSkipEnabledDueToBeingStuck = false;
            }
        }

        private void detectAndHandleVideoProgressMilestones() {
            this.mLastMilestoneCompleted = this.mPercentCompleted / 25;
            if (((float) this.mLastMilestoneCompleted) > this.mPreviousLastMilestoneCompleted) {
                switch (this.mLastMilestoneCompleted) {
                    case 1:
                        NativeXVideoPlayer.this.log("VideoProgressListener: 25% completed");
                        NativeXVideoPlayer.this.mHandler.post(new Runnable() {
                            public void run() {
                                try {
                                    synchronized (NativeXVideoPlayer.this.mLock) {
                                        if (!(NativeXVideoPlayer.this.mVideoListener == null || NativeXVideoPlayer.this.mMediaPlayer == null || VideoProgressListener.this.mMediaPlayerOfProgressListener != NativeXVideoPlayer.this.mMediaPlayer)) {
                                            NativeXVideoPlayer.this.mVideoListener.on25PercentCompleted();
                                        }
                                    }
                                } catch (Exception e) {
                                    NativeXVideoPlayer.this.log(e);
                                }
                            }
                        });
                        return;
                    case 2:
                        NativeXVideoPlayer.this.log("VideoProgressListener: 50% completed");
                        NativeXVideoPlayer.this.mHandler.post(new Runnable() {
                            public void run() {
                                try {
                                    synchronized (NativeXVideoPlayer.this.mLock) {
                                        if (!(NativeXVideoPlayer.this.mVideoListener == null || NativeXVideoPlayer.this.mMediaPlayer == null || VideoProgressListener.this.mMediaPlayerOfProgressListener != NativeXVideoPlayer.this.mMediaPlayer)) {
                                            NativeXVideoPlayer.this.mVideoListener.on50PercentCompleted();
                                        }
                                    }
                                } catch (Exception e) {
                                    NativeXVideoPlayer.this.log(e);
                                }
                            }
                        });
                        return;
                    case 3:
                        NativeXVideoPlayer.this.log("VideoProgressListener: 75% completed");
                        NativeXVideoPlayer.this.mHandler.post(new Runnable() {
                            public void run() {
                                try {
                                    synchronized (NativeXVideoPlayer.this.mLock) {
                                        if (!(NativeXVideoPlayer.this.mVideoListener == null || NativeXVideoPlayer.this.mMediaPlayer == null || VideoProgressListener.this.mMediaPlayerOfProgressListener != NativeXVideoPlayer.this.mMediaPlayer)) {
                                            NativeXVideoPlayer.this.mVideoListener.on75PercentCompleted();
                                        }
                                    }
                                } catch (Exception e) {
                                    NativeXVideoPlayer.this.log(e);
                                }
                            }
                        });
                        return;
                    default:
                        return;
                }
            }
        }

        private void detectAndHandleSkipRequestedButNotAvailable() {
            if (NativeXVideoPlayer.this.mIsSkipRequestedButNotAvailable && this.mPositionMilliseconds > 0) {
                NativeXVideoPlayer.this.log("VideoProgressListener: skip requested but not available");
                NativeXVideoPlayer.this.mIsSkipRequestedButNotAvailable = false;
                if (NativeXVideoPlayer.this.mOptions.allowSkipAfterMilliseconds >= 0 && NativeXVideoPlayer.this.mOptions.allowSkipAfterMilliseconds < NativeXVideoPlayer.this.mVideoDurationMilliseconds) {
                    NativeXVideoPlayer.this.log("VideoProgressListener: skip will be possible later - displaying skip countdown timer");
                    this.mShowSkipCountdownTimer = true;
                    NativeXVideoPlayer.this.mVideoDisplayer.displaySecondsRemaining(-1);
                } else if (this.mSecondsRemainingLastDisplayed == -1) {
                    NativeXVideoPlayer.this.log("VideoProgressListener: skip won't be possible later - forcing display of regular countdown timer");
                    this.mForceDisplayOfCountdownTimer = true;
                }
            }
        }

        private void detectAndHandleSkipPointReached() {
            if (NativeXVideoPlayer.this.mOptions.allowSkipAfterMilliseconds >= 0 && !this.mIsSkipPointPassed && this.mPositionMilliseconds >= NativeXVideoPlayer.this.mOptions.allowSkipAfterMilliseconds) {
                NativeXVideoPlayer.this.log("VideoProgressListener: skip point reached");
                if (this.mShowSkipCountdownTimer) {
                    NativeXVideoPlayer.this.log("VideoProgressListener: hiding skip countdown timer");
                    this.mShowSkipCountdownTimer = false;
                    NativeXVideoPlayer.this.mVideoDisplayer.displaySecondsRemainingUntilSkippable(-1);
                }
                NativeXVideoPlayer.this.mVideoDisplayer.enableSkip(true);
                this.mIsSkipPointPassed = true;
            }
        }

        private void maintainSkippabilityForGettingStuckOrUnstuck() {
            if (this.mIsVideoProgressStuck && !this.mIsSkipEnabledDueToBeingStuck && System.currentTimeMillis() > this.mStuckSinceTimeMilliseconds + ((long) NativeXVideoPlayer.this.mOptions.allowSkipAfterVideoStuckForMilliseconds)) {
                NativeXVideoPlayer.this.log("VideoProgressListener: allowing skip due to being stuck for a while");
                NativeXVideoPlayer.this.mVideoDisplayer.enableSkip(true);
                this.mIsSkipEnabledDueToBeingStuck = true;
            } else if (this.mWasVideoProgressStuck && !this.mIsVideoProgressStuck && !this.mIsSkipPointPassed) {
                NativeXVideoPlayer.this.log("VideoProgressListener: removing skip ability since we're unstuck and not past skip point");
                NativeXVideoPlayer.this.mVideoDisplayer.enableSkip(false);
            }
        }

        private void maintainSkipCountdownTimer() {
            if (this.mShowSkipCountdownTimer) {
                int skipSecondsRemaining = ((NativeXVideoPlayer.this.mOptions.allowSkipAfterMilliseconds - this.mPositionMilliseconds) / ControllerParameters.SECOND) + 1;
                if (skipSecondsRemaining >= 0 && skipSecondsRemaining != this.mSecondsRemainingLastDisplayed) {
                    NativeXVideoPlayer.this.log("VideoProgressListener: updating skip countdown timer");
                    NativeXVideoPlayer.this.mVideoDisplayer.displaySecondsRemainingUntilSkippable(skipSecondsRemaining);
                }
            }
        }

        private void maintainCompletionCountdownTimer() {
            if (!this.mShowSkipCountdownTimer) {
                if (this.mForceDisplayOfCountdownTimer || (NativeXVideoPlayer.this.mOptions.countdownAfterMilliseconds >= 0 && this.mPositionMilliseconds > NativeXVideoPlayer.this.mOptions.countdownAfterMilliseconds)) {
                    int secondsRemaining = ((NativeXVideoPlayer.this.mVideoDurationMilliseconds - this.mPositionMilliseconds) / ControllerParameters.SECOND) + 1;
                    if (secondsRemaining >= 0 && secondsRemaining != this.mSecondsRemainingLastDisplayed) {
                        NativeXVideoPlayer.this.mVideoDisplayer.displaySecondsRemaining(secondsRemaining);
                        this.mSecondsRemainingLastDisplayed = secondsRemaining;
                    }
                }
            }
        }
    }

    public static synchronized NativeXVideoPlayer getInstance() {
        NativeXVideoPlayer nativeXVideoPlayer;
        synchronized (NativeXVideoPlayer.class) {
            if (sInstance == null) {
                sInstance = new NativeXVideoPlayer();
            }
            nativeXVideoPlayer = sInstance;
        }
        return nativeXVideoPlayer;
    }

    public static synchronized void release() {
        synchronized (NativeXVideoPlayer.class) {
            if (sInstance != null) {
                sInstance = null;
            }
        }
    }

    public void setSystemUiVisibility(int vis) {
        this._systemUiVisibility = vis;
    }

    public boolean prepare(String videoUri, boolean isLoggingEnabled) {
        Exception e;
        Throwable th;
        log("prepare called with uri: " + videoUri);
        this.mIsLoggingEnabled = isLoggingEnabled;
        if (videoUri == null) {
            throw new IllegalArgumentException("null videoUri");
        }
        Exception exception = null;
        synchronized (this.mLock) {
            if (this.mVideoUri == null || !this.mVideoUri.equals(videoUri)) {
                resetToIdle();
                this.mVideoUri = videoUri;
                log("creating new video player");
                this.mMediaPlayer = new MediaPlayer();
                this.mMediaPlayer.setAudioStreamType(3);
                this.mMediaPlayer.setOnPreparedListener(this.mMediaPlayerListener);
                this.mMediaPlayer.setOnCompletionListener(this.mMediaPlayerListener);
                this.mMediaPlayer.setOnErrorListener(this.mMediaPlayerListener);
                FileInputStream fileInputStream = null;
                try {
                    if (URLUtil.isFileUrl(this.mVideoUri)) {
                        log("creating new file stream from file source: " + this.mVideoUri);
                        FileInputStream fileInputStream2 = new FileInputStream(Uri.parse(this.mVideoUri).getPath());
                        try {
                            this.mMediaPlayer.setDataSource(fileInputStream2.getFD());
                            fileInputStream = fileInputStream2;
                        } catch (IllegalArgumentException e2) {
                            e = e2;
                            fileInputStream = fileInputStream2;
                            exception = e;
                            log("finally block for input stream...");
                            if (fileInputStream != null) {
                                try {
                                    log("closing fileInputStream");
                                    fileInputStream.close();
                                } catch (IOException e3) {
                                    log("Error in closing file input stream.");
                                }
                            }
                            if (exception == null) {
                                log(exception);
                                resetToIdle();
                            } else {
                                log("setting state to preparing");
                                this.mPreparationState = VideoPreparationState.PREPARING;
                                this.mMediaPlayer.prepareAsync();
                            }
                            log("prepare finished");
                            if (exception == null) {
                                return false;
                            }
                            return true;
                        } catch (SecurityException e4) {
                            e = e4;
                            fileInputStream = fileInputStream2;
                            exception = e;
                            log("finally block for input stream...");
                            if (fileInputStream != null) {
                                log("closing fileInputStream");
                                fileInputStream.close();
                            }
                            if (exception == null) {
                                log("setting state to preparing");
                                this.mPreparationState = VideoPreparationState.PREPARING;
                                this.mMediaPlayer.prepareAsync();
                            } else {
                                log(exception);
                                resetToIdle();
                            }
                            log("prepare finished");
                            if (exception == null) {
                                return true;
                            }
                            return false;
                        } catch (IllegalStateException e5) {
                            e = e5;
                            fileInputStream = fileInputStream2;
                            exception = e;
                            log("finally block for input stream...");
                            if (fileInputStream != null) {
                                log("closing fileInputStream");
                                fileInputStream.close();
                            }
                            if (exception == null) {
                                log(exception);
                                resetToIdle();
                            } else {
                                log("setting state to preparing");
                                this.mPreparationState = VideoPreparationState.PREPARING;
                                this.mMediaPlayer.prepareAsync();
                            }
                            log("prepare finished");
                            if (exception == null) {
                                return false;
                            }
                            return true;
                        } catch (IOException e6) {
                            e = e6;
                            fileInputStream = fileInputStream2;
                            exception = e;
                            log("finally block for input stream...");
                            if (fileInputStream != null) {
                                log("closing fileInputStream");
                                fileInputStream.close();
                            }
                            if (exception == null) {
                                log("setting state to preparing");
                                this.mPreparationState = VideoPreparationState.PREPARING;
                                this.mMediaPlayer.prepareAsync();
                            } else {
                                log(exception);
                                resetToIdle();
                            }
                            log("prepare finished");
                            if (exception == null) {
                                return true;
                            }
                            return false;
                        } catch (Throwable th2) {
                            th = th2;
                            fileInputStream = fileInputStream2;
                            log("finally block for input stream...");
                            if (fileInputStream != null) {
                                try {
                                    log("closing fileInputStream");
                                    fileInputStream.close();
                                } catch (IOException e7) {
                                    log("Error in closing file input stream.");
                                }
                            }
                            throw th;
                        }
                    } else if (URLUtil.isHttpUrl(this.mVideoUri) || URLUtil.isHttpsUrl(this.mVideoUri)) {
                        log("setting data source for media player: " + this.mVideoUri);
                        this.mMediaPlayer.setDataSource(this.mVideoUri);
                    } else {
                        throw new IllegalArgumentException("Invalid video URI supplied.");
                    }
                    log("finally block for input stream...");
                    if (fileInputStream != null) {
                        try {
                            log("closing fileInputStream");
                            fileInputStream.close();
                        } catch (IOException e8) {
                            log("Error in closing file input stream.");
                        }
                    }
                } catch (IllegalArgumentException e9) {
                    e = e9;
                    exception = e;
                    log("finally block for input stream...");
                    if (fileInputStream != null) {
                        log("closing fileInputStream");
                        fileInputStream.close();
                    }
                    if (exception == null) {
                        log(exception);
                        resetToIdle();
                    } else {
                        log("setting state to preparing");
                        this.mPreparationState = VideoPreparationState.PREPARING;
                        this.mMediaPlayer.prepareAsync();
                    }
                    log("prepare finished");
                    if (exception == null) {
                        return false;
                    }
                    return true;
                } catch (SecurityException e10) {
                    e = e10;
                    exception = e;
                    log("finally block for input stream...");
                    if (fileInputStream != null) {
                        log("closing fileInputStream");
                        fileInputStream.close();
                    }
                    if (exception == null) {
                        log("setting state to preparing");
                        this.mPreparationState = VideoPreparationState.PREPARING;
                        this.mMediaPlayer.prepareAsync();
                    } else {
                        log(exception);
                        resetToIdle();
                    }
                    log("prepare finished");
                    if (exception == null) {
                        return true;
                    }
                    return false;
                } catch (IllegalStateException e11) {
                    e = e11;
                    exception = e;
                    log("finally block for input stream...");
                    if (fileInputStream != null) {
                        log("closing fileInputStream");
                        fileInputStream.close();
                    }
                    if (exception == null) {
                        log(exception);
                        resetToIdle();
                    } else {
                        log("setting state to preparing");
                        this.mPreparationState = VideoPreparationState.PREPARING;
                        this.mMediaPlayer.prepareAsync();
                    }
                    log("prepare finished");
                    if (exception == null) {
                        return false;
                    }
                    return true;
                } catch (IOException e12) {
                    e = e12;
                    exception = e;
                    log("finally block for input stream...");
                    if (fileInputStream != null) {
                        log("closing fileInputStream");
                        fileInputStream.close();
                    }
                    if (exception == null) {
                        log("setting state to preparing");
                        this.mPreparationState = VideoPreparationState.PREPARING;
                        this.mMediaPlayer.prepareAsync();
                    } else {
                        log(exception);
                        resetToIdle();
                    }
                    log("prepare finished");
                    if (exception == null) {
                        return true;
                    }
                    return false;
                } catch (Throwable th3) {
                    th = th3;
                    log("finally block for input stream...");
                    if (fileInputStream != null) {
                        log("closing fileInputStream");
                        fileInputStream.close();
                    }
                    throw th;
                }
                if (exception == null) {
                    log("setting state to preparing");
                    this.mPreparationState = VideoPreparationState.PREPARING;
                    this.mMediaPlayer.prepareAsync();
                } else {
                    log(exception);
                    resetToIdle();
                }
            } else {
                log("duplicate video uri found, exiting..");
                return true;
            }
        }
    }

    public boolean play(String videoUri, Options options, Context context, VideoListener videoListener, boolean isLoggingEnabled) {
        this.mIsLoggingEnabled = isLoggingEnabled;
        log("playing video: " + videoUri);
        synchronized (this.mLock) {
            if (!(videoUri == null || options == null || context == null || videoListener == null)) {
                if (options.skipButtonIcon != null) {
                    cleanUpOptions(options);
                    if ((this.mPreparationState == VideoPreparationState.NONE || !videoUri.equals(this.mVideoUri)) && !prepare(videoUri, isLoggingEnabled)) {
                        log("prepare failed (in play), exiting!");
                        return false;
                    }
                    this.mOptions = options;
                    this.mContext = context;
                    this.mVideoListener = videoListener;
                    Intent intent = new Intent(this.mContext, VideoActivity.class);
                    if (VERSION.SDK_INT >= 19) {
                        intent.putExtra(VideoActivity.INTENT_EXTRA_SYSTEM_UI_VISIBILITY, this._systemUiVisibility);
                    }
                    intent.setFlags(268435456);
                    try {
                        log("Starting video activity..");
                        this.mContext.startActivity(intent);
                        this.mIsPlayInProgress = true;
                        log("play finished successfully; play in progress..");
                        return true;
                    } catch (ActivityNotFoundException e) {
                        resetToIdle();
                        log("Activity not found exception caught, exiting...");
                        return false;
                    }
                }
            }
            resetToIdle();
            log("video, options, context, videoListener or skip button icon are null; exiting!");
            return false;
        }
    }

    public void cancel() {
        log("cancel called, resetting to idle");
        synchronized (this.mLock) {
            resetToIdle();
        }
        log("cancel call finished");
    }

    Options getOptions() {
        return this.mOptions;
    }

    void setVideoDisplayer(VideoDisplayer videoDisplayer) {
        this.mVideoDisplayer = videoDisplayer;
    }

    void surfaceCreated(SurfaceHolder surfaceHolder, VideoDisplayer videoDisplayer) {
        log("surfaceCreated called");
        synchronized (this.mLock) {
            if (videoDisplayer != this.mVideoDisplayer || this.mPreparationState == VideoPreparationState.NONE) {
                log("videoDisplayer came from a previous activity, or video is not prepared! exiting..");
                return;
            }
            this.mIsVideoBeingDisplayed = true;
            this.mMediaPlayer.setDisplay(surfaceHolder);
            if (this.mPreparationState == VideoPreparationState.PREPARED) {
                log("video prepared, initializing and starting video...");
                this.mVideoDisplayer.initializeVideo(this.mVideoWidthHeightProportion);
                this.mMediaPlayer.start();
            }
            new Thread(new VideoProgressListener(this.mMediaPlayer)).start();
            log("surfaceCreated finished successfully");
        }
    }

    void activityFinishing(VideoDisplayer videoDisplayer) {
        log("activityFinishing called");
        synchronized (this.mLock) {
            if (this.mVideoDisplayer != null && this.mVideoDisplayer == videoDisplayer) {
                log("activity finishing, closing video early");
                this.mVideoListener.onClosedEarly();
                resetToIdle();
            }
        }
        log("activityFinishing completed");
    }

    void mute() {
        if (this.mMediaPlayer != null) {
            log("muting media player...");
            this.mMediaPlayer.setVolume(0.0f, 0.0f);
        }
    }

    void unMute() {
        if (this.mMediaPlayer != null) {
            log("un-muting video player...");
            this.mMediaPlayer.setVolume(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        }
    }

    void skipRequestedButNotAvailable() {
        this.mIsSkipRequestedButNotAvailable = true;
    }

    void log(Exception exception) {
        if (this.mIsLoggingEnabled) {
            Log.e(LOGGING_TAG, "EXCEPTION: " + exception.getLocalizedMessage(), exception);
        }
    }

    private NativeXVideoPlayer() {
    }

    private void resetToIdle() {
        log("resetToIdle called..");
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        if (this.mVideoDisplayer != null) {
            this.mVideoDisplayer.close();
            this.mVideoDisplayer = null;
        }
        this.mPreparationState = VideoPreparationState.NONE;
        this.mIsVideoBeingDisplayed = false;
        this.mIsPlayInProgress = false;
        this.mVideoUri = null;
        this.mOptions = null;
        this.mContext = null;
        this.mVideoListener = null;
        this.mVideoWidthHeightProportion = -1.0f;
        this.mVideoDurationMilliseconds = -1;
        this.mIsSkipRequestedButNotAvailable = false;
    }

    private void log(String message) {
        if (this.mIsLoggingEnabled) {
            Log.d(LOGGING_TAG, message);
        }
    }

    private void cleanUpOptions(Options options) {
        if (options.controlIconMaxDimensionInDensityIndependentPixels < 1) {
            options.controlIconMaxDimensionInDensityIndependentPixels = 1;
        }
        if (options.controlsDistanceFromScreenEdgeInDensityIndependentPixels < 0) {
            options.controlsDistanceFromScreenEdgeInDensityIndependentPixels = 0;
        }
        if (options.controlsAlpha < 0) {
            options.controlsAlpha = 0;
        } else if (options.controlsAlpha > 255) {
            options.controlsAlpha = 255;
        }
        if (!(options.countdownMessageFormat == null || isValidFormatStringForOneInteger(options.countdownMessageFormat))) {
            options.countdownMessageFormat = null;
        }
        if (options.specialSkipCountdownMessageFormat != null && !isValidFormatStringForOneInteger(options.specialSkipCountdownMessageFormat)) {
            options.specialSkipCountdownMessageFormat = null;
        }
    }

    private boolean isValidFormatStringForOneInteger(String string) {
        try {
            String.format(string, new Object[]{Integer.valueOf(1)});
            return true;
        } catch (IllegalFormatException e) {
            return false;
        }
    }
}
