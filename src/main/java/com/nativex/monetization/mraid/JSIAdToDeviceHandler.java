package com.nativex.monetization.mraid;

import com.nativex.common.Log;
import com.nativex.common.Utilities;
import com.nativex.monetization.enums.AdEvent;
import com.nativex.monetization.mraid.MRAIDUtils.JSCommands;
import com.nativex.monetization.mraid.objects.CalendarEntryData;
import com.nativex.monetization.mraid.objects.ExpandProperties;
import com.nativex.monetization.mraid.objects.ObjectNames;
import com.nativex.monetization.mraid.objects.OrientationProperties;
import com.nativex.monetization.mraid.objects.ResizeProperties;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.util.Map;

class JSIAdToDeviceHandler {
    private final MRAIDContainer container;

    JSIAdToDeviceHandler(MRAIDContainer container) {
        this.container = container;
    }

    public void open(String encodedData) {
        MRAIDLogger.d("AdToDevice: open(" + encodedData + ")");
        if (this.container == null) {
            MRAIDLogger.e(" open - MRAIDContainer is null.");
            return;
        }
        try {
            String url = (String) MRAIDUtils.decodeData(encodedData).get(ParametersKeys.URL);
            if (!Utilities.stringIsEmpty(url)) {
                this.container.trackClick(url);
                MRAIDUtils.analyseUrl(this.container.getActivity(), url);
                this.container.fireListener(AdEvent.USER_NAVIGATES_OUT_OF_APP, "The user clicked on a link in the ad and is navigating out of the app");
                if (this.container.getWillCloseAdOnRedirect()) {
                    MRAIDManager.releaseAd(this.container);
                }
            }
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.OPEN);
        }
    }

    public void close() {
        MRAIDLogger.d("AdToDevice: close()");
        if (this.container != null) {
            this.container.close();
        } else {
            MRAIDLogger.e("close() failed. MRAIDContainer reference lost.");
        }
    }

    public void expand() {
        MRAIDLogger.d("AdToDevice: expand()");
        if (this.container != null) {
            this.container.expand(null);
        } else {
            MRAIDLogger.e("expand() failed. MRAIDContainer reference lost.");
        }
    }

    public void expand(String data) {
        MRAIDLogger.d("AdToDevice: expand(" + data + ")");
        if (data != null) {
            try {
                String url = (String) MRAIDUtils.decodeData(data).get(ParametersKeys.URL);
                if (Utilities.isHttpOrHttpsUrl(url)) {
                    this.container.expand(url);
                    return;
                }
            } catch (Exception e) {
                fireErrorEvent(null, e, JSCommands.EXPAND);
                return;
            }
        }
        this.container.expand(null);
    }

    private void fireErrorEvent(String msg, Throwable e, JSCommands command) {
        if (this.container != null) {
            this.container.fireErrorEvent(msg, e, command);
        } else if (e == null) {
            MRAIDLogger.e(command.getCommand() + " failed. " + msg);
        } else {
            MRAIDLogger.e(command.getCommand() + " failed. " + e.getMessage());
        }
    }

    public void setExpandProperties(String expandProperties) {
        MRAIDLogger.d("AdToDevice: setExpandProperties(" + expandProperties + ")");
        try {
            Map<String, String> data = MRAIDUtils.decodeData(expandProperties);
            ExpandProperties props = new ExpandProperties();
            props.setHeight(Integer.valueOf(Integer.parseInt((String) data.get("height"))));
            props.setWidth(Integer.valueOf(Integer.parseInt((String) data.get("width"))));
            props.setModal(Boolean.valueOf(Boolean.parseBoolean((String) data.get(ObjectNames.ExpandProperties.IS_MODAL))));
            props.setUseCustomClose(Boolean.valueOf(Boolean.parseBoolean((String) data.get(ObjectNames.ExpandProperties.USE_CUSTOM_CLOSE))));
            this.container.setExpandProperties(props);
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.SET_EXPAND_PROPERTIES);
        }
    }

    public void setOrientationProperties(String orientationProperties) {
        MRAIDLogger.d("AdToDevice: setOrientationProperties(" + orientationProperties + ")");
        try {
            Map<String, String> data = MRAIDUtils.decodeData(orientationProperties);
            OrientationProperties props = new OrientationProperties();
            props.setAllowOrientationChange(Boolean.valueOf(Boolean.parseBoolean((String) data.get(ObjectNames.OrientationProperties.ALLOW_ORIENTATION_CHANGE))));
            props.setForceOrientation((String) data.get(ObjectNames.OrientationProperties.FORCE_ORIENTATION));
            this.container.setOrientationProperties(props);
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.SET_ORIENTATION_PROPERTIES);
        }
    }

    public void setResizeProperties(String resizeProperties) {
        MRAIDLogger.d("AdToDevice: setResizeProperties(" + resizeProperties + ")");
        try {
            Map<String, String> data = MRAIDUtils.decodeData(resizeProperties);
            ResizeProperties props = new ResizeProperties();
            props.setAllowOffscreen(Boolean.valueOf(Boolean.parseBoolean((String) data.get(ObjectNames.ResizeProperties.ALLOWS_OFFSCREEN))));
            props.setCustomClosePosition((String) data.get(ObjectNames.ResizeProperties.CUSTOM_CLOSE_POSITION));
            props.setHeight(Integer.valueOf(Integer.parseInt((String) data.get("height"))));
            props.setOffsetX(Integer.valueOf(Integer.parseInt((String) data.get(ObjectNames.ResizeProperties.OFFSET_X))));
            props.setOffsetY(Integer.valueOf(Integer.parseInt((String) data.get(ObjectNames.ResizeProperties.OFFSET_Y))));
            props.setWidth(Integer.valueOf(Integer.parseInt((String) data.get("width"))));
            this.container.setResizeProperties(props);
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.SET_RESIZE_PROPERTIES);
        }
    }

    public void resize() {
        MRAIDLogger.d("AdToDevice: resize()");
        if (this.container != null) {
            this.container.resize();
        } else {
            MRAIDLogger.e("resize() failed. MRAIDContainer reference lost");
        }
    }

    public void log(String data) {
        MRAIDLogger.d("AdToDevice: log(" + data + ")");
        try {
            MRAIDLogger.d((String) MRAIDUtils.decodeData(data).get("log"));
        } catch (Exception e) {
            fireErrorEvent("Logging failed", null, JSCommands.LOG);
        }
    }

    public void loaded() {
        MRAIDLogger.d("AdToDevice: loaded()");
        try {
            this.container.onMraidLoaded();
        } catch (Exception e) {
            MRAIDLogger.e("Error occurred while initializing the MRAID controller", e);
        }
    }

    public void playVideo(String data) {
        MRAIDLogger.d("AdToDevice: playVideo(" + data + ")");
        try {
            this.container.playVideo((String) MRAIDUtils.decodeData(data).get(ParametersKeys.URL));
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.PLAY_VIDEO);
        }
    }

    public void storePicture(String data) {
        MRAIDLogger.d("AdToDevice: storePicture(" + data + ")");
        try {
            String url = (String) MRAIDUtils.decodeData(data).get(ParametersKeys.URL);
            if (Utilities.stringIsEmpty(url)) {
                fireErrorEvent("Picture url is invalid", null, JSCommands.STORE_PICTURE);
            } else {
                this.container.storePicture(url);
            }
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.STORE_PICTURE);
        }
    }

    public void createCalendarEvent(String data) {
        MRAIDLogger.d("AdToDevice: createCalendarEvent(" + data + ")");
        try {
            if (this.container == null) {
                fireErrorEvent("Container reference lost", null, JSCommands.CREATE_CALENDAR_EVENT);
                return;
            }
            Map<String, String> decodedData = MRAIDUtils.decodeData(data);
            CalendarEntryData entry = new CalendarEntryData();
            entry.setDescription((String) decodedData.get("description"));
            entry.setEnd((String) decodedData.get(ObjectNames.CalendarEntryData.END));
            entry.setId((String) decodedData.get(ObjectNames.CalendarEntryData.ID));
            entry.setLocation((String) decodedData.get(ObjectNames.CalendarEntryData.LOCATION));
            entry.setReminder((String) decodedData.get(ObjectNames.CalendarEntryData.REMINDER));
            entry.setStart((String) decodedData.get(ObjectNames.CalendarEntryData.START));
            entry.setStatus((String) decodedData.get("status"));
            entry.setSummary((String) decodedData.get(ObjectNames.CalendarEntryData.SUMMARY));
            entry.setTransparency((String) decodedData.get(ObjectNames.CalendarEntryData.TRANSPARENCY));
            this.container.addCalendarEntry(entry);
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.CREATE_CALENDAR_EVENT);
        }
    }

    public void useCustomClose(String data) {
        MRAIDLogger.d("AdToDevice: useCustomClose(" + data + ")");
        try {
            this.container.useCustomClose(Boolean.parseBoolean((String) MRAIDUtils.decodeData(data).get(ObjectNames.ExpandProperties.USE_CUSTOM_CLOSE)));
        } catch (Exception e) {
            fireErrorEvent(null, e, JSCommands.USE_CUSTOM_CLOSE);
        }
    }

    public void setPageSize(String data) {
        MRAIDLogger.d("AdToDevice: setPageSize(" + data + ")");
        try {
            Map<String, String> decodedData = MRAIDUtils.decodeData(data);
            int width = (int) Double.parseDouble((String) decodedData.get("width"));
            this.container.setPageSize(width, (int) Double.parseDouble((String) decodedData.get("height")));
        } catch (Exception e) {
            Log.e("Unable to parse setPageSize data", e);
        }
    }

    public void registerCallId(String callId) {
        try {
            this.container.registerCallId(callId);
        } catch (Exception e) {
            Log.e("Failed to register command callId with the controller", e);
        }
    }

    public void adConverted() {
        try {
            this.container.adConverted();
        } catch (Exception e) {
            Log.e("Failed to call redeem currency", e);
        }
    }

    public void prepareVideo(String url) {
        try {
            this.container.prepareVideo(url);
        } catch (Exception e) {
            Log.e("Failed to call prepare video", e);
        }
    }

    public void setVideoOptions(String videoOptionsJSON) {
        try {
            this.container.setVideoOptions(videoOptionsJSON);
        } catch (Exception e) {
            Log.e("Failed to call prepare video", e);
        }
    }

    public void setWillCloseAdOnRedirect(String data) {
        try {
            this.container.setWillCloseAdOnRedirect(Boolean.parseBoolean(data));
        } catch (Exception e) {
            Log.e("Failed to call setWillCloseAdOnRedirect", e);
        }
    }

    public void enableCloseRegion(String data) {
        try {
            this.container.enableCloseRegion(Boolean.parseBoolean(data));
        } catch (Exception e) {
            Log.e("Failed to call enableCloseRegion", e);
        }
    }
}
