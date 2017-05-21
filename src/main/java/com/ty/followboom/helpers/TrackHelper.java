package com.ty.followboom.helpers;

import android.content.Context;

public class TrackHelper {
    public static final String ACTION_ADD_COINS = "add_coins";
    public static final String ACTION_AVATAR = "action_avatar";
    public static final String ACTION_BACK = "back";
    public static final String ACTION_BUTTON_POSITIVE = "button_positive";
    public static final String ACTION_BUTTON_SKIP = "button_skip";
    public static final String ACTION_BUY = "buy";
    public static final String ACTION_CANCEL = "buy_cancel";
    public static final String ACTION_COINS_HISTORY = "coins_history";
    public static final String ACTION_CONFIRM = "buy_confirm";
    public static final String ACTION_CONTENT_EMPTY = "content_empty";
    public static final String ACTION_CONTENT_FOLLOW = "content_follow";
    public static final String ACTION_CONTENT_LIKE = "content_like";
    public static final String ACTION_CONTENT_MIX = "content_mix";
    public static final String ACTION_DOWNLOAD = "action_download";
    public static final String ACTION_DOWNLOAD_ALL = "action_download_all";
    public static final String ACTION_DOWNLOAD_FRAGMENT = "action_download_fragment";
    public static final String ACTION_EMAIL_US = "action_email_us";
    public static final String ACTION_FAQ = "action_faq";
    public static final String ACTION_FOLLOWERS = "action_followers";
    public static final String ACTION_FOLLOWINGS = "action_followings";
    public static final String ACTION_FOLlOW = "follow";
    public static final String ACTION_FRAGMENT = "action_fragment";
    public static final String ACTION_FREE_COINS = "free_coins";
    public static final String ACTION_GET_FOLLOWERS = "get_followers";
    public static final String ACTION_GET_LIKES = "get_likes";
    public static final String ACTION_ITEM = "item";
    public static final String ACTION_LIKE = "like";
    public static final String ACTION_LOADMORE = "loadmore";
    public static final String ACTION_LOGOUT = "action_logout";
    public static final String ACTION_NATIVEX = "action_nativex";
    public static final String ACTION_ORDER_STATUS = "order_status";
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PURCHASE = "purchase";
    public static final String ACTION_RATE = "action_rate";
    public static final String ACTION_RATE_US = "rate_us";
    public static final String ACTION_REPORT = "action_report";
    public static final String ACTION_REPOST = "action_repost";
    public static final String ACTION_SHARE_APP = "action_share_app";
    public static final String ACTION_SHOW = "show";
    public static final String ACTION_SIDERBAR = "action_sidebar";
    public static final String ACTION_SUPERSONIC = "action_supersonic";
    public static final String ACTION_TAB_FOLlOW = "tab_follow";
    public static final String ACTION_TAB_LIKE = "tab_like";
    public static final String ACTION_TAB_MIX = "tab_mix";
    public static final String ACTION_TAB_PEOPLE = "people";
    public static final String ACTION_TAB_TAGS = "tags";
    public static final String ACTION_TAB_VIEW = "tab_view";
    public static final String ACTION_TRACK = "track_action";
    public static final String ACTION_UPDATE_OFFERWALL = "update_offerwall";
    public static final String ACTION_USERINFO = "action_userinfo";
    public static final String CATEGORY_ACTION = "Action";
    public static final String CATEGORY_COINS_HISTORY = "CoinsHistory";
    public static final String CATEGORY_DOWNLOAD = "category_download";
    public static final String CATEGORY_FEED = "category_feed";
    public static final String CATEGORY_GET_COINS = "GetCoinsFragment";
    public static final String CATEGORY_GET_FOLLOWERS = "GetFollowersFragment";
    public static final String CATEGORY_GET_LIKES = "GetLikesFragment";
    public static final String CATEGORY_GET_LIKES_ACTIVITY = "GetLikesActivity";
    public static final String CATEGORY_GET_VIEWS = "GetViewsFragment";
    public static final String CATEGORY_GOOGLE_BILLING = "GoogleBilling";
    public static final String CATEGORY_MAIN = "category_main";
    public static final String CATEGORY_ORDER_STATUS = "OrderStatusHistory";
    public static final String CATEGORY_POPULAR = "category_popular";
    public static final String CATEGORY_RATE = "category_rate";
    public static final String CATEGORY_SEARCH = "category_search";
    public static final String CATEGORY_SETTINGS = "category_settings";
    public static final String CATEGORY_STORE = "StoreFragment";
    public static final String CATEGORY_TRACKER = "TrackerFragment";
    public static final String CATEGORY_USERMAIN = "category_usermain";
    public static final String CLICK = "click";
    public static final String LABEL_CLICK = "click";
    public static final String LABEL_FAILED = "failed";
    public static final String LABEL_LOGIN = "login";
    public static final String LABEL_NEGATIVE = "negative";
    public static final String LABEL_POSITIVE = "positive";
    public static final String LABEL_PROFILE = "my_profile";
    public static final String LABEL_SHOW = "show";
    public static final String LABEL_SUCCEED = "succeed";

    public static void initialize(Context context) {
        ParseHelper.initialize(context);
        FlurryHelper.initialize(context);
    }

    public static void track(String category, String action, String label) {
        ParseHelper.track(category, action, label);
        FlurryHelper.track(category, action, label);
    }
}
