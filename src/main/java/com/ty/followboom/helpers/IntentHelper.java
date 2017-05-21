package com.ty.followboom.helpers;

import android.app.Activity;
import android.content.Intent;
import com.ty.followboom.LoginActivity;
import com.ty.instaview.R;

public class IntentHelper {
    public static void gotoLoginActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
