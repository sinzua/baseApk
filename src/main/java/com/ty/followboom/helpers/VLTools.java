package com.ty.followboom.helpers;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Proxy;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.forwardwin.base.widgets.JsonSerializer;
import com.forwardwin.base.widgets.PreferenceHelper;
import com.lidroid.xutils.http.RequestParams;
import com.ty.entities.PostItem;
import com.ty.followboom.CoinsHistoryActivity;
import com.ty.followboom.FAQActivity;
import com.ty.followboom.LoginActivity;
import com.ty.followboom.MainActivity;
import com.ty.followboom.OrderStatusActivity;
import com.ty.followboom.PostActivity;
import com.ty.followboom.TrackerFollowlistActivity;
import com.ty.followboom.entities.ActionLimitParams;
import com.ty.followboom.models.UserInfoManager;
import com.ty.instaview.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.codehaus.jackson.util.BufferRecycler;

public class VLTools {
    public static final String AUTHENTICATE = "Authenticate";
    public static final long DEFAULT_RATE_US_THREHOLD = 10000;
    public static final int DEFAULT_REQUEST_DELAY = 0;
    public static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;
    private static final String GOOGLE_PLAY_ACTIVITY_NAME = "com.google.android.finsky.activities.LaunchUrlHandlerActivity";
    private static final String GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending";
    private static final String MARKET_CLIENT_URL_PREFIX = "market://details?id=";
    public static final int OVER_LIMIT_THIS_HOUR = 2;
    public static final int OVER_LIMIT_THIS_MINUTE = 1;
    public static final int OVER_LIMIT_TODAY = 3;
    public static final int STATUS_OK = 0;
    public static final String TAG = VLTools.class.getName();

