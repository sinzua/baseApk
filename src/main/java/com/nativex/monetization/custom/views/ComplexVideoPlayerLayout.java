package com.nativex.monetization.custom.views;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.VideoView;
import com.nativex.common.Log;
import com.nativex.common.StringConstants;
import com.nativex.monetization.dialogs.custom.MessageDialog;
import com.nativex.monetization.enums.StringResources;
import com.nativex.monetization.interfaces.ICustomVideoView;
import com.nativex.monetization.manager.DensityManager;
import com.nativex.monetization.manager.StringsManager;
import com.nativex.monetization.theme.ThemeElementTypes;
import com.nativex.monetization.theme.ThemeManager;
import com.nativex.monetization.ui.DeviceScreenSize;
import com.nativex.monetization.ui.ScreenDependentSize;
import com.supersonicads.sdk.precache.DownloadManager;
import com.supersonicads.sdk.utils.Constants.ControllerParameters;

public class ComplexVideoPlayerLayout extends RelativeLayout implements ICustomVideoView {
    private static final int CONTROLS_DEFAULT_TIMEOUT = 3000;
    private static final int CONTROLS_HINT_DEFAULT_TIMEOUT = 3000;
    private static final int CONTROLS_PLAY_BUTTON_SIZE = 35;
    @IdRes
    private static final int ID_CHILD_VIDEO_VIEW = 1000;
    @IdRes
    private static final int ID_CLOSE = 1003;
    @IdRes
    private static final int ID_CONTROLS = 1002;
    @IdRes
    private static final int ID_CUSTOM_VIDEO_VIEW = 1001;
    @IdRes
    private static final int ID_PROGRESS_BAR = 1004;
    @IdRes
    private static final int ID_PROGRESS_CONTAINER = 1007;
    @IdRes
    private static final int ID_TIME_DURATION = 1006;
    @IdRes
    private static final int ID_TIME_ELAPSED = 1005;
    private static final int MARGIN_CONTROLS_BOTTOM = 10;
    private static final int MARGIN_CONTROLS_LEFT = 10;
    private static final int MARGIN_CONTROLS_RIGHT = 10;
    private static final int MARGIN_CONTROLS_TOP = 10;
    private static final int MSG_HIDE = 1001;
    private static final int MSG_HIDE_CONTROLS_ANIMATION_END = 1004;
    private static final int MSG_HIDE_CONTROLS_HINT_ANIMATION_END = 1007;
    private static final int MSG_HIDE_HINT = 1003;
    private static final int MSG_SHOW_CONTROLS_ANIMATION_END = 1005;
    private static final int MSG_SHOW_CONTROLS_HINT_ANIMATION_END = 1006;
    private static final int MSG_UPDATE_BUFFER = 1002;
    private static final int MSG_UPDATE_PROGRESS = 1000;
    private static final int PROGRESS_BAR_UPDATE_PERIOD = 500;
    private static final int PROGRESS_UPDATE_PERIOD = 100;
    private static final ScreenDependentSize closeButtonSize = new ScreenDependentSize(25, 30, 30, 40);
    private static final ComplexVideoPlayerLayoutInnerHandler handler = new ComplexVideoPlayerLayoutInnerHandler(Looper.getMainLooper());
    private int buffered = 0;
    private Controls controls = null;
    private boolean controlsAreDisplayed = false;
    private TextView controlsHint = null;
    private MessageDialog errorDialog = null;
    private boolean errorOccurred = false;
    private boolean finished = false;
    private boolean hidePlayButton = false;
    private OnClickListener onClose = null;
    private final OnCompletionListener onCompletionListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
            mp.setOnCompletionListener(null);
            mp.setOnBufferingUpdateListener(null);
            mp.setOnErrorListener(null);
            mp.setOnInfoListener(null);
            mp.setOnPreparedListener(null);
            mp.setOnSeekCompleteListener(null);
            mp.setOnVideoSizeChangedListener(null);
            mp.reset();
            ComplexVideoPlayerLayout.this.finished = true;
            Log.d("VideoLayout: buffered " + ComplexVideoPlayerLayout.this.buffered);
            if (ComplexVideoPlayerLayout.this.userSetOnCompletionListener != null) {
                ComplexVideoPlayerLayout.this.userSetOnCompletionListener.videoCompleted(ComplexVideoPlayerLayout.this.errorOccurred);
            }
        }
    };
    private final OnErrorListener onError = new OnErrorListener() {
        public boolean onError(MediaPlayer mp, int what, int extra) {
            final MediaPlayer mPlayer = mp;
            Log.d("CustomVideoView Error Code: " + extra);
            Log.d("CustomVideoView What Code: " + what);
            String dialogBody = StringsManager.getString(StringResources.VIDEO_CANNOT_BE_PLAYED);
            switch (what) {
                case 1:
                    dialogBody = StringsManager.getString(StringResources.VIDEO_UNKNOWN_ERROR);
                    Log.d("CustomVideoView: MediaPlayer unknown error. Error Code: " + extra);
                    break;
                case 100:
                    dialogBody = StringsManager.getString(StringResources.VIDEO_SERVER_ERROR);
                    Log.d("CustomVideoView: MediaPlayer server died error.");
                    break;
            }
            ComplexVideoPlayerLayout.this.errorOccurred = true;
            if (ComplexVideoPlayerLayout.this.errorDialog == null) {
                ComplexVideoPlayerLayout.this.errorDialog = new MessageDialog(ComplexVideoPlayerLayout.this.getContext());
                ComplexVideoPlayerLayout.this.errorDialog.setTitle(StringsManager.getString(StringResources.VIDEO_MEDIA_PLAYER_ERROR));
                ComplexVideoPlayerLayout.this.errorDialog.setMessage(dialogBody);
                ComplexVideoPlayerLayout.this.errorDialog.setButtonText(StringConstants.MESSAGE_DIALOG_BUTTON_TEXT);
                ComplexVideoPlayerLayout.this.errorDialog.setOnButtonClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        ComplexVideoPlayerLayout.this.errorDialog.dismiss();
                        ComplexVideoPlayerLayout.this.onCompletionListener.onCompletion(mPlayer);
                    }
                });
                ComplexVideoPlayerLayout.this.errorDialog.setCancelable(false);
                ComplexVideoPlayerLayout.this.errorDialog.show();
            }
            mp.reset();
            return true;
        }
    };
    private final OnClickListener onLayoutClick = new OnClickListener() {
        public void onClick(View v) {
            ComplexVideoPlayerLayout.this.showControls(3000);
        }
    };
    private final OnClickListener onPlayPauseClicked = new OnClickListener() {
        public void onClick(View v) {
            if (ComplexVideoPlayerLayout.this.finished) {
                ComplexVideoPlayerLayout.this.replay();
                ComplexVideoPlayerLayout.this.controls.onPlay();
            } else if (ComplexVideoPlayerLayout.this.prepared) {
                if (ComplexVideoPlayerLayout.this.paused) {
                    ComplexVideoPlayerLayout.this.controls.onPlay();
                    ComplexVideoPlayerLayout.this.videoView.start();
                    ComplexVideoPlayerLayout.this.paused = false;
                    return;
                }
                ComplexVideoPlayerLayout.this.controls.onPause();
                ComplexVideoPlayerLayout.this.videoView.pause();
                ComplexVideoPlayerLayout.this.paused = true;
            } else if (!ComplexVideoPlayerLayout.this.starting) {
            } else {
                if (ComplexVideoPlayerLayout.this.paused) {
                    ComplexVideoPlayerLayout.this.controls.onPlay();
                    ComplexVideoPlayerLayout.this.paused = false;
                    return;
                }
                ComplexVideoPlayerLayout.this.controls.onPause();
                ComplexVideoPlayerLayout.this.paused = true;
            }
        }
    };
    private final OnPreparedListener onPreparedListener = new OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
            ComplexVideoPlayerLayout.this.prepared = true;
            if (ComplexVideoPlayerLayout.this.paused) {
                ComplexVideoPlayerLayout.this.videoView.pause();
            }
            if (ComplexVideoPlayerLayout.this.userOnPreparedListener != null) {
                ComplexVideoPlayerLayout.this.userOnPreparedListener.onPrepared(mp);
            }
            ComplexVideoPlayerLayout.this.sendHandlerMessage(ControllerParameters.SECOND, 100);
        }
    };
    private boolean paused = false;
    private boolean prepared = false;
    private boolean starting = false;
    private OnPreparedListener userOnPreparedListener;
    private OnVideoCompleted userSetOnCompletionListener = null;
    private String videoDuration = null;
    private String videoURI = null;
    private VideoView videoView = null;

    private static class ComplexVideoPlayerLayoutInnerHandler extends Handler {
        public ComplexVideoPlayerLayoutInnerHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            ComplexVideoPlayerLayout layout = null;
            if (msg.obj instanceof ComplexVideoPlayerLayout) {
                layout = msg.obj;
            }
            if (layout != null) {
                switch (msg.what) {
                    case ControllerParameters.SECOND /*1000*/:
                        removeMessages(ControllerParameters.SECOND);
                        layout.updateProgress();
                        return;
                    case 1001:
                        layout.hideControls();
                        return;
                    case 1002:
                        layout.updateBuffer();
                        return;
                    case 1003:
                        layout.hideControlsHint();
                        return;
                    case DownloadManager.MESSAGE_MALFORMED_URL_EXCEPTION /*1004*/:
                        layout.onHideControlsAnimationEnd();
                        return;
                    case DownloadManager.MESSAGE_EMPTY_URL /*1007*/:
                        layout.onHideControlsHintAnimation();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private class Controls extends RelativeLayout {
        private Button close = null;
        private LinearProgressBar customProgress = null;
        private TextView duration = null;
        private TextView elapsed = null;
        private Button playPause = null;
        private LinearLayout progressLayout = null;

        public Controls(Context context) {
            super(context);
            setupLayout();
            createCloseButton(context);
            createProgressLayout(context);
            createCustomProgressBar(context);
        }

        public void clearAnimations() {
            clearAnimation();
            this.close.clearAnimation();
            this.progressLayout.clearAnimation();
        }

        private void setupLayout() {
            LayoutParams params = new LayoutParams(-1, -1);
            params.setMargins(10, 10, 10, 10);
            setLayoutParams(params);
        }

        private void createProgressLayout(Context context) {
            this.progressLayout = new LinearLayout(context);
            LayoutParams params = new LayoutParams(-1, -2);
            params.addRule(12);
            params.setMargins(5, 5, 5, 5);
            this.progressLayout.setLayoutParams(params);
            this.progressLayout.setId(DownloadManager.MESSAGE_EMPTY_URL);
            addView(this.progressLayout);
        }

        private void createCloseButton(Context context) {
            LayoutParams params = new LayoutParams(ComplexVideoPlayerLayout.closeButtonSize.size, ComplexVideoPlayerLayout.closeButtonSize.size);
            params.addRule(11);
            params.addRule(10);
            this.close = new Button(context);
            this.close.setLayoutParams(params);
            ThemeManager.setViewBackground(this.close, ThemeElementTypes.VIDEO_PLAYER_CLOSE_BUTTON_BACKGROUND);
            this.close.setId(1003);
            addView(this.close);
        }

        private void createCustomProgressBar(Context context) {
            this.progressLayout.setWeightSum(100.0f);
            this.progressLayout.setGravity(16);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2, 100.0f);
            this.customProgress = new LinearProgressBar(context);
            this.customProgress.setLayoutParams(params);
            this.customProgress.setMax(ControllerParameters.SECOND);
            this.customProgress.setId(DownloadManager.MESSAGE_MALFORMED_URL_EXCEPTION);
            int playSize = DensityManager.getDIP(context, 35.0f);
            int playMargins = DensityManager.getDIP(context, 5.0f);
            this.playPause = new Button(context);
            params = new LinearLayout.LayoutParams(playSize, playSize);
            params.setMargins(playMargins, 0, playMargins, 0);
            this.playPause.setLayoutParams(params);
            this.playPause.setClickable(true);
            ThemeManager.setViewBackground(this.playPause, ThemeElementTypes.VIDEO_PLAYER_CONTROLS_PLAY_BUTTON_BACKGROUND);
            this.elapsed = new TextView(context);
            this.elapsed.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            this.elapsed.setId(DownloadManager.MESSAGE_HTTP_NOT_FOUND);
            this.elapsed.setTextColor(ThemeManager.getColor(ThemeElementTypes.VIDEO_PLAYER_PROGRESS_ELAPSED_TEXT_COLOR).intValue());
            this.elapsed.setMaxLines(1);
            this.duration = new TextView(context);
            this.duration.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            this.duration.setTextColor(ThemeManager.getColor(ThemeElementTypes.VIDEO_PLAYER_PROGRESS_DURATION_TEXT_COLOR).intValue());
            this.duration.setId(DownloadManager.MESSAGE_HTTP_EMPTY_RESPONSE);
            this.duration.setMaxLines(1);
            this.progressLayout.addView(this.playPause);
            this.progressLayout.addView(this.elapsed);
            this.progressLayout.addView(this.customProgress);
            this.progressLayout.addView(this.duration);
        }

        public void setOnPlayPauseClickListener(OnClickListener listener) {
            this.playPause.setOnClickListener(listener);
        }

        public void onPlay() {
            ThemeManager.setViewBackground(this.playPause, ThemeElementTypes.VIDEO_PLAYER_CONTROLS_PAUSE_BUTTON_BACKGROUND);
        }

        public void onPause() {
            ThemeManager.setViewBackground(this.playPause, ThemeElementTypes.VIDEO_PLAYER_CONTROLS_PLAY_BUTTON_BACKGROUND);
        }

        public void hidePlayPauseButton() {
            this.playPause.setVisibility(8);
            this.playPause.setClickable(false);
        }

        public void showPlayPauseButton() {
            this.playPause.setVisibility(0);
            this.playPause.setClickable(true);
        }

        public void release() {
            this.close.setOnClickListener(null);
            this.close.setBackgroundDrawable(null);
            this.playPause.setOnClickListener(null);
            this.playPause.setBackgroundDrawable(null);
            this.customProgress.release();
            removeAllViews();
        }
    }

    public interface OnVideoCompleted {
        void videoCompleted(boolean z);
    }

    public interface OnVideoProgressChanged {
        void onVideoProgressChanged(int i);
    }

    public ComplexVideoPlayerLayout(Context context, DeviceScreenSize screenSize) {
        super(context);
        setParams(screenSize);
        createVideoView(context);
    }

    public ComplexVideoPlayerLayout(Context context) {
        super(context);
    }

    private void setParams(DeviceScreenSize screenSize) {
        ScreenDependentSize.setScreenSizes(screenSize, closeButtonSize);
        setId(1001);
    }

    private void createVideoView(Context context) {
        setWillNotDraw(false);
        LayoutParams params = new LayoutParams(-1, -1);
        params.addRule(13);
        this.videoView = new VideoView(context);
        this.videoView.setLayoutParams(params);
        this.videoView.setId(ControllerParameters.SECOND);
        addView(this.videoView);
        this.videoView.setZOrderOnTop(false);
        this.videoView.setOnErrorListener(this.onError);
        this.videoView.setOnCompletionListener(this.onCompletionListener);
        this.videoView.setOnPreparedListener(this.onPreparedListener);
        int hintPadding = DensityManager.getDIP(getContext(), 20.0f);
        this.controlsHint = new TextView(getContext());
        this.controlsHint.setText(StringsManager.getString(StringResources.VIDEO_PLAYER_CONTROLS_HINT));
        this.controlsHint.setTextColor(ThemeManager.getColor(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_HINT_TEXT_COLOR).intValue());
        ThemeManager.setViewBackground(this.controlsHint, ThemeElementTypes.VIDEO_PLAYER_CONTROLS_HINT_BACKGROUND);
        this.controlsHint.setVisibility(8);
        this.controlsHint.setPadding(hintPadding, hintPadding, hintPadding, hintPadding);
        this.controlsHint.setGravity(17);
        this.controlsHint.setTextSize(20.0f);
        LayoutParams hintParams = new LayoutParams(-2, -2);
        hintParams.addRule(13);
        this.controlsHint.setLayoutParams(hintParams);
        addView(this.controlsHint);
    }

    public void displayPlayPauseButton(boolean display) {
        if (this.controls == null) {
            this.hidePlayButton = !display;
        } else if (display) {
            this.controls.showPlayPauseButton();
        } else {
            this.controls.hidePlayPauseButton();
        }
    }

    private void createControls() {
        this.videoDuration = null;
        this.controls = new Controls(getContext());
        this.controls.setVisibility(4);
        this.controls.setId(1002);
        this.controls.setOnPlayPauseClickListener(this.onPlayPauseClicked);
        if (this.hidePlayButton) {
            this.controls.hidePlayPauseButton();
        }
        addView(this.controls);
        if (this.onClose != null) {
            this.controls.close.setOnClickListener(this.onClose);
        }
        setOnClickListener(this.onLayoutClick);
    }

    void sendHandlerMessage(int what, long delay) {
        Message msg = handler.obtainMessage(what, this);
        if (delay > 0) {
            handler.sendMessageDelayed(msg, delay);
        } else {
            handler.sendMessage(msg);
        }
    }

    private void updateBuffer() {
        if (this.videoView != null) {
            this.buffered = this.videoView.getBufferPercentage();
        }
        sendHandlerMessage(1002, 5000);
    }

    private void onHideControlsAnimationEnd() {
        if (this.controls != null) {
            this.controls.setVisibility(4);
        }
    }

    private void onHideControlsHintAnimation() {
        if (this.controlsHint != null) {
            this.controlsHint.setVisibility(8);
        }
    }

    private void hideControlsHint() {
        Animation animation = ThemeManager.getAnimation(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_HINT_HIDE_ANIMATION);
        long duration = 0;
        if (animation != null) {
            this.controlsHint.startAnimation(animation);
            duration = animation.getDuration();
        }
        if (duration > 0) {
            sendHandlerMessage(DownloadManager.MESSAGE_EMPTY_URL, duration);
        } else {
            this.controlsHint.setVisibility(8);
        }
    }

    public float getVideoProgress() {
        float currentPosition;
        float position = (float) this.videoView.getCurrentPosition();
        float duration = (float) this.videoView.getDuration();
        if (position > duration) {
            position = duration;
        }
        if (duration > 0.0f) {
            currentPosition = position / duration;
        } else {
            currentPosition = 0.0f;
        }
        if (currentPosition < 0.0f) {
            return 0.0f;
        }
        return currentPosition;
    }

    private void updateProgress() {
        if (this.controlsAreDisplayed && this.videoView != null && this.controls != null) {
            int newPosition;
            String videoLength;
            int position = this.videoView.getCurrentPosition();
            int duration = this.videoView.getDuration();
            if (position > duration) {
                position = duration;
            }
            if (duration > 0) {
                newPosition = (this.controls.customProgress.getMax() * position) / duration;
            } else {
                newPosition = 0;
            }
            int bufferedPosition = (this.controls.customProgress.getMax() * this.videoView.getBufferPercentage()) / 100;
            if (duration > 0) {
                if (this.videoDuration == null) {
                    this.videoDuration = convertTime(duration);
                }
                videoLength = this.videoDuration;
            } else {
                videoLength = convertTime(0);
            }
            this.controls.customProgress.setPosition(newPosition);
            this.controls.customProgress.setBufferPosition(bufferedPosition);
            this.controls.elapsed.setText(convertTime(position));
            this.controls.duration.setText(videoLength);
            if (this.videoView.isPlaying()) {
                sendHandlerMessage(ControllerParameters.SECOND, 500);
            }
        }
    }

    private String convertTime(int time) {
        int hours = (time / ControllerParameters.SECOND) / 3600;
        int minutes = (time / ControllerParameters.SECOND) / 60;
        int seconds = (time / ControllerParameters.SECOND) % 60;
        return (hours > 0 ? hours + ":" : "") + (minutes > 9 ? Integer.valueOf(minutes) : "0" + minutes) + ":" + (seconds > 9 ? Integer.valueOf(seconds) : "0" + seconds);
    }

    private void hideControls() {
        handler.removeMessages(DownloadManager.MESSAGE_HTTP_NOT_FOUND);
        handler.removeMessages(DownloadManager.MESSAGE_MALFORMED_URL_EXCEPTION);
        handler.removeMessages(ControllerParameters.SECOND);
        handler.removeMessages(1001);
        long animationDuration = 0;
        if (this.controls != null) {
            this.controls.clearAnimations();
            this.controlsAreDisplayed = false;
            Animation animation = ThemeManager.getAnimation(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_HIDE_ANIMATION);
            if (animation != null) {
                animationDuration = animation.getDuration();
                this.controls.startAnimation(animation);
            } else {
                Animation closeAnimation = ThemeManager.getAnimation(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_CLOSE_HIDE_ANIMATION);
                Animation progressAnimation = ThemeManager.getAnimation(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_PLAYER_HIDE_ANIMATION);
                if (!(closeAnimation == null && progressAnimation == null)) {
                    if (closeAnimation != null) {
                        animationDuration = progressAnimation.getDuration();
                        this.controls.close.startAnimation(closeAnimation);
                    }
                    if (progressAnimation != null) {
                        animationDuration = Math.max(progressAnimation.getDuration(), animationDuration);
                        this.controls.progressLayout.startAnimation(progressAnimation);
                    }
                }
            }
            if (animationDuration > 0) {
                sendHandlerMessage(DownloadManager.MESSAGE_MALFORMED_URL_EXCEPTION, animationDuration);
            } else {
                this.controls.setVisibility(4);
            }
        }
    }

    private void showControls(long timeout) {
        if (this.controls != null) {
            if (!this.controlsAreDisplayed) {
                this.controlsAreDisplayed = true;
                updateProgress();
                this.controls.clearAnimations();
                long animationDuration = 0;
                Animation animation = ThemeManager.getAnimation(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_SHOW_ANIMATION);
                if (animation != null) {
                    this.controls.startAnimation(animation);
                    animationDuration = animation.getDuration();
                } else {
                    Animation closeAnimation = ThemeManager.getAnimation(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_CLOSE_SHOW_ANIMATION);
                    Animation progressAnimation = ThemeManager.getAnimation(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_PLAYER_SHOW_ANIMATION);
                    if (closeAnimation != null) {
                        this.controls.close.startAnimation(closeAnimation);
                        animationDuration = closeAnimation.getDuration();
                    }
                    if (progressAnimation != null) {
                        this.controls.progressLayout.startAnimation(progressAnimation);
                        animationDuration = Math.max(progressAnimation.getDuration(), animationDuration);
                    }
                }
                this.controls.setVisibility(0);
                this.controls.bringToFront();
                handler.removeMessages(DownloadManager.MESSAGE_HTTP_NOT_FOUND);
                if (animationDuration > 0) {
                    sendHandlerMessage(DownloadManager.MESSAGE_HTTP_NOT_FOUND, animationDuration);
                }
            }
            if (timeout > 0) {
                handler.removeMessages(1001);
                handler.removeMessages(DownloadManager.MESSAGE_MALFORMED_URL_EXCEPTION);
                sendHandlerMessage(1001, 3000);
            }
        }
    }

    public void start() {
        this.videoView.start();
        this.starting = true;
        if (this.controls == null) {
            createControls();
        }
        this.controls.onPlay();
        this.buffered = 0;
        sendHandlerMessage(1002, 500);
        showControlsHint();
    }

    private void showControlsHint() {
        Animation animation = ThemeManager.getAnimation(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_HINT_SHOW_ANIMATION);
        if (animation != null) {
            this.controlsHint.startAnimation(animation);
        }
        this.controlsHint.setVisibility(0);
        sendHandlerMessage(1003, 3000);
    }

    public void pause() {
        if (this.videoView != null) {
            this.paused = true;
            this.videoView.pause();
        }
        if (this.controls != null) {
            this.controls.onPause();
        }
    }

    public void setOnPreparedListener(OnPreparedListener listener) {
        this.userOnPreparedListener = listener;
    }

    public void stopPlayback() {
        this.videoView.stopPlayback();
        if (this.controls != null) {
            this.controls.onPause();
        }
    }

    public void setVideoSource(String source) {
        this.videoURI = source;
        this.videoView.setVideoPath(source);
    }

    public void setOnCompletionListener(OnVideoCompleted listener) {
        this.userSetOnCompletionListener = listener;
    }

    public void setOnCloseListener(OnClickListener onClose) {
        this.onClose = onClose;
        if (this.controls != null) {
            this.controls.close.setOnClickListener(onClose);
        }
    }

    public boolean isPlaying() {
        return this.videoView.isPlaying();
    }

    public void seekTo(int msec) {
        this.videoView.seekTo(msec);
        updateProgress();
    }

    public int getDuration() {
        return this.videoView.getDuration();
    }

    public int getCurrentPosition() {
        return this.videoView.getCurrentPosition();
    }

    public void release() {
        if (this.controls != null) {
            this.controls.release();
            this.controls = null;
        }
        if (this.errorDialog != null) {
            this.errorDialog.dismiss();
            this.errorDialog = null;
        }
        handler.removeMessages(1001);
        handler.removeMessages(ControllerParameters.SECOND);
        handler.removeMessages(1002);
        setOnClickListener(null);
        removeAllViews();
    }

    void replay() {
        this.prepared = false;
        this.finished = false;
        this.paused = false;
        this.videoView.setVideoPath(this.videoURI);
        this.videoView.start();
        this.starting = true;
    }
}
