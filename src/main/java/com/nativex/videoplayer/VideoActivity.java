package com.nativex.videoplayer;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.nativex.network.volley.DefaultRetryPolicy;
import com.nativex.videoplayer.VideoPlayer.Options;
import java.util.IllegalFormatException;

public class VideoActivity extends Activity implements VideoDisplayer {
    public static final String INTENT_EXTRA_SYSTEM_UI_VISIBILITY = "videoSystemUIVisibility";
    private ProgressBar mBufferingProgressBar;
    private TextView mCountdownTimerTextView;
    private boolean mIsClientRequestedCloseInProgress = false;
    private boolean mIsMuted = false;
    private boolean mIsVideoShowing = false;
    private ImageView mMutedImageView;
    private ImageView mNotMutedImageView;
    private Options mOptions;
    private TextView mSkipCountdownTimerTextView;
    private ImageView mSkipImageView;
    private final SurfaceHolderListener mSurfaceHolderListener = new SurfaceHolderListener();
    private SurfaceView mSurfaceView;
    private NativeXVideoPlayer mVideoPlayer;
    private float mVideoWidthHeightProportion;

    private class SurfaceHolderListener implements Callback {
        private SurfaceHolderListener() {
        }

        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            VideoActivity.this.mVideoPlayer.surfaceCreated(surfaceHolder, VideoActivity.this);
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mVideoPlayer = NativeXVideoPlayer.getInstance();
        this.mVideoPlayer.setVideoDisplayer(this);
        this.mOptions = this.mVideoPlayer.getOptions();
        if (this.mOptions == null) {
            finish();
            return;
        }
        setRequestedOrientation(this.mOptions.orientation);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new LayoutParams(-1, -1));
        relativeLayout.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        relativeLayout.setKeepScreenOn(true);
        this.mSurfaceView = new SurfaceView(this);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.addRule(13);
        this.mSurfaceView.setLayoutParams(layoutParams);
        relativeLayout.addView(this.mSurfaceView);
        float screenDensity = getResources().getDisplayMetrics().density;
        int marginPixels = (int) (((float) this.mOptions.controlsDistanceFromScreenEdgeInDensityIndependentPixels) * screenDensity);
        if (!(!this.mOptions.allowMute || this.mOptions.mutedButtonIcon == null || this.mOptions.notMutedButtonIcon == null)) {
            this.mNotMutedImageView = setUpControlIconImageView(11, 12, this.mOptions.notMutedButtonIcon, this.mOptions.controlsAlpha, relativeLayout, marginPixels, screenDensity, this.mOptions.controlIconMaxDimensionInDensityIndependentPixels, new OnClickListener() {
                public void onClick(View view) {
                    VideoActivity.this.toggleMute();
                }
            });
            this.mMutedImageView = setUpControlIconImageView(11, 12, this.mOptions.mutedButtonIcon, this.mOptions.controlsAlpha, relativeLayout, marginPixels, screenDensity, this.mOptions.controlIconMaxDimensionInDensityIndependentPixels, new OnClickListener() {
                public void onClick(View view) {
                    VideoActivity.this.toggleMute();
                }
            });
            this.mMutedImageView.setVisibility(8);
            if (this.mOptions.startMuted) {
                toggleMute();
            }
        }
        this.mSkipImageView = setUpControlIconImageView(11, 10, this.mOptions.skipButtonIcon, this.mOptions.controlsAlpha, relativeLayout, marginPixels, screenDensity, this.mOptions.controlIconMaxDimensionInDensityIndependentPixels, new OnClickListener() {
            public void onClick(View view) {
                VideoActivity.this.finish();
            }
        });
        this.mSkipImageView.setVisibility(8);
        this.mSkipCountdownTimerTextView = new TextView(this);
        LayoutParams skipCountdownTimerLayoutParams = new LayoutParams(-2, -2);
        skipCountdownTimerLayoutParams.setMargins(marginPixels, marginPixels, marginPixels, marginPixels);
        skipCountdownTimerLayoutParams.addRule(9);
        skipCountdownTimerLayoutParams.addRule(10);
        this.mSkipCountdownTimerTextView.setLayoutParams(skipCountdownTimerLayoutParams);
        this.mSkipCountdownTimerTextView.setTextColor(this.mOptions.countdownMessageTextColor);
        relativeLayout.addView(this.mSkipCountdownTimerTextView);
        this.mSkipCountdownTimerTextView.setVisibility(8);
        this.mCountdownTimerTextView = new TextView(this);
        LayoutParams layoutParams2 = new LayoutParams(-2, -2);
        layoutParams2.setMargins(marginPixels, marginPixels, marginPixels, marginPixels);
        layoutParams2.addRule(9);
        layoutParams2.addRule(10);
        this.mCountdownTimerTextView.setLayoutParams(layoutParams2);
        this.mCountdownTimerTextView.setTextColor(this.mOptions.countdownMessageTextColor);
        relativeLayout.addView(this.mCountdownTimerTextView);
        this.mBufferingProgressBar = new ProgressBar(this);
        LayoutParams bufferingLayoutParams = new LayoutParams(-2, -2);
        bufferingLayoutParams.addRule(13);
        this.mBufferingProgressBar.setLayoutParams(bufferingLayoutParams);
        this.mBufferingProgressBar.setVisibility(8);
        relativeLayout.addView(this.mBufferingProgressBar);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(relativeLayout);
        int vis = getIntent().getIntExtra(INTENT_EXTRA_SYSTEM_UI_VISIBILITY, 0);
        if (VERSION.SDK_INT >= 19) {
            try {
                getWindow().getDecorView().setSystemUiVisibility(vis);
            } catch (Exception e) {
                this.mVideoPlayer.log(e);
            }
        }
        resizeForCurrentScreenDimensions();
        SurfaceHolder surfaceHolder = this.mSurfaceView.getHolder();
        surfaceHolder.setType(3);
        surfaceHolder.addCallback(this.mSurfaceHolderListener);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        resizeForCurrentScreenDimensions();
    }

    public void onBackPressed() {
        if (this.mSkipImageView.getVisibility() == 0) {
            finish();
        } else {
            this.mVideoPlayer.skipRequestedButNotAvailable();
        }
    }

    public void close() {
        this.mIsClientRequestedCloseInProgress = true;
        if (!isFinishing()) {
            finish();
        }
    }

    public void enableSkip(boolean isEnabled) {
        final int desiredVisibility = isEnabled ? 0 : 8;
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (VideoActivity.this.mSkipImageView.getVisibility() != desiredVisibility) {
                        VideoActivity.this.mSkipImageView.setVisibility(desiredVisibility);
                    }
                } catch (Exception e) {
                    VideoActivity.this.mVideoPlayer.log(e);
                }
            }
        });
    }

    public void isStuck(final boolean isStuck) {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    VideoActivity.this.mBufferingProgressBar.setVisibility(isStuck ? 0 : 8);
                } catch (Exception e) {
                    VideoActivity.this.mVideoPlayer.log(e);
                }
            }
        });
    }

    public void displaySecondsRemaining(int seconds) {
        updateCountdownTimer(this.mCountdownTimerTextView, this.mOptions.countdownMessageFormat, seconds);
    }

    public void displaySecondsRemainingUntilSkippable(int seconds) {
        updateCountdownTimer(this.mSkipCountdownTimerTextView, this.mOptions.specialSkipCountdownMessageFormat, seconds);
    }

    public void initializeVideo(final float videoWidthHeightProportion) {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    VideoActivity.this.mVideoWidthHeightProportion = videoWidthHeightProportion;
                    VideoActivity.this.mIsVideoShowing = true;
                    VideoActivity.this.resizeForCurrentScreenDimensions();
                } catch (Exception e) {
                    VideoActivity.this.mVideoPlayer.log(e);
                }
            }
        });
    }

    protected void onPause() {
        super.onPause();
        if (!this.mIsClientRequestedCloseInProgress) {
            this.mVideoPlayer.activityFinishing(this);
            if (!isFinishing()) {
                finish();
            }
        }
    }

    private void resizeForCurrentScreenDimensions() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        float screenWidthHeightProportion = ((float) screenWidth) / ((float) screenHeight);
        if (this.mIsVideoShowing) {
            int viewWidth;
            int viewHeight;
            if (this.mVideoWidthHeightProportion > screenWidthHeightProportion) {
                viewWidth = screenWidth;
                viewHeight = (int) (((float) screenWidth) / this.mVideoWidthHeightProportion);
            } else {
                viewWidth = (int) (this.mVideoWidthHeightProportion * ((float) screenHeight));
                viewHeight = screenHeight;
            }
            ViewGroup.LayoutParams videoLayoutParams = this.mSurfaceView.getLayoutParams();
            videoLayoutParams.width = viewWidth;
            videoLayoutParams.height = viewHeight;
            this.mSurfaceView.requestLayout();
        }
    }

    private void toggleMute() {
        boolean z = false;
        if (this.mIsMuted) {
            this.mVideoPlayer.unMute();
            this.mMutedImageView.setVisibility(8);
            this.mNotMutedImageView.setVisibility(0);
        } else {
            this.mVideoPlayer.mute();
            this.mMutedImageView.setVisibility(0);
            this.mNotMutedImageView.setVisibility(8);
        }
        this.mMutedImageView.invalidate();
        this.mNotMutedImageView.invalidate();
        if (!this.mIsMuted) {
            z = true;
        }
        this.mIsMuted = z;
    }

    private ImageView setUpControlIconImageView(int horizontalAlignment, int verticalAlignment, Drawable icon, int alpha, RelativeLayout parentRelativeLayout, int marginPixels, float screenDensity, int controlIconMaxDimensionInDensityIndependentPixels, OnClickListener onClickListener) {
        int heightDp;
        int widthDp;
        ImageView imageView = new ImageView(this);
        float iconWidthToHeightRatio = ((float) icon.getIntrinsicWidth()) / ((float) icon.getIntrinsicHeight());
        if (iconWidthToHeightRatio == DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) {
            heightDp = controlIconMaxDimensionInDensityIndependentPixels;
            widthDp = controlIconMaxDimensionInDensityIndependentPixels;
        } else if (iconWidthToHeightRatio > DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) {
            widthDp = controlIconMaxDimensionInDensityIndependentPixels;
            heightDp = (int) (((float) controlIconMaxDimensionInDensityIndependentPixels) * (DefaultRetryPolicy.DEFAULT_BACKOFF_MULT / iconWidthToHeightRatio));
        } else {
            widthDp = (int) (((float) controlIconMaxDimensionInDensityIndependentPixels) * iconWidthToHeightRatio);
            heightDp = controlIconMaxDimensionInDensityIndependentPixels;
        }
        LayoutParams layoutParams = new LayoutParams((int) (((float) widthDp) * screenDensity), (int) (((float) heightDp) * screenDensity));
        layoutParams.setMargins(marginPixels, marginPixels, marginPixels, marginPixels);
        layoutParams.addRule(horizontalAlignment);
        layoutParams.addRule(verticalAlignment);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageDrawable(icon);
        imageView.setAlpha(alpha);
        imageView.setBackgroundColor(0);
        imageView.setOnClickListener(onClickListener);
        parentRelativeLayout.addView(imageView);
        return imageView;
    }

    private void updateCountdownTimer(final TextView textView, final String messageFormat, final int seconds) {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (textView != null) {
                        if (seconds < 0) {
                            textView.setVisibility(8);
                            return;
                        }
                        String countdownMessage = null;
                        if (messageFormat != null) {
                            try {
                                countdownMessage = String.format(messageFormat, new Object[]{Integer.valueOf(seconds)});
                            } catch (IllegalFormatException e) {
                            }
                        }
                        if (countdownMessage == null) {
                            countdownMessage = "";
                        }
                        textView.setText(countdownMessage);
                        textView.invalidate();
                        if (textView.getVisibility() == 8) {
                            textView.setVisibility(0);
                        }
                    }
                } catch (Exception e2) {
                    VideoActivity.this.mVideoPlayer.log(e2);
                }
            }
        });
    }
}
