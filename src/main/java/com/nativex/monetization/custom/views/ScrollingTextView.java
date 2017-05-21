package com.nativex.monetization.custom.views;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

public class ScrollingTextView extends TextView {
    public ScrollingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupView();
    }

    public ScrollingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    public ScrollingTextView(Context context) {
        super(context);
        setupView();
    }

    private void setupView() {
        setSingleLine();
        setEllipsize(TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
    }

    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    public void onWindowFocusChanged(boolean focused) {
        if (focused) {
            super.onWindowFocusChanged(true);
        }
    }

    public boolean isFocused() {
        return true;
    }
}
