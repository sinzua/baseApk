package com.nativex.monetization.dialogs.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.nativex.common.Log;
import com.nativex.monetization.custom.views.ScrollingTextView;
import com.nativex.monetization.theme.ThemeElementTypes;
import com.nativex.monetization.theme.ThemeManager;
import com.nativex.network.volley.toolbox.NetworkImageView;
import com.nativex.volleytoolbox.NativeXVolley;

public class MessageDialogTitle extends RelativeLayout {
    private static final int DEFAULT_CLOSE_BUTTON_SIZE = 30;
    private static final int DEFAULT_ICON_SIZE = 24;
    private static final int DEFAULT_TITLE_TEXT_SIZE = 16;
    @IdRes
    public static final int ID_CLOSE = 4234;
    @IdRes
    private static final int ID_ICON = 4236;
    @IdRes
    private static final int ID_TITLE = 4235;
    private ImageView closeButton;
    private NetworkImageView icon;
    private ScrollingTextView titleText;

    public MessageDialogTitle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MessageDialogTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessageDialogTitle(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.titleText = new ScrollingTextView(getContext());
        this.closeButton = new ImageView(getContext());
        this.icon = new NetworkImageView(getContext());
        this.titleText.setId(ID_TITLE);
        this.closeButton.setId(ID_CLOSE);
        this.icon.setId(ID_ICON);
        addView(this.icon);
        addView(this.titleText);
        addView(this.closeButton);
        setupViews();
    }

    private void setupViews() {
        LayoutParams params = new LayoutParams(-2, -2);
        params.addRule(1, ID_ICON);
        params.addRule(0, ID_CLOSE);
        this.titleText.setLayoutParams(params);
        this.titleText.setPadding(10, 10, 10, 10);
        this.titleText.setTypeface(Typeface.DEFAULT_BOLD);
        this.titleText.setTextSize(16.0f);
        this.titleText.setTextColor(ThemeManager.getColor(ThemeElementTypes.MESSAGE_DIALOG_TITLE_TEXT_COLOR).intValue());
        params = new LayoutParams(30, 30);
        params.addRule(10);
        params.addRule(11);
        params.setMargins(0, 5, 5, 0);
        this.closeButton.setLayoutParams(params);
        this.closeButton.setImageDrawable(ThemeManager.getDrawable(ThemeElementTypes.MESSAGE_DIALOG_CLOSE_BUTTON_BACKGROUND));
        params = new LayoutParams(24, 24);
        params.addRule(9);
        params.addRule(15);
        params.setMargins(20, 10, 10, 10);
        this.icon.setLayoutParams(params);
    }

    public void showIcon(boolean show) {
        if (this.icon == null) {
            return;
        }
        if (show) {
            this.icon.setVisibility(0);
            if (this.titleText != null) {
                LayoutParams params = new LayoutParams(-2, -2);
                params.addRule(1, ID_ICON);
                params.addRule(0, ID_CLOSE);
                this.titleText.setLayoutParams(params);
                return;
            }
            return;
        }
        this.icon.setVisibility(8);
        if (this.titleText != null) {
            params = new LayoutParams(-2, -2);
            params.addRule(9);
            params.addRule(0, ID_CLOSE);
            this.titleText.setLayoutParams(params);
        }
    }

    public void setOnCloseClickListener(OnClickListener listener) {
        try {
            if (this.closeButton != null) {
                this.closeButton.setOnClickListener(listener);
            }
        } catch (Exception e) {
            Log.e("FeaturedDialogTitle: Unexpected exception caught in setOnCloseClickListener().", e);
            e.printStackTrace();
        }
    }

    public void setTitle(String text) {
        try {
            if (this.titleText != null) {
                this.titleText.setText(text);
            }
        } catch (Exception e) {
            Log.e("FeaturedDialogTitle: Unexpected exception caught in setTitle().", e);
            e.printStackTrace();
        }
    }

    public void setIcon(String url) {
        this.icon.setImageUrl(url, NativeXVolley.getInstance().getImageLoader());
    }

    public void setIcon(Drawable icon) {
        this.icon.setImageDrawable(icon);
    }

    public void release() {
        if (this.closeButton != null) {
            this.closeButton.setOnClickListener(null);
            this.closeButton.setImageDrawable(null);
            this.closeButton = null;
        }
        if (this.icon != null) {
            this.icon.setImageDrawable(null);
            this.icon = null;
        }
        this.titleText = null;
        removeAllViews();
    }
}
