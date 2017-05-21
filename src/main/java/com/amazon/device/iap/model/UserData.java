package com.amazon.device.iap.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.amazon.device.iap.internal.model.UserDataBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public final class UserData implements Parcelable {
    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        public UserData createFromParcel(Parcel parcel) {
            return new UserData(parcel);
        }

        public UserData[] newArray(int i) {
            return new UserData[i];
        }
    };
    private static final String MARKETPLACE = "marketplace";
    private static final String USER_ID = "userId";
    private final String marketplace;
    private final String userId;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.userId, this.marketplace});
    }

    private UserData(Parcel parcel) {
        this.userId = parcel.readString();
        this.marketplace = parcel.readString();
    }

    public String getUserId() {
        return this.userId;
    }

    public String getMarketplace() {
        return this.marketplace;
    }

    public JSONObject toJSON() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("userId", this.userId);
            jSONObject.put(MARKETPLACE, this.marketplace);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public String toString() {
        String str = null;
        try {
            str = toJSON().toString(4);
        } catch (JSONException e) {
        }
        return str;
    }

    public UserData(UserDataBuilder userDataBuilder) {
        this.userId = userDataBuilder.getUserId();
        this.marketplace = userDataBuilder.getMarketplace();
    }
}
