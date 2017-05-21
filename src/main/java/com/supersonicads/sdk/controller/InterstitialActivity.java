package com.supersonicads.sdk.controller;

import android.os.Bundle;
import com.supersonicads.sdk.utils.Logger;

public class InterstitialActivity extends ControllerActivity {
    private static final String TAG = ControllerActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(TAG, "onCreate");
    }

    protected void onResume() {
        super.onResume();
        Logger.i(TAG, "onResume");
    }

    protected void onPause() {
        super.onPause();
        Logger.i(TAG, "onPause");
    }
}
