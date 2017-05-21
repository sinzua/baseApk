package com.nativex.monetization.mraid;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import com.nativex.common.Log;
import com.nativex.monetization.enums.AdEvent;

class MRAIDDialog extends Dialog {
    private static final int MAX_ATTACH_RETRIES = 20;
    private int _visibility = 0;
    private int attachCount = 0;
    private MRAIDContainer container;
    private View detachDetector;
    private boolean dismissed = false;
    private Display display;
    private final OnDismissListener onDismiss = new OnDismissListener() {
        public void onDismiss(DialogInterface dialog) {
            try {
                MRAIDDialog.this.container.detachFromParent();
                MRAIDDialog.this.container = null;
                MRAIDLogger.i("The WebView is detached from its parent");
            } catch (Exception e) {
                MRAIDLogger.e("The WebView failed to detach from its parent", e);
            }
        }
    };
    private ViewGroup rootGroup;

    private class RootGroup extends ViewGroup {
        private DisplayMetrics _metrics = new DisplayMetrics();

        public RootGroup(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public RootGroup(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public RootGroup(Context context) {
            super(context);
        }

        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            if (getChildCount() > 0) {
                getChildAt(0).layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
            }
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            if (VERSION.SDK_INT < 19 || MRAIDDialog.this._visibility <= 0) {
                MRAIDDialog.this.display.getMetrics(this._metrics);
            } else {
                MRAIDDialog.this.display.getRealMetrics(this._metrics);
            }
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(this._metrics.widthPixels, 1073741824);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(this._metrics.heightPixels, 1073741824);
            if (getChildCount() > 0) {
                getChildAt(0).measure(widthMeasureSpec, heightMeasureSpec);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public MRAIDDialog(Activity context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public MRAIDDialog(Activity context, int theme) {
        super(context, theme);
        init(context);
    }

    public MRAIDDialog(Activity context) {
        super(context);
        init(context);
    }

    private void init(Activity context) {
        getWindow().requestFeature(1);
        getWindow().addFlags(1024);
        getWindow().addFlags(256);
        getWindow().clearFlags(2);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        LayoutParams attributes = getWindow().getAttributes();
        this.display = context.getWindowManager().getDefaultDisplay();
        getWindow().setAttributes(attributes);
        setCancelable(false);
        getWindow().setWindowAnimations(16973824);
        setDetachDetectorView(context);
        this.rootGroup = new RootGroup(context);
        setContentView(this.rootGroup);
        this._visibility = context.getWindow().getDecorView().getSystemUiVisibility();
        MRAIDLogger.i("Dialog created");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(-1, -1);
    }

    private void setDetachDetectorView(Activity context) {
        this.detachDetector = new View(context) {
            protected void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                MRAIDDialog.this.dismiss();
            }
        };
        this.detachDetector.setClickable(false);
        this.detachDetector.setEnabled(false);
        this.detachDetector.setWillNotDraw(true);
        this.detachDetector.setVisibility(8);
        context.addContentView(this.detachDetector, new ViewGroup.LayoutParams(1, 1));
        setOnDismissListener(this.onDismiss);
    }

    public void setContainer(MRAIDContainer container) {
        this.container = container;
        setDialogContent();
    }

    private void setDialogContent() {
        if (this.container != null) {
            if (this.container.getParent() == null) {
                if (MRAIDManager.isRunningOnUnity() && VERSION.SDK_INT <= 15) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        MRAIDLogger.e("catch exception in sleep routine...", e);
                        e.printStackTrace();
                    }
                }
                this.rootGroup.addView(this.container, new ViewGroup.LayoutParams(-1, -1));
                MRAIDLogger.i("The WebView is ready and the dialog is shown");
            } else if (this.attachCount >= 20) {
                this.container.fireErrorEvent("Failed to attach the ad to the view hierarchy.", null, null);
                this.container.fireListener(AdEvent.ERROR, "Failed to attach the ad to the view hierarchy");
                MRAIDManager.releaseAd(this.container);
            } else {
                this.attachCount++;
                MRAIDContainer.handler.postDelayed(new Runnable() {
                    public void run() {
                        try {
                            MRAIDDialog.this.setDialogContent();
                        } catch (Exception e) {
                            Log.e("Unhandled exception", e);
                        }
                    }
                }, 100);
                MRAIDLogger.i("The WebView is still attached to a parent. Retry " + this.attachCount);
            }
        }
    }

    public void show() {
        getWindow().setFlags(8, 8);
        super.show();
        if (VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(this._visibility);
        }
        getWindow().clearFlags(8);
        if (this.container != null) {
            this.container.setVisibility(0);
        }
        getWindow().addFlags(128);
    }

    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
        MRAIDManager.releaseAd(this.container, false);
    }

    public synchronized void dismiss() {
        if (!this.dismissed) {
            this.dismissed = true;
            getWindow().clearFlags(128);
            if (this.container != null) {
                this.container.dialogDismissed();
            }
            if (this.detachDetector != null) {
                try {
                    ((ViewGroup) this.detachDetector.getParent()).removeView(this.detachDetector);
                } catch (Exception e) {
                }
            }
            super.dismiss();
            MRAIDLogger.i("Dialog dismiss called");
        }
    }
}
