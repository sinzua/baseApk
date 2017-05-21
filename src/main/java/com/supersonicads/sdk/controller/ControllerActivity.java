package com.supersonicads.sdk.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.OrientationEventListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.supersonicads.sdk.agent.SupersonicAdsPublisherAgent;
import com.supersonicads.sdk.controller.SupersonicWebView.OnWebViewControllerChangeListener;
import com.supersonicads.sdk.controller.SupersonicWebView.State;
import com.supersonicads.sdk.data.AdUnitsState;
import com.supersonicads.sdk.data.SSAEnums.ProductType;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import com.supersonicads.sdk.utils.Logger;
import com.supersonicads.sdk.utils.SDKUtils;
import com.supersonicads.sdk.utils.SupersonicSharedPrefHelper;
import com.ty.followboom.helpers.TrackHelper;

public class ControllerActivity extends Activity implements OnWebViewControllerChangeListener, VideoEventsListener {
    private static final String TAG = ControllerActivity.class.getSimpleName();
    private static final int WEB_VIEW_VIEW_ID = 1;
    final LayoutParams MATCH_PARENT_LAYOUT_PARAMS = new LayoutParams(-1, -1);
    public int applicationRotation = -1;
    private boolean calledFromOnCreate = false;
    public int currentRequestedRotation = -1;
    private RelativeLayout mContainer;
    private int mOrientation;
    private OrientationManager mOrientationManager;
    public int mOrientationType = -1;
    private String mProductType;
    private AdUnitsState mState;
    private SupersonicWebView mWebViewController;
    private FrameLayout mWebViewFrameContainer;

    private class OrientationManager extends OrientationEventListener {
        public int currentOrientation = 1;
        public int defaultOrientation = 1;

        public OrientationManager(Context context, int rate) {
            super(context, rate);
            this.defaultOrientation = SDKUtils.getDeviceDefaultOrientation(context);
            this.currentOrientation = context.getResources().getConfiguration().orientation;
        }

