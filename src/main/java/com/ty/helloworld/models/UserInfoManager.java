package com.ty.helloworld.models;

import android.content.Context;
import com.hw.entities.UserInfo;
import com.ty.helloworld.helpers.JsonSerializer;
import com.ty.helloworld.helpers.PreferenceHelper;

public class UserInfoManager {
    private static final String IG_USER_DATA = "hw_userdata";
    private static final String KEY_USERINFO = "hw_userinfo";
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

    public void save(Context context, UserInfo userInfo) {
        PreferenceHelper.saveContent(context, KEY_USERINFO, IG_USER_DATA, JsonSerializer.getInstance().serialize(userInfo));
    }

    public UserInfo getUserInfo(Context context) {
        return (UserInfo) JsonSerializer.getInstance().deserialize(PreferenceHelper.getContent(context, KEY_USERINFO, IG_USER_DATA), UserInfo.class);
    }
}
