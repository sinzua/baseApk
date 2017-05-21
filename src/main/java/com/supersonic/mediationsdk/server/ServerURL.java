package com.supersonic.mediationsdk.server;

import android.text.TextUtils;
import android.util.Pair;
import com.supersonic.mediationsdk.config.ConfigFile;
import com.supersonic.mediationsdk.utils.SupersonicUtils;
import com.supersonicads.sdk.precache.DownloadManager;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Vector;

public class ServerURL {
    private static final String AMPERSAND = "&";
    public static final String APPLICATION_KEY = "applicationKey";
    public static final String APPLICATION_USER_ID = "applicationUserId";
    private static String BASE_URL_PREFIX = "https://init.supersonicads.com/api/rest/v";
    private static String BASE_URL_SUFFIX = "/ultra/cpv?platform=android&";
    private static final String EQUAL = "=";
    public static final String GAID = "advId";
    private static final String IMPRESSION = "impression";
    private static final String PLACEMENT = "placementId";
    public static final String PLUGIN_TYPE = "pluginType";
    public static final String PLUGIN_VERSION = "pluginVersion";
    public static final String SDK_VERSION = "sdkVersion";

    public static String getUniqueUsersURL(String applicationKey, String applicationUserId) throws UnsupportedEncodingException {
        Vector<Pair<String, String>> array = new Vector();
        array.add(new Pair("applicationKey", applicationKey));
        array.add(new Pair("applicationUserId", applicationUserId));
        array.add(new Pair("sdkVersion", SupersonicUtils.getSDKVersion()));
        return Server.getUniqueUsersBaseURL() + createURLParams(array);
    }

    public static String getCPVProvidersURL(String applicationKey, String applicationUserId, String gaid) throws UnsupportedEncodingException {
        Vector<Pair<String, String>> array = new Vector();
        array.add(new Pair("applicationKey", applicationKey));
        array.add(new Pair("applicationUserId", applicationUserId));
        array.add(new Pair("sdkVersion", SupersonicUtils.getSDKVersion()));
        if (!TextUtils.isEmpty(ConfigFile.getConfigFile().getPluginType())) {
            array.add(new Pair(PLUGIN_TYPE, ConfigFile.getConfigFile().getPluginType()));
        }
        if (!TextUtils.isEmpty(ConfigFile.getConfigFile().getPluginVersion())) {
            array.add(new Pair(PLUGIN_VERSION, ConfigFile.getConfigFile().getPluginVersion()));
        }
        if (!TextUtils.isEmpty(gaid)) {
            array.add(new Pair(GAID, gaid));
        }
        return getBaseUrl(SupersonicUtils.getSDKVersion()) + createURLParams(array);
    }

    public static String getRequestURL(String requestUrl, boolean hit, int placementId) throws UnsupportedEncodingException {
        Vector<Pair<String, String>> array = new Vector();
        array.add(new Pair(IMPRESSION, Boolean.toString(hit)));
        array.add(new Pair(PLACEMENT, Integer.toString(placementId)));
        return requestUrl + "&" + createURLParams(array);
    }

    public static String createURLParams(Vector<Pair<String, String>> array) throws UnsupportedEncodingException {
        String str = "";
        Iterator i$ = array.iterator();
        while (i$.hasNext()) {
            Pair<String, String> pair = (Pair) i$.next();
            if (str.length() > 0) {
                str = str + "&";
            }
            str = str + ((String) pair.first) + "=" + URLEncoder.encode((String) pair.second, DownloadManager.UTF8_CHARSET);
        }
        return str;
    }

    private static String getBaseUrl(String sdkVersion) {
        return BASE_URL_PREFIX + sdkVersion + BASE_URL_SUFFIX;
    }
}
