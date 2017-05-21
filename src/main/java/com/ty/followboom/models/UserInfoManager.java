package com.ty.followboom.models;

import android.content.Context;
import com.forwardwin.base.widgets.JsonSerializer;
import com.ty.followboom.entities.UserInfo;
import com.ty.followboom.helpers.AppConfigHelper;
import com.ty.followboom.helpers.MeSingleton;
import com.ty.followboom.okhttp.RequestCallback;
import com.ty.followboom.okhttp.responses.LoginResponse;
import com.ty.instagramapi.InstagramService;

public class UserInfoManager {
    private static UserInfoManager sUserInfoManager;

    public static UserInfoManager getSingleton() {
        if (sUserInfoManager == null) {
            synchronized (UserInfoManager.class) {
                if (sUserInfoManager == null) {
                    sUserInfoManager = new UserInfoManager();
                }
            }
        }
        return sUserInfoManager;
    }

    public void saveUserInfo(Context context) {
        AppConfigHelper.saveUserData(context, JsonSerializer.getInstance().serialize(new UserInfo(InstagramService.getInstance().getUserInfo().getUserName(), InstagramService.getInstance().getUserInfo().getPassword(), InstagramService.getInstance().getUserInfo().getAvatarUrl(), Long.toString(InstagramService.getInstance().getUserInfo().getUserid().longValue()), InstagramService.getInstance().getUserInfo().getUuid(), InstagramService.getInstance().getUserInfo().getCookie(), AppConfigHelper.getAccountInfo(context))));
    }

    public void save(Context context, UserInfo userInfo) {
        AppConfigHelper.saveUserData(context, JsonSerializer.getInstance().serialize(userInfo));
    }

    public boolean needLogin(Context context) {
        return AppConfigHelper.getUserInfo(context) == null || AppConfigHelper.getUserInfo(context).getKey() == null;
    }

    public UserInfo getUserInfo(Context context) {
        return AppConfigHelper.getUserInfo(context);
    }

    public void logout(Context context) {
        UserInfo userInfo = AppConfigHelper.getUserInfo(context);
        if (userInfo != null) {
            userInfo.setKey(null);
            AppConfigHelper.saveUserData(context, JsonSerializer.getInstance().serialize(userInfo));
        }
        MeSingleton.getSingleton().setMyPost(null);
    }

    public void initUserInfo(Context context, RequestCallback<LoginResponse> loginCallBack) {
        UserInfo igUser = AppConfigHelper.getUserInfo(context);
        if (igUser != null) {
            InstagramService.getInstance().getUserInfo().setUserName(igUser.getUsername());
            InstagramService.getInstance().getUserInfo().setUserid(Long.valueOf(Long.parseLong(igUser.getUserId())));
            InstagramService.getInstance().getUserInfo().setPassword(igUser.getPassword());
            InstagramService.getInstance().getUserInfo().setCookie(igUser.getKey());
            InstagramService.getInstance().getUserInfo().setAvatarUrl(igUser.getAvatarUrl());
            LikeServerInstagram.getSingleton().LoginToServer(context, loginCallBack);
            return;
        }
        InstagramService.getInstance().setUserInfo(null);
    }
}
