package com.nativex.monetization.custom.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.nativex.monetization.interfaces.ICustomProgressBar;
import com.nativex.monetization.theme.ThemeElementTypes;
import com.nativex.monetization.theme.ThemeManager;
import com.nativex.network.volley.DefaultRetryPolicy;

public class LinearProgressBar extends RelativeLayout implements ICustomProgressBar {
    private static final int HEIGHT_BAR = 10;
    @IdRes
    private static final int ID_BUFFER_IMAGE = 1002;
    @IdRes
    private static final int ID_EMPTY_IMAGE = 1003;
    @IdRes
    private static final int ID_PROGRESS_IMAGE = 1001;
    @IdRes
    private static final int ID_PROGRESS_LAYOUT = 1000;
    private static final int MARGIN_BAR_BOTTOM = 0;
    private static final int MARGIN_BAR_LEFT = 5;
    private static final int MARGIN_BAR_RIGHT = 5;
    private static final int MARGIN_BAR_TOP = 0;
    private final Drawable backgroundDrawable = null;
    private Drawable bufferDrawable = null;
    private ImageView bufferImage = null;
    private Drawable emptyDrawable = null;
    private ImageView emptyImage = null;
    private int positionBuffer = 0;
    private int positionCurrent = 0;
    private int positionMax = 100;
    private Drawable progressDrawable = null;
    private ImageView progressImage = null;

    public LinearProgressBar(Context context) {
        super(context);
        setupLayout();
        createControls(context);
    }

    private void setupLayout() {
        setId(1000);
    }

    private void createControls(Context context) {
        loadDrawables(context);
        createEmptyImage(context);
        createBufferImage(context);
        createProgressImage(context);
    }

    private void createProgressImage(Context context) {
        this.progressImage = new ImageView(context);
        this.progressImage.setBackgroundDrawable(this.progressDrawable);
        this.progressImage.setLayoutParams(setBarParams(-1, 10));
        this.progressImage.setMaxHeight(10);
        this.progressImage.setMinimumHeight(10);
        this.progressImage.setId(ID_PROGRESS_IMAGE);
        addView(this.progressImage);
    }

    private void createBufferImage(Context context) {
        this.bufferImage = new ImageView(context);
        this.bufferImage.setLayoutParams(setBarParams(-1, 10));
        this.bufferImage.setBackgroundDrawable(this.bufferDrawable);
        this.bufferImage.setMaxHeight(10);
        this.bufferImage.setMinimumHeight(10);
        this.bufferImage.setId(ID_BUFFER_IMAGE);
        addView(this.bufferImage);
    }

    private void createEmptyImage(Context context) {
        this.emptyImage = new ImageView(context);
        this.emptyImage.setLayoutParams(setBarParams(-1, 10));
        this.emptyImage.setBackgroundDrawable(this.emptyDrawable);
        this.emptyImage.setMaxHeight(10);
        this.emptyImage.setMinimumHeight(10);
        this.emptyImage.setId(ID_EMPTY_IMAGE);
        addView(this.emptyImage);
    }

    private void loadDrawables(Context context) {
        this.progressDrawable = ThemeManager.getDrawable(ThemeElementTypes.VIDEO_PLAYER_PROGRESS_BACKGROUND_WATCHED);
        this.bufferDrawable = ThemeManager.getDrawable(ThemeElementTypes.VIDEO_PLAYER_PROGRESS_BACKGROUND_BUFFERED);
        this.emptyDrawable = ThemeManager.getDrawable(ThemeElementTypes.VIDEO_PLAYER_PROGRESS_BACKGROUND_EMPTY);
    }

    public void release() {
        this.progressImage.setBackgroundDrawable(null);
        this.bufferImage.setBackgroundDrawable(null);
        this.emptyImage.setBackgroundDrawable(null);
        removeAllViews();
        this.progressDrawable = null;
        this.bufferDrawable = null;
        this.emptyDrawable = null;
    }

    public void setPosition(int progress) {
        if (progress >= 0 && progress <= this.positionMax) {
            this.positionCurrent = progress;
            updateProgressBar();
        }
    }

    public void setProgress(float progress) {
        if (progress >= 0.0f && progress <= DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) {
            this.positionCurrent = (int) (((float) this.positionMax) * progress);
            updateProgressBar();
        }
    }

    public void setBufferPosition(int progress) {
        if (progress >= 0 && progress <= this.positionMax) {
            this.positionBuffer = progress;
            updateProgressBar();
        }
    }

    public void setBufferProgress(float progress) {
        if (progress >= 0.0f && progress <= DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) {
            this.positionBuffer = (int) (((float) this.positionMax) * progress);
            updateProgressBar();
        }
    }

    public void setBackgroundDrawable(Drawable background) {
        if (background == null) {
            background = this.backgroundDrawable;
        }
        super.setBackgroundDrawable(background);
        invalidate();
    }

    public void setBufferDrawable(Drawable buffer) {
        ImageView imageView = this.bufferImage;
        if (buffer == null) {
            buffer = this.bufferDrawable;
        }
        imageView.setBackgroundDrawable(buffer);
        this.bufferImage.invalidate();
    }

    public void setProgressDrawable(Drawable progress) {
        ImageView imageView = this.progressImage;
        if (progress == null) {
            progress = this.progressDrawable;
        }
        imageView.setBackgroundDrawable(progress);
        this.progressImage.invalidate();
    }

    public void setEmptyDrawable(Drawable empty) {
        ImageView imageView = this.emptyImage;
        if (empty == null) {
            empty = this.emptyDrawable;
        }
        imageView.setBackgroundDrawable(empty);
        this.emptyImage.invalidate();
    }

    @Deprecated
    public void setTickDrawable(Drawable tick) {
    }

    public int getCurrentPosition() {
        return this.positionCurrent;
    }

    public int getBufferedPosition() {
        return this.positionBuffer;
    }

    public void setMax(int max) {
        this.positionMax = max;
    }

    public int getMax() {
        return this.positionMax;
    }

    private void updateProgressBar() {
        if (getWidth() > 0) {
            int maxWidth = (getWidth() - 5) - 5;
            this.progressImage.setLayoutParams(setBarParams(getNewWidth(this.positionCurrent, maxWidth), 10));
            this.bufferImage.setLayoutParams(setBarParams(getNewWidth(this.positionBuffer, maxWidth), 10));
        }
    }

    private LayoutParams setBarParams(int width, int height) {
        LayoutParams params = new LayoutParams(width, height);
        params.setMargins(5, 0, 5, 0);
        params.addRule(10);
        params.addRule(9);
        return params;
    }

    private int getNewWidth(int position, int width) {
        return (int) (((float) width) * (((float) position) / ((float) this.positionMax)));
    }

    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == 0) {
            updateProgressBar();
        }
    }
}
