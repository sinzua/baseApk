package com.supersonicads.sdk.agent;

import android.content.Context;
import android.text.TextUtils;
import com.supersonicads.sdk.SSAAdvertiserTest;
import com.supersonicads.sdk.data.SSAObj;
import com.supersonicads.sdk.precache.DownloadManager;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import com.supersonicads.sdk.utils.Logger;
import com.supersonicads.sdk.utils.SDKUtils;
import com.supersonicads.sdk.utils.SupersonicSharedPrefHelper;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URL;

public class SupersonicAdsAdvertiserAgent implements SSAAdvertiserTest {
    private static final String BUNDLE_ID = "bundleId";
    private static final String DEVICE_IDS = "deviceIds";
    private static final String DOMAIN = "/campaigns/onLoad?";
    private static String PACKAGE_NAME = null;
    private static String SERVICE_HOST_NAME = "www.supersonicads.com";
    private static int SERVICE_PORT = 443;
    private static String SERVICE_PROTOCOL = "https";
    private static final String SIGNATURE = "signature";
    private static final String TAG = "SupersonicAdsAdvertiserAgent";
    private static String TIME_API = "https://www.supersonicads.com/timestamp.php";
    public static SupersonicAdsAdvertiserAgent sInstance;

    private class Result {
        private int mResponseCode;
        private String mResponseString;

        public Result(int responseCode, String responseString) {
            setResponseCode(responseCode);
            setResponseString(responseString);
        }

        public int getResponseCode() {
            return this.mResponseCode;
        }

        public void setResponseCode(int responseCode) {
            this.mResponseCode = responseCode;
        }

        public String getResponseString() {
            return this.mResponseString;
        }

        public void setResponseString(String responseString) {
            this.mResponseString = responseString;
        }
    }

    public static final class SuperSonicAdsAdvertiserException extends RuntimeException {
        private static final long serialVersionUID = 8169178234844720921L;

        public SuperSonicAdsAdvertiserException(Throwable t) {
            super(t);
        }
    }

    private SupersonicAdsAdvertiserAgent() {
    }

    public static synchronized SupersonicAdsAdvertiserAgent getInstance() {
        SupersonicAdsAdvertiserAgent supersonicAdsAdvertiserAgent;
        synchronized (SupersonicAdsAdvertiserAgent.class) {
            Logger.i(TAG, "getInstance()");
            if (sInstance == null) {
                sInstance = new SupersonicAdsAdvertiserAgent();
            }
            supersonicAdsAdvertiserAgent = sInstance;
        }
        return supersonicAdsAdvertiserAgent;
    }

