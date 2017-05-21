package com.nativex.monetization.theme;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.Animation;

public class ThemeManager {
    private static ThemeManager instance;
    private Theme theme;

    private static void ensureInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
    }

    private static void checkTheme() {
        if (instance.theme == null) {
            instance.theme = new OriginalTheme();
        }
        if (!instance.theme.isInitialized()) {
            instance.theme.initialize();
        }
    }

    public static void setTheme(Theme theme) {
        ensureInstance();
        if (!(instance.theme == null || instance.theme == theme)) {
            instance.theme.release();
        }
        instance.theme = theme;
    }

    public static Theme getTheme() {
        ensureInstance();
        return instance.theme;
    }

    public static Drawable getDrawable(ThemeElementTypes type) {
        return getDrawable(type, false);
    }

    public static Drawable getDrawable(ThemeElementTypes type, boolean colorDrawableIfNull) {
        ensureInstance();
        checkTheme();
        Drawable drawable = instance.theme.getDrawable(type);
        if (drawable == null && colorDrawableIfNull) {
            return new ColorDrawable(getColor(type).intValue());
        }
        return drawable;
    }

    public static Integer getColor(ThemeElementTypes type) {
        ensureInstance();
        checkTheme();
        Integer color = instance.theme.getColor(type);
        if (color == null) {
            return Integer.valueOf(ViewCompat.MEASURED_STATE_MASK);
        }
        return color;
    }

    public static Animation getAnimation(ThemeElementTypes type) {
        ensureInstance();
        checkTheme();
        return instance.theme.getAnimation(type);
    }

    public static void setViewBackground(View v, ThemeElementTypes type) {
        Drawable backgroundDrawable = getDrawable(type);
        if (backgroundDrawable != null) {
            v.setBackgroundDrawable(backgroundDrawable);
        } else {
            v.setBackgroundColor(getColor(type).intValue());
        }
    }

    public static void reset() {
        if (instance != null && instance.theme != null) {
            instance.theme.release();
        }
    }

    public static void release() {
        if (instance != null) {
            if (instance.theme != null) {
                instance.theme.release();
            }
            instance = null;
        }
    }
}
