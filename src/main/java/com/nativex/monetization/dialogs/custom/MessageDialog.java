package com.nativex.monetization.dialogs.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import com.nativex.monetization.theme.ThemeElementTypes;
import com.nativex.monetization.theme.ThemeManager;
import com.nativex.network.volley.DefaultRetryPolicy;

public class MessageDialog extends BaseDialog {
    private MessageDialogBody body;
    private final OnClickListener onClose = new OnClickListener() {
        public void onClick(View v) {
            MessageDialog.this.dismiss();
        }
    };
    private MessageDialogTitle title;
    private boolean userClickedOk = false;

    public /* bridge */ /* synthetic */ void onOrientationChange() {
        super.onOrientationChange();
    }

    public /* bridge */ /* synthetic */ void setDialogMaxHeight(int i) {
        super.setDialogMaxHeight(i);
    }

    public /* bridge */ /* synthetic */ void setDialogMaxWidth(int i) {
        super.setDialogMaxWidth(i);
    }

    public /* bridge */ /* synthetic */ void setDialogMinHeight(int i) {
        super.setDialogMinHeight(i);
    }

    public /* bridge */ /* synthetic */ void setDialogMinWidth(int i) {
        super.setDialogMinWidth(i);
    }

    public MessageDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.title = new MessageDialogTitle(getContext());
        this.body = new MessageDialogBody(getContext());
        addView(this.title);
        addView(this.body);
        setDialogBackground(ThemeManager.getDrawable(ThemeElementTypes.MESSAGE_DIALOG_BACKGROUND));
        this.title.setLayoutParams(new LayoutParams(-1, -2));
        this.title.setOnCloseClickListener(this.onClose);
        this.body.setButtonClickListener(this.onClose);
        this.body.setLayoutParams(new LayoutParams(-1, -2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void setOnButtonClickListener(OnClickListener listener) {
        this.body.setButtonClickListener(listener);
    }

    public void setOnCloseListener(OnClickListener listener) {
        this.title.setOnCloseClickListener(listener);
    }

    public void showDialogIcon(boolean show) {
        if (this.title != null) {
            this.title.showIcon(show);
        }
    }

    public void setTitle(String title) {
        this.title.setTitle(title);
    }

    public void setMessage(String message) {
        this.body.setText(message);
    }

    public void setButtonText(String text) {
        this.body.setButtonText(text);
    }

    public void setDialogIcon(String url) {
        this.title.setIcon(url);
    }

    public void setTextGravity(int gravity) {
        if (this.body != null) {
            this.body.setTextGravity(gravity);
        }
    }

    public void setDialogIcon(Drawable icon) {
        this.title.setIcon(icon);
    }

    void release() {
        if (this.title != null) {
            this.title.release();
            this.title = null;
        }
        if (this.body != null) {
            this.body.release();
            this.body = null;
        }
    }

    public void dismiss() {
        super.dismiss();
        release();
    }

    public void setUserClickedOk(boolean didClick) {
        this.userClickedOk = didClick;
    }

    public boolean didUserClickOk() {
        return this.userClickedOk;
    }
}
