package com.nativex.monetization.mraid;

import android.text.TextUtils;
import java.util.List;

class MRAIDSchemeHandler {
    private final JSIAdToDeviceHandler jsInterface;

    public MRAIDSchemeHandler(JSIAdToDeviceHandler jsInterface) {
        this.jsInterface = jsInterface;
    }

    public boolean handleScheme(UrlScheme scheme, String url, List<String> callsHandled) {
        if (this.jsInterface == null || scheme == null) {
            return false;
        }
        String urlData = getEncodedData(url);
        if (scheme == UrlScheme.SIZE_SCRIPT_SET_PAGE_SIZE) {
            this.jsInterface.setPageSize(urlData);
        }
        if (!TextUtils.isEmpty(urlData)) {
            String callId = (String) MRAIDUtils.decodeData(urlData).get("callId");
            if (!TextUtils.isEmpty(callId)) {
                if (callsHandled.contains(callId)) {
                    return true;
                }
                callsHandled.add(callId);
                this.jsInterface.registerCallId(callId);
            }
        }
        switch (scheme) {
            case NON_REWARD_CLOSE:
            case NON_REWARD_CLOSE_OLD:
            case CLOSE:
                this.jsInterface.close();
                break;
            case EXPAND:
                this.jsInterface.expand(urlData);
                break;
            case LOG:
                this.jsInterface.log(urlData);
                break;
            case OPEN:
                this.jsInterface.open(urlData);
                break;
            case PLAY_VIDEO:
                this.jsInterface.playVideo(urlData);
                break;
            case RESIZE:
                this.jsInterface.resize();
                break;
            case SET_EXPAND_PROPERTIES:
                this.jsInterface.setExpandProperties(urlData);
                break;
            case SET_ORIENTATION_PROPERTIES:
                this.jsInterface.setOrientationProperties(urlData);
                break;
            case SET_RESIZE_PROPERTIES:
                this.jsInterface.setResizeProperties(urlData);
                break;
            case STORE_PICTURE:
                this.jsInterface.storePicture(urlData);
                break;
            case LOADED:
                this.jsInterface.loaded();
                break;
            case USE_CUSTOM_CLOSE:
                this.jsInterface.useCustomClose(urlData);
                break;
            case CREATE_CALENDAR_EVENT:
                this.jsInterface.createCalendarEvent(urlData);
                break;
            case GOOGLE_MARKET:
            case GOOGLE_PLAY:
                this.jsInterface.open("url=" + url);
                break;
            case MARKET_CUSTOM_SCHEME:
                this.jsInterface.open("url=" + url);
                break;
            case AD_CONVERTED:
                this.jsInterface.adConverted();
                break;
            default:
                return false;
        }
        return true;
    }

    private String getEncodedData(String url) {
        try {
            String[] urlSplit = url.split("\\?");
            if (urlSplit.length > 1) {
                return urlSplit[1];
            }
        } catch (Exception e) {
        }
        return null;
    }
}