    public void reportAppStarted(final Context context) {
        if (!SupersonicSharedPrefHelper.getSupersonicPrefHelper(context).getReportAppStarted()) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        if (SupersonicAdsAdvertiserAgent.this.performRequest(new URL(SupersonicAdsAdvertiserAgent.SERVICE_PROTOCOL, SupersonicAdsAdvertiserAgent.SERVICE_HOST_NAME, SupersonicAdsAdvertiserAgent.SERVICE_PORT, SupersonicAdsAdvertiserAgent.DOMAIN + SupersonicAdsAdvertiserAgent.this.getRequestParameters(context)), context).getResponseCode() == 200) {
                            SupersonicSharedPrefHelper.getSupersonicPrefHelper(context).setReportAppStarted(true);
                        }
                    } catch (MalformedURLException e) {
                    }
                }
            }).start();
        }
    }

    public void setDomain(String protocol, String host, int port) {
        SERVICE_PROTOCOL = protocol;
        SERVICE_HOST_NAME = host;
        SERVICE_PORT = port;
    }

    public void setTimeAPI(String url) {
        TIME_API = url;
    }

    public void setPackageName(String packageName) {
        PACKAGE_NAME = packageName;
    }

    public void clearReportApp(Context context) {
        SupersonicSharedPrefHelper.getSupersonicPrefHelper(context).setReportAppStarted(false);
    }

    private String getRequestParameters(Context context) {
        String pckName;
        StringBuilder parameters = new StringBuilder();
        if (TextUtils.isEmpty(PACKAGE_NAME)) {
            pckName = SDKUtils.getPackageName(context);
        } else {
            pckName = PACKAGE_NAME;
        }
        if (!TextUtils.isEmpty(pckName)) {
            parameters.append(RequestParameters.AMPERSAND).append("bundleId").append(RequestParameters.EQUAL).append(SDKUtils.encodeString(pckName));
        }
        SDKUtils.loadGoogleAdvertiserInfo(context);
        String advertiserId = SDKUtils.getAdvertiserId();
        boolean isLAT = SDKUtils.isLimitAdTrackingEnabled();
        if (TextUtils.isEmpty(advertiserId)) {
            advertiserId = "";
        } else {
            parameters.append(RequestParameters.AMPERSAND).append("deviceIds").append(SDKUtils.encodeString(RequestParameters.LEFT_BRACKETS)).append(SDKUtils.encodeString(RequestParameters.AID)).append(SDKUtils.encodeString(RequestParameters.RIGHT_BRACKETS)).append(RequestParameters.EQUAL).append(SDKUtils.encodeString(advertiserId));
            parameters.append(RequestParameters.AMPERSAND).append(SDKUtils.encodeString(RequestParameters.isLAT)).append(RequestParameters.EQUAL).append(SDKUtils.encodeString(Boolean.toString(isLAT)));
        }
        StringBuilder signature = new StringBuilder();
        signature.append(pckName);
        signature.append(advertiserId);
        signature.append(getUTCTimeStamp(context));
        parameters.append(RequestParameters.AMPERSAND).append(SIGNATURE).append(RequestParameters.EQUAL).append(SDKUtils.getMD5(signature.toString()));
        return parameters.toString();
    }

    public Result performRequest(URL url, Context context) {
        Throwable th;
        Result requestResult = new Result();
        HttpURLConnection connection = null;
        int responseCode = 0;
        InputStream is = null;
        StringBuilder builder = null;
        try {
            url.toURI();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(DownloadManager.OPERATION_TIMEOUT);
            connection.setReadTimeout(DownloadManager.OPERATION_TIMEOUT);
            connection.connect();
            responseCode = connection.getResponseCode();
            is = connection.getInputStream();
            byte[] buffer = new byte[102400];
            StringBuilder builder2 = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    builder2.append(line + "\n");
                }
                if (0 == 0) {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                        }
                    }
                    if (responseCode != 200) {
                        Logger.i(TAG, " RESPONSE CODE: " + responseCode + " URL: " + url);
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                    requestResult.setResponseCode(responseCode);
                    if (builder2 != null) {
                        requestResult.setResponseString("empty");
                        builder = builder2;
                    } else {
                        requestResult.setResponseString(builder2.toString());
                        builder = builder2;
                    }
                    return requestResult;
                }
                if (is != null) {
                    is.close();
                }
                if (responseCode != 200) {
                    Logger.i(TAG, " RESPONSE CODE: " + responseCode + " URL: " + url);
                }
                if (connection != null) {
                    connection.disconnect();
                }
                requestResult.setResponseCode(responseCode);
                if (builder2 != null) {
                    requestResult.setResponseString(builder2.toString());
                    builder = builder2;
                } else {
                    requestResult.setResponseString("empty");
                    builder = builder2;
                }
                return requestResult;
            } catch (MalformedURLException e2) {
                builder = builder2;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e3) {
                    }
                }
                if (responseCode != 200) {
                    Logger.i(TAG, " RESPONSE CODE: " + responseCode + " URL: " + url);
                }
                if (connection != null) {
                    connection.disconnect();
                }
                requestResult.setResponseCode(responseCode);
                if (builder != null) {
                    requestResult.setResponseString(builder.toString());
                } else {
                    requestResult.setResponseString("empty");
                }
                return requestResult;
            } catch (URISyntaxException e4) {
                builder = builder2;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e5) {
                    }
                }
                if (responseCode != 200) {
                    Logger.i(TAG, " RESPONSE CODE: " + responseCode + " URL: " + url);
                }
                if (connection != null) {
                    connection.disconnect();
                }
                requestResult.setResponseCode(responseCode);
                if (builder != null) {
                    requestResult.setResponseString(builder.toString());
                } else {
                    requestResult.setResponseString("empty");
                }
                return requestResult;
            } catch (SocketTimeoutException e6) {
                builder = builder2;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e7) {
                    }
                }
                if (responseCode != 200) {
                    Logger.i(TAG, " RESPONSE CODE: " + responseCode + " URL: " + url);
                }
                if (connection != null) {
                    connection.disconnect();
                }
                requestResult.setResponseCode(responseCode);
                if (builder != null) {
                    requestResult.setResponseString(builder.toString());
                } else {
                    requestResult.setResponseString("empty");
                }
                return requestResult;
            } catch (FileNotFoundException e8) {
                builder = builder2;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e9) {
                    }
                }
                if (responseCode != 200) {
                    Logger.i(TAG, " RESPONSE CODE: " + responseCode + " URL: " + url);
                }
                if (connection != null) {
                    connection.disconnect();
                }
                requestResult.setResponseCode(responseCode);
                if (builder != null) {
                    requestResult.setResponseString(builder.toString());
                } else {
                    requestResult.setResponseString("empty");
                }
                return requestResult;
            } catch (IOException e10) {
                builder = builder2;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e11) {
                    }
                }
                if (responseCode != 200) {
                    Logger.i(TAG, " RESPONSE CODE: " + responseCode + " URL: " + url);
                }
                if (connection != null) {
                    connection.disconnect();
                }
                requestResult.setResponseCode(responseCode);
                if (builder != null) {
                    requestResult.setResponseString(builder.toString());
                } else {
                    requestResult.setResponseString("empty");
                }
                return requestResult;
            } catch (Throwable th2) {
                th = th2;
                builder = builder2;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e12) {
                    }
                }
                if (responseCode != 200) {
                    Logger.i(TAG, " RESPONSE CODE: " + responseCode + " URL: " + url);
                }
                if (connection != null) {
                    connection.disconnect();
                }
                requestResult.setResponseCode(responseCode);
                if (builder != null) {
                    requestResult.setResponseString(builder.toString());
                } else {
                    requestResult.setResponseString("empty");
                }
                throw th;
            }
        } catch (MalformedURLException e13) {
            if (is != null) {
                is.close();
            }
            if (responseCode != 200) {
                Logger.i(TAG, " RESPONSE CODE: " + responseCode + " URL: " + url);
            }
            if (connection != null) {
                connection.disconnect();
            }
            requestResult.setResponseCode(responseCode);
            if (builder != null) {
                requestResult.setResponseString("empty");
            } else {
                requestResult.setResponseString(builder.toString());
            }
            return requestResult;
        } catch (URISyntaxException e14) {
            if (is != null) {
                is.close();
            }
            if (responseCode != 200) {
                Logger.i(TAG, " RESPONSE CODE: " + responseCode + " URL: " + url);
            }
            if (connection != null) {
                connection.disconnect();
            }
            requestResult.setResponseCode(responseCode);
            if (builder != null) {
                requestResult.setResponseString("empty");
            } else {
                requestResult.setResponseString(builder.toString());
            }
            return requestResult;
        } catch (SocketTimeoutException e15) {
            if (is != null) {
                is.close();
            }
            if (responseCode != 200) {
                Logger.i(TAG, " RESPONSE CODE: " + responseCode + " URL: " + url);
            }
            if (connection != null) {
                connection.disconnect();
            }
            requestResult.setResponseCode(responseCode);
            if (builder != null) {
                requestResult.setResponseString("empty");
            } else {
                requestResult.setResponseString(builder.toString());
            }
            return requestResult;
        } catch (FileNotFoundException e16) {
            if (is != null) {
                is.close();
            }
            if (responseCode != 200) {
                Logger.i(TAG, " RESPONSE CODE: " + responseCode + " URL: " + url);
            }
            if (connection != null) {
                connection.disconnect();
            }
            requestResult.setResponseCode(responseCode);
            if (builder != null) {
                requestResult.setResponseString("empty");
            } else {
                requestResult.setResponseString(builder.toString());
            }
            return requestResult;
        } catch (IOException e17) {
            if (is != null) {
                is.close();
            }
            if (responseCode != 200) {
                Logger.i(TAG, " RESPONSE CODE: " + responseCode + " URL: " + url);
            }
            if (connection != null) {
                connection.disconnect();
            }
            requestResult.setResponseCode(responseCode);
            if (builder != null) {
                requestResult.setResponseString("empty");
            } else {
                requestResult.setResponseString(builder.toString());
            }
            return requestResult;
        } catch (Throwable th3) {
            th = th3;
            if (is != null) {
                is.close();
            }
            if (responseCode != 200) {
                Logger.i(TAG, " RESPONSE CODE: " + responseCode + " URL: " + url);
            }
            if (connection != null) {
                connection.disconnect();
            }
            requestResult.setResponseCode(responseCode);
            if (builder != null) {
                requestResult.setResponseString("empty");
            } else {
                requestResult.setResponseString(builder.toString());
            }
            throw th;
        }
    }

    private int getUTCTimeStamp(Context context) {
        try {
            Result result = performRequest(new URL(TIME_API), context);
            if (result.getResponseCode() == 200) {
                SSAObj ssaObj = new SSAObj(result.getResponseString());
                if (ssaObj.containsKey("timestamp")) {
                    int time = Integer.parseInt(ssaObj.getString("timestamp"));
                    return time - (time % 60);
                }
            }
        } catch (MalformedURLException e) {
        }
        return 0;
    }
}
