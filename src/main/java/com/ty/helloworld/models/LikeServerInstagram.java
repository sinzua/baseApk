package com.ty.helloworld.models;

import android.text.TextUtils;
import com.hw.entities.UserInfo;
import com.hw.hwapi.HewService;
import com.squareup.okhttp.Call;
import com.ty.helloworld.entities.FriendShip;
import com.ty.helloworld.okhttp.HttpSingleton;
import com.ty.helloworld.okhttp.JsonCallback;
import com.ty.helloworld.okhttp.RequestCallback;
import com.ty.helloworld.okhttp.requests.CLoginRequest;
import com.ty.helloworld.okhttp.requests.GetFriendShipRequest;
import com.ty.helloworld.okhttp.requests.RequstURL;
import com.ty.helloworld.okhttp.requests.TrackActionRequest;
import com.ty.helloworld.okhttp.responses.BasicResponse;
import com.ty.helloworld.okhttp.responses.GetFriendShipResponse;

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

    public void cLogin(final RequestCallback<BasicResponse> loginCallback) {
        UserInfo userinfo = HewService.getInstance().getUserInfo();
        if (userinfo != null && !TextUtils.isEmpty(HewService.getInstance().getUserInfo().getCookie())) {
            Call mLoginCall = HttpSingleton.newCall(new CLoginRequest(userinfo));
            mLoginCall.enqueue(new JsonCallback<BasicResponse>(mLoginCall, BasicResponse.class) {
                public void onResponse(BasicResponse basicResponse) {
                    loginCallback.onResponse(basicResponse);
                }

                public void onFailure(Exception e) {
                    loginCallback.onFailure(e);
                }
            });
        }
    }

    public void getFriendShip(final RequestCallback<GetFriendShipResponse> getFriendShipCallback) {
        UserInfo userinfo = HewService.getInstance().getUserInfo();
        if (userinfo != null && !TextUtils.isEmpty(userinfo.getCookie())) {
            Call mGetFriendShipCall = HttpSingleton.newCall(new GetFriendShipRequest(userinfo.getUserid().longValue()));
            mGetFriendShipCall.enqueue(new JsonCallback<GetFriendShipResponse>(mGetFriendShipCall, GetFriendShipResponse.class) {
                public void onResponse(GetFriendShipResponse boardResponse) {
                    getFriendShipCallback.onResponse(boardResponse);
                }

                public void onFailure(Exception e) {
                    getFriendShipCallback.onFailure(e);
                }
            });
        }
    }

    public void trackAction(FriendShip order, final RequestCallback<BasicResponse> trackActionCallback) {
        UserInfo userinfo = HewService.getInstance().getUserInfo();
        if (userinfo != null && !TextUtils.isEmpty(HewService.getInstance().getUserInfo().getCookie())) {
            Call mTrackActionCall = HttpSingleton.newCall(new TrackActionRequest(userinfo.getUserid().longValue(), order.getOrderId(), order.getTargetUserId(), order.getOrderKind()));
            mTrackActionCall.enqueue(new JsonCallback<BasicResponse>(mTrackActionCall, BasicResponse.class) {
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
