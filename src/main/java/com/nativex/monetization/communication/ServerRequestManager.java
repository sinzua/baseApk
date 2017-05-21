package com.nativex.monetization.communication;

import android.support.annotation.NonNull;
import com.nativex.common.JsonRequestConstants.Session;
import com.nativex.common.JsonRequestConstants.URLS;
import com.nativex.common.Log;
import com.nativex.common.OnTaskCompletedListener;
import com.nativex.common.ServerConfig;
import com.nativex.common.Utilities;
import com.nativex.monetization.Constants;
import com.nativex.monetization.business.CreateSessionResponseData;
import com.nativex.monetization.business.GetDeviceBalanceResponseData;
import com.nativex.monetization.business.GetOfferCacheResponseData;
import com.nativex.monetization.business.RedeemDeviceBalanceResponseData;
import com.nativex.monetization.listeners.OnGetCacheDownloadCompletedListener;
import com.nativex.monetization.listeners.onRichMediaDownloadedListener;
import com.nativex.monetization.manager.MonetizationJsonRequestManager;
import com.nativex.monetization.manager.MonetizationSharedDataManager;
import com.nativex.monetization.manager.SessionManager;
import com.nativex.network.volley.Response.ErrorListener;
import com.nativex.network.volley.Response.Listener;
import com.nativex.network.volley.ServerError;
import com.nativex.network.volley.VolleyError;
import com.nativex.network.volley.toolbox.StringRequest;
import com.nativex.volleytoolbox.GsonRequest;
import com.nativex.volleytoolbox.NativeXVolley;
import java.util.HashMap;
import java.util.Map;

public class ServerRequestManager {
    public static final String REQUEST_TAG = "NativeXRequest";
    private static boolean createSessionExecuting = false;
    private static boolean deviceBalanceExecuting = false;
    private static ServerRequestManager instance = null;
    private MonetizationJsonRequestManager jsonRequestManager;
    private ServerResponseHandler responseHandler;

    private ServerRequestManager() {
        this.responseHandler = null;
        this.jsonRequestManager = null;
        this.responseHandler = new ServerResponseHandler();
        this.jsonRequestManager = new MonetizationJsonRequestManager();
    }

    public static synchronized ServerRequestManager getInstance() {
        ServerRequestManager serverRequestManager;
        synchronized (ServerRequestManager.class) {
            if (instance == null) {
                instance = new ServerRequestManager();
            }
            serverRequestManager = instance;
        }
        return serverRequestManager;
    }

    public boolean isCreateSessionExecuting() {
        return createSessionExecuting;
    }

    public static void release() {
        instance = null;
    }

    public void createSession(@NonNull final OnTaskCompletedListener listener) {
        try {
            if (createSessionExecuting) {
                Log.w("ServerRequestManager.createSession called, when one is already running!!");
                return;
            }
            GsonRequest<CreateSessionResponseData> request = new GsonRequest(1, ServerConfig.applyConfiguration(URLS.CREATE_SESSION, false), CreateSessionResponseData.class, this.jsonRequestManager.getCreateSessionRequest(), new Listener<CreateSessionResponseData>() {
                public void onResponse(CreateSessionResponseData sessionResponse) {
                    try {
                        ServerRequestManager.createSessionExecuting = false;
                        ServerRequestManager.this.responseHandler.handleCreateSession(sessionResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    listener.onTaskCompleted();
                }
            }, new ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error Response obtained for CreateSession request. " + error.getClass().getCanonicalName(), error);
                    ServerRequestManager.createSessionExecuting = false;
                    listener.onTaskCompleted();
                }
            });
            request.setRequestName("CreateSession");
            request.setTag(REQUEST_TAG);
            createSessionExecuting = true;
            NativeXVolley.getInstance().getRequestQueue().add(request);
        } catch (Exception e) {
            Log.d("ServerRequestManager: Unexpected exception caught while executing CreateSession request.");
            e.printStackTrace();
        }
    }

    private boolean shouldExecute() {
        if (SessionManager.hasSession()) {
            return true;
        }
        return false;
    }

