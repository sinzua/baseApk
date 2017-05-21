package com.nativex.monetization.theme;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.ShapeDrawable.ShaderFactory;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.view.ViewCompat;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import com.nativex.common.FileConstants;
import com.nativex.monetization.manager.DensityManager;
import com.nativex.monetization.manager.MonetizationSharedDataManager;
import com.nativex.network.volley.DefaultRetryPolicy;
import com.parse.ParseException;

public class OriginalTheme extends ThemeFromAssets {
    private static final int SHADER_ID_BANNER = 443;

    public class CustomShapeDrawable extends ShapeDrawable {
        private final int fillColor1;
        private final int fillColor2;
        private final int shader;
        final Shape shape;
        private final int stroke;
        private final Paint strokePaint;
        Float strokeWidth;

        private CustomShapeDrawable(OriginalTheme this$0, int stroke, int fillColor1, int fillColor2, float strokeWidth, int shader) {
            this(stroke, fillColor1, fillColor2, this$0.createShape(), shader);
            this.strokePaint.setStrokeWidth(strokeWidth);
            this.strokeWidth = Float.valueOf(strokeWidth);
        }

        private CustomShapeDrawable(OriginalTheme this$0, int stroke, int fillColor1, int fillColor2, float strokeWidth) {
            this(this$0, stroke, fillColor1, fillColor2, strokeWidth, -1);
        }

        public Drawable mutate() {
            if (this.strokeWidth != null) {
                return new CustomShapeDrawable(OriginalTheme.this, this.stroke, this.fillColor1, this.fillColor2, this.strokeWidth.floatValue(), this.shader);
            }
            return new CustomShapeDrawable(this.stroke, this.fillColor1, this.fillColor2, OriginalTheme.this.createShape(), this.shader);
        }

        public CustomShapeDrawable(OriginalTheme this$0, int strokeColor, int fillColor1, int fillColor2, Shape shape) {
            this(strokeColor, fillColor1, fillColor2, shape, -1);
        }

        public CustomShapeDrawable(int strokeColor, int fillColor1, int fillColor2, Shape shape, int shader) {
            super(shape);
            this.stroke = strokeColor;
            this.fillColor1 = fillColor1;
            this.fillColor2 = fillColor2;
            this.shape = shape;
            this.shader = shader;
            this.strokePaint = new Paint();
            this.strokePaint.setStyle(Style.STROKE);
            this.strokePaint.setStrokeWidth(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            this.strokePaint.setColor(strokeColor);
            this.strokePaint.setAntiAlias(true);
            final OriginalTheme originalTheme = OriginalTheme.this;
            final int i = shader;
            final int i2 = fillColor2;
            final int i3 = fillColor1;
            setShaderFactory(new ShaderFactory() {
                public Shader resize(int width, int height) {
                    switch (i) {
                        case OriginalTheme.SHADER_ID_BANNER /*443*/:
                            return new RadialGradient((float) (width / 2), (float) (height / 2), (float) width, i2, i3, TileMode.MIRROR);
                        default:
                            return new LinearGradient(0.0f, 0.0f, 0.0f, (float) ((width * 2) / 3), i2, i3, TileMode.MIRROR);
                    }
                }
            });
        }

        protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
            super.onDraw(shape, canvas, paint);
            shape.draw(canvas, this.strokePaint);
        }
    }

    public void initialize() {
        initEncodedStrings();
        initColors();
        initAnimations();
        super.initialize();
    }

    private void initEncodedStrings() {
        addEncodedString(ThemeElementTypes.LOGO, FileConstants.LOGO);
        addEncodedString(ThemeElementTypes.MRAID_CLOSE_BUTTON_DEFAULT, FileConstants.MRAID_CLOSE_BUTTON);
        addEncodedString(ThemeElementTypes.MESSAGE_DIALOG_BACKGROUND, FileConstants.CTA_DIALOG_BG9);
        addEncodedString(ThemeElementTypes.MESSAGE_DIALOG_CLOSE_BUTTON_BACKGROUND, FileConstants.CTA_CLOSE_X2);
        addEncodedString(ThemeElementTypes.MESSAGE_DIALOG_BODY_BACKGROUND, FileConstants.CTA_OFFER_BG);
        addEncodedString(ThemeElementTypes.MESSAGE_DIALOG_OK_BUTTON_BACKGROUND_NORMAL, FileConstants.CTA_DIALOG_BUTTON_9);
        addEncodedString(ThemeElementTypes.MESSAGE_DIALOG_OK_BUTTON_BACKGROUND_PRESSED, FileConstants.CTA_DIALOG_BUTTON_DOWN9);
        addEncodedString(ThemeElementTypes.VIDEO_PLAYER_CLOSE_BUTTON_BACKGROUND, FileConstants.CLOSE_BUTTON);
        addEncodedString(ThemeElementTypes.VIDEO_PLAYER_PROGRESS_BACKGROUND_WATCHED, FileConstants.PROGRESS_BAR_PROGRESS);
        addEncodedString(ThemeElementTypes.VIDEO_PLAYER_PROGRESS_BACKGROUND_EMPTY, FileConstants.PROGRESS_BAR_EMPTY);
        addEncodedString(ThemeElementTypes.VIDEO_PLAYER_PROGRESS_BACKGROUND_BUFFERED, FileConstants.PROGRESS_BAR_BUFFER);
        addEncodedString(ThemeElementTypes.VIDEO_ACTION_DIALOG_OFFER_BACKGROUND, FileConstants.CTA_OFFER_BG_2X);
        addEncodedString(ThemeElementTypes.VIDEO_ACTION_DIALOG_SCROLLBAR_SELECTED_BACKGROUND, FileConstants.CTA_DOT_ACTIVE_2X);
        addEncodedString(ThemeElementTypes.VIDEO_ACTION_DIALOG_SCROLLBAR_EMPTY_BACKGROUND, FileConstants.CTA_DOT_INACTIVE_2X);
        addEncodedString(ThemeElementTypes.VIDEO_ACTION_DIALOG_CLOSE_BUTTON_BACKGROUND, FileConstants.CTA_CLOSE_X2);
        addEncodedString(ThemeElementTypes.VIDEO_ACTION_DIALOG_BACKGROUND, FileConstants.CTA_DIALOG_BG9);
        addEncodedString(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_PLAY_BUTTON_BACKGROUND, FileConstants.VIDEO_PLAYER_PLAY);
        addEncodedString(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_PAUSE_BUTTON_BACKGROUND, FileConstants.VIDEO_PLAYER_PAUSE);
        addEncodedString(ThemeElementTypes.NATIVE_VIDEO_PLAYER_CLOSE_BUTTON, FileConstants.NATIVE_PLAYER_CLOSE_BUTTON);
        addEncodedString(ThemeElementTypes.NATIVE_VIDEO_PLAYER_MUTE_BUTTON, FileConstants.NATIVE_PLAYER_MUTE_BUTTON);
        addEncodedString(ThemeElementTypes.NATIVE_VIDEO_PLAYER_UNMUTE_BUTTON, FileConstants.NATIVE_PLAYER_UNMUTE_BUTTON);
        addEncodedString(ThemeElementTypes.NATIVE_VIDEO_PLAYER_PRIVACY_BUTTON, FileConstants.NATIVE_PLAYER_PRIVACY_BUTTON);
    }

