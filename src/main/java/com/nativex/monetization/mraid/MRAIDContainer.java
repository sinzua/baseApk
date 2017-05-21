package com.nativex.monetization.mraid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.webkit.JsResult;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.google.gson.Gson;
import com.nativex.common.JsonRequestConstants.VideoPlayerOptions;
import com.nativex.common.Log;
import com.nativex.common.Utilities;
import com.nativex.monetization.activities.InterstitialActivity;
import com.nativex.monetization.enums.AdEvent;
import com.nativex.monetization.enums.BannerPosition;
import com.nativex.monetization.enums.VideoProgress;
import com.nativex.monetization.listeners.OnAdEvent;
import com.nativex.monetization.listeners.OnAdEventBase;
import com.nativex.monetization.listeners.OnAdEventV2;
import com.nativex.monetization.manager.ActivityManager;
import com.nativex.monetization.manager.DensityManager;
import com.nativex.monetization.manager.SessionManager;
import com.nativex.monetization.mraid.MRAIDUtils.ClosePosition;
import com.nativex.monetization.mraid.MRAIDUtils.Events;
import com.nativex.monetization.mraid.MRAIDUtils.Features;
import com.nativex.monetization.mraid.MRAIDUtils.JSCommands;
import com.nativex.monetization.mraid.MRAIDUtils.JSDialogAction;
import com.nativex.monetization.mraid.MRAIDUtils.NativeXEvents;
import com.nativex.monetization.mraid.MRAIDUtils.Orientation;
import com.nativex.monetization.mraid.MRAIDUtils.PlacementType;
import com.nativex.monetization.mraid.objects.CalendarEntryData;
import com.nativex.monetization.mraid.objects.CurrentPosition;
import com.nativex.monetization.mraid.objects.DefaultPosition;
import com.nativex.monetization.mraid.objects.ExpandProperties;
import com.nativex.monetization.mraid.objects.MaxSize;
import com.nativex.monetization.mraid.objects.OrientationProperties;
import com.nativex.monetization.mraid.objects.ResizeProperties;
import com.nativex.monetization.mraid.objects.ScreenSize;
import com.nativex.monetization.theme.ThemeElementTypes;
import com.nativex.monetization.theme.ThemeManager;
import com.nativex.network.volley.DefaultRetryPolicy;
import com.nativex.videoplayer.NativeXVideoPlayer;
import com.nativex.videoplayer.VideoPlayer.Options;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class MRAIDContainer extends ViewGroup {
    private static final long DELAY_BRING_TO_FRONT = 100;
    private static final long DELAY_REATTACH = 40;
    private static final String MRAID_INTERFACE_NAME = "nativeXSDK";
    private static final int MSG_BRING_TO_FRONT = 1000;
    private static final int MSG_CLOSE_ANIMATION_END = 1003;
    private static final long MSG_CLOSE_ANIMATION_MIN_DURATION = 500;
    private static final int MSG_LOAD_AD_DATA = 1002;
    private static final int MSG_REATTACH = 1001;
    private static final int REATTACH_RETRY_COUNT = 20;
    private static int closeRegionSize;
    static final MRAIDContainerInnerHandler handler = new MRAIDContainerInnerHandler(Looper.getMainLooper());
    private Activity activity;
    private String activityClassName;
    private boolean adConverted = false;
    private boolean adExpired = false;
    private boolean adFinishedLoading = false;
    private AdInfo adInfo = new AdInfo();
    private boolean adIsCached = false;
    private boolean adIsPending = true;
    private String adName;
    private JSIAdToDevice adToDeviceInterface;
    private int attemptedReleaseCount = 0;
    private BannerPosition bannerPosition = null;
    private boolean closeIndicatorIsVisible = true;
    private ClosePosition closePosition = ClosePosition.TOP_RIGHT;
    private boolean closePositionChanged = true;
    private Rect closeRect;
    private ImageView closeRegion;
    private boolean closeRegionIsEnabled = true;
    private String containerName;
    private CurrentPosition currentPosition;
    private int defaultActivityOrientation;
    private DefaultPosition defaultPosition;
    private JSIDeviceToAd deviceToAdHandler;
    private MRAIDDialog dialog = null;
    private boolean didFireDisplayListener = false;
    private ExpandProperties expandProperties;
    private AlphaAnimation hideAlphaAnimation;
    private int lastEventLevel = 0;
    private MaxSize maxSize;
    private Set<String> md5usedInThisAd = null;
    private boolean mraidLoaded = false;
    private int oldHeight = 0;
    private int oldWidth = 0;
    private final OnClickListener onCloseClicked = new OnClickListener() {
        public void onClick(View v) {
            if (MRAIDContainer.this.closeRegionIsEnabled || MRAIDContainer.this.closeIndicatorIsVisible) {
                MRAIDContainer.this.close();
            }
        }
    };
    private OnAdEventBase onRichMediaEvent;
    private OrientationProperties orientationProperties;
    private PlacementType placementType = PlacementType.INLINE;
    private AdPosition position;
    private boolean released = false;
    private ResizeProperties resizeProperties;
    private MRAIDSchemeHandler schemeHandler;
    private MRAIDWebView secondPartWebView = null;
    boolean shouldHaltVideo = true;
    private AlphaAnimation showAlphaAnimation;
    private States state = States.LOADING;
    private final Rect tempRect = new Rect();
    private String url;
    private boolean videoAutoPlay = false;
    private String videoOptionsJSON = null;
    private boolean wasDisplayFired = false;
    private MRAIDWebView webView;
    private boolean willCloseAdOnRedirect = true;
    private List<MRAIDDialogWorker> workers;

    static class MRAIDContainerInnerHandler extends Handler {
        public MRAIDContainerInnerHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            MRAIDContainer container = null;
            if (msg.obj != null && (msg.obj instanceof MRAIDContainer)) {
                container = msg.obj;
            }
            if (container != null) {
                switch (msg.what) {
                    case 1000:
                        container.bringToFront();
                        return;
                    case MRAIDContainer.MSG_REATTACH /*1001*/:
                        container.attachToCurrentActivity(msg.arg1 + 1);
                        return;
                    case MRAIDContainer.MSG_LOAD_AD_DATA /*1002*/:
                        container.loadData(msg.arg1);
                        return;
                    case MRAIDContainer.MSG_CLOSE_ANIMATION_END /*1003*/:
                        container.onCloseAnimationEnd();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public MRAIDContainer(Activity context, AttributeSet attrs, int defStyle) {
        super(context.getApplicationContext(), attrs, defStyle);
        init(context);
    }

    public MRAIDContainer(Activity context, AttributeSet attrs) {
        super(context.getApplicationContext(), attrs);
        init(context);
    }

    public MRAIDContainer(Activity context, String url) {
        super(context.getApplicationContext());
        init(context);
        loadAdUrl(url);
    }

    public MRAIDContainer(Activity context) {
        super(context.getApplicationContext());
        init(context);
    }

    public MRAIDContainer(Context context) {
        super(context);
    }

    boolean isAdCached() {
        return this.adIsCached;
    }

    void setAdCached(boolean isCached) {
        this.adIsCached = isCached;
        if (!isCached && this.adFinishedLoading) {
            if (MRAIDManager.isRunningOnUnity() && this.placementType == PlacementType.INTERSTITIAL) {
                createDialog();
                showDialog();
                return;
            }
            setVisibility(0);
        }
    }

    boolean isAdExpired() {
        return this.adExpired;
    }

    void setAdExpired(boolean isExpired) {
        this.adExpired = isExpired;
    }

    private void init(Activity context) {
        setActivity(context);
        setWillNotDraw(false);
        this.defaultActivityOrientation = context.getRequestedOrientation();
        setBackgroundColor(MRAIDWebView.MODAL_BACKGROUND);
        JSIAdToDeviceHandler adToDeviceHandler = new JSIAdToDeviceHandler(this);
        this.schemeHandler = new MRAIDSchemeHandler(adToDeviceHandler);
        this.adToDeviceInterface = new JSIAdToDevice(adToDeviceHandler);
        this.deviceToAdHandler = new JSIDeviceToAd(this);
        this.workers = new ArrayList();
        this.position = new AdPosition(null);
        this.maxSize = new MaxSize();
        this.expandProperties = new ExpandProperties();
        this.expandProperties.setUseCustomClose(Boolean.valueOf(false));
        this.currentPosition = new CurrentPosition();
        this.closeRegion = new ImageView(context.getApplicationContext());
        addView(this.closeRegion);
        this.closeRect = new Rect();
        this.closeRegion.setClickable(true);
        this.closeRegion.setOnClickListener(this.onCloseClicked);
        addCloseRegion();
        this.showAlphaAnimation = new AlphaAnimation(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        this.showAlphaAnimation.setDuration(0);
        this.showAlphaAnimation.setFillAfter(true);
        this.hideAlphaAnimation = new AlphaAnimation(0.0f, 0.0f);
        this.hideAlphaAnimation.setDuration(0);
        this.hideAlphaAnimation.setFillAfter(true);
        setVisibility(8);
        BannerPosition.initialize(context);
    }

    public boolean isAdPending() {
        return this.adIsPending;
    }

    @SuppressLint({"AddJavascriptInterface"})
    MRAIDWebView createWebView() {
        MRAIDWebView webView = new MRAIDWebView(this.activity);
        webView.setSchemeHandler(this.schemeHandler);
        webView.addJavascriptInterface(this.adToDeviceInterface, MRAID_INTERFACE_NAME);
        webView.setContainer(this);
        return webView;
    }

    void hideCloseIndicator() {
        this.closeIndicatorIsVisible = false;
        this.closeRegion.setImageDrawable(null);
        this.closeRegion.setBackgroundDrawable(null);
    }

    void showCloseIndicator() {
        if (this.expandProperties.getUseCustomClose().booleanValue()) {
            hideCloseIndicator();
            return;
        }
        changeCloseIndicatorBackground();
        this.closeRegion.setClickable(true);
        this.closeIndicatorIsVisible = true;
    }

    private void addCloseRegion() {
        if (this.placementType == PlacementType.INLINE) {
            switch (this.state) {
                case RESIZED:
                    hideCloseIndicator();
                    this.closeRegion.setVisibility(0);
                    break;
                case EXPANDED:
                    showCloseIndicator();
                    this.closeRegion.setVisibility(0);
                    break;
                default:
                    this.closeRegion.setVisibility(8);
                    break;
            }
        }
        this.closeRegion.setVisibility(0);
        showCloseIndicator();
        bringChildToFront(this.closeRegion);
    }

    private void removeCloseRegion() {
        if (this.placementType == PlacementType.INLINE) {
            switch (this.state) {
                case RESIZED:
                case EXPANDED:
                    return;
                default:
                    this.closeRegion.setVisibility(8);
                    return;
            }
        }
    }

    private void changeCloseIndicatorBackground() {
        Drawable closeDrawable = null;
        if (this.state != States.RESIZED) {
            closeDrawable = ThemeManager.getDrawable(this.closePosition.getThemeElementType(), false);
            if (closeDrawable == null) {
                closeDrawable = ThemeManager.getDrawable(ThemeElementTypes.MRAID_CLOSE_BUTTON_DEFAULT, true);
            }
        }
        this.closeRegion.setImageDrawable(closeDrawable);
        this.closeRegion.setScaleType(ScaleType.FIT_CENTER);
    }

    String getActivityClassName() {
        return this.activityClassName;
    }

    public void setAdName(String name) {
        this.adName = name;
        this.adInfo.setPlacement(name);
    }

    public String getAdName() {
        return this.adName;
    }

    public String getContainerName() {
        if (this.containerName == null) {
            this.containerName = getContainerName(this.placementType, this.adName);
        }
        return this.containerName;
    }

    public static String getContainerName(PlacementType type, String name) {
        if (name == null) {
            name = "";
        }
        return type.getValue() + "_" + name;
    }

    public void setPlacementType(PlacementType type) {
        this.placementType = type;
        if (type == PlacementType.INTERSTITIAL) {
            setBackgroundColor(MRAIDWebView.MODAL_BACKGROUND);
        } else {
            setBackgroundColor(0);
        }
        showCloseIndicator();
    }

    PlacementType getPlacementType() {
        return this.placementType;
    }

    public Set<String> getMD5ListUsed() {
        return this.md5usedInThisAd;
    }

    public void setMD5ListUsed(Set<String> mD5Set) {
        this.md5usedInThisAd = mD5Set;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.placementType == PlacementType.INLINE || !MRAIDManager.isRunningOnUnity()) {
            MRAIDLogger.d(this, "Attached to window");
            MRAIDManager.releaseAdCancel(this);
            for (MRAIDDialogWorker worker : this.workers) {
                worker.showDialog(getActivity());
            }
            if (!this.adFinishedLoading || isAdCached()) {
                setVisibility(8);
                return;
            }
            changeOrientation();
            fireListener(AdEvent.BEFORE_DISPLAY, "Before Ad is displayed");
            setVisibility(0);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.placementType == PlacementType.INLINE || !MRAIDManager.isRunningOnUnity()) {
            try {
                MRAIDLogger.d(this, "Detached from window");
                MRAIDManager.releaseAdDelayed(this);
                for (MRAIDDialogWorker worker : this.workers) {
                    worker.dismissDialog();
                }
            } catch (Exception e) {
                MRAIDLogger.e("Exception caught while detaching from window. Probably already detached", e);
            }
        }
    }

    void detachFromParent() {
        Log.d("Detaching container");
        try {
            ((ViewGroup) getParent()).removeView(this);
        } catch (Exception e) {
        }
    }

    void attachToActivity(Activity activity) {
        if (this.placementType != PlacementType.INTERSTITIAL || (activity instanceof InterstitialActivity) || MRAIDManager.isRunningOnUnity()) {
            Activity parent = activity;
            if (parent != null) {
                setActivity(parent);
            } else {
                parent = getActivity();
            }
            if (parent != null) {
                attachToCurrentActivity(0);
            }
        }
    }

    private boolean attachToCurrentActivity(int count) {
        if (this.activity != null) {
            if (MRAIDManager.isRunningOnUnity() && this.placementType == PlacementType.INTERSTITIAL) {
                if (!(this.dialog == null || this.dialog.getContext() == this.activity)) {
                    this.dialog.dismiss();
                    this.dialog = null;
                }
                createDialog();
                if (!isAdCached() && this.adFinishedLoading) {
                    showDialog();
                }
                this.adIsPending = false;
                return true;
            }
            try {
                detachFromParent();
                makeContainerInvisibleForVideoAutoPlay();
                this.activity.addContentView(this, new LayoutParams(-1, -1));
                this.adIsPending = false;
                return true;
            } catch (Exception e) {
            }
        }
        sendReattachMessage(count);
        return false;
    }

    void dialogDismissed() {
        this.dialog = null;
    }

    private MRAIDDialog createDialog() {
        if (this.dialog == null && this.activity != null) {
            this.dialog = new MRAIDDialog(this.activity);
            this.dialog.setContainer(this);
        }
        return this.dialog;
    }

    private void showDialog() {
        this.dialog.show();
    }

    private void sendReattachMessage(int count) {
        if (count < 20) {
            try {
                Message msg = handler.obtainMessage(MSG_REATTACH, this);
                msg.arg1 = count;
                handler.sendMessageDelayed(msg, DELAY_REATTACH);
            } catch (Exception e) {
            }
        }
    }

    public void loadAdUrl(String url) {
        try {
            if (this.webView == null) {
                if (this.activity == null) {
                    MRAIDLogger.e("Container not attached to activity, could not load the ad");
                    return;
                }
                this.webView = createWebView();
                this.webView.setLayoutParams(new LayoutParams(-1, -1));
                this.webView.setVisibility(4);
                if (closeRegionSize == 0) {
                    closeRegionSize = DensityManager.getDIP(getActivity(), 50.0f);
                }
                addView(this.webView);
            }
            MRAIDLogger.i("MRAID Ad Url: " + url);
            this.webView.loadAd(url);
            this.url = url;
        } catch (Exception e) {
            fireListener(AdEvent.ERROR, "Error while downloading the ad");
            MRAIDManager.releaseAd(this);
            MRAIDLogger.e("Unable to create WebView", e);
        }
    }

    void setClosePosition(ClosePosition position) {
        if (this.closePosition != position) {
            this.closePositionChanged = true;
        }
        this.closePosition = position;
        if (this.closePositionChanged && this.closeIndicatorIsVisible) {
            changeCloseIndicatorBackground();
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isClickInChildRegion(this.closeRegion, ev)) {
            return true;
        }
        if (this.state == States.EXPANDED || this.placementType == PlacementType.INTERSTITIAL) {
            MRAIDWebView currentWebView = getCurrentWebView();
            if (this.secondPartWebView != null && currentWebView == this.secondPartWebView) {
                return super.onInterceptTouchEvent(ev);
            }
            if (isClickInChildRegion(currentWebView, ev)) {
                return super.onInterceptTouchEvent(ev);
            }
            return true;
        }
        MRAIDWebView webView = getCurrentWebView();
        if (webView == null || !isClickInChildRegion(webView, ev)) {
            return false;
        }
        if (ev.getAction() == 0) {
            fireListener(AdEvent.USER_TOUCH, "The user touched the banner");
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean isClickInChildRegion(View child, MotionEvent event) {
        if (event.getX() >= ((float) child.getRight()) || event.getX() <= ((float) child.getLeft()) || event.getY() >= ((float) child.getBottom()) || event.getY() <= ((float) child.getTop())) {
            return false;
        }
        return true;
    }

    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (isClickInChildRegion(this.closeRegion, event)) {
            switch (event.getAction()) {
                case 0:
                case 1:
                    event.setLocation(event.getX() - ((float) this.closeRegion.getLeft()), event.getY() - ((float) this.closeRegion.getTop()));
                    this.closeRegion.onTouchEvent(event);
                    return true;
                default:
                    return true;
            }
        } else if (this.state == States.EXPANDED || this.placementType == PlacementType.INTERSTITIAL) {
            super.onTouchEvent(event);
            return true;
        } else {
            MRAIDWebView webView = getCurrentWebView();
            if (webView == null || !isClickInChildRegion(webView, event)) {
                return false;
            }
            super.onTouchEvent(event);
            return true;
        }
    }

    void setActivity(Activity activity) {
        this.activity = activity;
        this.activityClassName = activity.getComponentName().getClassName();
    }

    boolean isAttachedTo(Activity activity) {
        if (!Utilities.stringIsEmpty(this.activityClassName) && this.activityClassName.equals(activity.getComponentName().getClassName())) {
            return true;
        }
        return false;
    }

    Activity getActivity() {
        return this.activity;
    }

    public void setAdPosition(Rect position) {
        this.bannerPosition = null;
        this.position.setCustomPosition(position);
        postInvalidate();
        requestLayout();
    }

    public boolean isActive() {
        return (this.activity == null || Utilities.stringIsEmpty(this.activityClassName)) ? false : true;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.activity != null) {
            Rect adPosition;
            this.tempRect.set(0, 0, r - l, b - t);
            this.position.validateCustomPosition(this.closeRegion, this.tempRect);
            if (this.bannerPosition == null || this.state == States.RESIZED) {
                adPosition = this.position.getPosition(this.activity.getResources().getConfiguration().orientation);
                if (adPosition == null) {
                    this.tempRect.set(0, 0, getCurrentWebView().getMeasuredWidth(), getCurrentWebView().getMeasuredHeight());
                    adPosition = this.tempRect;
                }
            } else {
                adPosition = this.bannerPosition.calculateSize(l, t, r, b);
            }
            switch (this.state) {
                case RESIZED:
                    setWebViewLayout(this.webView, adPosition);
                    setCloseRegionPosition(adPosition);
                    break;
                case EXPANDED:
                    this.tempRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
                    setWebViewLayout(getCurrentWebView(), this.tempRect);
                    this.closeRect.set(getMeasuredWidth() - this.closeRegion.getMeasuredWidth(), 0, getMeasuredWidth(), this.closeRegion.getMeasuredHeight());
                    break;
                case LOADING:
                    setWebViewLayout(this.webView, adPosition);
                    this.closeRect.set(getMeasuredWidth() - this.closeRegion.getMeasuredWidth(), 0, getMeasuredWidth(), this.closeRegion.getMeasuredHeight());
                    break;
                default:
                    if (this.adExpired) {
                        MRAIDManager.releaseAd(this);
                    }
                    setWebViewLayout(this.webView, adPosition);
                    this.closeRect.set(adPosition.right - this.closeRegion.getMeasuredWidth(), adPosition.top, adPosition.right, adPosition.top + this.closeRegion.getMeasuredHeight());
                    break;
            }
            if (this.placementType == PlacementType.INTERSTITIAL || this.state == States.EXPANDED || this.state == States.RESIZED) {
                this.closeRegion.layout(this.closeRect.left, this.closeRect.top, this.closeRect.right, this.closeRect.bottom);
            } else {
                this.closeRegion.layout(0, 0, 0, 0);
            }
            if (this.currentPosition.setCurrentPosition(getCurrentWebView())) {
                setCurrentPosition(false);
            }
        }
    }

    private void setWebViewLayout(MRAIDWebView webView, Rect layout) {
        if (webView != null && layout != null) {
            try {
                webView.layout(layout.left, layout.top, layout.right, layout.bottom);
            } catch (Exception e) {
                Log.w("Failed to layout the RichMedia webview.", e);
            }
        }
    }

    private void setCloseRegionPosition(Rect adPosition) {
        int l;
        int t;
        int r;
        int b;
        switch (this.closePosition) {
            case BOTTOM_LEFT:
                l = adPosition.left;
                t = adPosition.bottom - this.closeRegion.getMeasuredHeight();
                r = adPosition.left + this.closeRegion.getMeasuredWidth();
                b = adPosition.bottom;
                break;
            case BOTTOM_RIGHT:
                l = adPosition.right - this.closeRegion.getMeasuredWidth();
                t = adPosition.bottom - this.closeRegion.getMeasuredHeight();
                r = adPosition.right;
                b = adPosition.bottom;
                break;
            case BOTTOM_CENTER:
                l = adPosition.left + ((adPosition.width() - this.closeRegion.getMeasuredWidth()) / 2);
                t = adPosition.bottom;
                r = l + this.closeRegion.getMeasuredWidth();
                b = t + this.closeRegion.getMeasuredHeight();
                break;
            case CENTER:
                l = adPosition.left + ((adPosition.width() - this.closeRegion.getMeasuredWidth()) / 2);
                t = adPosition.top + ((adPosition.height() - this.closeRegion.getMeasuredHeight()) / 2);
                r = l + this.closeRegion.getMeasuredWidth();
                b = t + this.closeRegion.getMeasuredHeight();
                break;
            case TOP_LEFT:
                l = adPosition.left;
                t = adPosition.top;
                r = l + this.closeRegion.getMeasuredWidth();
                b = t + this.closeRegion.getMeasuredHeight();
                break;
            case TOP_CENTER:
                l = adPosition.left + ((adPosition.width() - this.closeRegion.getMeasuredWidth()) / 2);
                t = adPosition.top;
                r = l + this.closeRegion.getMeasuredWidth();
                b = t + this.closeRegion.getMeasuredHeight();
                break;
            default:
                l = adPosition.right - this.closeRegion.getMeasuredWidth();
                t = adPosition.top;
                r = adPosition.right;
                b = adPosition.top + this.closeRegion.getMeasuredHeight();
                break;
        }
        this.closeRect.set(l, t, r, b);
    }

    void forceAdPosition(Rect position) {
        this.position.setForcedPosition(position);
        postInvalidate();
        requestLayout();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.activity != null) {
            MRAIDWebView webView = getCurrentWebView();
            if (webView != null) {
                int widthSpec = 0;
                int heightSpec = 0;
                if (this.state == States.EXPANDED || this.placementType == PlacementType.INTERSTITIAL) {
                    widthSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), 1073741824);
                    heightSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), 1073741824);
                } else if (this.state == States.RESIZED) {
                    adPosition = this.position.getPosition(this.activity.getResources().getConfiguration().orientation);
                    widthSpec = MeasureSpec.makeMeasureSpec(adPosition.width(), 1073741824);
                    heightSpec = MeasureSpec.makeMeasureSpec(adPosition.height(), 1073741824);
                } else {
                    if (this.bannerPosition != null) {
                        adPosition = this.bannerPosition.calculateSize(0, 0, MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
                    } else {
                        adPosition = this.position.getPosition(this.activity.getResources().getConfiguration().orientation);
                    }
                    if (adPosition != null && adPosition.height() > 0 && adPosition.width() > 0) {
                        widthSpec = MeasureSpec.makeMeasureSpec(adPosition.width(), 1073741824);
                        heightSpec = MeasureSpec.makeMeasureSpec(adPosition.height(), 1073741824);
                    }
                }
                webView.measure(widthSpec, heightSpec);
            }
            this.closeRegion.measure(MeasureSpec.makeMeasureSpec(closeRegionSize, 1073741824), MeasureSpec.makeMeasureSpec(closeRegionSize, 1073741824));
        }
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (!(this.oldWidth == width && this.oldHeight == height)) {
            this.closePositionChanged = true;
            this.maxSize.setHeight(Integer.valueOf(DensityManager.getMRAIDDip((float) height)));
            this.maxSize.setWidth(Integer.valueOf(DensityManager.getMRAIDDip((float) width)));
            setSizes();
        }
        this.oldWidth = width;
        this.oldHeight = height;
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, 1073741824), MeasureSpec.makeMeasureSpec(height, 1073741824));
    }

    public void fireErrorEvent(String msg, Throwable e, JSCommands command) {
        if (msg == null) {
            try {
                this.deviceToAdHandler.fireErrorEvent(e, command);
            } catch (Exception ex) {
                MRAIDLogger.e("MRAID: MRAIDWebView reference lost.", ex);
            }
        } else if (e == null) {
            this.deviceToAdHandler.fireErrorEvent(msg, command);
        } else {
            this.deviceToAdHandler.fireErrorEvent(msg, e, command);
        }
    }

    @SuppressLint({"WrongConstant"})
    private synchronized void release() {
        try {
            if (!this.released) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    onReleaseFailed(new Exception("Not on UI thread"));
                } else {
                    Activity currentActivity = this.activity;
                    this.activity = null;
                    for (MRAIDDialogWorker worker : this.workers) {
                        worker.release();
                    }
                    this.workers.clear();
                    removeAllViews();
                    if (this.webView != null) {
                        this.webView.release();
                    }
                    if (this.secondPartWebView != null) {
                        this.secondPartWebView.release();
                    }
                    try {
                        if (this.placementType == PlacementType.INLINE) {
                            detachFromParent();
                        }
                    } catch (Exception e) {
                    }
                    if (currentActivity != null) {
                        currentActivity.setRequestedOrientation(this.defaultActivityOrientation);
                        if (currentActivity instanceof InterstitialActivity) {
                            currentActivity.finish();
                        }
                    }
                    if (this.dialog != null) {
                        this.dialog.dismiss();
                    }
                    this.released = true;
                    MRAIDLogger.d(RequestParameters.LEFT_BRACKETS + getContainerName() + "] Ad released");
                    fireListener(AdEvent.DISMISSED, "Ad is dismissed");
                }
            }
        } catch (Exception e2) {
            onReleaseFailed(e2);
        }
    }

    private void onReleaseFailed(Exception e) {
        if (this.attemptedReleaseCount < 10) {
            MRAIDLogger.e(RequestParameters.LEFT_BRACKETS + getContainerName() + "] Attempt " + this.attemptedReleaseCount + " to release the ad failed: " + (e != null ? e.getMessage() : ""));
            MRAIDManager.releaseAd(this);
            return;
        }
        MRAIDLogger.e(RequestParameters.LEFT_BRACKETS + getContainerName() + "] Ad failed to release.", e);
    }

    void shouldReleaseWithCloseAnimation(boolean closeAnimation) {
        if (closeAnimation) {
            releaseWithCloseAnimation();
        } else {
            release();
        }
    }

    void releaseWithCloseAnimation() {
        if (!this.released) {
            final MRAIDContainer container = this;
            try {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            if (MRAIDContainer.this.state != States.HIDDEN) {
                                MRAIDContainer.this.state = States.HIDDEN;
                                MRAIDContainer.this.deviceToAdHandler.setState(States.HIDDEN);
                            }
                            if (MRAIDContainer.this.attemptedReleaseCount == 0 && MRAIDContainer.this.placementType == PlacementType.INTERSTITIAL) {
                                final Drawable containerDrawable = new ColorDrawable(Color.argb(255, 10, 10, 10));
                                containerDrawable.setAlpha(128);
                                container.setBackgroundDrawable(containerDrawable);
                                Animation animation = new Animation() {
                                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                                        containerDrawable.setAlpha((int) (128.0f * (DefaultRetryPolicy.DEFAULT_BACKOFF_MULT - interpolatedTime)));
                                    }
                                };
                                animation.setDuration(MRAIDContainer.MSG_CLOSE_ANIMATION_MIN_DURATION);
                                container.startAnimation(animation);
                                MRAIDContainer.handler.sendMessageDelayed(MRAIDContainer.handler.obtainMessage(MRAIDContainer.MSG_CLOSE_ANIMATION_END, container), MRAIDContainer.MSG_CLOSE_ANIMATION_MIN_DURATION);
                            } else {
                                MRAIDContainer.this.onCloseAnimationEnd();
                            }
                            MRAIDContainer.this.attemptedReleaseCount = MRAIDContainer.this.attemptedReleaseCount + 1;
                        } catch (Exception e) {
                            MRAIDLogger.e("Unhandled exception", e);
                        }
                    }
                });
            } catch (Exception e) {
                MRAIDLogger.e(this, "Failed to release ad", e);
            }
            try {
                if (this.shouldHaltVideo) {
                    NativeXVideoPlayer.getInstance().cancel();
                }
            } catch (Exception e2) {
            }
        }
    }

    private void onCloseAnimationEnd() {
        release();
    }

    @SuppressLint({"WrongConstant"})
    void changeOrientation(OrientationProperties orientationProperties) {
        if (this.activity != null && (this.activity instanceof InterstitialActivity)) {
            Orientation forcedOrientation = orientationProperties.getForceOrientation();
            if (forcedOrientation != Orientation.NONE) {
                switch (forcedOrientation) {
                    case LANDSCAPE:
                        if (VERSION.SDK_INT >= 9) {
                            this.activity.setRequestedOrientation(6);
                            return;
                        } else {
                            this.activity.setRequestedOrientation(0);
                            return;
                        }
                    case PORTRAIT:
                        if (VERSION.SDK_INT >= 9) {
                            this.activity.setRequestedOrientation(7);
                            return;
                        } else {
                            this.activity.setRequestedOrientation(1);
                            return;
                        }
                }
            }
            if (orientationProperties.getAllowOrientationChange().booleanValue()) {
                this.activity.setRequestedOrientation(this.defaultActivityOrientation);
            } else {
                this.activity.setRequestedOrientation(getOrientationToSet());
            }
        }
    }

    @IntegerRes
    int getOrientationToSet() {
        int orientation = this.activity.getResources().getConfiguration().orientation;
        int rotation = this.activity.getWindowManager().getDefaultDisplay().getRotation();
        int orientationToSet = this.defaultActivityOrientation;
        switch (orientation) {
            case 1:
                if (rotation == 0 || rotation == 3) {
                    return 1;
                }
                return 9;
            case 2:
                if (rotation == 0 || rotation == 1) {
                    return 0;
                }
                return 8;
            default:
                return orientationToSet;
        }
    }

    States getState() {
        return this.state;
    }

    @SuppressLint({"WrongConstant"})
    void close() {
        try {
            switch (this.state) {
                case RESIZED:
                    break;
                case EXPANDED:
                    if (getCurrentWebView() == this.secondPartWebView) {
                        removeView(this.secondPartWebView);
                        addView(this.webView, 0);
                        bringChildToFront(this.closeRegion);
                        this.closeRegion.setClickable(true);
                        break;
                    }
                    break;
                default:
                    closeAd();
                    return;
            }
            this.state = States.DEFAULT;
            fireListener(AdEvent.COLLAPSED, "The ad was returned to default state from being expanded or resized");
            this.deviceToAdHandler.setState(States.DEFAULT);
            setClosePosition(ClosePosition.TOP_RIGHT);
            removeCloseRegion();
            if (this.activity != null) {
                this.activity.setRequestedOrientation(this.defaultActivityOrientation);
            }
            this.position.setForcedPosition(null);
            setBackgroundColor(0);
            requestLayout();
            postInvalidate();
        } catch (Exception e) {
            MRAIDLogger.e("Exception caught in close()", e);
            fireErrorEvent(null, e, JSCommands.CLOSE);
        }
    }

    public void expand(String url) {
        try {
            if (this.placementType == PlacementType.INTERSTITIAL) {
                fireErrorEvent("Ad is interstitial", null, JSCommands.EXPAND);
            } else if (this.state != States.EXPANDED) {
                if (Utilities.isHttpOrHttpsUrl(url)) {
                    removeView(this.webView);
                    if (this.secondPartWebView == null) {
                        this.secondPartWebView = createWebView();
                    }
                    addView(this.secondPartWebView, 0);
                    this.secondPartWebView.resetMRAID();
                    this.secondPartWebView.loadAd(url);
                } else {
                    if (this.secondPartWebView != null) {
                        this.secondPartWebView.release();
                    }
                    this.secondPartWebView = null;
                    this.deviceToAdHandler.setState(this.webView, States.EXPANDED);
                }
                this.state = States.EXPANDED;
                fireListener(AdEvent.EXPANDED, "The ad was expanded");
                bringToFront();
                setBackgroundColor(MRAIDWebView.MODAL_BACKGROUND);
                changeOrientation();
                addCloseRegion();
                requestLayout();
                postInvalidate();
            }
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.EXPAND);
        }
    }

    void resize() {
        try {
            if (this.placementType == PlacementType.INTERSTITIAL) {
                fireErrorEvent("Ad is interstitial", null, JSCommands.RESIZE);
            } else if (this.state == States.EXPANDED) {
                String msg = "WebView is expanded, cannot resize";
                MRAIDLogger.e(msg);
                fireErrorEvent(msg, null, JSCommands.RESIZE);
            } else if (isResizePropertiesValid()) {
                setClosePosition(this.resizeProperties.getCustomClosePosition());
                this.position.setForcedPosition(this.resizeProperties.getPositionRect(this.webView, this.maxSize));
                this.state = States.RESIZED;
                fireListener(AdEvent.RESIZED, "The ad was resized");
                this.deviceToAdHandler.setState(States.RESIZED);
                bringToFront();
                addCloseRegion();
                requestLayout();
                postInvalidate();
            }
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.RESIZE);
        }
    }

    private boolean isResizePropertiesValid() {
        if (this.resizeProperties == null) {
            fireErrorEvent("Resize properties null.", null, JSCommands.RESIZE);
            return false;
        } else if (this.resizeProperties.getWidth() < this.closeRegion.getMeasuredWidth() && this.resizeProperties.getHeight() < this.closeRegion.getMeasuredHeight()) {
            fireErrorEvent("The WebView cannot be smaller than the close region.", null, JSCommands.RESIZE);
            return false;
        } else if (this.resizeProperties.getWidth() > this.maxSize.getWidth() || this.resizeProperties.getHeight() > this.maxSize.getHeight()) {
            fireErrorEvent("The WebView could not be bigger than the screen when resized.", null, JSCommands.RESIZE);
            return false;
        } else if (this.resizeProperties.getWidth() == this.maxSize.getWidth() && this.resizeProperties.getHeight() == this.maxSize.getHeight()) {
            fireErrorEvent("The resize() method should not be used to expand fullscreen, please use expand()", null, JSCommands.RESIZE);
            return false;
        } else {
            Rect resizeRect = this.resizeProperties.getCheckRect(this.webView);
            if (this.resizeProperties.allowOffscreen().booleanValue()) {
                boolean closeIsOnScreen = true;
                switch (this.resizeProperties.getCustomClosePosition()) {
                    case BOTTOM_LEFT:
                        if (resizeRect.bottom > this.maxSize.getHeight() || resizeRect.left < 0) {
                            closeIsOnScreen = false;
                            break;
                        }
                    case BOTTOM_RIGHT:
                        if (resizeRect.bottom > this.maxSize.getHeight() || resizeRect.right > this.maxSize.getWidth()) {
                            closeIsOnScreen = false;
                            break;
                        }
                    case BOTTOM_CENTER:
                        if (resizeRect.left < 0 || resizeRect.bottom > this.maxSize.getHeight() || resizeRect.right > this.maxSize.getWidth()) {
                            closeIsOnScreen = false;
                            break;
                        }
                    case CENTER:
                        int posX = resizeRect.left + ((resizeRect.width() - this.closeRegion.getMeasuredWidth()) / 2);
                        int posY = resizeRect.top + ((resizeRect.height() - this.closeRegion.getMeasuredHeight()) / 2);
                        if (posX < 0 || this.closeRegion.getMeasuredWidth() + posX > this.maxSize.getHeight()) {
                            closeIsOnScreen = false;
                        }
                        if (posY < 0 || this.closeRegion.getMeasuredHeight() + posY > this.maxSize.getHeight()) {
                            closeIsOnScreen = false;
                            break;
                        }
                    case TOP_LEFT:
                        if (resizeRect.top < 0 || resizeRect.left < 0) {
                            closeIsOnScreen = false;
                            break;
                        }
                    case TOP_CENTER:
                        if (resizeRect.left < 0 || resizeRect.top < 0 || resizeRect.right > this.maxSize.getWidth()) {
                            closeIsOnScreen = false;
                            break;
                        }
                    default:
                        if (resizeRect.top < 0 || resizeRect.right > this.maxSize.getWidth()) {
                            closeIsOnScreen = false;
                            break;
                        }
                }
                if (!closeIsOnScreen) {
                    fireErrorEvent("Close region must be on screen", null, JSCommands.RESIZE);
                    return false;
                }
            }
            return true;
        }
    }

    public void onMraidLoaded() {
        this.mraidLoaded = true;
        getPageSize();
        getCurrentWebView().setMraidLoaded();
        getCurrentWebView().setVisibility(0);
        setDeviceFeatures();
        setNativeVideoFeatures();
        setSizes();
        setPlacementType();
        postInvalidate();
    }

    void setPageSize(int width, int height) {
        if (this.placementType == PlacementType.INLINE) {
            postInvalidate();
            requestLayout();
        }
    }

    private void setSizes() {
        if (this.mraidLoaded) {
            setMaxSize();
            setScreenSize();
        }
    }

    private void setMaxSize() {
        try {
            if (this.expandProperties == null) {
                this.expandProperties = new ExpandProperties();
                this.expandProperties.setModal(Boolean.valueOf(true));
                this.expandProperties.setUseCustomClose(Boolean.valueOf(false));
            }
            this.expandProperties.setHeight(Integer.valueOf(this.maxSize.getHeight()));
            this.expandProperties.setWidth(Integer.valueOf(this.maxSize.getWidth()));
            this.deviceToAdHandler.setMaxSize(this.maxSize);
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.SET_MAX_SIZE);
        }
    }

    private void setDeviceFeatures() {
        try {
            this.deviceToAdHandler.setDeviceFeatures(this.activity);
        } catch (Exception e) {
            fireErrorEvent("MRAID loaded: Activity instance lost.", e, null);
        }
    }

    private void setNativeVideoFeatures() {
        try {
            this.deviceToAdHandler.setNativeVideoFeatures();
        } catch (Exception e) {
            fireErrorEvent("MRAID loaded: setNativeVideoFeatures.", e, null);
        }
    }

    private void setPlacementType() {
        try {
            this.deviceToAdHandler.setPlacementType(this.placementType);
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.SET_PLACEMENT_TYPE);
        }
    }

    void setExpandProperties(ExpandProperties props) {
        this.expandProperties = props;
    }

    void setResizeProperties(ResizeProperties props) {
        this.resizeProperties = props;
    }

    private void setScreenSize() {
        try {
            WindowManager manager = (WindowManager) getContext().getSystemService("window");
            int height = DensityManager.getMRAIDDip((float) manager.getDefaultDisplay().getHeight());
            int width = DensityManager.getMRAIDDip((float) manager.getDefaultDisplay().getWidth());
            ScreenSize screenSize = new ScreenSize();
            screenSize.setWidth(Integer.valueOf(width));
            screenSize.setHeight(Integer.valueOf(height));
            this.deviceToAdHandler.setScreenSize(screenSize);
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.SET_SCREEN_SIZE);
        }
    }

    private void closeAd() {
        MRAIDManager.releaseAd(this);
    }

    void setOrientationProperties(OrientationProperties props) {
        this.orientationProperties = props;
        if (this.placementType == PlacementType.INTERSTITIAL || this.state == States.EXPANDED) {
            changeOrientation();
        }
    }

    OrientationProperties getOrientationProperties() {
        return this.orientationProperties;
    }

    void changeOrientation() {
        if (this.orientationProperties != null) {
            changeOrientation(this.orientationProperties);
        } else {
            MRAIDLogger.d("OrientationProperties are null");
        }
    }

    ClosePosition getCustomClosePosition() {
        if (this.resizeProperties != null) {
            return this.resizeProperties.getCustomClosePosition();
        }
        return ClosePosition.TOP_RIGHT;
    }

    void runJSCommand(final JSCommand instance) {
        if (getCurrentWebView() != null) {
            final MRAIDWebView commandWebView = getCurrentWebView();
            post(new Runnable() {
                public void run() {
                    try {
                        commandWebView.runJSCommand(instance);
                    } catch (Exception e) {
                        try {
                            MRAIDContainer.this.fireErrorEvent("Unhandled exception", e, instance.getCommand());
                        } catch (Exception e2) {
                            MRAIDLogger.e("Unhandled exception (and can't fire error event)", e);
                        }
                    }
                }
            });
            return;
        }
        fireErrorEvent("WebView reference lost", null, instance.getCommand());
    }

    void onPageFinished() {
        try {
            MRAIDWebView currentWebView = getCurrentWebView();
            if (currentWebView == null) {
                fireErrorEvent("WebView reference lost", null, JSCommands.FIRE_EVENT);
                MRAIDManager.releaseAd(this);
                return;
            }
            if (!currentWebView.isAdLoaded()) {
                if (currentWebView == this.webView) {
                    setDefaultPosition();
                    setCurrentPosition();
                    this.state = States.DEFAULT;
                    this.deviceToAdHandler.setState(States.DEFAULT);
                    getPageSize();
                } else if (currentWebView == this.secondPartWebView) {
                    this.state = States.EXPANDED;
                    setCurrentPosition();
                    this.deviceToAdHandler.setState(this.webView, States.EXPANDED);
                    this.deviceToAdHandler.setState(this.secondPartWebView, States.EXPANDED);
                } else {
                    fireErrorEvent("Invalid WebView loaded", null, JSCommands.FIRE_EVENT);
                    MRAIDManager.releaseAd(this);
                    return;
                }
                currentWebView.setVisibility(0);
                currentWebView.setAdLoaded();
                this.adFinishedLoading = true;
                this.deviceToAdHandler.fireEvent(Events.READY);
                fireListener(AdEvent.FETCHED, "Ad finished downloading");
                addCloseRegion();
                if (!this.adIsCached) {
                    fireListener(AdEvent.BEFORE_DISPLAY, "Before Ad is displayed");
                    if (this.placementType != PlacementType.INTERSTITIAL) {
                        setVisibility(0);
                    } else if (MRAIDManager.isRunningOnUnity()) {
                        setVisibility(8);
                        createDialog();
                        showDialog();
                        if (SessionManager.isBackupAdsEnabled() && getActivity() != null) {
                            Log.d("(Unity) Backup ads enabled; page finished and cached == false, fetching new ad for " + getAdName());
                            MRAIDManager.StartSecondaryAdDownload(getActivity(), getAdName(), getOnRichMediaEventListener());
                        }
                    } else if (this.activity instanceof InterstitialActivity) {
                        attachToCurrentActivity(0);
                    } else {
                        ActivityManager.startMRAIDInterstitial(getContext(), this.adName, false);
                    }
                } else if (!MRAIDManager.isRunningOnUnity()) {
                    setVisibility(8);
                }
            }
            if (this.expandProperties.getUseCustomClose().booleanValue()) {
                hideCloseIndicator();
            } else {
                showCloseIndicator();
            }
            postInvalidate();
            requestLayout();
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.FIRE_EVENT);
        }
    }

    private void getPageSize() {
        JSCommand command = JSCommands.GET_PAGE_SIZE.instance();
        command.setJsObjectName("nativeXSizeScript");
        runJSCommand(command);
    }

    private void setDefaultPosition() {
        try {
            if (this.defaultPosition == null) {
                this.defaultPosition = new DefaultPosition();
                this.defaultPosition.setPosition(this.webView);
            }
            this.deviceToAdHandler.setDefaultPosition(this.defaultPosition);
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.SET_DEFAULT_POSITION);
        }
    }

    void setCurrentPosition() {
        setCurrentPosition(true);
    }

    void setCurrentPosition(boolean resetPosition) {
        if (resetPosition) {
            try {
                this.currentPosition.setCurrentPosition(getCurrentWebView());
            } catch (Exception e) {
                fireErrorEvent(null, e, JSCommands.SET_CURRENT_POSITION);
                return;
            }
        }
        this.deviceToAdHandler.setCurrentPosition(this.currentPosition);
    }

    MRAIDWebView getCurrentWebView() {
        if (this.state != States.EXPANDED || this.secondPartWebView == null) {
            return this.webView;
        }
        return this.secondPartWebView;
    }

    void setIsViewable(boolean b) {
        JSIDeviceToAd jSIDeviceToAd = this.deviceToAdHandler;
        boolean z = b && getVisibility() == 0;
        jSIDeviceToAd.setIsViewable(z);
    }

    void playVideo(String url) {
        try {
            ActivityManager.startMRAIDVideo(getContext(), url, this);
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.PLAY_VIDEO);
        }
    }

    void useCustomClose(boolean useCustomClose) {
        this.expandProperties.setUseCustomClose(Boolean.valueOf(useCustomClose));
        if (this.state == States.LOADING) {
            return;
        }
        if (useCustomClose) {
            hideCloseIndicator();
        } else if (this.state != States.RESIZED) {
            showCloseIndicator();
        }
    }

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    public void bringToFront() {
        sendBringToFrontMessage();
        super.bringToFront();
    }

    private void sendBringToFrontMessage() {
        if (this.state == States.RESIZED || this.state == States.EXPANDED) {
            handler.sendMessageDelayed(handler.obtainMessage(1000, this), DELAY_BRING_TO_FRONT);
        }
    }

    void addCalendarEntry(CalendarEntryData entry) {
        try {
            if (Features.CALENDAR.isSupported(getActivity())) {
                this.workers.add(MRAIDWorkerFactory.createCalendarWorker(this, entry));
            }
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.CREATE_CALENDAR_EVENT);
        }
    }

    void showJSDialog(String url, String message, JsResult result, JSDialogAction action) {
        try {
            this.workers.add(MRAIDWorkerFactory.createJSDialogWorker(this, url, message, result, action));
        } catch (Exception e) {
            fireErrorEvent("Failed to create JS dialog", e, null);
        }
    }

    void storePicture(String url) {
        try {
            if (Features.STORE_PICTURE.isSupported(getActivity())) {
                this.workers.add(MRAIDWorkerFactory.createStorePictureWorker(this, url));
            }
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.STORE_PICTURE);
        }
    }

    public void workerDone(MRAIDDialogWorker worker) {
        try {
            this.workers.remove(worker);
            worker.release();
        } catch (Exception e) {
            MRAIDLogger.e(this, "Failed to release a worker", e);
        }
    }

    public boolean reload() {
        try {
            if (this.state == States.EXPANDED || this.state == States.RESIZED) {
                return false;
            }
            if (getCurrentWebView() == this.secondPartWebView) {
                removeView(this.secondPartWebView);
                addView(this.webView, 0);
            }
            this.state = States.LOADING;
            this.expandProperties.setUseCustomClose(Boolean.valueOf(false));
            showCloseIndicator();
            this.webView.resetMRAID();
            if (this.secondPartWebView != null) {
                this.secondPartWebView.resetMRAID();
            }
            this.currentPosition = new CurrentPosition();
            this.closePosition = ClosePosition.TOP_RIGHT;
            this.closePositionChanged = true;
            this.defaultPosition = null;
            this.webView.loadAd(this.url);
            return true;
        } catch (Exception e) {
            MRAIDLogger.e("Exception caught while reloading the ad", e);
            return false;
        }
    }

    void trackVideo(String url, VideoProgress progress) {
        this.deviceToAdHandler.trackVideo(url, progress);
    }

    void trackClick(String url) {
    }

    boolean isSecondPartWebView(MRAIDWebView mraidWebView) {
        if (this.secondPartWebView == null || this.secondPartWebView != mraidWebView) {
            return false;
        }
        return true;
    }

    boolean isAdLoaded() {
        return this.adFinishedLoading;
    }

    public void sendLoadDataMessage(MRAIDWebView wv) {
        handler.obtainMessage(MSG_LOAD_AD_DATA, this.webView == wv ? 0 : 1, 0, this).sendToTarget();
    }

    void loadData(int index) {
        switch (index) {
            case 0:
                this.webView.loadDownloadedData();
                return;
            case 1:
                if (this.secondPartWebView != null) {
                    this.secondPartWebView.loadDownloadedData();
                    return;
                }
                break;
        }
        fireListener(AdEvent.ERROR, "Error while downloading the ad");
        MRAIDManager.releaseAd(this);
    }

    public void setVisibility(int visibility) {
        boolean z = true;
        super.setVisibility(visibility);
        if (visibility == 0 && !this.didFireDisplayListener) {
            this.didFireDisplayListener = true;
            fireListener(AdEvent.DISPLAYED, "Ad displayed");
        }
        View currentWebView = getCurrentWebView();
        if (currentWebView != null) {
            if (currentWebView.getVisibility() != 0) {
                z = false;
            }
            setIsViewable(z);
        }
    }

    public synchronized void registerCallId(String callId) {
        JSCommand instance = JSCommands.CALL_RECEIVED.instance();
        instance.setParams(callId);
        runJSCommand(instance);
    }

    public synchronized void fireListener(final AdEvent event, final String message) {
        if (canFireEvent(event)) {
            registerEvent(event);
            handler.post(new Runnable() {
                public void run() {
                    try {
                        if (MRAIDContainer.this.onRichMediaEvent != null) {
                            if (MRAIDContainer.this.onRichMediaEvent instanceof OnAdEvent) {
                                ((OnAdEvent) MRAIDContainer.this.onRichMediaEvent).onEvent(event, message);
                            }
                            if (MRAIDContainer.this.onRichMediaEvent instanceof OnAdEventV2) {
                                ((OnAdEventV2) MRAIDContainer.this.onRichMediaEvent).onEvent(event, MRAIDContainer.this.adInfo, message);
                            }
                        }
                    } catch (Exception e) {
                        MRAIDLogger.e("Unhandled exception", e);
                    }
                }
            });
        }
    }

    private boolean canFireEvent(AdEvent event) {
        if (event == AdEvent.DISPLAYED) {
            this.wasDisplayFired = true;
        }
        if (event.getEventLevel() < 0) {
            return true;
        }
        if (event.getEventLevel() <= this.lastEventLevel) {
            return false;
        }
        if (event != AdEvent.DISMISSED || this.wasDisplayFired) {
            return true;
        }
        return false;
    }

    void registerEvent(AdEvent event) {
        this.lastEventLevel = Math.max(event.getEventLevel(), this.lastEventLevel);
    }

    public void setOnRichMediaEventListener(OnAdEventBase listener) {
        this.onRichMediaEvent = listener;
    }

    public OnAdEventBase getOnRichMediaEventListener() {
        return this.onRichMediaEvent;
    }

    void adConverted() {
        this.adConverted = true;
    }

    boolean hasAdConverted() {
        return this.adConverted;
    }

    public BannerPosition getBannerPosition() {
        return this.bannerPosition;
    }

    public void setBannerPosition(BannerPosition bannerPosition) {
        if (bannerPosition != null) {
            this.position.setCustomPosition(null);
        }
        this.bannerPosition = bannerPosition;
        requestLayout();
        postInvalidate();
    }

    void setAdInfo(AdInfo adInfo) {
        this.adInfo = adInfo;
    }

    void prepareVideo(String url) {
        NativeXVideoPlayer.getInstance().prepare(url, Log.isLogging());
    }

    void setIsVideoAutoPlay(boolean autoPlay) {
        this.videoAutoPlay = autoPlay;
    }

    public boolean isVideoAutoPlay() {
        return this.videoAutoPlay;
    }

    void clearAllAlphaAnimations() {
        clearAnimation();
    }

    void makeContainerInvisibleForVideoAutoPlay() {
        if (isVideoAutoPlay()) {
            makeContainerInvisible();
        }
    }

    void makeContainerInvisible() {
        if (!this.hideAlphaAnimation.hasStarted()) {
            startAnimation(this.hideAlphaAnimation);
        }
    }

    void makeContainerVisible() {
        clearAllAlphaAnimations();
        startAnimation(this.showAlphaAnimation);
    }

    public void fireVideoCancelledEvent() {
        this.deviceToAdHandler.fireNativeXEvent(NativeXEvents.VIDEO_CANCELLED);
    }

    void setVideoOptions(String videoOptions) {
        this.videoOptionsJSON = videoOptions;
        checkForAutoPlay(this.videoOptionsJSON);
    }

    void setWillCloseAdOnRedirect(boolean willClose) {
        this.willCloseAdOnRedirect = willClose;
    }

    boolean getWillCloseAdOnRedirect() {
        return this.willCloseAdOnRedirect;
    }

    void enableCloseRegion(boolean enable) {
        this.closeRegionIsEnabled = enable;
    }

    void checkForAutoPlay(String videoPlayerOptionsJSON) {
        try {
            JSONObject jsonObject = new JSONObject(videoPlayerOptionsJSON);
            if (jsonObject.has(VideoPlayerOptions.AUTO_PLAY)) {
                setIsVideoAutoPlay(jsonObject.getBoolean(VideoPlayerOptions.AUTO_PLAY));
            }
        } catch (JSONException e) {
        }
    }

    public Options getVideoOptions() {
        Options options = new Options();
        options.mutedButtonIcon = ThemeManager.getDrawable(ThemeElementTypes.NATIVE_VIDEO_PLAYER_MUTE_BUTTON);
        options.notMutedButtonIcon = ThemeManager.getDrawable(ThemeElementTypes.NATIVE_VIDEO_PLAYER_UNMUTE_BUTTON);
        options.skipButtonIcon = ThemeManager.getDrawable(ThemeElementTypes.NATIVE_VIDEO_PLAYER_CLOSE_BUTTON);
        OrientationProperties orientationProperties = getOrientationProperties();
        if (orientationProperties != null) {
            Orientation forcedOrientation = orientationProperties.getForceOrientation();
            if (forcedOrientation != Orientation.NONE) {
                switch (forcedOrientation) {
                    case LANDSCAPE:
                        if (VERSION.SDK_INT < 9) {
                            options.orientation = 0;
                            break;
                        }
                        options.orientation = 6;
                        break;
                    case PORTRAIT:
                        if (VERSION.SDK_INT < 9) {
                            options.orientation = 1;
                            break;
                        }
                        options.orientation = 7;
                        break;
                }
            } else if (orientationProperties.getAllowOrientationChange().booleanValue()) {
                options.orientation = this.defaultActivityOrientation;
            } else {
                int orientation = this.activity.getResources().getConfiguration().orientation;
                int rotation = this.activity.getWindowManager().getDefaultDisplay().getRotation();
                int orientationToSet = this.defaultActivityOrientation;
                switch (orientation) {
                    case 1:
                        if (rotation != 0 && rotation != 3) {
                            orientationToSet = 9;
                            break;
                        }
                        orientationToSet = 1;
                        break;
                        break;
                    case 2:
                        if (rotation != 0 && rotation != 1) {
                            orientationToSet = 8;
                            break;
                        }
                        orientationToSet = 0;
                        break;
                }
                options.orientation = orientationToSet;
            }
        }
        if (this.videoOptionsJSON != null) {
            try {
                com.nativex.monetization.mraid.objects.VideoPlayerOptions videoPlayerOptions = (com.nativex.monetization.mraid.objects.VideoPlayerOptions) new Gson().fromJson(this.videoOptionsJSON, com.nativex.monetization.mraid.objects.VideoPlayerOptions.class);
                if (videoPlayerOptions != null) {
                    if (videoPlayerOptions.isAllowMute()) {
                        options.allowMute = videoPlayerOptions.isAllowMute();
                    }
                    if (videoPlayerOptions.isStartMuted()) {
                        options.startMuted = videoPlayerOptions.isStartMuted();
                    }
                    if (videoPlayerOptions.getAllowSkipAfterMilliseconds() != Integer.MIN_VALUE) {
                        options.allowSkipAfterMilliseconds = videoPlayerOptions.getAllowSkipAfterMilliseconds();
                    }
                    if (videoPlayerOptions.getCountdownAfterMilliseconds() != Integer.MIN_VALUE) {
                        options.countdownAfterMilliseconds = videoPlayerOptions.getCountdownAfterMilliseconds();
                    }
                    if (videoPlayerOptions.getAllowSkipAfterVideoStuckForMilliseconds() != Integer.MIN_VALUE) {
                        options.allowSkipAfterVideoStuckForMilliseconds = videoPlayerOptions.getAllowSkipAfterVideoStuckForMilliseconds();
                    }
                    if (videoPlayerOptions.getIconMaximumDimensionInDIP() != Integer.MIN_VALUE) {
                        options.controlIconMaxDimensionInDensityIndependentPixels = videoPlayerOptions.getIconMaximumDimensionInDIP();
                    }
                    if (videoPlayerOptions.getIconsDistanceFromScreenEdgeInDIP() != Integer.MIN_VALUE) {
                        options.controlsDistanceFromScreenEdgeInDensityIndependentPixels = videoPlayerOptions.getIconsDistanceFromScreenEdgeInDIP();
                    }
                    if (videoPlayerOptions.getCountdownMessageTextColor() != null) {
                        try {
                            options.countdownMessageTextColor = Color.parseColor(videoPlayerOptions.getCountdownMessageTextColor());
                        } catch (IllegalArgumentException e) {
                        }
                    }
                    if (videoPlayerOptions.getCountdownMessageFormat() != null) {
                        options.countdownMessageFormat = videoPlayerOptions.getCountdownMessageFormat();
                    }
                    if (videoPlayerOptions.getSpecialSkipCountdownMessageFormat() != null) {
                        options.specialSkipCountdownMessageFormat = videoPlayerOptions.getSpecialSkipCountdownMessageFormat();
                    }
                    if (videoPlayerOptions.getErrorMessageToast() != null) {
                        options.errorMessageToast = videoPlayerOptions.getErrorMessageToast();
                    }
                    if (videoPlayerOptions.getControlsAlpha() != Integer.MIN_VALUE) {
                        options.controlsAlpha = videoPlayerOptions.getControlsAlpha();
                    }
                }
            } catch (Exception e2) {
                MRAIDLogger.d("Exception in parsing video options JSON. " + e2.getClass().getCanonicalName());
            }
        }
        return options;
    }
}
