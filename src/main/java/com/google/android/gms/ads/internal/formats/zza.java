package com.google.android.gms.ads.internal.formats;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import com.google.android.gms.internal.zzhb;
import com.parse.ParseException;
import java.util.List;

@zzhb
public class zza {
    private static final int zzxI = Color.rgb(12, 174, ParseException.SESSION_MISSING);
    private static final int zzxJ = Color.rgb(ParseException.EMAIL_MISSING, ParseException.EMAIL_MISSING, ParseException.EMAIL_MISSING);
    static final int zzxK = zzxJ;
    static final int zzxL = zzxI;
    private final int mTextColor;
    private final String zzxM;
    private final List<Drawable> zzxN;
    private final int zzxO;
    private final int zzxP;
    private final int zzxQ;

    public zza(String str, List<Drawable> list, Integer num, Integer num2, Integer num3, int i) {
        this.zzxM = str;
        this.zzxN = list;
        this.zzxO = num != null ? num.intValue() : zzxK;
        this.mTextColor = num2 != null ? num2.intValue() : zzxL;
        this.zzxP = num3 != null ? num3.intValue() : 12;
        this.zzxQ = i;
    }

    public int getBackgroundColor() {
        return this.zzxO;
    }

    public String getText() {
        return this.zzxM;
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public int getTextSize() {
        return this.zzxP;
    }

    public List<Drawable> zzdG() {
        return this.zzxN;
    }

    public int zzdH() {
        return this.zzxQ;
    }
}
