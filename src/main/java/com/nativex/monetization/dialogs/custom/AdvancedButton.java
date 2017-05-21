package com.nativex.monetization.dialogs.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import com.nativex.monetization.manager.DensityManager;
import com.nativex.monetization.theme.ThemeElementTypes;
import com.nativex.monetization.theme.ThemeManager;
import com.nativex.monetization.ui.DeviceScreenSize;
import com.nativex.monetization.ui.DeviceScreenSize.SCREEN_SIZE;
import com.nativex.network.volley.DefaultRetryPolicy;

public class AdvancedButton extends Button {
    private static final int MIN_HEIGHT = 60;
    private final OnFocusChangeListener buttonFocusListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if (AdvancedButton.this.state == STATE.DEPRESSED) {
                AdvancedButton.this.buttonDepressed();
            }
            if (AdvancedButton.this.userFocusListener != null) {
                AdvancedButton.this.userFocusListener.onFocusChange(v, hasFocus);
            }
        }
    };
    private final OnTouchListener buttonTouchListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                    AdvancedButton.this.buttonPressed();
                    break;
                case 2:
                    break;
                default:
                    AdvancedButton.this.buttonDepressed();
                    break;
            }
            if (AdvancedButton.this.userTouchListener != null) {
                return AdvancedButton.this.userTouchListener.onTouch(v, event);
            }
            return false;
        }
    };
    private Drawable depressedBackground;
    private final ShadowLayerAttributes depressedShadow = new ShadowLayerAttributes();
    private String depressedText = null;
    private int depressedTextColor = ViewCompat.MEASURED_STATE_MASK;
    private float depressedTextSize = 14.0f;
    private Drawable focusedBackground;
    private final ShadowLayerAttributes focusedShadow = new ShadowLayerAttributes();
    private String focusedText = null;
    private int focusedTextColor = ViewCompat.MEASURED_STATE_MASK;
    private float focusedTextSize = 14.0f;
    private Drawable pressedBackground;
    private final ShadowLayerAttributes pressedShadow = new ShadowLayerAttributes();
    private String pressedText = null;
    private int pressedTextColor = ViewCompat.MEASURED_STATE_MASK;
    private float pressedTextSize = 14.0f;
    private STATE state = null;
    private OnFocusChangeListener userFocusListener = null;
    private OnTouchListener userTouchListener = null;

    private enum STATE {
        PRESSED,
        DEPRESSED
    }

    private class ShadowLayerAttributes {
        int color;
        float dx;
        float dy;
        float radius;

        private ShadowLayerAttributes() {
            this.radius = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
            this.dx = 0.0f;
            this.dy = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
            this.color = -1;
        }

        public void setAttributes(float radius, float dx, float dy, int color) {
            this.radius = radius;
            this.dx = dx;
            this.dy = dy;
            this.color = color;
        }
    }

    private void buttonPressed() {
        this.state = STATE.PRESSED;
        setBackgroundDrawable(this.pressedBackground);
        setTextColor(this.pressedTextColor);
        setTextSize(this.pressedTextSize);
        setShadowLayer(this.pressedShadow.radius, this.pressedShadow.dx, this.pressedShadow.dy, this.pressedShadow.color);
        if (this.pressedText != null) {
            setText(this.pressedText);
        }
    }

    private void buttonDepressed() {
        this.state = STATE.DEPRESSED;
        if (isFocused()) {
            buttonFocused();
            return;
        }
        setBackgroundDrawable(this.depressedBackground);
        setTextColor(this.depressedTextColor);
        setTextSize(this.depressedTextSize);
        setShadowLayer(this.depressedShadow.radius, this.depressedShadow.dx, this.depressedShadow.dy, this.depressedShadow.color);
        if (this.depressedText != null) {
            setText(this.depressedText);
        }
    }

    private void buttonFocused() {
        setBackgroundDrawable(this.focusedBackground);
        setTextColor(this.focusedTextColor);
        setTextSize(this.focusedTextSize);
        setShadowLayer(this.focusedShadow.radius, this.focusedShadow.dx, this.focusedShadow.dy, this.focusedShadow.color);
        if (this.focusedText != null) {
            setText(this.focusedText);
        }
    }

    public AdvancedButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AdvancedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdvancedButton(Context context) {
        super(context);
        init();
    }

    void init() {
        this.depressedBackground = ThemeManager.getDrawable(ThemeElementTypes.MESSAGE_DIALOG_OK_BUTTON_BACKGROUND_NORMAL);
        this.pressedBackground = ThemeManager.getDrawable(ThemeElementTypes.MESSAGE_DIALOG_OK_BUTTON_BACKGROUND_PRESSED);
        this.focusedBackground = ThemeManager.getDrawable(ThemeElementTypes.MESSAGE_DIALOG_OK_BUTTON_BACKGROUND_NORMAL);
        super.setOnTouchListener(this.buttonTouchListener);
        super.setOnFocusChangeListener(this.buttonFocusListener);
        buttonDepressed();
        setMinHeight(DeviceScreenSize.getDeviceScreenSize() == SCREEN_SIZE.SMALL ? DensityManager.getDIP(getContext(), 50.0f) : 60);
    }

    public void setOnTouchListener(OnTouchListener l) {
        this.userTouchListener = l;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        this.userFocusListener = l;
    }

    public void release() {
        setBackgroundDrawable(null);
        setOnTouchListener(null);
        setOnClickListener(null);
        setText(null);
        this.pressedBackground = null;
        this.depressedBackground = null;
        this.focusedBackground = null;
    }

    public void setBackgroundPressedDrawable(Drawable drawable) {
        this.pressedBackground = drawable;
        if (this.state == STATE.PRESSED) {
            setBackgroundDrawable(drawable);
        }
    }

    public void setBackgroundDepressedDrawable(Drawable drawable) {
        this.focusedBackground = drawable;
        this.depressedBackground = drawable;
        if (this.state == STATE.DEPRESSED) {
            setBackgroundDrawable(drawable);
        }
    }

    public void setPressedAttributes(String text, int textColor, float textSize, Drawable drawable) {
        this.pressedText = text;
        this.pressedTextColor = textColor;
        this.pressedTextSize = textSize;
        this.pressedBackground = drawable;
        if (this.state == STATE.PRESSED) {
            buttonPressed();
        }
    }

    public void setDepressedAttributes(String text, int textColor, float textSize, Drawable drawable) {
        this.depressedText = text;
        this.depressedTextColor = textColor;
        this.depressedTextSize = textSize;
        this.depressedBackground = drawable;
        if (this.state == STATE.DEPRESSED) {
            buttonDepressed();
        }
    }

    public void setDepressedText(String text) {
        this.depressedText = text;
        if (this.state == STATE.DEPRESSED) {
            setText(text);
        }
    }

    public void setDepressedTextColor(int color) {
        this.depressedTextColor = color;
        if (this.state == STATE.DEPRESSED) {
            setTextColor(color);
        }
    }

    public void setDepressedTextSize(float size) {
        this.depressedTextSize = size;
        if (this.state == STATE.DEPRESSED) {
            setTextSize(size);
        }
    }

    public void setPressedTextSize(float size) {
        this.pressedTextSize = size;
        if (this.state == STATE.PRESSED) {
            setTextSize(size);
        }
    }

    public void setPressedText(String text) {
        this.pressedText = text;
        if (this.state == STATE.PRESSED) {
            setText(text);
        }
    }

    public void setPressedTextColor(int color) {
        this.pressedTextColor = color;
        if (this.state == STATE.PRESSED) {
            setTextColor(color);
        }
    }

    public void setPressedShadowLayer(float radius, float dx, float dy, int color) {
        this.pressedShadow.setAttributes(radius, dx, dy, color);
        if (this.state == STATE.PRESSED) {
            setShadowLayer(radius, dx, dy, color);
        }
    }

    public void setDepressedShadowLayer(float radius, float dx, float dy, int color) {
        this.depressedShadow.setAttributes(radius, dx, dy, color);
        if (this.state == STATE.DEPRESSED) {
            setShadowLayer(radius, dx, dy, color);
        }
    }

    public void setFocusedShadowLayer(float radius, float dx, float dy, int color) {
        this.focusedShadow.setAttributes(radius, dx, dy, color);
        if (this.state == STATE.DEPRESSED) {
            setShadowLayer(radius, dx, dy, color);
        }
    }

    public void setFocusedAttributes(String text, int textColor, float textSize, Drawable drawable) {
        this.focusedText = text;
        this.focusedTextColor = textColor;
        this.focusedTextSize = textSize;
        this.focusedBackground = drawable;
        if (isFocused()) {
            buttonFocused();
        }
    }

    public void setFocusedText(String text) {
        this.focusedText = text;
        if (isFocused()) {
            setText(text);
        }
    }

    public void setFocusedTextColor(int color) {
        this.focusedTextColor = color;
        if (isFocused()) {
            setTextColor(color);
        }
    }

    public void setFocusedTextSize(float size) {
        this.focusedTextSize = size;
        if (isFocused()) {
            setTextSize(size);
        }
    }
}
