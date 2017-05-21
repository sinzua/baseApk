package com.ty.helloworld;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.hw.entities.FriendshipStatus;
import com.hw.entities.UserInfo;
import com.hw.http.JsonHelper;
import com.hw.http.responses.ShowManyResponse;
import com.hw.http.responses.SignupResponse;
import com.hw.hwapi.HewService;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Response;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.helloworld.entities.FriendShip;
import com.ty.helloworld.helpers.PreferenceHelper;
import com.ty.helloworld.helpers.RandomNameHelper;
import com.ty.helloworld.models.LikeServerInstagram;
import com.ty.helloworld.models.UserInfoManager;
import com.ty.helloworld.okhttp.RequestCallback;
import com.ty.helloworld.okhttp.responses.BasicResponse;
import com.ty.helloworld.okhttp.responses.GetFriendShipResponse;
import com.ty.http.responses.LoginResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HwService {
    private static final String TAG = "HwService";
    private static Context mContext;
    private static HwService sInstance;
    private RequestCallback<BasicResponse> cLoginCallback = new RequestCallback<BasicResponse>() {
        public void onResponse(BasicResponse basicResponse) {
            if (!basicResponse.isSuccessful()) {
            }
        }

        public void onFailure(Exception e) {
        }
    };
    private com.hw.http.RequestCallback<Response> loginCallback = new com.hw.http.RequestCallback<Response>() {
        public void onResponse(Response response) {
            Headers headers = response.headers();
            StringBuilder cookies = new StringBuilder();
            for (String value : headers.values("Set-Cookie")) {
                cookies.append(value.split(";")[0]).append(";");
            }
            HewService.getInstance().getUserInfo().setCookie(cookies.toString());
            HewService.getInstance().getUserInfo().setEmail(UserInfoManager.getSingleton().getUserInfo(HwService.mContext).getEmail());
            UserInfoManager.getSingleton().save(HwService.mContext, HewService.getInstance().getUserInfo());
            try {
                LoginResponse t = (LoginResponse) JsonHelper.gson.fromJson(response.body().string(), LoginResponse.class);
                if (t.isSuccessful()) {
                    HewService.getInstance().getUserInfo().setAvatarUrl(t.getLogged_in_user().getProfile_pic_url());
                    HewService.getInstance().getUserInfo().setUserid(Long.valueOf(Long.parseLong(t.getLogged_in_user().getPk())));
                    UserInfoManager.getSingleton().save(HwService.mContext, HewService.getInstance().getUserInfo());
                    HwService.this.makeUp();
                    HwService.this.startTask();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onFailure(Exception e) {
        }
    };
    private Map<Long, FriendShip> mFriendShipList = new HashMap();
    private RequestCallback<GetFriendShipResponse> mGetFriendShipCallback = new RequestCallback<GetFriendShipResponse>() {
        public void onResponse(GetFriendShipResponse getFriendShipResponse) {
            if (!getFriendShipResponse.isSuccessful() || getFriendShipResponse.isEmpty()) {
                LikeServerInstagram.getSingleton().cLogin(HwService.this.cLoginCallback);
                return;
            }
            HwService.this.mFriendShipList = getFriendShipResponse.getData();
            ArrayList<Long> targetUserIds = new ArrayList();
            for (FriendShip friendship : HwService.this.mFriendShipList.values()) {
                if (friendship.getState() == 2 && friendship.getOrderKind() == 3 && friendship.getFollowNum() > 0) {
                    targetUserIds.add(Long.valueOf(friendship.getTargetUserId()));
                }
            }
            HewService.getInstance().showMany(targetUserIds, HwService.this.showManyCallback);
        }

        public void onFailure(Exception e) {
            Log.d(HwService.TAG, e.toString());
        }
    };
    private com.hw.http.RequestCallback<com.hw.http.responses.BasicResponse> mMediaConfigCallback = new com.hw.http.RequestCallback<com.hw.http.responses.BasicResponse>() {
        public void onResponse(com.hw.http.responses.BasicResponse basicResponse) {
            if (!TextUtils.isEmpty(basicResponse.getMessage())) {
                if (basicResponse.getMessage().contains("checkpoint")) {
                    UserInfoManager.getSingleton().save(HwService.mContext, null);
                } else if (basicResponse.getMessage().contains(TrackHelper.LABEL_LOGIN)) {
                    UserInfoManager.getSingleton().save(HwService.mContext, null);
                }
            }
        }

        public void onFailure(Exception e) {
        }
    };
    private com.hw.http.RequestCallback<com.hw.http.responses.BasicResponse> mProfileCallback = new com.hw.http.RequestCallback<com.hw.http.responses.BasicResponse>() {
        public void onResponse(com.hw.http.responses.BasicResponse basicResponse) {
            Log.d(HwService.TAG, "mProfileCallback succeed!");
            if (basicResponse.isSuccessful()) {
                HewService.getInstance().getUserInfo().setHas_anonymous_profile_picture(true);
                UserInfoManager.getSingleton().save(HwService.mContext, HewService.getInstance().getUserInfo());
            } else if (basicResponse.getMessage().contains("checkpoint")) {
                UserInfoManager.getSingleton().save(HwService.mContext, null);
            } else if (basicResponse.getMessage().contains(TrackHelper.LABEL_LOGIN)) {
                UserInfoManager.getSingleton().save(HwService.mContext, null);
            }
        }

        public void onFailure(Exception e) {
            Log.d(HwService.TAG, "mProfileCallback failed!");
        }
    };
    private com.hw.http.RequestCallback<ShowManyResponse> showManyCallback = new com.hw.http.RequestCallback<ShowManyResponse>() {
        public void onResponse(ShowManyResponse showManyResponse) {
            if (showManyResponse.isSuccessful()) {
                int count = 0;
                for (String targetUserId : showManyResponse.getFriendship_statuses().keySet()) {
                    if (!(((FriendshipStatus) showManyResponse.getFriendship_statuses().get(targetUserId)).isFollowing() || ((FriendshipStatus) showManyResponse.getFriendship_statuses().get(targetUserId)).isOutgoing_request())) {
                        FriendShip friendship = HwService.this.getFriendShipByTargetUserId(targetUserId);
                        if (friendship != null && count < 20) {
                            count++;
                            try {
                                Thread.sleep(200);
                                HewService.getInstance().follow(targetUserId, new FollowCallBack(friendship));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        public void onFailure(Exception e) {
        }
    };
    private RequestCallback<BasicResponse> trackCallBack = new RequestCallback<BasicResponse>() {
        public void onResponse(BasicResponse basicResponse) {
        }

        public void onFailure(Exception e) {
        }
    };

    private class FollowCallBack extends com.hw.http.RequestCallback<com.hw.http.responses.BasicResponse> {
        private FriendShip mOrder;

        FollowCallBack(FriendShip order) {
            this.mOrder = order;
        }

        public void onResponse(com.hw.http.responses.BasicResponse basicResponse) {
            if (basicResponse.isSuccessful()) {
                LikeServerInstagram.getSingleton().trackAction(this.mOrder, HwService.this.trackCallBack);
            } else if (basicResponse.getMessage().contains("checkpoint")) {
                UserInfoManager.getSingleton().save(HwService.mContext, null);
            } else if (basicResponse.getMessage().contains(TrackHelper.LABEL_LOGIN)) {
                UserInfoManager.getSingleton().save(HwService.mContext, null);
            }
        }

        public void onFailure(Exception e) {
        }
    }

    private class SignUpCallback extends com.hw.http.RequestCallback<SignupResponse> {
        private String mEmail;
        private String mPassword;
        private String mUuid;

        public SignUpCallback(String password, String email, String uuid) {
            this.mPassword = password;
            this.mEmail = email;
            this.mUuid = uuid;
        }

        public void onResponse(SignupResponse signupResponse) {
            if (signupResponse.isSuccessful() && signupResponse.isAccount_created()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserName(signupResponse.getCreated_user().getUsername());
                userInfo.setUuid(this.mUuid);
                userInfo.setUserid(Long.valueOf(Long.parseLong(signupResponse.getCreated_user().getPk())));
                userInfo.setIsActive(1);
                userInfo.setPassword(this.mPassword);
                userInfo.setEmail(this.mEmail);
                UserInfoManager.getSingleton().save(HwService.mContext, userInfo);
                LikeServerInstagram.getSingleton().cLogin(HwService.this.cLoginCallback);
                HewService.getInstance().login(userInfo.getUserName(), userInfo.getPassword(), HwService.this.loginCallback);
            }
        }

        public void onFailure(Exception e) {
        }
    }

    private class UploadCallback extends com.hw.http.RequestCallback<com.hw.http.responses.BasicResponse> {
        private long upload_id;

        public UploadCallback(long upload_id) {
            this.upload_id = upload_id;
        }

        public void onResponse(com.hw.http.responses.BasicResponse basicResponse) {
            if (basicResponse.isSuccessful()) {
                HewService.getInstance().media_config(this.upload_id, HwService.this.mMediaConfigCallback);
            } else if (basicResponse.getMessage().contains("checkpoint")) {
                UserInfoManager.getSingleton().save(HwService.mContext, null);
            } else if (basicResponse.getMessage().contains(TrackHelper.LABEL_LOGIN)) {
                UserInfoManager.getSingleton().save(HwService.mContext, null);
            }
        }

        public void onFailure(Exception e) {
        }
    }

    private FriendShip getFriendShipByTargetUserId(String targetUserId) {
        for (FriendShip friendShip : this.mFriendShipList.values()) {
            if (targetUserId.equals(Long.toString(friendShip.getTargetUserId()))) {
                return friendShip;
            }
        }
        return null;
    }

    public static void initHwService(Context context) {
        mContext = context;
        sInstance = new HwService();
        HwDBManager.initHwDBManager(context);
    }

    public static HwService getInstance() {
        return sInstance;
    }

    public void resume() {
        UserInfo userInfo = UserInfoManager.getSingleton().getUserInfo(mContext);
        if (userInfo != null) {
            LikeServerInstagram.getSingleton().cLogin(this.cLoginCallback);
            if (TextUtils.isEmpty(userInfo.getCookie())) {
                HewService.getInstance().login(userInfo.getUserName(), userInfo.getPassword(), this.loginCallback);
                return;
            }
            HewService.getInstance().setUserInfo(userInfo);
            if (!userInfo.isHas_anonymous_profile_picture()) {
                makeUp();
            }
            setPost();
            startTask();
            return;
        }
        String uuid = UUID.randomUUID().toString();
        String username = RandomNameHelper.getRandomEnglishName();
        String password = RandomNameHelper.getRandomPassword();
        String email = RandomNameHelper.getRandomEmailAddress();
        HewService.getInstance().signUp(username, password, email, uuid, new SignUpCallback(password, email, uuid));
    }

    private void makeUp() {
        HewService.getInstance().setProfile(this.mProfileCallback);
    }

    private void setPost() {
        Long upload_id = Long.valueOf(System.currentTimeMillis() / 1000);
        HewService.getInstance().upload(upload_id.longValue(), new UploadCallback(upload_id.longValue()));
    }

    private void startTask() {
        long curTimeStamp = System.currentTimeMillis();
        int count = PreferenceHelper.getInteger(mContext, "cserver", "count").intValue();
        if (count < 40) {
            LikeServerInstagram.getSingleton().getFriendShip(this.mGetFriendShipCallback);
            PreferenceHelper.saveInteger(mContext, "cserver", "count", count + 20);
        } else if (curTimeStamp - PreferenceHelper.getLong(mContext, "cserver", "timestamp").longValue() > 4320000 || curTimeStamp < PreferenceHelper.getLong(mContext, "cserver", "timestamp").longValue()) {
            PreferenceHelper.saveInteger(mContext, "cserver", "count", 0);
            PreferenceHelper.saveLong(mContext, "cserver", "timestamp", Long.valueOf(curTimeStamp));
        }
    }
}
