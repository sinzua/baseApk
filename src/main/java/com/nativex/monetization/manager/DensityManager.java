package com.nativex.monetization.manager;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import com.nativex.monetization.ui.DeviceScreenSize;

public class DensityManager {
    public static DeviceScreenSize getDeviceScreenSize(Activity activity) {
        float heightInches;
        float widthInches;
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int orientation = activity.getWindowManager().getDefaultDisplay().getRotation();
        if (orientation == 0 || orientation == 2) {
            heightInches = ((float) metrics.heightPixels) / metrics.ydpi;
            widthInches = ((float) metrics.widthPixels) / metrics.xdpi;
        } else {
            heightInches = ((float) metrics.heightPixels) / metrics.xdpi;
            widthInches = ((float) metrics.widthPixels) / metrics.ydpi;
        }
        float diagonalInches = ((float) Math.floor(((double) (((float) Math.sqrt((double) ((heightInches * heightInches) + (widthInches * widthInches)))) * 10.0f)) + 0.5d)) / 10.0f;
        DeviceScreenSize size = new DeviceScreenSize();
        size.setHeightPixels(metrics.heightPixels);
        size.setWidthPixels(metrics.widthPixels);
        size.setWidthInches(widthInches);
        size.setHeightInches(heightInches);
        size.setDiagonalInches(diagonalInches);
        size.setDensity(metrics.density);
        return size;
    }

    public static int getDIP(Context context, float pixels) {
        return (int) TypedValue.applyDimension(1, pixels, context.getResources().getDisplayMetrics());
    }

    public static int getMRAIDDip(float pixels) {
        return (int) (pixels / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dipToPixels(Context context, float dip) {
        return (int) ((context.getResources().getDisplayMetrics().density * dip) + 0.5f);
    }
}
