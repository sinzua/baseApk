package com.lidroid.xutils.view;

import android.content.Context;
import android.view.animation.AnimationUtils;

public class ResLoader {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$lidroid$xutils$view$ResType;

    static /* synthetic */ int[] $SWITCH_TABLE$com$lidroid$xutils$view$ResType() {
        int[] iArr = $SWITCH_TABLE$com$lidroid$xutils$view$ResType;
        if (iArr == null) {
            iArr = new int[ResType.values().length];
            try {
                iArr[ResType.Animation.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ResType.Boolean.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ResType.Color.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ResType.ColorStateList.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[ResType.Dimension.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[ResType.DimensionPixelOffset.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[ResType.DimensionPixelSize.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[ResType.Drawable.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[ResType.IntArray.ordinal()] = 10;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[ResType.Integer.ordinal()] = 9;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[ResType.Movie.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[ResType.String.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[ResType.StringArray.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr[ResType.Text.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                iArr[ResType.TextArray.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                iArr[ResType.Xml.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            $SWITCH_TABLE$com$lidroid$xutils$view$ResType = iArr;
        }
        return iArr;
    }

    public static Object loadRes(ResType type, Context context, int id) {
        if (context == null || id < 1) {
            return null;
        }
        switch ($SWITCH_TABLE$com$lidroid$xutils$view$ResType()[type.ordinal()]) {
            case 1:
                return AnimationUtils.loadAnimation(context, id);
            case 2:
                return Boolean.valueOf(context.getResources().getBoolean(id));
            case 3:
                return Integer.valueOf(context.getResources().getColor(id));
            case 4:
                return context.getResources().getColorStateList(id);
            case 5:
                return Float.valueOf(context.getResources().getDimension(id));
            case 6:
                return Integer.valueOf(context.getResources().getDimensionPixelOffset(id));
            case 7:
                return Integer.valueOf(context.getResources().getDimensionPixelSize(id));
            case 8:
                return context.getResources().getDrawable(id);
            case 9:
                return Integer.valueOf(context.getResources().getInteger(id));
            case 10:
                return context.getResources().getIntArray(id);
            case 11:
                return context.getResources().getMovie(id);
            case 12:
                return context.getResources().getString(id);
            case 13:
                return context.getResources().getStringArray(id);
            case 14:
                return context.getResources().getText(id);
            case 15:
                return context.getResources().getTextArray(id);
            case 16:
                return context.getResources().getXml(id);
            default:
                return null;
        }
    }
}
