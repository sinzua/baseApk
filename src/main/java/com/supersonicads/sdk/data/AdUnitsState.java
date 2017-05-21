package com.supersonicads.sdk.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class AdUnitsState implements Parcelable {
    public static final Creator<AdUnitsState> CREATOR = new Creator<AdUnitsState>() {
        public AdUnitsState createFromParcel(Parcel source) {
            return new AdUnitsState(source);
        }

        public AdUnitsState[] newArray(int size) {
            return new AdUnitsState[size];
        }
    };
    private int mDisplayedProduct;
    private String mInterstitialAppKey;
    private Map<String, String> mInterstitialExtraParams;
    private boolean mInterstitialInitSuccess;
    private boolean mInterstitialReportInit;
    private String mInterstitialUserId;
    private Map<String, String> mOfferWallExtraParams;
    private boolean mOfferwallInitSuccess;
    private boolean mOfferwallReportInit;
    private String mRewardedVideoAppKey;
    private Map<String, String> mRewardedVideoExtraParams;
    private boolean mRewardedVideoInitSuccess;
    private String mRewardedVideoUserId;
    private boolean mShouldRestore;

    public AdUnitsState() {
        initialize();
    }

    private AdUnitsState(Parcel source) {
        boolean z = true;
        initialize();
        try {
            boolean z2;
            this.mShouldRestore = source.readByte() != (byte) 0;
            this.mDisplayedProduct = source.readInt();
            if (source.readByte() != (byte) 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.mRewardedVideoInitSuccess = z2;
            this.mRewardedVideoAppKey = source.readString();
            this.mRewardedVideoUserId = source.readString();
            this.mRewardedVideoExtraParams = getMapFromJsonString(source.readString());
            if (source.readByte() != (byte) 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.mInterstitialReportInit = z2;
            if (source.readByte() != (byte) 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.mInterstitialInitSuccess = z2;
            this.mInterstitialAppKey = source.readString();
            this.mInterstitialUserId = source.readString();
            this.mInterstitialExtraParams = getMapFromJsonString(source.readString());
            if (source.readByte() != (byte) 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.mOfferwallInitSuccess = z2;
            if (source.readByte() == (byte) 0) {
                z = false;
            }
            this.mOfferwallReportInit = z;
            this.mOfferWallExtraParams = getMapFromJsonString(source.readString());
        } catch (Throwable th) {
            initialize();
        }
    }

    private void initialize() {
        this.mShouldRestore = false;
        this.mDisplayedProduct = -1;
        this.mInterstitialReportInit = true;
        this.mOfferwallReportInit = true;
        this.mOfferwallInitSuccess = false;
        this.mInterstitialInitSuccess = false;
        this.mRewardedVideoInitSuccess = false;
        String str = "";
        this.mInterstitialUserId = str;
        this.mInterstitialAppKey = str;
        this.mRewardedVideoUserId = str;
        this.mRewardedVideoAppKey = str;
        this.mRewardedVideoExtraParams = new HashMap();
        this.mInterstitialExtraParams = new HashMap();
        this.mOfferWallExtraParams = new HashMap();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i = 1;
        try {
            int i2;
            dest.writeByte((byte) (this.mShouldRestore ? 1 : 0));
            dest.writeInt(this.mDisplayedProduct);
            if (this.mRewardedVideoInitSuccess) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            dest.writeByte((byte) i2);
            dest.writeString(this.mRewardedVideoAppKey);
            dest.writeString(this.mRewardedVideoUserId);
            dest.writeString(new JSONObject(this.mRewardedVideoExtraParams).toString());
            if (this.mInterstitialReportInit) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            dest.writeByte((byte) i2);
            if (this.mInterstitialInitSuccess) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            dest.writeByte((byte) i2);
            dest.writeString(this.mInterstitialAppKey);
            dest.writeString(this.mInterstitialUserId);
            dest.writeString(new JSONObject(this.mInterstitialExtraParams).toString());
            if (this.mOfferwallInitSuccess) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            dest.writeByte((byte) i2);
            if (!this.mOfferwallReportInit) {
                i = 0;
            }
            dest.writeByte((byte) i);
            dest.writeString(new JSONObject(this.mOfferWallExtraParams).toString());
        } catch (Throwable th) {
        }
    }

    public boolean isRewardedVideoInitSuccess() {
        return this.mRewardedVideoInitSuccess;
    }

    public String getRewardedVideoAppKey() {
        return this.mRewardedVideoAppKey;
    }

    public String getRewardedVideoUserId() {
        return this.mRewardedVideoUserId;
    }

    public Map<String, String> getRewardedVideoExtraParams() {
        return this.mRewardedVideoExtraParams;
    }

    public boolean isInterstitialInitSuccess() {
        return this.mInterstitialInitSuccess;
    }

    public String getInterstitialAppKey() {
        return this.mInterstitialAppKey;
    }

    public String getInterstitialUserId() {
        return this.mInterstitialUserId;
    }

    public Map<String, String> getInterstitialExtraParams() {
        return this.mInterstitialExtraParams;
    }

    public boolean reportInitInterstitial() {
        return this.mInterstitialReportInit;
    }

    public boolean shouldRestore() {
        return this.mShouldRestore;
    }

    public int getDisplayedProduct() {
        return this.mDisplayedProduct;
    }

    public boolean getOfferwallInitSuccess() {
        return this.mOfferwallInitSuccess;
    }

    public Map<String, String> getOfferWallExtraParams() {
        return this.mOfferWallExtraParams;
    }

    public boolean reportInitOfferwall() {
        return this.mOfferwallReportInit;
    }

    public void setRewardedVideoInitSuccess(boolean mRewardedVideoInitSuccess) {
        this.mRewardedVideoInitSuccess = mRewardedVideoInitSuccess;
    }

    public void setRewardedVideoAppKey(String mRewardedVideoAppKey) {
        this.mRewardedVideoAppKey = mRewardedVideoAppKey;
    }

    public void setRewardedVideoUserId(String mRewardedVideoUserId) {
        this.mRewardedVideoUserId = mRewardedVideoUserId;
    }

    public void setRewardedVideoExtraParams(Map<String, String> mRewardedVideoExtraParams) {
        this.mRewardedVideoExtraParams = mRewardedVideoExtraParams;
    }

    public void setOfferWallExtraParams(Map<String, String> offerWallExtraParams) {
        this.mOfferWallExtraParams = offerWallExtraParams;
    }

    public void setInterstitialInitSuccess(boolean mInterstitialInitSuccess) {
        this.mInterstitialInitSuccess = mInterstitialInitSuccess;
    }

    public void setInterstitialAppKey(String mInterstitialAppKey) {
        this.mInterstitialAppKey = mInterstitialAppKey;
    }

    public void setInterstitialUserId(String mInterstitialUserId) {
        this.mInterstitialUserId = mInterstitialUserId;
    }

    public void setInterstitialExtraParams(Map<String, String> mInterstitialExtraParams) {
        this.mInterstitialExtraParams = mInterstitialExtraParams;
    }

    public void setReportInitInterstitial(boolean shouldReport) {
        this.mInterstitialReportInit = shouldReport;
    }

    public void setShouldRestore(boolean mShouldRestore) {
        this.mShouldRestore = mShouldRestore;
    }

    public void adOpened(int product) {
        this.mDisplayedProduct = product;
    }

    public void adClosed() {
        this.mDisplayedProduct = -1;
    }

    public void setOfferwallInitSuccess(boolean rewardedVideoInitSuccess) {
        this.mRewardedVideoInitSuccess = rewardedVideoInitSuccess;
    }

    public void setOfferwallReportInit(boolean offerwallReportInit) {
        this.mOfferwallReportInit = offerwallReportInit;
    }

    private Map<String, String> getMapFromJsonString(String jsonString) {
        Map<String, String> result = new HashMap();
        try {
            JSONObject json = new JSONObject(jsonString);
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                result.put(key, json.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Throwable e2) {
            e2.printStackTrace();
        }
        return result;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        try {
            builder.append("shouldRestore:").append(this.mShouldRestore).append(", ");
            builder.append("displayedProduct:").append(this.mDisplayedProduct).append(", ");
            builder.append("RVInitSuccess:").append(this.mRewardedVideoInitSuccess).append(", ");
            builder.append("RVAppKey").append(this.mRewardedVideoAppKey).append(", ");
            builder.append("RVUserId").append(this.mRewardedVideoUserId).append(", ");
            builder.append("RVExtraParams").append(this.mRewardedVideoExtraParams).append(", ");
            builder.append("ISReportInit:").append(this.mInterstitialReportInit).append(", ");
            builder.append("ISInitSuccess:").append(this.mInterstitialInitSuccess).append(", ");
            builder.append("ISAppKey").append(this.mInterstitialAppKey).append(", ");
            builder.append("ISUserId").append(this.mInterstitialUserId).append(", ");
            builder.append("ISExtraParams").append(this.mInterstitialExtraParams).append(", ");
            builder.append("OWReportInit").append(this.mOfferwallReportInit).append(", ");
            builder.append("OWInitSuccess").append(this.mOfferwallInitSuccess).append(", ");
            builder.append("OWExtraParams").append(this.mOfferWallExtraParams).append(", ");
        } catch (Throwable th) {
        }
        return builder.toString();
    }
}
