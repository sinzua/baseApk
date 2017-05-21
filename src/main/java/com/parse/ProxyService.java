package com.parse;

import android.content.Intent;
import android.os.IBinder;

interface ProxyService {
    IBinder onBind(Intent intent);

    void onCreate();

    void onDestroy();

    int onStartCommand(Intent intent, int i, int i2);
}
