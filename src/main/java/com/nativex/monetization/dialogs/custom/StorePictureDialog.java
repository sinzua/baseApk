package com.nativex.monetization.dialogs.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import com.nativex.monetization.theme.ThemeElementTypes;
import com.nativex.monetization.theme.ThemeManager;
import com.nativex.network.volley.DefaultRetryPolicy;

public class StorePictureDialog extends BaseDialog {
    private StorePictureDialogBody body;
    private final OnClickListener onClose = new OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == StorePictureDialogBody.ID_BODY_BUTTON) {
                if (StorePictureDialog.this.userButtonListener != null) {
                    StorePictureDialog.this.userButtonListener.onClick(v);
                }
            } else if (v.getId() == MessageDialogTitle.ID_CLOSE && StorePictureDialog.this.userCloseListener != null) {
                StorePictureDialog.this.userCloseListener.onClick(v);
            }
            StorePictureDialog.this.dismiss();
        }
    };
    private OnClickListener userButtonListener;
    private OnClickListener userCloseListener;

    public /* bridge */ /* synthetic */ void dismiss() {
        super.dismiss();
    }

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

    public StorePictureDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        MessageDialogTitle title = new MessageDialogTitle(getContext());
        this.body = new StorePictureDialogBody(getContext());
        addView(title);
        addView(this.body);
        setDialogBackground(ThemeManager.getDrawable(ThemeElementTypes.MESSAGE_DIALOG_BACKGROUND));
        title.setLayoutParams(new LayoutParams(-1, -2));
        title.setOnCloseClickListener(this.onClose);
        this.body.setButtonClickListener(this.onClose);
        this.body.setLayoutParams(new LayoutParams(-1, -2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        title.setTitle("Confirm storing picture");
        this.body.setText("You are requested to store a picture on the device' SDCard. Do you wish to proceed?");
        this.body.setButtonText("Proceed");
    }

    public void setImage(Bitmap image) {
        if (this.body != null) {
            this.body.setImage(image);
        }
    }

    public void setOnButtonClickListener(OnClickListener listener) {
        this.userButtonListener = listener;
    }

    public void setOnCloseClickListener(OnClickListener listener) {
        this.userCloseListener = listener;
    }
}