        public void onOrientationChanged(int degrees) {
            if (degrees == -1) {
                ControllerActivity.this.setRequestedOrientation(ControllerActivity.this.mOrientation);
            } else if (degrees <= 45 || degrees > 315) {
                switch (ControllerActivity.this.mOrientationType) {
                    case 0:
                        if (this.defaultOrientation != 2) {
                            ControllerActivity.this.mOrientation = 1;
                            break;
                        } else {
                            ControllerActivity.this.mOrientation = 9;
                            break;
                        }
                    case 1:
                        ControllerActivity.this.mOrientation = 0;
                        break;
                    case 2:
                        if (this.defaultOrientation == 2) {
                            if (this.currentOrientation != 2) {
                                ControllerActivity.this.mOrientation = 0;
                                break;
                            } else {
                                ControllerActivity.this.mOrientation = 0;
                                break;
                            }
                        }
                        ControllerActivity.this.mOrientation = 1;
                        break;
                }
                ControllerActivity.this.setRequestedOrientation(ControllerActivity.this.mOrientation);
            } else if (degrees > 45 && degrees <= 135) {
                switch (ControllerActivity.this.mOrientationType) {
                    case 0:
                        ControllerActivity.this.mOrientation = 1;
                        break;
                    case 1:
                        ControllerActivity.this.mOrientation = 8;
                        break;
                    case 2:
                        if (this.defaultOrientation != 2) {
                            ControllerActivity.this.mOrientation = 8;
                            break;
                        } else {
                            ControllerActivity.this.mOrientation = 1;
                            break;
                        }
                }
                ControllerActivity.this.setRequestedOrientation(ControllerActivity.this.mOrientation);
            } else if (degrees > 135 && degrees <= 225) {
                switch (ControllerActivity.this.mOrientationType) {
                    case 0:
                        if (this.defaultOrientation != 2) {
                            ControllerActivity.this.mOrientation = 1;
                            break;
                        } else {
                            ControllerActivity.this.mOrientation = 9;
                            break;
                        }
                    case 1:
                        ControllerActivity.this.mOrientation = 8;
                        break;
                    case 2:
                        if (this.defaultOrientation == 2) {
                            if (this.currentOrientation != 2) {
                                ControllerActivity.this.mOrientation = 8;
                                break;
                            } else {
                                ControllerActivity.this.mOrientation = 8;
                                break;
                            }
                        }
                        ControllerActivity.this.mOrientation = 9;
                        break;
                }
                ControllerActivity.this.setRequestedOrientation(ControllerActivity.this.mOrientation);
            } else if (degrees > 225 && degrees <= 315) {
                switch (ControllerActivity.this.mOrientationType) {
                    case 0:
                        ControllerActivity.this.mOrientation = 9;
                        break;
                    case 1:
                        ControllerActivity.this.mOrientation = 0;
                        break;
                    case 2:
                        if (this.defaultOrientation == 2) {
                            if (this.currentOrientation != 2) {
                                ControllerActivity.this.mOrientation = 9;
                                break;
                            } else {
                                ControllerActivity.this.mOrientation = 9;
                                break;
                            }
                        }
                        ControllerActivity.this.mOrientation = 0;
                        break;
                }
                ControllerActivity.this.setRequestedOrientation(ControllerActivity.this.mOrientation);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(TAG, "onCreate");
        hideActivityTitle();
        hideActivtiyStatusBar();
        this.mWebViewController = SupersonicAdsPublisherAgent.getInstance(this).getWebViewController();
        this.mWebViewController.setId(1);
        this.mWebViewController.setOnWebViewControllerChangeListener(this);
        this.mProductType = getIntent().getStringExtra(ParametersKeys.PRODUCT_TYPE);
        if (!TextUtils.isEmpty(this.mProductType) && ProductType.OfferWall.toString().equalsIgnoreCase(this.mProductType)) {
            if (savedInstanceState != null) {
                AdUnitsState state = (AdUnitsState) savedInstanceState.getParcelable("state");
                if (state != null) {
                    this.mState = state;
                    this.mWebViewController.restoreState(state);
                }
                finish();
            } else {
                this.mState = this.mWebViewController.getSavedState();
            }
        }
        if (!TextUtils.isEmpty(this.mProductType) && ProductType.BrandConnect.toString().equalsIgnoreCase(this.mProductType)) {
            this.mWebViewController.setVideoEventsListener(this);
        }
        this.mContainer = new RelativeLayout(this);
        setContentView(this.mContainer, this.MATCH_PARENT_LAYOUT_PARAMS);
        this.mWebViewFrameContainer = this.mWebViewController.getLayout();
        if (this.mContainer.findViewById(1) == null && this.mWebViewFrameContainer.getParent() != null) {
            this.calledFromOnCreate = true;
            finish();
        }
        initOrientationManager();
    }

    private void initOrientationManager() {
        this.mOrientationManager = new OrientationManager(this, 3);
        Intent intent = getIntent();
        handleOrientationState(intent.getStringExtra(ParametersKeys.ORIENTATION_SET_FLAG), intent.getIntExtra(ParametersKeys.ROTATION_SET_FLAG, 0));
    }

    private void handleOrientationState(String orientation, int rotation) {
        if (orientation == null) {
            return;
        }
        if (ParametersKeys.ORIENTATION_LANDSCAPE.equalsIgnoreCase(orientation)) {
            setInitiateLandscapeOrientation();
            setOrientationEventListener(1);
        } else if (ParametersKeys.ORIENTATION_PORTRAIT.equalsIgnoreCase(orientation)) {
            setInitiatePortraitOrientation();
            setOrientationEventListener(0);
        } else if (ParametersKeys.ORIENTATION_APPLICATION.equalsIgnoreCase(orientation)) {
            setOrientationEventListener(2);
        } else if (!ParametersKeys.ORIENTATION_DEVICE.equalsIgnoreCase(orientation)) {
        } else {
            if (isDeviceOrientationLocked()) {
                setRequestedOrientation(1);
            } else {
                setOrientationEventListener(2);
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(this.mProductType) && ProductType.OfferWall.toString().equalsIgnoreCase(this.mProductType)) {
            this.mState.setShouldRestore(true);
            outState.putParcelable("state", this.mState);
        }
    }

    protected void onResume() {
        super.onResume();
        Logger.i(TAG, "onResume");
        this.mContainer.addView(this.mWebViewFrameContainer, this.MATCH_PARENT_LAYOUT_PARAMS);
        if (this.mWebViewController != null) {
            this.mWebViewController.registerConnectionReceiver(this);
            this.mWebViewController.resume();
            this.mWebViewController.viewableChange(true, ParametersKeys.MAIN);
        }
        if (this.mOrientationManager != null && this.mOrientationManager.canDetectOrientation()) {
            this.mOrientationManager.enable();
        }
        ((AudioManager) getSystemService("audio")).requestAudioFocus(null, 3, 2);
    }

    protected void onPause() {
        super.onPause();
        Logger.i(TAG, "onPause");
        ((AudioManager) getSystemService("audio")).abandonAudioFocus(null);
        if (this.mWebViewController != null) {
            this.mWebViewController.unregisterConnectionReceiver(this);
            this.mWebViewController.pause();
            this.mWebViewController.viewableChange(false, ParametersKeys.MAIN);
        }
        removeWebViewContainerView();
        if (this.mOrientationManager != null) {
            this.mOrientationManager.disable();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        Logger.i(TAG, "onDestroy");
        if (this.calledFromOnCreate) {
            removeWebViewContainerView();
        }
        if (this.mWebViewController != null) {
            this.mWebViewController.setState(State.Gone);
            this.mWebViewController.removeVideoEventsListener();
        }
    }

    private void removeWebViewContainerView() {
        if (this.mContainer != null) {
            ViewGroup parent = (ViewGroup) this.mWebViewFrameContainer.getParent();
            if (parent.findViewById(1) != null) {
                parent.removeView(this.mWebViewFrameContainer);
            }
        }
    }

    public void onBackPressed() {
        Logger.i(TAG, "onBackPressed");
        switch (SupersonicSharedPrefHelper.getSupersonicPrefHelper().getBackButtonState()) {
            case Device:
                super.onBackPressed();
                return;
            case Controller:
                if (this.mWebViewController != null) {
                    this.mWebViewController.nativeNavigationPressed(TrackHelper.ACTION_BACK);
                    return;
                }
                return;
            default:
                return;
        }
    }

    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Logger.i(TAG, "onUserLeaveHint");
    }

    private void hideActivityTitle() {
        requestWindowFeature(1);
    }

    private void hideActivtiyStatusBar() {
        getWindow().setFlags(1024, 1024);
    }

    private void keepScreenOn() {
        runOnUiThread(new Runnable() {
            public void run() {
                ControllerActivity.this.getWindow().addFlags(128);
            }
        });
    }

    private void cancelScreenOn() {
        runOnUiThread(new Runnable() {
            public void run() {
                ControllerActivity.this.getWindow().clearFlags(128);
            }
        });
    }

    private boolean isDeviceOrientationLocked() {
        if (System.getInt(getContentResolver(), "accelerometer_rotation", 0) == 1) {
            return false;
        }
        return true;
    }

    private void setInitiateLandscapeOrientation() {
        int rotation = ((WindowManager) getSystemService("window")).getDefaultDisplay().getRotation();
        Logger.i(TAG, "setInitiateLandscapeOrientation");
        if (rotation == 0) {
            Logger.i(TAG, "ROTATION_0");
            this.mOrientation = 0;
            setRequestedOrientation(0);
        } else if (rotation == 2) {
            Logger.i(TAG, "ROTATION_180");
            this.mOrientation = 8;
            setRequestedOrientation(8);
        } else if (rotation == 3) {
            Logger.i(TAG, "ROTATION_270 Right Landscape");
            this.mOrientation = 8;
            setRequestedOrientation(8);
        } else if (rotation == 1) {
            Logger.i(TAG, "ROTATION_90 Left Landscape");
            this.mOrientation = 0;
            setRequestedOrientation(0);
        } else {
            Logger.i(TAG, "No Rotation");
        }
    }

    private void setInitiatePortraitOrientation() {
        int rotation = SDKUtils.getRotation(this);
        Logger.i(TAG, "setInitiatePortraitOrientation");
        if (rotation == 0) {
            Logger.i(TAG, "ROTATION_0");
            this.mOrientation = 1;
            setRequestedOrientation(1);
        } else if (rotation == 2) {
            Logger.i(TAG, "ROTATION_180");
            this.mOrientation = 9;
            setRequestedOrientation(9);
        } else if (rotation == 1) {
            Logger.i(TAG, "ROTATION_270 Right Landscape");
            this.mOrientation = 1;
            setRequestedOrientation(1);
        } else if (rotation == 3) {
            Logger.i(TAG, "ROTATION_90 Left Landscape");
            this.mOrientation = 1;
            setRequestedOrientation(1);
        } else {
            Logger.i(TAG, "No Rotation");
        }
    }

    private void setRotation(int rotation) {
        if (rotation == 0) {
            Logger.i(TAG, "ROTATION_0");
            this.mOrientation = 1;
            setRequestedOrientation(1);
        } else if (rotation == 2) {
            Logger.i(TAG, "ROTATION_180");
            this.mOrientation = 9;
            setRequestedOrientation(9);
        } else if (rotation == 3) {
            Logger.i(TAG, "ROTATION_270 Right Landscape");
            this.mOrientation = 8;
            setRequestedOrientation(8);
        } else if (rotation == 1) {
            Logger.i(TAG, "ROTATION_90 Left Landscape");
            this.mOrientation = 0;
            setRequestedOrientation(0);
        } else {
            Logger.i(TAG, "No Rotation");
        }
    }

    private void setOrientationEventListener(int orientationType) {
        this.mOrientationType = orientationType;
        Logger.i(TAG, "setOrientationEventListener(" + orientationType + ")");
    }

    public void onHide() {
        runOnUiThread(new Runnable() {
            public void run() {
                ControllerActivity.this.finish();
            }
        });
    }

    public void onSetOrientationCalled(String orientation, int rotation) {
        handleOrientationState(orientation, rotation);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !this.mWebViewController.inCustomView()) {
            return super.onKeyDown(keyCode, event);
        }
        this.mWebViewController.hideCustomView();
        return true;
    }

    public void setRequestedOrientation(int requestedOrientation) {
        if (this.currentRequestedRotation != requestedOrientation) {
            Logger.i(TAG, "Rotation: Req = " + requestedOrientation + " Curr = " + this.currentRequestedRotation);
            this.currentRequestedRotation = requestedOrientation;
            super.setRequestedOrientation(requestedOrientation);
        }
    }

    public void onVideoStarted() {
        toggleKeepScreen(true);
    }

    public void onVideoPaused() {
        toggleKeepScreen(false);
    }

    public void onVideoResumed() {
        toggleKeepScreen(true);
    }

    public void onVideoEnded() {
        toggleKeepScreen(false);
    }

    public void onVideoStopped() {
        toggleKeepScreen(false);
    }

    public void toggleKeepScreen(boolean screenOn) {
        if (screenOn) {
            keepScreenOn();
        } else {
            cancelScreenOn();
        }
    }
}
