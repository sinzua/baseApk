package com.google.android.gms.ads.internal.client;

import android.os.Parcel;
import com.google.android.gms.ads.search.SearchAdRequest;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzhb;

@zzhb
public final class SearchAdRequestParcel implements SafeParcelable {
    public static final zzam CREATOR = new zzam();
    public final int backgroundColor;
    public final int versionCode;
    public final int zzvd;
    public final int zzve;
    public final int zzvf;
    public final int zzvg;
    public final int zzvh;
    public final int zzvi;
    public final int zzvj;
    public final String zzvk;
    public final int zzvl;
    public final String zzvm;
    public final int zzvn;
    public final int zzvo;
    public final String zzvp;

    SearchAdRequestParcel(int versionCode, int anchorTextColor, int backgroundColor, int backgroundGradientBottom, int backgroundGradientTop, int borderColor, int borderThickness, int borderType, int callButtonColor, String channels, int descriptionTextColor, String fontFace, int headerTextColor, int headerTextSize, String query) {
        this.versionCode = versionCode;
        this.zzvd = anchorTextColor;
        this.backgroundColor = backgroundColor;
        this.zzve = backgroundGradientBottom;
        this.zzvf = backgroundGradientTop;
        this.zzvg = borderColor;
        this.zzvh = borderThickness;
        this.zzvi = borderType;
        this.zzvj = callButtonColor;
        this.zzvk = channels;
        this.zzvl = descriptionTextColor;
        this.zzvm = fontFace;
        this.zzvn = headerTextColor;
        this.zzvo = headerTextSize;
        this.zzvp = query;
    }

    public SearchAdRequestParcel(SearchAdRequest searchAdRequest) {
        this.versionCode = 1;
        this.zzvd = searchAdRequest.getAnchorTextColor();
        this.backgroundColor = searchAdRequest.getBackgroundColor();
        this.zzve = searchAdRequest.getBackgroundGradientBottom();
        this.zzvf = searchAdRequest.getBackgroundGradientTop();
        this.zzvg = searchAdRequest.getBorderColor();
        this.zzvh = searchAdRequest.getBorderThickness();
        this.zzvi = searchAdRequest.getBorderType();
        this.zzvj = searchAdRequest.getCallButtonColor();
        this.zzvk = searchAdRequest.getCustomChannels();
        this.zzvl = searchAdRequest.getDescriptionTextColor();
        this.zzvm = searchAdRequest.getFontFace();
        this.zzvn = searchAdRequest.getHeaderTextColor();
        this.zzvo = searchAdRequest.getHeaderTextSize();
        this.zzvp = searchAdRequest.getQuery();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzam.zza(this, out, flags);
    }
}
