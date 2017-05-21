package com.nativex.monetization.mraid.objects;

import com.google.gson.annotations.SerializedName;
import com.nativex.monetization.mraid.MRAIDUtils.Orientation;

public class OrientationProperties {
    @SerializedName("allowOrientationChange")
    private Boolean allowOrientationChange;
    @SerializedName("forceOrientation")
    private String forceOrientation;

    public Boolean getAllowOrientationChange() {
        return this.allowOrientationChange;
    }

    public void setAllowOrientationChange(Boolean allowOrientationChange) {
        this.allowOrientationChange = allowOrientationChange;
    }

    public Orientation getForceOrientation() {
        return Orientation.getOrientation(this.forceOrientation);
    }

    public void setForceOrientation(String forceOrientation) {
        this.forceOrientation = forceOrientation;
    }

    public void setForceOrientation(Orientation forceOrientation) {
        this.forceOrientation = forceOrientation.getValue();
    }
}
