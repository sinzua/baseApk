package com.supersonic.mediationsdk.sdk;

import android.app.Activity;

public interface BaseApi {
    void onPause(Activity activity);

    void onResume(Activity activity);

    void release(Activity activity);

    void setAge(int i);

    void setGender(String str);
}