    public static void gotoMainActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void gotoMainActivity(Activity activity, int tabIndex) {
        Intent intent = new Intent();
        intent.setClass(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public static void gotoLogin(Activity activity) {
        Toast.makeText(activity, "Token expired, please log in again", 0).show();
        Intent intent = new Intent();
        intent.setClass(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public static void gotoOrderStatus(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, OrderStatusActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public static void gotoCoinsHistory(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, CoinsHistoryActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public static void gotoFAQ(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, FAQActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public static void gotoPostActivity(Activity activity, PostItem videoPost, int type) {
        Intent intent = new Intent();
        intent.setClass(activity, PostActivity.class);
        intent.putExtra(PostActivity.VIDEO_POST_PARAMS, JsonSerializer.getInstance().serialize(videoPost));
        intent.putExtra("type", type);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public static void gotoTrackerFollowlistActivity(Activity activity, int type) {
        Intent intent = new Intent();
        intent.setClass(activity, TrackerFollowlistActivity.class);
        intent.putExtra("type", type);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public static void setToggleButtonText(ToggleButton togglebutton, String text) {
        togglebutton.setText(text);
        togglebutton.setTextOn(text);
        togglebutton.setTextOff(text);
    }

    public static <T> boolean verifyArrayList(ArrayList<T> list) {
        return list != null && list.size() > 0;
    }

    public static boolean isMobileNO(String mobiles) {
        if (TextUtils.isEmpty(mobiles) || mobiles.length() != 11) {
            return false;
        }
        return mobiles.matches("[1][358]\\d{9}");
    }

    public static int dipToPixel(int dip) {
        if (dip < 0) {
            return -((int) ((((float) (-dip)) * DENSITY) + 0.5f));
        }
        return (int) ((((float) dip) * DENSITY) + 0.5f);
    }

    public static String removePointIfHave(String price) {
        int firstIndexOfPoint = price.indexOf(".");
        if (firstIndexOfPoint <= 0 || firstIndexOfPoint >= price.length()) {
            return price;
        }
        return price.substring(0, price.indexOf("."));
    }

    public static void showLoadingView(View loadingView, View loadingItem, View loadingFailedView) {
        loadingView.setVisibility(0);
        loadingItem.setVisibility(0);
        loadingFailedView.setVisibility(8);
    }

    public static void hideLoadingView(View loadingView, View loadingItem, View loadingFailedView) {
        loadingView.setVisibility(8);
        loadingItem.setVisibility(0);
        loadingFailedView.setVisibility(8);
    }

    public static void showLoadingFailedView(View loadingView, View loadingItem, View loadingFailedView) {
        loadingView.setVisibility(0);
        loadingItem.setVisibility(8);
        loadingFailedView.setVisibility(0);
    }

    public static void buildRequestHeader(Context context, RequestParams params) {
        params.addHeader("mobileType", "android");
        params.addHeader("systemVersion", getPhoneInfo());
        params.addHeader("appVersion", getAppInfo(context));
        params.addHeader("Content-Type", "application/json; charset=utf-8");
    }

    public static String getPhoneInfo() {
        return Build.MODEL + "/" + VERSION.SDK + "/" + VERSION.RELEASE;
    }

    public static String getAppInfo(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info == null ? "0" : info.versionCode + "";
        } catch (NameNotFoundException e) {
            return "";
        }
    }

    public static String getDeviceId(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        } catch (Exception e) {
            return "";
        }
    }

    private static String getNextDailyLoginTime() {
        Calendar c = Calendar.getInstance();
        c.add(6, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return c.getTimeInMillis() + "";
    }

    public static void updateVersion(Context context) {
        launchMarketApp(context, MARKET_CLIENT_URL_PREFIX + context.getPackageName());
    }

    public static void rateUs(Context context) {
        launchMarketApp(context, MARKET_CLIENT_URL_PREFIX + context.getPackageName());
    }

    public static void emailUs(Context context) {
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:vinegrabfree@yahoo.com"));
        intent.putExtra("android.intent.extra.SUBJECT", String.format("%s an feedback uid %s un %s", new Object[]{context.getResources().getString(R.string.app_name), UserInfoManager.getSingleton().getUserInfo(context).getUserId(), UserInfoManager.getSingleton().getUserInfo(context).getUsername()}));
        context.startActivity(Intent.createChooser(intent, "Choose"));
    }

    private static void launchMarketApp(Context context, String marketClientUrl) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.android.vending");
        if (intent != null) {
            intent.setComponent(new ComponentName("com.android.vending", GOOGLE_PLAY_ACTIVITY_NAME));
            intent.setData(Uri.parse(marketClientUrl));
            try {
                context.startActivity(intent);
                return;
            } catch (ActivityNotFoundException e) {
            }
        }
        intent = new Intent("android.intent.action.VIEW", Uri.parse(marketClientUrl));
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 65536);
        if (resolveInfos != null && resolveInfos.size() > 0) {
            context.startActivity(Intent.createChooser(intent, ""));
        }
    }

    public boolean proxyStatus() {
        return TextUtils.isEmpty(Proxy.getDefaultHost());
    }

    public static boolean isAmazon(Activity activity) {
        return activity.getApplication().getPackageName().contains("amazon");
    }

    public static boolean isYandex(Activity activity) {
        return activity.getApplication().getPackageName().contains("yandex");
    }

    public static boolean isGoogle(Activity activity) {
        return activity.getApplication().getPackageName().contains("google") || activity.getApplication().getPackageName().equals("com.ty.topvideo");
    }

    public static void logout(Activity activity) {
        UserInfoManager.getSingleton().logout(activity);
        AppConfigHelper.saveUserData(activity, null);
        gotoLogin(activity);
    }

    public static int checkLikeTime(Context context) {
        ActionLimitParams likeActionLimitParams = (ActionLimitParams) JsonSerializer.getInstance().deserialize(PreferenceHelper.getContent(context, "appinfo", "like_limit_params"), ActionLimitParams.class);
        if (likeActionLimitParams == null) {
            likeActionLimitParams = new ActionLimitParams();
        }
        ArrayList<Integer> likeLimitArray = initLimitArray(PreferenceHelper.getContent(context, "appinfo", "like_limit_array"));
        if (likeActionLimitParams.getStartTimeInMinute() == 0) {
            likeActionLimitParams.setStartTimeInMinute(System.currentTimeMillis());
        }
        if (likeActionLimitParams.getStartTimeInHour() == 0) {
            likeActionLimitParams.setStartTimeInHour(System.currentTimeMillis());
        }
        if (likeActionLimitParams.getStartTimeInDay() == 0) {
            likeActionLimitParams.setStartTimeInDay(System.currentTimeMillis());
        }
        long curTime = System.currentTimeMillis();
        if (curTime - likeActionLimitParams.getStartTimeInDay() < ErrorReporter.MAX_REPORT_AGE && likeActionLimitParams.getCountInDay() > ((Integer) likeLimitArray.get(2)).intValue()) {
            PreferenceHelper.saveContent(context, "appinfo", "like_limit_params", JsonSerializer.getInstance().serialize(likeActionLimitParams));
            return 3;
        } else if (curTime - likeActionLimitParams.getStartTimeInHour() < 3600000 && likeActionLimitParams.getCountInHour() > ((Integer) likeLimitArray.get(1)).intValue()) {
            PreferenceHelper.saveContent(context, "appinfo", "like_limit_params", JsonSerializer.getInstance().serialize(likeActionLimitParams));
            return 2;
        } else if (curTime - likeActionLimitParams.getStartTimeInMinute() >= 60000 || likeActionLimitParams.getCountInMinute() <= ((Integer) likeLimitArray.get(0)).intValue()) {
            if (curTime - likeActionLimitParams.getStartTimeInDay() > ErrorReporter.MAX_REPORT_AGE) {
                likeActionLimitParams.setCountInDay(0);
                likeActionLimitParams.setStartTimeInDay(0);
            }
            if (curTime - likeActionLimitParams.getStartTimeInHour() > 3600000) {
                likeActionLimitParams.setCountInHour(0);
                likeActionLimitParams.setStartTimeInHour(0);
            }
            if (curTime - likeActionLimitParams.getStartTimeInMinute() > 60000) {
                likeActionLimitParams.setCountInMinute(0);
                likeActionLimitParams.setStartTimeInMinute(0);
            }
            likeActionLimitParams.setCountInDay(likeActionLimitParams.getCountInDay() + 1);
            likeActionLimitParams.setCountInHour(likeActionLimitParams.getCountInHour() + 1);
            likeActionLimitParams.setCountInMinute(likeActionLimitParams.getCountInMinute() + 1);
            PreferenceHelper.saveContent(context, "appinfo", "like_limit_params", JsonSerializer.getInstance().serialize(likeActionLimitParams));
            return 0;
        } else {
            PreferenceHelper.saveContent(context, "appinfo", "like_limit_params", JsonSerializer.getInstance().serialize(likeActionLimitParams));
            return 1;
        }
    }

    private static ArrayList<Integer> initLimitArray(String limitArrayJson) {
        ArrayList<Integer> limitArray = new ArrayList();
        if (TextUtils.isEmpty(limitArrayJson)) {
            limitArray.add(Integer.valueOf(30));
            limitArray.add(Integer.valueOf(200));
            limitArray.add(Integer.valueOf(BufferRecycler.DEFAULT_WRITE_CONCAT_BUFFER_LEN));
            return limitArray;
        }
        try {
            return (ArrayList) JsonSerializer.getInstance().deserialize(limitArrayJson, ArrayList.class, Integer.class);
        } catch (Exception e) {
            limitArray.add(Integer.valueOf(30));
            limitArray.add(Integer.valueOf(200));
            limitArray.add(Integer.valueOf(BufferRecycler.DEFAULT_WRITE_CONCAT_BUFFER_LEN));
            return limitArray;
        }
    }

    public static int checkFollowTime(Context context) {
        ActionLimitParams likeActionLimitParams = (ActionLimitParams) JsonSerializer.getInstance().deserialize(PreferenceHelper.getContent(context, "appinfo", "follow_limit_params"), ActionLimitParams.class);
        if (likeActionLimitParams == null) {
            likeActionLimitParams = new ActionLimitParams();
        }
        ArrayList<Integer> followLimitArray = initLimitArray(PreferenceHelper.getContent(context, "appinfo", "follow_limit_array"));
        if (likeActionLimitParams.getStartTimeInMinute() == 0) {
            likeActionLimitParams.setStartTimeInMinute(System.currentTimeMillis());
        }
        if (likeActionLimitParams.getStartTimeInHour() == 0) {
            likeActionLimitParams.setStartTimeInHour(System.currentTimeMillis());
        }
        if (likeActionLimitParams.getStartTimeInDay() == 0) {
            likeActionLimitParams.setStartTimeInDay(System.currentTimeMillis());
        }
        long curTime = System.currentTimeMillis();
        if (curTime - likeActionLimitParams.getStartTimeInDay() < ErrorReporter.MAX_REPORT_AGE && likeActionLimitParams.getCountInDay() > ((Integer) followLimitArray.get(2)).intValue()) {
            PreferenceHelper.saveContent(context, "appinfo", "follow_limit_params", JsonSerializer.getInstance().serialize(likeActionLimitParams));
            return 3;
        } else if (curTime - likeActionLimitParams.getStartTimeInHour() < 3600000 && likeActionLimitParams.getCountInHour() > ((Integer) followLimitArray.get(1)).intValue()) {
            PreferenceHelper.saveContent(context, "appinfo", "follow_limit_params", JsonSerializer.getInstance().serialize(likeActionLimitParams));
            return 2;
        } else if (curTime - likeActionLimitParams.getStartTimeInMinute() >= 60000 || likeActionLimitParams.getCountInMinute() <= ((Integer) followLimitArray.get(0)).intValue()) {
            if (curTime - likeActionLimitParams.getStartTimeInDay() > ErrorReporter.MAX_REPORT_AGE) {
                likeActionLimitParams.setCountInDay(0);
                likeActionLimitParams.setStartTimeInDay(0);
            }
            if (curTime - likeActionLimitParams.getStartTimeInHour() > 3600000) {
                likeActionLimitParams.setCountInHour(0);
                likeActionLimitParams.setStartTimeInHour(0);
            }
            if (curTime - likeActionLimitParams.getStartTimeInMinute() > 60000) {
                likeActionLimitParams.setCountInMinute(0);
                likeActionLimitParams.setStartTimeInMinute(0);
            }
            likeActionLimitParams.setCountInDay(likeActionLimitParams.getCountInDay() + 1);
            likeActionLimitParams.setCountInHour(likeActionLimitParams.getCountInHour() + 1);
            likeActionLimitParams.setCountInMinute(likeActionLimitParams.getCountInMinute() + 1);
            PreferenceHelper.saveContent(context, "appinfo", "follow_limit_params", JsonSerializer.getInstance().serialize(likeActionLimitParams));
            return 0;
        } else {
            PreferenceHelper.saveContent(context, "appinfo", "follow_limit_params", JsonSerializer.getInstance().serialize(likeActionLimitParams));
            return 1;
        }
    }

    public static boolean inReview(Context context) {
        return false;
    }
}