    private void initColors() {
        setColor(ThemeElementTypes.MESSAGE_DIALOG_TITLE_TEXT_COLOR, -1);
        setColor(ThemeElementTypes.MESSAGE_DIALOG_BODY_TEXT_COLOR, -1);
        setColor(ThemeElementTypes.MESSAGE_DIALOG_OK_BUTTON_TEXT_COLOR, ViewCompat.MEASURED_STATE_MASK);
        setColor(ThemeElementTypes.VIDEO_PLAYER_PROGRESS_DURATION_TEXT_COLOR, -1);
        setColor(ThemeElementTypes.VIDEO_PLAYER_PROGRESS_ELAPSED_TEXT_COLOR, -1);
        setColor(ThemeElementTypes.VIDEO_ACTION_DIALOG_TITLE_CONGRATULATIONS_TEXT_COLOR, -1);
        setColor(ThemeElementTypes.VIDEO_ACTION_DIALOG_TITLE_REWARD_TEXT_COLOR, Color.argb(255, ParseException.ACCOUNT_ALREADY_LINKED, 213, 221));
        setColor(ThemeElementTypes.VIDEO_ACTION_DIALOG_TITLE_BACKGROUND, 0);
        setColor(ThemeElementTypes.VIDEO_ACTION_DIALOG_SCROLLBAR_BACKGROUND, 0);
        setColor(ThemeElementTypes.VIDEO_ACTION_DIALOG_OFFER_NAME_TEXT_COLOR, -1);
        setColor(ThemeElementTypes.VIDEO_ACTION_DIALOG_OFFER_DESCRIPTION_TEXT_COLOR, -1);
        setColor(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_HINT_TEXT_COLOR, Color.argb(200, 136, ParseException.ACCOUNT_ALREADY_LINKED, 249));
        setDrawable(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_HINT_BACKGROUND, new CustomShapeDrawable(Color.argb(200, 136, ParseException.ACCOUNT_ALREADY_LINKED, 249), 0, Color.argb(100, 0, 0, 0), 3.0f));
    }

    private void initAnimations() {
        Animation videoControlsShowAnimation = new AlphaAnimation(0.0f, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        videoControlsShowAnimation.setDuration(200);
        videoControlsShowAnimation.setFillAfter(true);
        setAnimation(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_SHOW_ANIMATION, videoControlsShowAnimation);
        Animation videoControlsHintShowAnimation = new AlphaAnimation(0.0f, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        videoControlsHintShowAnimation.setDuration(200);
        videoControlsHintShowAnimation.setFillAfter(true);
        setAnimation(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_HINT_SHOW_ANIMATION, videoControlsHintShowAnimation);
        Animation videoControlsHintHideAnimation = new AlphaAnimation(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, 0.0f);
        videoControlsHintHideAnimation.setDuration(400);
        videoControlsHintHideAnimation.setFillAfter(true);
        setAnimation(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_HINT_HIDE_ANIMATION, videoControlsHintHideAnimation);
        Animation videoControlsHideAnimation = new AlphaAnimation(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, 0.0f);
        videoControlsHideAnimation.setDuration(400);
        videoControlsHideAnimation.setFillAfter(true);
        setAnimation(ThemeElementTypes.VIDEO_PLAYER_CONTROLS_HIDE_ANIMATION, videoControlsHideAnimation);
    }

    private Shape createShape() {
        int roundEdge = 10;
        if (MonetizationSharedDataManager.getContext() != null) {
            roundEdge = DensityManager.getDIP(MonetizationSharedDataManager.getContext(), 10.0f);
        }
        return new RoundRectShape(new float[]{(float) (roundEdge - 1), (float) roundEdge, (float) (roundEdge - 1), (float) roundEdge, (float) (roundEdge - 1), (float) roundEdge, (float) (roundEdge - 1), (float) roundEdge}, null, null);
    }
}
