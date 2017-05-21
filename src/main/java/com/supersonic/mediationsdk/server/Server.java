package com.supersonic.mediationsdk.server;

import android.util.Log;
import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import com.supersonic.mediationsdk.logger.SupersonicLoggerManager;
import com.supersonic.mediationsdk.logger.ThreadExceptionHandler;
import org.json.JSONObject;

public class Server {
    private static String TAG = "ServerUtils";
    private static String UniqueUsersURL = "https://ua.supersonicads.com/api/rest/v1.1/uniqueusers?";

    protected static String getUniqueUsersBaseURL() {
        return UniqueUsersURL;
    }

    public static void notifyUniqueUser(final String applicationKey, final String applicationUserId) {
        new Thread(new Runnable() {
            JSONObject obj = null;

            public void run() {
                try {
                    this.obj = new JSONObject(HttpFunctions.getStringFromURL(ServerURL.getUniqueUsersURL(applicationKey, applicationUserId)));
                } catch (Throwable e) {
                    StringBuilder logMessage = new StringBuilder("notifyUniqueUser(appKey:");
                    if (applicationKey != null) {
                        logMessage.append(applicationKey);
                    } else {
                        logMessage.append("null");
                    }
                    logMessage.append(", userId:");
                    if (applicationUserId != null) {
                        logMessage.append(applicationUserId);
                    } else {
                        logMessage.append("null");
                    }
                    logMessage.append(")");
                    if (e != null) {
                        SupersonicLoggerManager.getLogger().log(SupersonicTag.NETWORK, logMessage.toString() + ", e:" + Log.getStackTraceString(e), 0);
                    } else {
                        SupersonicLoggerManager.getLogger().log(SupersonicTag.NETWORK, logMessage.toString(), 0);
                    }
                }
            }
        }).start();
    }

    public static JSONObject callRequestURL(String requestUrl, boolean hit, int placementId) {
        Throwable e;
        StringBuilder builder;
        JSONObject obj = null;
        try {
            JSONObject obj2 = new JSONObject(HttpFunctions.getStringFromURL(ServerURL.getRequestURL(requestUrl, hit, placementId)));
            try {
                SupersonicLoggerManager.getLogger().log(SupersonicTag.NETWORK, "callRequestURL(reqUrl:" + requestUrl + ", " + "hit:" + hit + ")", 1);
                return obj2;
            } catch (Throwable th) {
                e = th;
                obj = obj2;
                builder = new StringBuilder("callRequestURL(reqUrl:");
                if (requestUrl != null) {
                    builder.append(requestUrl);
                } else {
                    builder.append("null");
                }
                builder.append(", hit:").append(hit).append(")");
                if (e == null) {
                    SupersonicLoggerManager.getLogger().log(SupersonicTag.NETWORK, builder.toString(), 0);
                    return obj;
                }
                SupersonicLoggerManager.getLogger().log(SupersonicTag.NETWORK, builder.toString() + ", e:" + Log.getStackTraceString(e), 0);
                return obj;
            }
        } catch (Throwable th2) {
            e = th2;
            builder = new StringBuilder("callRequestURL(reqUrl:");
            if (requestUrl != null) {
                builder.append("null");
            } else {
                builder.append(requestUrl);
            }
            builder.append(", hit:").append(hit).append(")");
            if (e == null) {
                SupersonicLoggerManager.getLogger().log(SupersonicTag.NETWORK, builder.toString() + ", e:" + Log.getStackTraceString(e), 0);
                return obj;
            }
            SupersonicLoggerManager.getLogger().log(SupersonicTag.NETWORK, builder.toString(), 0);
            return obj;
        }
    }

    public static void callAsyncRequestURL(final String requestUrl, final boolean hit, final int placementId) {
        Thread asyncRequestURL = new Thread(new Runnable() {
            public void run() {
                Server.callRequestURL(requestUrl, hit, placementId);
            }
        }, "callAsyncRequestURL");
        asyncRequestURL.setUncaughtExceptionHandler(new ThreadExceptionHandler());
        asyncRequestURL.start();
    }
}
