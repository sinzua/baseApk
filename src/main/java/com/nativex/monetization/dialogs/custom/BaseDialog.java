package com.nativex.monetization.dialogs.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import com.nativex.common.Log;
import com.nativex.monetization.manager.DensityManager;
import com.nativex.network.volley.DefaultRetryPolicy;

class BaseDialog extends Dialog {
    private static final float DIALOG_MAX_SCREEN_PERCENTAGE = 0.9f;
    private DialogContent dialogContent;
    private int dialogMaxHeight = -2;
    private int dialogMaxWidth = 600;
    private int dialogMinHeight = -2;
    private int dialogMinWidth = 400;

    private class DialogContent extends LinearLayout {
        public DialogContent(Context context) {
            super(context);
            try {
                setWeightSum(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                setOrientation(1);
            } catch (Exception e) {
                Log.e("BaseDialog.DialogContent: Unexpected exception caught in DialogContent()");
                e.printStackTrace();
            }
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            try {
                int widthMode = MeasureSpec.getMode(widthMeasureSpec);
                int heightMode = MeasureSpec.getMode(heightMeasureSpec);
                int width = MeasureSpec.getSize(widthMeasureSpec);
                int height = MeasureSpec.getSize(heightMeasureSpec);
                DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
                int maxHeight = (int) (((float) metrics.heightPixels) * BaseDialog.DIALOG_MAX_SCREEN_PERCENTAGE);
                int maxWidth = (int) (((float) metrics.widthPixels) * BaseDialog.DIALOG_MAX_SCREEN_PERCENTAGE);
                BaseDialog.this.calcDialogMaxWidthAndHeight(maxHeight, maxWidth);
                int measuredWidth = widthMeasureSpec;
                int measuredHeight = heightMeasureSpec;
                switch (widthMode) {
                    case Integer.MIN_VALUE:
                    case 0:
                        if (BaseDialog.this.dialogMaxWidth <= 0) {
                            if (width > maxWidth) {
                                measuredWidth = MeasureSpec.makeMeasureSpec(maxWidth, Integer.MIN_VALUE);
                                break;
                            }
                        } else if (BaseDialog.this.dialogMaxWidth >= width) {
                            if (BaseDialog.this.dialogMinWidth > width) {
                                measuredWidth = MeasureSpec.makeMeasureSpec(BaseDialog.this.dialogMinWidth, 1073741824);
                                break;
                            }
                        } else {
                            measuredWidth = MeasureSpec.makeMeasureSpec(BaseDialog.this.dialogMaxWidth, Integer.MIN_VALUE);
                            break;
                        }
                        break;
                }
                switch (heightMode) {
                    case Integer.MIN_VALUE:
                    case 0:
                        if (BaseDialog.this.dialogMaxHeight <= 0) {
                            if (height > maxHeight) {
                                measuredHeight = MeasureSpec.makeMeasureSpec(maxHeight, Integer.MIN_VALUE);
                                break;
                            }
                        } else if (BaseDialog.this.dialogMaxHeight >= height) {
                            if (BaseDialog.this.dialogMinHeight > height) {
                                measuredHeight = MeasureSpec.makeMeasureSpec(BaseDialog.this.dialogMinHeight, 1073741824);
                                break;
                            }
                        } else {
                            measuredHeight = MeasureSpec.makeMeasureSpec(BaseDialog.this.dialogMaxHeight, Integer.MIN_VALUE);
                            break;
                        }
                        break;
                }
                super.onMeasure(measuredWidth, measuredHeight);
            } catch (Exception e) {
                Log.e("BaseDialog.DialogContent: Unexpected exception caught in onMeasure()");
                e.printStackTrace();
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }

        public void release() {
            removeAllViews();
        }
    }

    BaseDialog(Context context) {
        super(context);
        try {
            setupDialog();
        } catch (Exception e) {
            Log.e("BaseDialog: Unexpected exception caught in BaseDialog(Context)");
            e.printStackTrace();
            dismiss();
        }
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        try {
            setupDialog();
        } catch (Exception e) {
            Log.e("BaseDialog: Unexpected exception caught in BaseDialog(Context, int)");
            e.printStackTrace();
            dismiss();
        }
    }

    private void setupDialog() {
        requestWindowFeature(1);
        setCancelable(false);
        this.dialogContent = new DialogContent(getContext());
        LayoutParams params = getWindow().getAttributes();
        params.width = -2;
        params.height = -2;
        int dialogPadding = DensityManager.getDIP(getContext(), 5.0f);
        this.dialogContent.setPadding(dialogPadding, dialogPadding, dialogPadding, dialogPadding);
        setContentView(this.dialogContent, params);
    }

    void addView(View v) {
        try {
            this.dialogContent.addView(v);
        } catch (Exception e) {
            Log.e("BaseDialog: Unexpected exception caught in addView(View)", e);
            e.printStackTrace();
        }
    }

    protected void addView(View v, ViewGroup.LayoutParams params) {
        try {
            v.setLayoutParams(params);
            this.dialogContent.addView(v);
        } catch (Exception e) {
            Log.e("BaseDialog: Unexpected exception caught in addView(View, LayoutParams)", e);
            e.printStackTrace();
        }
    }

    public void setDialogMaxWidth(int dialogMaxWidth) {
        try {
            this.dialogMaxWidth = dialogMaxWidth;
            this.dialogContent.forceLayout();
            this.dialogContent.invalidate();
        } catch (Exception e) {
            Log.e("BaseDialog: Unexpected exception caught in setDialogMaxWidth()");
            e.printStackTrace();
        }
    }

    public void setDialogMinWidth(int dialogMinWidth) {
        try {
            this.dialogMinWidth = dialogMinWidth;
            this.dialogContent.forceLayout();
            this.dialogContent.invalidate();
        } catch (Exception e) {
            Log.e("BaseDialog: Unexpected exception caught in setDialogMinWidth()");
            e.printStackTrace();
        }
    }

    public void setDialogMinHeight(int dialogMinHeight) {
        try {
            this.dialogMinHeight = dialogMinHeight;
            this.dialogContent.forceLayout();
            this.dialogContent.invalidate();
        } catch (Exception e) {
            Log.e("BaseDialog: Unexpected exception caught in setDialogMinHeight()");
            e.printStackTrace();
        }
    }

    public void setDialogMaxHeight(int dialogMaxHeight) {
        try {
            this.dialogMaxHeight = dialogMaxHeight;
            this.dialogContent.forceLayout();
            this.dialogContent.invalidate();
        } catch (Exception e) {
            Log.e("BaseDialog: Unexpected exception caught in setDialogMaxHeight()");
            e.printStackTrace();
        }
    }

    private void calcDialogMaxWidthAndHeight(int maxHeight, int maxWidth) {
        try {
            if (this.dialogMaxHeight > 0) {
                if (maxHeight < this.dialogMaxHeight) {
                    this.dialogMaxHeight = maxHeight;
                }
                if (this.dialogMaxHeight < this.dialogMinHeight) {
                    this.dialogMinHeight = this.dialogMaxHeight;
                }
            }
            if (this.dialogMaxWidth > 0) {
                if (maxWidth < this.dialogMaxWidth) {
                    this.dialogMaxWidth = maxWidth;
                }
                if (this.dialogMaxWidth < this.dialogMinWidth) {
                    this.dialogMinWidth = this.dialogMaxWidth;
                }
            }
        } catch (Exception e) {
            Log.e("BaseDialog: Unexpected exception caught in calcDialogMaxWidthAndHeight()");
            e.printStackTrace();
        }
    }

    public void onOrientationChange() {
        try {
            this.dialogContent.forceLayout();
            this.dialogContent.invalidate();
        } catch (Exception e) {
            Log.e("BaseDialog: Unexpected exception caught in onOrientationChange()");
            e.printStackTrace();
        }
    }

    void setDialogBackground(Drawable background) {
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        if (this.dialogContent != null) {
            this.dialogContent.setBackgroundDrawable(background);
            int dialogPadding = DensityManager.getDIP(getContext(), 5.0f);
            this.dialogContent.setPadding(dialogPadding, dialogPadding, dialogPadding, dialogPadding);
        }
    }

    public void dismiss() {
        super.dismiss();
        if (this.dialogContent != null) {
            this.dialogContent.release();
        }
    }
}
