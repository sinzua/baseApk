package com.ty.followboom.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Proxy;
import android.text.TextUtils;
import com.forwardwin.base.widgets.JsonSerializer;
import com.forwardwin.base.widgets.PreferenceHelper;
import com.ty.followboom.entities.AccountInfo;
import com.ty.followboom.entities.LoginData;
import com.ty.followboom.entities.OfferWallInfo;
import com.ty.followboom.entities.QueryOfferWallResult;
import com.ty.followboom.entities.TaskRewards;
import com.ty.followboom.entities.UserInfo;
import java.util.ArrayList;
import java.util.Iterator;

public class AppConfigHelper {
    public static final String APP_CONFIG = "app_config";
    public static final String APP_VERSION = "2";
    private static final String DEFAULT_RATE_STRING = "Rate Us 5 Star";
    public static final String DOWNLOAD_FILE_LIST = "download_file_list";
    public static final String IG_USER_DATA = "ig_user_data";
    public static final String IN_REVIEW_VERSION = "in_review_version";
    public static final String KEY_APPINFO = "appinfo";
    public static final String KEY_USERINFO = "userinfo";
    public static final String LAST_RATE_VERSION = "last_rate_version";
    public static final String NATIVEX_APPID = "86920";
    public static final String OFFER_WALL_INFO = "offer_wall_info";
    public static final String PACKAGENAME = "instaview";
    public static final String SUPERSONIC_APP_KEY = "4f6c5a55";
    public static final String USERNAME = "username";
    private static SharedPreferences mySharedPreferences;

    public static void saveUserName(Context context, String userName) {
        PreferenceHelper.saveContent(context, "userinfo", "username", userName);
    }

    public static String getUserName(Context context) {
        return PreferenceHelper.getContent(context, "userinfo", "username");
    }

    public static UserInfo getUserInfo(Context context) {
        return (UserInfo) JsonSerializer.getInstance().deserialize(PreferenceHelper.getContent(context, "userinfo", IG_USER_DATA), UserInfo.class);
    }

    public static AccountInfo getAccountInfo(Context context) {
        AccountInfo accountInfo = new AccountInfo();
        if (!(getAppConfig(context) == null || TextUtils.isEmpty(getAppConfig(context).getCoins()))) {
            accountInfo.setAccountLevel(getAppConfig(context).getAccountLevel());
            accountInfo.setCoins(getAppConfig(context).getCoins());
        }
        return accountInfo;
    }

    public static void saveUserData(Context context, String value) {
        PreferenceHelper.saveContent(context, "userinfo", IG_USER_DATA, value);
    }

    public static LoginData getAppConfig(Context context) {
        return (LoginData) JsonSerializer.getInstance().deserialize(PreferenceHelper.getContent(context, "appinfo", APP_CONFIG), LoginData.class);
    }

    public static void saveAppConfig(Context context, String appConfig) {
        PreferenceHelper.saveContent(context, "appinfo", APP_CONFIG, appConfig);
    }

    public static String getRateString(Context context) {
        return getAppConfig(context) == null ? DEFAULT_RATE_STRING : getAppConfig(context).getRateString();
    }

    public static int getRateRewardCoins(Context context) {
        if (getAppConfig(context) == null) {
            return 0;
        }
        Iterator it = getAppConfig(context).getTaskRewards().iterator();
        while (it.hasNext()) {
            TaskRewards taskReward = (TaskRewards) it.next();
            if (taskReward.getRewardTypeId() == 0) {
                return taskReward.getRewardCoin();
            }
        }
        return 0;
    }

    public static String getFAQUrl(Context context) {
        return (getAppConfig(context) == null || TextUtils.isEmpty(getAppConfig(context).getFaqUrl())) ? InstagramHelper.FAQ_URL : getAppConfig(context).getFaqUrl();
    }

    public static boolean showAdmob(Context context) {
        if (isPro(context)) {
            return false;
        }
        if (getAppConfig(context) == null || TextUtils.isEmpty(getAppConfig(context).getAdmobEnabled()) || getAppConfig(context).getAdmobEnabled().equals("1")) {
            return true;
        }
        return false;
    }

    public static boolean isPro(Context context) {
        return "instaview".contains("pro");
    }

    public static boolean allowT(Context context) {
        return TextUtils.isEmpty(Proxy.getDefaultHost()) || isApkDebugable(context);
    }

    public static boolean isApkDebugable(Context context) {
        try {
            if ((context.getApplicationInfo().flags & 2) != 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getCoins(Context context) {
        return (getUserInfo(context) == null || TextUtils.isEmpty(getUserInfo(context).getAccountInfo().getCoins())) ? "0" : getUserInfo(context).getAccountInfo().getCoins();
    }

    public static String getLastRateVersion(Context context) {
        return PreferenceHelper.getContent(context, "userinfo", "last_rate_version");
    }

    public static void saveRateVersion(Context context, String currentVersion) {
        PreferenceHelper.saveContent(context, "userinfo", "last_rate_version", currentVersion);
    }

    public static String getInReviewVersion(Context context) {
        return getAppConfig(context) == null ? VLTools.getAppInfo(context) : getAppConfig(context).getInrVersion();
    }

    public static void saveInReviewVersion(Context context, String currentVersion) {
        PreferenceHelper.saveContent(context, "userinfo", IN_REVIEW_VERSION, currentVersion);
    }

    public static ArrayList<String> getOfferWallList(Context context) {
        return new ArrayList();
    }

    public static String getDefaultFreeCoin(Context context) {
        return getAppConfig(context).getDefaultFreeCoin();
    }

    public static void saveOfferWallItemByName(Context context, String offerwallPlatform, QueryOfferWallResult queryOfferWallResult) {
        OfferWallInfo offerWallInfo = getOfferWallInfo(context);
        offerWallInfo.getOfferWallInfo().put(offerwallPlatform, queryOfferWallResult);
        saveOfferWallInfo(context, offerWallInfo);
    }

    public static QueryOfferWallResult getOfferWallItemByName(Context context, String offerwallPlatform) {
        if (getOfferWallInfo(context).getOfferWallInfo() == null || !getOfferWallInfo(context).getOfferWallInfo().containsKey(offerwallPlatform)) {
            return null;
        }
        return (QueryOfferWallResult) getOfferWallInfo(context).getOfferWallInfo().get(offerwallPlatform);
    }

    public static void saveOfferWallInfo(Context context, OfferWallInfo offerWallInfoJson) {
        PreferenceHelper.saveContent(context, "appinfo", OFFER_WALL_INFO, JsonSerializer.getInstance().serialize(offerWallInfoJson));
    }

    public static OfferWallInfo getOfferWallInfo(Context context) {
        if (!TextUtils.isEmpty(PreferenceHelper.getContent(context, "appinfo", OFFER_WALL_INFO))) {
            return (OfferWallInfo) JsonSerializer.getInstance().deserialize(PreferenceHelper.getContent(context, "appinfo", OFFER_WALL_INFO), OfferWallInfo.class);
        }
        OfferWallInfo offerWallInfoTmp = new OfferWallInfo();
        saveOfferWallInfo(context, offerWallInfoTmp);
        return offerWallInfoTmp;
    }
}
