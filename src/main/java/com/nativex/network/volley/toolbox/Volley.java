package com.nativex.network.volley.toolbox;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build.VERSION;
import com.nativex.network.volley.RequestQueue;
import java.io.File;

public class Volley {
    public static RequestQueue newRequestQueue(Context context, File cacheDir, HttpStack stack) {
        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();
            userAgent = packageName + "/" + context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
        }
        if (stack == null) {
            if (VERSION.SDK_INT >= 9) {
                stack = new HurlStack();
            } else {
                stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
            }
        }
        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), new BasicNetwork(stack));
        queue.start();
        return queue;
    }

    public static RequestQueue newRequestQueue(Context context, File cacheDir) {
        return newRequestQueue(context, cacheDir, null);
    }
}
