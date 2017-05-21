package com.nativex.monetization.enums;

import android.app.Activity;
import android.graphics.Rect;
import com.nativex.monetization.manager.DensityManager;
import com.nativex.monetization.ui.DeviceScreenSize;

public enum BannerPosition {
    TOP,
    BOTTOM;
    
    private static int PHONE_LANDSCAPE_HEIGHT;
    private static int PHONE_LANDSCAPE_WIDTH;
    private static int PHONE_PORTRAIT_HEIGHT;
    private static int PHONE_PORTRAIT_WIDTH;
    private static int TABLET_LANDSCAPE_HEIGHT;
    private static int TABLET_LANDSCAPE_WIDTH;
    private static int TABLET_PORTRAIT_HEIGHT;
    private static int TABLET_PORTRAIT_WIDTH;
    private static boolean initialized;
    private static boolean isTablet;

    static {
        initialized = false;
    }

    public static final void initialize(Activity activity) {
        boolean z = true;
        if (!initialized) {
            initialized = true;
            DeviceScreenSize screenSize = DensityManager.getDeviceScreenSize(activity);
            if (((double) screenSize.getDiagonalInches()) <= 5.5d) {
                z = false;
            }
            isTablet = z;
            PHONE_PORTRAIT_WIDTH = (int) (screenSize.getDensity() * 320.0f);
            PHONE_PORTRAIT_HEIGHT = (int) (screenSize.getDensity() * 50.0f);
            PHONE_LANDSCAPE_WIDTH = (int) (screenSize.getDensity() * 480.0f);
            PHONE_LANDSCAPE_HEIGHT = (int) (screenSize.getDensity() * 32.0f);
            TABLET_PORTRAIT_WIDTH = (int) (screenSize.getDensity() * 768.0f);
            TABLET_PORTRAIT_HEIGHT = (int) (screenSize.getDensity() * 66.0f);
            TABLET_LANDSCAPE_WIDTH = (int) (screenSize.getDensity() * 1024.0f);
            TABLET_LANDSCAPE_HEIGHT = (int) (screenSize.getDensity() * 66.0f);
        }
    }

    public static final boolean isInitialized() {
        return initialized;
    }

    public Rect calculateSize(int left, int top, int right, int bottom) {
        int width;
        int height;
        int screenWidth = right - left;
        if (screenWidth > bottom - top) {
            if (isTablet) {
                width = TABLET_LANDSCAPE_WIDTH;
                height = TABLET_LANDSCAPE_HEIGHT;
            } else {
                width = PHONE_LANDSCAPE_WIDTH;
                height = PHONE_LANDSCAPE_HEIGHT;
            }
        } else if (isTablet) {
            width = TABLET_PORTRAIT_WIDTH;
            height = TABLET_PORTRAIT_HEIGHT;
        } else {
            width = PHONE_PORTRAIT_WIDTH;
            height = PHONE_PORTRAIT_HEIGHT;
        }
        int l = 0;
        if (width < screenWidth) {
            l = (screenWidth - width) / 2;
        } else {
            width = Math.min(screenWidth, width);
        }
        switch (this) {
            case BOTTOM:
                return new Rect(l, bottom - height, l + width, bottom);
            default:
                return new Rect(l, 0, l + width, height);
        }
    }
}
