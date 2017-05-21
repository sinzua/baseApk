package com.ty.followboom.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.forwardwin.base.widgets.ToastHelper;
import com.squareup.okhttp.Call;
import com.ty.entities.UserInfo;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.okhttp.HttpSingleton;
import com.ty.followboom.okhttp.JsonCallback;
import com.ty.followboom.okhttp.RequestCallback;
import com.ty.followboom.okhttp.requests.BuyAmazonCoinsConfirmRequest;
import com.ty.followboom.okhttp.requests.BuyCoinsConfirmRequest;
import com.ty.followboom.okhttp.requests.GetAccountInfoRequest;
import com.ty.followboom.okhttp.requests.GetBoardRequest;
import com.ty.followboom.okhttp.requests.GetCoinsHistoryRequest;
import com.ty.followboom.okhttp.requests.GetFollowersRequest;
import com.ty.followboom.okhttp.requests.GetLikesRequest;
import com.ty.followboom.okhttp.requests.GetOrderStatusRequest;
import com.ty.followboom.okhttp.requests.GetViewsRequest;
import com.ty.followboom.okhttp.requests.LoginRequest;
import com.ty.followboom.okhttp.requests.RateUsRequest;
import com.ty.followboom.okhttp.requests.RequstURL;
import com.ty.followboom.okhttp.requests.TrackActionRequest;
import com.ty.followboom.okhttp.requests.UnfollowReportRequest;
import com.ty.followboom.okhttp.responses.BasicResponse;
import com.ty.followboom.okhttp.responses.BoardResponse;
import com.ty.followboom.okhttp.responses.BuyCoinsConfirmResponse;
import com.ty.followboom.okhttp.responses.CoinsHistoryResponse;
import com.ty.followboom.okhttp.responses.CommonCoinsResponse;
import com.ty.followboom.okhttp.responses.GetAccountInfoResponse;
import com.ty.followboom.okhttp.responses.LoginResponse;
import com.ty.followboom.okhttp.responses.OrderStatusResponse;
import com.ty.instagramapi.InstagramService;
import java.util.ArrayList;

public class LikeServerInstagram {
    private static final String TAG = "LikeServerInstagram";
    private static LikeServerInstagram sLikeServer;
    public String DEFAULT_SYSTEM_VERSION = "instagrabandroid/ ";
    public String DEFAULT_USER_AGENT = "instagrab ";
    public String SERVER_URL = RequstURL.SERVER_URL;

    public static LikeServerInstagram getSingleton() {
        if (sLikeServer == null) {
            synchronized (LikeServerInstagram.class) {
                if (sLikeServer == null) {
                    sLikeServer = new LikeServerInstagram();
                }
            }
        }
        return sLikeServer;
    }

    public String getServerUrl() {
        return this.SERVER_URL;
    }

    public String getSystemVersion() {
        return this.DEFAULT_SYSTEM_VERSION;
    }

    public String getUserAgent() {
        return this.DEFAULT_USER_AGENT;
    }

