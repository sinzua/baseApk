package com.ty.followboom;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.forwardwin.base.widgets.JsonSerializer;
import com.parse.ParseAnalytics;
import com.ty.followboom.helpers.AppConfigHelper;
import com.ty.followboom.helpers.ParseHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.models.TrackActionManager;
import com.ty.followboom.models.UserInfoManager;
import com.ty.followboom.okhttp.RequestCallback;
import com.ty.followboom.okhttp.responses.LoginResponse;
import com.ty.instaview.R;

public class SplashActivity extends Activity {
    private static final int SHOW_TIME_MIN = 3000;
    private static final String TAG = "SplashActivity";
    Runnable goToMainActivity = new Runnable() {
        public void run() {
            if (UserInfoManager.getSingleton().needLogin(SplashActivity.this)) {
                VLTools.gotoLogin(SplashActivity.this);
                SplashActivity.this.finish();
                return;
            }
            UserInfoManager.getSingleton().initUserInfo(SplashActivity.this, SplashActivity.this.mLoginCallBack);
        }
    };
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    long loadingTime = System.currentTimeMillis() - SplashActivity.this.mStartTime;
                    if (loadingTime < 3000) {
                        SplashActivity.this.mHandler.postDelayed(SplashActivity.this.goToMainActivity, 3000 - loadingTime);
                        return;
                    } else {
                        SplashActivity.this.mHandler.post(SplashActivity.this.goToMainActivity);
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private RequestCallback<LoginResponse> mLoginCallBack = new RequestCallback<LoginResponse>() {
        public void onResponse(LoginResponse loginResponse) {
            Log.e(SplashActivity.TAG, "LoginToServer : " + loginResponse.getStatus().getStatusMsg());
            AppConfigHelper.saveAppConfig(SplashActivity.this, JsonSerializer.getInstance().serialize(loginResponse.getData()));
            UserInfoManager.getSingleton().saveUserInfo(SplashActivity.this);
            TrackActionManager.getSingleton().init(SplashActivity.this);
            ParseHelper.signup(SplashActivity.this);
            VLTools.gotoMainActivity(SplashActivity.this);
            SplashActivity.this.finish();
        }

        public void onFailure(Exception e) {
            Toast.makeText(SplashActivity.this, e.toString(), 1).show();
            VLTools.gotoLogin(SplashActivity.this);
            SplashActivity.this.finish();
        }
    };
    private long mStartTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_splash);
        ParseAnalytics.trackAppOpened(getIntent());
        this.mStartTime = System.currentTimeMillis();
        this.mHandler.sendEmptyMessage(0);
    }
}