    public void getDeviceBalance() {
        try {
            if (!MonetizationSharedDataManager.isCurrencySupported()) {
                Log.i("Balance disabled for this app");
            } else if (!deviceBalanceExecuting) {
                if (shouldExecute()) {
                    deviceBalanceExecuting = true;
                    GsonRequest<GetDeviceBalanceResponseData> request = new GsonRequest(1, ServerConfig.applyConfiguration(URLS.GET_DEVICE_BALANCE, false), GetDeviceBalanceResponseData.class, this.jsonRequestManager.getAvailableDeviceBalanceRequest(), new Listener<GetDeviceBalanceResponseData>() {
                        public void onResponse(GetDeviceBalanceResponseData getDeviceBalance) {
                            boolean getDeviceBalanceStillRunning = false;
                            try {
                                getDeviceBalanceStillRunning = ServerRequestManager.this.responseHandler.handleGetDeviceBalance(getDeviceBalance);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ServerRequestManager.deviceBalanceExecuting = getDeviceBalanceStillRunning;
                        }
                    }, new ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error Response obtained for GetDeviceBalance request. " + error.getClass().getCanonicalName());
                            ServerRequestManager.deviceBalanceExecuting = false;
                        }
                    });
                    request.setRequestName("GetDeviceBalance");
                    request.setTag(REQUEST_TAG);
                    NativeXVolley.getInstance().getRequestQueue().add(request);
                    return;
                }
                Log.e("GetDeviceBalance request did not met the initial requirements");
            }
        } catch (Exception e) {
            Log.d("ServerRequestManager: Unexpected exception caught while executing GetDeviceBalance request.", e);
        }
    }

    public void redeemCurrency(final RedeemRewardData balanceData) {
        try {
            if (!shouldExecute() || balanceData.unRedeemedBalances == null) {
                Log.e("RedeemCurrency request did not meet the initial requirements");
                return;
            }
            GsonRequest<RedeemDeviceBalanceResponseData> request = new GsonRequest(1, ServerConfig.applyConfiguration(URLS.REDEEM_BALANCE, false), RedeemDeviceBalanceResponseData.class, this.jsonRequestManager.getRedeemCurrencyRequest(balanceData.getUnredeemedPayoutIds(), SessionManager.getSessionId()), new Listener<RedeemDeviceBalanceResponseData>() {
                public void onResponse(RedeemDeviceBalanceResponseData responseData) {
                    ServerRequestManager.this.responseHandler.handleRedeemCurrency(responseData, balanceData);
                    ServerRequestManager.deviceBalanceExecuting = false;
                }
            }, new ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error Response obtained for RedeemCurrency request. " + error.getClass().getCanonicalName());
                    ServerRequestManager.deviceBalanceExecuting = false;
                }
            });
            request.setTag(REQUEST_TAG);
            NativeXVolley.getInstance().getRequestQueue().add(request);
        } catch (Exception e) {
            Log.d("ServerRequestManager: Unexpected exception caught while executing RedeemCurrency request.", e);
        }
    }

    public void actionTaken(int id) {
        try {
            StringRequest request = new StringRequest(1, ServerConfig.applyConfiguration(URLS.ACTION_TAKEN, false), this.jsonRequestManager.getActionTakenRequest(id), new Listener<String>() {
                public void onResponse(String responseData) {
                    if (responseData == null || !responseData.contains("SUCCESS")) {
                        Log.d("ActionTaken Not Successful.");
                    } else {
                        Log.d("ActionTaken Successful.");
                    }
                }
            }, new ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error Response obtained for ActionTaken request." + error.getClass().getCanonicalName());
                }
            });
            request.setHeader(Constants.HTTP_HEADER_CONTENT_TYPE, "application/json");
            request.setRequestName("ActionTaken");
            request.setTag(REQUEST_TAG);
            NativeXVolley.getInstance().getRequestQueue().add(request);
        } catch (Exception e) {
            Log.d("ServerRequestManager: Unexpected exception caught while executing ActionTaken request.", e);
        }
    }

    public void getOfferCache(final OnGetCacheDownloadCompletedListener listener) {
        try {
            if (shouldExecute()) {
                String sessionId = SessionManager.getSessionId();
                Map<String, String> params = new HashMap();
                params.put(Session.SESSION_ID, sessionId);
                GsonRequest<GetOfferCacheResponseData> request = new GsonRequest(0, Utilities.appendParamsToUrl(ServerConfig.applyConfiguration(URLS.GET_OFFER_CACHE_V2, false), params), GetOfferCacheResponseData.class, null, new Listener<GetOfferCacheResponseData>() {
                    public void onResponse(GetOfferCacheResponseData responseData) {
                        if (responseData != null) {
                            listener.downloadComplete(responseData);
                        } else {
                            listener.downloadComplete(null);
                        }
                    }
                }, new ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error Response obtained for GetOfferCache request. " + error.getClass().getCanonicalName());
                        listener.downloadComplete(null);
                    }
                });
                request.setRequestName("GetOfferCache");
                request.setTag(REQUEST_TAG);
                NativeXVolley.getInstance().getRequestQueue().add(request);
                return;
            }
            Log.e("GetOfferCache request did not met the initial requirements");
        } catch (Exception e) {
            Log.d("ServerRequestManager: Unexpected exception caught while executing getOfferCache request.", e);
        }
    }

    public void getRichMedia(String url, final onRichMediaDownloadedListener listener) {
        try {
            if (shouldExecute()) {
                StringRequest request = new StringRequest(0, url, null, new Listener<String>() {
                    public void onResponse(String responseData) {
                        if (responseData == null || responseData.isEmpty()) {
                            listener.downloadComplete("NO AD", responseData);
                        } else {
                            listener.downloadComplete(null, responseData);
                        }
                    }
                }, new ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        if (!(error instanceof ServerError)) {
                            Log.e("ServerRequestManager: Error Response obtained for RichMedia request. " + error.getClass().getCanonicalName());
                        } else if (error.getMessage() != null) {
                            Log.e("ServerRequestManager: Error Response obtained for RichMedia request. Message: " + error.getMessage());
                        } else {
                            Log.e("ServerRequestManager: Error Response obtained for RichMedia request. " + error.getClass().getCanonicalName());
                        }
                        listener.downloadComplete(null, null);
                    }
                });
                request.setRequestName("RichMedia");
                request.setTag(REQUEST_TAG);
                NativeXVolley.getInstance().getRequestQueue().add(request);
                return;
            }
            Log.e("getRichMedia request did not met the initial requirements");
        } catch (Exception e) {
            Log.d("ServerRequestManager: Unexpected exception caught while executing getRichMedia request.", e);
        }
    }
}
