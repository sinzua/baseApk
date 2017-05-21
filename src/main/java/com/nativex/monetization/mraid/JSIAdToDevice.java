package com.nativex.monetization.mraid;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.webkit.JavascriptInterface;

class JSIAdToDevice {
    private static final int MSG_AD_CONVERTED = 1015;
    private static final int MSG_CLOSE = 1001;
    private static final int MSG_CREATE_CALENDAR_EVENT = 1011;
    private static final int MSG_ENABLE_CLOSE_REGION = 1019;
    private static final int MSG_EXPAND = 1002;
    private static final int MSG_LOADED = 1008;
    private static final int MSG_LOG = 1007;
    private static final int MSG_OPEN = 1000;
    private static final int MSG_PLAY_VIDEO = 1009;
    private static final int MSG_RESIZE = 1006;
    private static final int MSG_SET_EXPAND_PROPERTIES = 1003;
    private static final int MSG_SET_ORIENTATION_PROPERTIES = 1004;
    private static final int MSG_SET_PAGE_SIZE = 1014;
    private static final int MSG_SET_RESIZE_PROPERTIES = 1005;
    private static final int MSG_SET_WILL_CLOSE_AD_ON_REDIRECT = 1018;
    private static final int MSG_STORE_PICTURE = 1010;
    private static final int MSG_USE_CUSTOM_CLOSE = 1012;
    private static final int MSG_VIDEO_OPTIONS = 1017;
    private static final int MSG_VIDEO_PREPARE = 1016;
    private static final JSIAdToDeviceInnerHandler handler = new JSIAdToDeviceInnerHandler(Looper.getMainLooper());
    private final JSIAdToDeviceHandler jsiHandler;

    private class HandlerData {
        private String data;
        private JSIAdToDeviceHandler dataHandler;

        private HandlerData() {
        }
    }

    private static class JSIAdToDeviceInnerHandler extends Handler {
        public JSIAdToDeviceInnerHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (msg.obj instanceof HandlerData) {
                HandlerData data = msg.obj;
                switch (msg.what) {
                    case 1000:
                        data.dataHandler.open(data.data);
                        return;
                    case JSIAdToDevice.MSG_CLOSE /*1001*/:
                        data.dataHandler.close();
                        return;
                    case JSIAdToDevice.MSG_EXPAND /*1002*/:
                        data.dataHandler.expand(data.data);
                        return;
                    case JSIAdToDevice.MSG_SET_EXPAND_PROPERTIES /*1003*/:
                        data.dataHandler.setExpandProperties(data.data);
                        return;
                    case 1004:
                        data.dataHandler.setOrientationProperties(data.data);
                        return;
                    case 1005:
                        data.dataHandler.setResizeProperties(data.data);
                        return;
                    case 1006:
                        data.dataHandler.resize();
                        return;
                    case 1007:
                        data.dataHandler.log(data.data);
                        return;
                    case 1008:
                        data.dataHandler.loaded();
                        return;
                    case 1009:
                        data.dataHandler.playVideo(data.data);
                        return;
                    case 1010:
                        data.dataHandler.storePicture(data.data);
                        return;
                    case 1011:
                        data.dataHandler.createCalendarEvent(data.data);
                        return;
                    case 1012:
                        data.dataHandler.useCustomClose(data.data);
                        return;
                    case 1014:
                        data.dataHandler.setPageSize(data.data);
                        return;
                    case 1015:
                        data.dataHandler.adConverted();
                        return;
                    case 1016:
                        data.dataHandler.prepareVideo(data.data);
                        return;
                    case 1017:
                        data.dataHandler.setVideoOptions(data.data);
                        return;
                    case 1018:
                        data.dataHandler.setWillCloseAdOnRedirect(data.data);
                        return;
                    case JSIAdToDevice.MSG_ENABLE_CLOSE_REGION /*1019*/:
                        data.dataHandler.enableCloseRegion(data.data);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public JSIAdToDevice(JSIAdToDeviceHandler handler) {
        this.jsiHandler = handler;
    }

    @JavascriptInterface
    public void open(String data) {
        handler.obtainMessage(1000, createHandlerData(data)).sendToTarget();
    }

    @JavascriptInterface
    public void close() {
        handler.obtainMessage(MSG_CLOSE, createHandlerData(null)).sendToTarget();
    }

    @JavascriptInterface
    public void expand() {
        handler.obtainMessage(MSG_EXPAND, createHandlerData(null)).sendToTarget();
    }

    @JavascriptInterface
    public void expand(String data) {
        handler.obtainMessage(MSG_EXPAND, createHandlerData(data)).sendToTarget();
    }

    @JavascriptInterface
    public void setExpandProperties(String data) {
        handler.obtainMessage(MSG_SET_EXPAND_PROPERTIES, createHandlerData(data)).sendToTarget();
    }

    @JavascriptInterface
    public void setOrientationProperties(String data) {
        handler.obtainMessage(1004, createHandlerData(data)).sendToTarget();
    }

    @JavascriptInterface
    public void setResizeProperties(String data) {
        handler.obtainMessage(1005, createHandlerData(data)).sendToTarget();
    }

    @JavascriptInterface
    public void resize() {
        handler.obtainMessage(1006, createHandlerData(null)).sendToTarget();
    }

    @JavascriptInterface
    public void log(String data) {
        handler.obtainMessage(1007, createHandlerData(data)).sendToTarget();
    }

    @JavascriptInterface
    public void loaded() {
        handler.obtainMessage(1008, createHandlerData(null)).sendToTarget();
    }

    @JavascriptInterface
    public void playVideo(String data) {
        handler.obtainMessage(1009, createHandlerData(data)).sendToTarget();
    }

    @JavascriptInterface
    public void storePicture(String data) {
        handler.obtainMessage(1010, createHandlerData(data)).sendToTarget();
    }

    @JavascriptInterface
    public void createCalendarEvent(String data) {
        handler.obtainMessage(1011, createHandlerData(data)).sendToTarget();
    }

    @JavascriptInterface
    public void useCustomClose(String data) {
        handler.obtainMessage(1012, createHandlerData(data)).sendToTarget();
    }

    @JavascriptInterface
    public void setPageSize(String data) {
        handler.obtainMessage(1014, createHandlerData(data)).sendToTarget();
    }

    @JavascriptInterface
    private HandlerData createHandlerData(String data) {
        if (this.jsiHandler == null) {
            return null;
        }
        HandlerData handlerData = new HandlerData();
        handlerData.data = data;
        handlerData.dataHandler = this.jsiHandler;
        return handlerData;
    }

    @JavascriptInterface
    public void adConverted() {
        handler.obtainMessage(1015, createHandlerData(null)).sendToTarget();
    }

    @JavascriptInterface
    public void prepareVideo(String url) {
        handler.obtainMessage(1016, createHandlerData(url)).sendToTarget();
    }

    @JavascriptInterface
    public void setVideoOptions(String data) {
        handler.obtainMessage(1017, createHandlerData(data)).sendToTarget();
    }

    @JavascriptInterface
    public void willCloseAdOnRedirect(String data) {
        handler.obtainMessage(1018, createHandlerData(data)).sendToTarget();
    }

    @JavascriptInterface
    public void shouldEnableCloseRegion(String data) {
        handler.obtainMessage(MSG_ENABLE_CLOSE_REGION, createHandlerData(data)).sendToTarget();
    }
}
