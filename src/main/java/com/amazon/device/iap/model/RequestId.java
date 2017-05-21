package com.amazon.device.iap.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.amazon.device.iap.internal.util.d;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public final class RequestId implements Parcelable {
    public static final Creator<RequestId> CREATOR = new Creator<RequestId>() {
        public RequestId createFromParcel(Parcel parcel) {
            return new RequestId(parcel);
        }

        public RequestId[] newArray(int i) {
            return new RequestId[i];
        }
    };
    private static final String ENCODED_ID = "encodedId";
    private final String encodedId;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.encodedId);
    }

    private RequestId(Parcel parcel) {
        this.encodedId = parcel.readString();
    }

    public RequestId() {
        this.encodedId = UUID.randomUUID().toString();
    }

    private RequestId(String str) {
        d.a((Object) str, ENCODED_ID);
        this.encodedId = str;
    }

    public String toString() {
        return this.encodedId;
    }

    public static RequestId fromString(String str) {
        return new RequestId(str);
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.encodedId.equals(((RequestId) obj).encodedId);
    }

    public int hashCode() {
        return (this.encodedId == null ? 0 : this.encodedId.hashCode()) + 31;
    }

    public JSONObject toJSON() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(ENCODED_ID, this.encodedId);
        } catch (JSONException e) {
        }
        return jSONObject;
    }
}
