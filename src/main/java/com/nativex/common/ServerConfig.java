package com.nativex.common;

import com.nativex.common.JsonRequestConstants.UniversalQueryParameters;
import com.nativex.monetization.manager.MonetizationSharedDataManager;
import java.util.HashMap;
import java.util.Map;

public final class ServerConfig {
    private static final String PRODUCTION_SERVER_DOMAIN = "appclick.co";
    private static final String TEST_HOST_NAME = "nativex-sdk-testapi.appspot.com";
    private static boolean isTestEndPointEnabled = false;
    private static String serverUrl = null;

    public static final String applyConfiguration(String url, boolean https) {
        String urlToApply = serverUrl;
        if (urlToApply == null) {
            urlToApply = "http://appclick.co/";
        }
        if (https && urlToApply.equals("http://appclick.co/")) {
            urlToApply = "https://appclick.co/";
        }
        if (isTestEndPointEnabled) {
            urlToApply = "http://nativex-sdk-testapi.appspot.com/";
        }
        if (!url.startsWith(urlToApply)) {
            url = urlToApply + url;
        }
        Map<String, String> params = new HashMap();
        params.put(UniversalQueryParameters.APP_ID, MonetizationSharedDataManager.getAppId());
        return Utilities.appendParamsToUrl(url, params);
    }

    public static void setTestEndpointEnabled(boolean enabled) {
        isTestEndPointEnabled = enabled;
    }

    public static final String applyHttp(String url, boolean https) {
        if (url.startsWith("http")) {
            return url;
        }
        if (https) {
            return "https://" + url;
        }
        return "http://" + url;
    }
}