    public void LoginToServer(Context context, final RequestCallback<LoginResponse> loginCallback) {
        UserInfo userinfo = InstagramService.getInstance().getUserInfo();
        if (userinfo == null || TextUtils.isEmpty(userinfo.getCookie())) {
            ToastHelper.showToast(context, "please login instagram first");
            return;
        }
        try {
            Call mLoginCall = HttpSingleton.newCall(new LoginRequest(Long.toString(userinfo.getUserid().longValue()), Long.toString(userinfo.getUserid().longValue()), VLTools.getDeviceId(context), userinfo.getUserName()));
            mLoginCall.enqueue(new JsonCallback<LoginResponse>(mLoginCall, LoginResponse.class) {
                public void onResponse(LoginResponse loginResponse) {
                    loginCallback.onResponse(loginResponse);
                }

                public void onFailure(Exception e) {
                    loginCallback.onFailure(e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void getAccountInfo(Context context, final RequestCallback<GetAccountInfoResponse> accountInfoCallback) {
        UserInfo userinfo = InstagramService.getInstance().getUserInfo();
        if (userinfo == null || TextUtils.isEmpty(InstagramService.getInstance().getUserInfo().getCookie())) {
            ToastHelper.showToast(context, "please login instagram first");
            return;
        }
        Call mGetAccountInfoCall = HttpSingleton.newCall(new GetAccountInfoRequest(Long.toString(userinfo.getUserid().longValue()), Long.toString(userinfo.getUserid().longValue())));
        mGetAccountInfoCall.enqueue(new JsonCallback<GetAccountInfoResponse>(mGetAccountInfoCall, GetAccountInfoResponse.class) {
            public void onResponse(GetAccountInfoResponse accountInfoResponse) {
                accountInfoCallback.onResponse(accountInfoResponse);
            }

            public void onFailure(Exception e) {
                accountInfoCallback.onFailure(e);
            }
        });
    }

    public void getLikes(Context context, String mediaId, String videoUrl, String videoLowURL, String thumbnailUrl, String goodsId, int type, String userName, String postCode, int startAt, RequestCallback<CommonCoinsResponse> accountInfoCallback) {
        UserInfo userinfo = InstagramService.getInstance().getUserInfo();
        if (userinfo == null || TextUtils.isEmpty(InstagramService.getInstance().getUserInfo().getCookie())) {
            ToastHelper.showToast(context, "please login instagram first");
            return;
        }
        Call mGetLikesCall = HttpSingleton.newCall(new GetLikesRequest(Long.toString(userinfo.getUserid().longValue()), Long.toString(userinfo.getUserid().longValue()), mediaId, videoUrl, videoLowURL, thumbnailUrl, goodsId, type, userName, postCode, startAt));
        final RequestCallback<CommonCoinsResponse> requestCallback = accountInfoCallback;
        mGetLikesCall.enqueue(new JsonCallback<CommonCoinsResponse>(mGetLikesCall, CommonCoinsResponse.class) {
            public void onResponse(CommonCoinsResponse commonCoinsResponse) {
                requestCallback.onResponse(commonCoinsResponse);
            }

            public void onFailure(Exception e) {
                requestCallback.onFailure(e);
            }
        });
    }

    public void getViews(Context context, String mediaId, String videoUrl, String videoLowURL, String thumbnailUrl, String goodsId, int type, String userName, String postCode, String trackingToken, Long targetUserId, Long timestamp, int startAt, RequestCallback<CommonCoinsResponse> accountInfoCallback) {
        UserInfo userinfo = InstagramService.getInstance().getUserInfo();
        if (userinfo == null || TextUtils.isEmpty(InstagramService.getInstance().getUserInfo().getCookie())) {
            ToastHelper.showToast(context, "please login instagram first");
            return;
        }
        Call mGetViewsCall = HttpSingleton.newCall(new GetViewsRequest(Long.toString(userinfo.getUserid().longValue()), Long.toString(userinfo.getUserid().longValue()), mediaId, videoUrl, videoLowURL, thumbnailUrl, goodsId, type, userName, postCode, trackingToken, targetUserId, timestamp, startAt));
        final RequestCallback<CommonCoinsResponse> requestCallback = accountInfoCallback;
        mGetViewsCall.enqueue(new JsonCallback<CommonCoinsResponse>(mGetViewsCall, CommonCoinsResponse.class) {
            public void onResponse(CommonCoinsResponse commonCoinsResponse) {
                requestCallback.onResponse(commonCoinsResponse);
            }

            public void onFailure(Exception e) {
                requestCallback.onFailure(e);
            }
        });
    }

    public void getFollowers(Context context, String avatarUrl, String goodsId, String userName, int startAt, final RequestCallback<CommonCoinsResponse> accountInfoCallback) {
        UserInfo userinfo = InstagramService.getInstance().getUserInfo();
        if (userinfo == null || TextUtils.isEmpty(InstagramService.getInstance().getUserInfo().getCookie())) {
            ToastHelper.showToast(context, "please login instagram first");
            return;
        }
        Call mGetFollowersCall = HttpSingleton.newCall(new GetFollowersRequest(Long.toString(userinfo.getUserid().longValue()), Long.toString(userinfo.getUserid().longValue()), avatarUrl, goodsId, userName, startAt));
        mGetFollowersCall.enqueue(new JsonCallback<CommonCoinsResponse>(mGetFollowersCall, CommonCoinsResponse.class) {
            public void onResponse(CommonCoinsResponse commonCoinsResponse) {
                accountInfoCallback.onResponse(commonCoinsResponse);
            }

            public void onFailure(Exception e) {
                accountInfoCallback.onFailure(e);
            }
        });
    }

    public void rateUs(Context context, int type, final RequestCallback<GetAccountInfoResponse> accountInfoCallback) {
        UserInfo userinfo = InstagramService.getInstance().getUserInfo();
        if (userinfo == null || TextUtils.isEmpty(InstagramService.getInstance().getUserInfo().getCookie())) {
            ToastHelper.showToast(context, "please login instagram first");
            return;
        }
        Call mRateUsCall = HttpSingleton.newCall(new RateUsRequest(Long.toString(userinfo.getUserid().longValue()), type, Long.toString(userinfo.getUserid().longValue())));
        mRateUsCall.enqueue(new JsonCallback<GetAccountInfoResponse>(mRateUsCall, GetAccountInfoResponse.class) {
            public void onResponse(GetAccountInfoResponse getAccountInfoResponse) {
                accountInfoCallback.onResponse(getAccountInfoResponse);
            }

            public void onFailure(Exception e) {
                accountInfoCallback.onFailure(e);
            }
        });
    }

    public void buyGoogleCoinsConfirm(Context context, String productId, String token, String androidSignedData, String androidSignature, RequestCallback<BuyCoinsConfirmResponse> buyCoinsConfirmCallback) {
        UserInfo userinfo = InstagramService.getInstance().getUserInfo();
        if (userinfo == null || TextUtils.isEmpty(InstagramService.getInstance().getUserInfo().getCookie())) {
            ToastHelper.showToast(context, "please login instagram first");
            return;
        }
        Call mBuyCoinsConfirmCall = HttpSingleton.newCall(new BuyCoinsConfirmRequest(context, Long.toString(userinfo.getUserid().longValue()), Long.toString(userinfo.getUserid().longValue()), productId, token, androidSignedData, androidSignature));
        final RequestCallback<BuyCoinsConfirmResponse> requestCallback = buyCoinsConfirmCallback;
        mBuyCoinsConfirmCall.enqueue(new JsonCallback<BuyCoinsConfirmResponse>(mBuyCoinsConfirmCall, BuyCoinsConfirmResponse.class) {
            public void onResponse(BuyCoinsConfirmResponse buyCoinsConfirmResponse) {
                requestCallback.onResponse(buyCoinsConfirmResponse);
            }

            public void onFailure(Exception e) {
                requestCallback.onFailure(e);
            }
        });
    }

    public void buyAmazonCoinsConfirm(Context context, String productId, String originalReceipt, String amazonUserId, final RequestCallback<BuyCoinsConfirmResponse> buyCoinsConfirmCallback) {
        UserInfo userinfo = InstagramService.getInstance().getUserInfo();
        if (userinfo == null || TextUtils.isEmpty(InstagramService.getInstance().getUserInfo().getCookie())) {
            ToastHelper.showToast(context, "please login instagram first");
            return;
        }
        Call mBuyCoinsConfirmCall = HttpSingleton.newCall(new BuyAmazonCoinsConfirmRequest(context, Long.toString(userinfo.getUserid().longValue()), Long.toString(userinfo.getUserid().longValue()), productId, originalReceipt, amazonUserId));
        mBuyCoinsConfirmCall.enqueue(new JsonCallback<BuyCoinsConfirmResponse>(mBuyCoinsConfirmCall, BuyCoinsConfirmResponse.class) {
            public void onResponse(BuyCoinsConfirmResponse buyCoinsConfirmResponse) {
                buyCoinsConfirmCallback.onResponse(buyCoinsConfirmResponse);
            }

            public void onFailure(Exception e) {
                buyCoinsConfirmCallback.onFailure(e);
            }
        });
    }

    public void getOrderStatus(Context context, final RequestCallback<OrderStatusResponse> orderStatusCallback) {
        UserInfo userinfo = InstagramService.getInstance().getUserInfo();
        if (userinfo == null || TextUtils.isEmpty(InstagramService.getInstance().getUserInfo().getCookie())) {
            ToastHelper.showToast(context, "please login instagram first");
            return;
        }
        Call mGetOrderStatusCall = HttpSingleton.newCall(new GetOrderStatusRequest(Long.toString(userinfo.getUserid().longValue()), Long.toString(userinfo.getUserid().longValue())));
        mGetOrderStatusCall.enqueue(new JsonCallback<OrderStatusResponse>(mGetOrderStatusCall, OrderStatusResponse.class) {
            public void onResponse(OrderStatusResponse orderStatusResponse) {
                orderStatusCallback.onResponse(orderStatusResponse);
            }

            public void onFailure(Exception e) {
                orderStatusCallback.onFailure(e);
            }
        });
    }

    public void getCoinsHistory(Context context, final RequestCallback<CoinsHistoryResponse> coinsHistoryCallback) {
        UserInfo userinfo = InstagramService.getInstance().getUserInfo();
        if (userinfo == null || TextUtils.isEmpty(InstagramService.getInstance().getUserInfo().getCookie())) {
            ToastHelper.showToast(context, "please login instagram first");
            return;
        }
        Call mGetCoinsHistoryCall = HttpSingleton.newCall(new GetCoinsHistoryRequest(Long.toString(userinfo.getUserid().longValue()), Long.toString(userinfo.getUserid().longValue())));
        mGetCoinsHistoryCall.enqueue(new JsonCallback<CoinsHistoryResponse>(mGetCoinsHistoryCall, CoinsHistoryResponse.class) {
            public void onResponse(CoinsHistoryResponse coinsHistoryResponse) {
                coinsHistoryCallback.onResponse(coinsHistoryResponse);
            }

            public void onFailure(Exception e) {
                coinsHistoryCallback.onFailure(e);
            }
        });
    }

    public void getBoard(Context context, int type, int page, final RequestCallback<BoardResponse> getBoardCallback) {
        UserInfo userinfo = InstagramService.getInstance().getUserInfo();
        if (userinfo == null || TextUtils.isEmpty(InstagramService.getInstance().getUserInfo().getCookie())) {
            ToastHelper.showToast(context, "please login instagram first");
            return;
        }
        Call mGetBoardCall = HttpSingleton.newCall(new GetBoardRequest(Long.toString(userinfo.getUserid().longValue()), type, page, Long.toString(userinfo.getUserid().longValue())));
        mGetBoardCall.enqueue(new JsonCallback<BoardResponse>(mGetBoardCall, BoardResponse.class) {
            public void onResponse(BoardResponse boardResponse) {
                getBoardCallback.onResponse(boardResponse);
            }

            public void onFailure(Exception e) {
                getBoardCallback.onFailure(e);
            }
        });
    }

    public void trackAction(Context context, int actionType, long orderId, final RequestCallback<BasicResponse> trackActionCallback) {
        UserInfo userinfo = InstagramService.getInstance().getUserInfo();
        if (userinfo == null || TextUtils.isEmpty(InstagramService.getInstance().getUserInfo().getCookie())) {
            ToastHelper.showToast(context, "please login instagram first");
            return;
        }
        Call mTrackActionCall = HttpSingleton.newCall(new TrackActionRequest(Long.toString(userinfo.getUserid().longValue()), Long.toString(userinfo.getUserid().longValue()), actionType, orderId));
        mTrackActionCall.enqueue(new JsonCallback<BasicResponse>(mTrackActionCall, BasicResponse.class) {
            public void onResponse(BasicResponse basicResponse) {
                trackActionCallback.onResponse(basicResponse);
            }

            public void onFailure(Exception e) {
                trackActionCallback.onFailure(e);
            }
        });
    }

    public void unfollowReport(ArrayList<Long> userIds, final RequestCallback<BasicResponse> trackActionCallback) {
        UserInfo userinfo = InstagramService.getInstance().getUserInfo();
        if (userinfo != null && !TextUtils.isEmpty(InstagramService.getInstance().getUserInfo().getCookie())) {
            Call mUnfollowReportCall = HttpSingleton.newCall(new UnfollowReportRequest(Long.toString(userinfo.getUserid().longValue()), Long.toString(userinfo.getUserid().longValue()), userIds));
            mUnfollowReportCall.enqueue(new JsonCallback<BasicResponse>(mUnfollowReportCall, BasicResponse.class) {
                public void onResponse(BasicResponse basicResponse) {
                    trackActionCallback.onResponse(basicResponse);
                }

                public void onFailure(Exception e) {
                    trackActionCallback.onFailure(e);
                }
            });
        }
    }
}
