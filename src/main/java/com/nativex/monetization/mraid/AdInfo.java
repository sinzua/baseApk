package com.nativex.monetization.mraid;

import com.nativex.common.JsonRequestConstants.RichMedia;
import org.json.JSONException;
import org.json.JSONObject;

public class AdInfo extends JSONObject {
    private String placement;

    public AdInfo(String jsonString) throws JSONException {
        super(jsonString);
    }

    public String getPlacement() {
        return this.placement;
    }

    public void setPlacement(String p) {
        this.placement = p;
    }

    public JSONObject getAdBehaviorJSONObject() {
        try {
            if (has(RichMedia.AD_BEHAVIOR)) {
                return getJSONObject(RichMedia.AD_BEHAVIOR);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public boolean willPlayAudio() {
        try {
            if (has(RichMedia.AD_BEHAVIOR)) {
                JSONObject adBehavior = getJSONObject(RichMedia.AD_BEHAVIOR);
                if (adBehavior.has(RichMedia.WILL_PLAY_AUDIO)) {
                    return adBehavior.getBoolean(RichMedia.WILL_PLAY_AUDIO);
                }
            }
        } catch (Exception e) {
        }
        return false;
    }
}
