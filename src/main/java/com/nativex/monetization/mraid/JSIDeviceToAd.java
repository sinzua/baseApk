package com.nativex.monetization.mraid;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.gson.Gson;
import com.nativex.common.JsonRequestConstants.VideoPlayerOptions;
import com.nativex.common.Log;
import com.nativex.monetization.enums.VideoProgress;
import com.nativex.monetization.mraid.MRAIDUtils.Events;
import com.nativex.monetization.mraid.MRAIDUtils.Features;
import com.nativex.monetization.mraid.MRAIDUtils.JSCommands;
import com.nativex.monetization.mraid.MRAIDUtils.NativeXEvents;
import com.nativex.monetization.mraid.MRAIDUtils.PlacementType;
import com.nativex.monetization.mraid.objects.CurrentPosition;
import com.nativex.monetization.mraid.objects.DefaultPosition;
import com.nativex.monetization.mraid.objects.ExpandProperties;
import com.nativex.monetization.mraid.objects.MaxSize;
import com.nativex.monetization.mraid.objects.OrientationProperties;
import com.nativex.monetization.mraid.objects.ResizeProperties;
import com.nativex.monetization.mraid.objects.ScreenSize;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

class JSIDeviceToAd {
    private static final long ERROR_COMMANDS_DELAY = 100;
    private static final int MSG_DELAYED_ERROR = 242432;
    private static final JSIDeviceToAdInnerHandler handler = new JSIDeviceToAdInnerHandler(Looper.getMainLooper());
    private final MRAIDContainer container;
    private final DelayedErrors delayedErrors = new DelayedErrors();

    private class DelayedErrors extends ArrayList<JSCommand> {
        private static final long serialVersionUID = 4441288652625883092L;

        private DelayedErrors() {
        }

        public void runDelayed() {
            if (size() > 0 && !JSIDeviceToAd.handler.hasMessages(JSIDeviceToAd.MSG_DELAYED_ERROR)) {
                JSIDeviceToAd.handler.sendMessageDelayed(JSIDeviceToAd.handler.obtainMessage(JSIDeviceToAd.MSG_DELAYED_ERROR, this), JSIDeviceToAd.ERROR_COMMANDS_DELAY);
            }
        }

        public synchronized void executeCommands() {
            if (size() != 0) {
                List<JSCommand> commandsCopy = new ArrayList(this);
                clear();
                for (JSCommand command : commandsCopy) {
                    try {
                        JSIDeviceToAd.this.container.runJSCommand(command);
                    } catch (Exception e) {
                        add(command);
                    }
                }
                runDelayed();
            }
        }
    }

    private static class JSIDeviceToAdInnerHandler extends Handler {
        public JSIDeviceToAdInnerHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (msg.obj instanceof DelayedErrors) {
                removeMessages(msg.what);
                ((DelayedErrors) msg.obj).executeCommands();
            }
        }
    }

    public JSIDeviceToAd(MRAIDContainer container) {
        this.container = container;
    }

    public void setDeviceFeatures(Activity activity) {
        try {
            for (Features feature : Features.values()) {
                try {
                    setFeatureSupport(feature, feature.isSupported(activity));
                } catch (Exception e) {
                    fireErrorEvent("Error while checking feature support (" + feature.getName() + "). " + e.getClass().getSimpleName() + ": " + e.getMessage(), JSCommands.SET_FEATURE_SUPPORT);
                }
            }
        } catch (Throwable e2) {
            Log.e("JSDeviceToAd: Exception caught in setDeviceFeatures", e2);
            fireErrorEvent(e2, JSCommands.SET_FEATURE_SUPPORT);
        }
    }

    public void setNativeVideoFeatures() {
        try {
            for (Field field : VideoPlayerOptions.class.getDeclaredFields()) {
                if (field.getType() == String.class) {
                    setNativeVideoFeatureSupport((String) field.get(null));
                }
            }
        } catch (Throwable e) {
            Log.e("JSDeviceToAd: Exception caught in setNativeVideoFeatures", e);
            fireErrorEvent(e, JSCommands.SET_NATIVE_VIDEO_FEATURE_SUPPORT);
        }
    }

    public void close() {
        this.container.runJSCommand(JSCommands.CLOSE.instance());
    }

    public void fireErrorEvent(Throwable e, JSCommands action) {
        if (action != null || e == null) {
            fireErrorEvent(MRAIDUtils.getExceptionDescription(e), action);
        } else {
            MRAIDLogger.e("", e);
        }
    }

    public void fireErrorEvent(String msg, Throwable e, JSCommands action) {
        if (action != null || e == null) {
            fireErrorEvent(msg + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + MRAIDUtils.getExceptionDescription(e), action);
        } else {
            MRAIDLogger.e(msg, e);
        }
    }

    public void fireErrorEvent(String msg, JSCommands action) {
        if (action == null) {
            MRAIDLogger.e(msg);
        }
        JSCommand command = JSCommands.FIRE_ERROR_EVENT.instance();
        msg = MRAIDUtils.wrapInJSQuotes(msg);
        String commandName = null;
        if (action != null) {
            commandName = MRAIDUtils.wrapInJSQuotes(action.getCommand());
        }
        command.setParams(msg, commandName);
        try {
            this.container.runJSCommand(command);
        } catch (Exception e) {
            Log.e("JSIDeviceToAdd: Exception caught in fireErrorEvent()");
            this.delayedErrors.add(command);
            this.delayedErrors.runDelayed();
        }
    }

    public void fireChangeEvent(States newState) {
        JSCommand command = JSCommands.FIRE_CHANGE_EVENT.instance();
        command.setParams(newState.getName());
        this.container.runJSCommand(command);
    }

    public void trackVideo(String url, VideoProgress progress) {
        JSCommand command = JSCommands.TRACK_VIDEO.instance();
        command.setJsObjectName("richmedia.tracker");
        command.setParams(MRAIDUtils.wrapInJSQuotes(url), progress.toString());
        this.container.runJSCommand(command);
    }

    public void fireNativeXEvent(NativeXEvents event) {
        JSCommand command = JSCommands.FIRE_EVENT.instance();
        command.setJsObjectName("nativex");
        command.setParams(event.getEvent());
        this.container.runJSCommand(command);
    }

    public void fireEvent(Events event) {
        JSCommand command = JSCommands.FIRE_EVENT.instance();
        command.setParams(event.getEvent());
        this.container.runJSCommand(command);
    }

    public void setPlacementType(PlacementType type) {
        JSCommand command = JSCommands.SET_PLACEMENT_TYPE.instance();
        command.setParams(type.getName());
        this.container.runJSCommand(command);
    }

    public void setIsViewable(boolean isViewable) {
        JSCommand command = JSCommands.SET_IS_VIEWABLE.instance();
        command.setParams(Boolean.toString(isViewable));
        this.container.runJSCommand(command);
    }

    void setFeatureSupport(Features feature, boolean isEnabled) {
        JSCommand command = JSCommands.SET_FEATURE_SUPPORT.instance();
        command.setParams(feature.getName(), Boolean.toString(isEnabled));
        this.container.runJSCommand(command);
    }

    void setNativeVideoFeatureSupport(String nativeVideoOption) {
        JSCommand command = JSCommands.SET_NATIVE_VIDEO_FEATURE_SUPPORT.instance();
        command.setJsObjectName("nativex");
        command.setParams(MRAIDUtils.wrapInJSQuotes(nativeVideoOption));
        this.container.runJSCommand(command);
    }

    public void setState(States state) {
        setState(null, state);
    }

    public void setState(MRAIDWebView webView, States state) {
        JSCommand command = JSCommands.SET_STATE.instance();
        command.setParams(state.getName());
        if (webView == null) {
            this.container.runJSCommand(command);
        } else {
            webView.runJSCommand(command);
        }
    }

    public void setScreenSize(ScreenSize size) {
        JSCommand command = JSCommands.SET_SCREEN_SIZE.instance();
        command.setParams(new Gson().toJson((Object) size));
        this.container.runJSCommand(command);
    }

    public void setMaxSize(MaxSize size) {
        JSCommand command = JSCommands.SET_MAX_SIZE.instance();
        command.setParams(new Gson().toJson((Object) size));
        this.container.runJSCommand(command);
    }

    public void setOrientationProperties(OrientationProperties props) {
        JSCommand command = JSCommands.SET_ORIENTATION_PROPERTIES.instance();
        command.setParams(new Gson().toJson((Object) props));
        this.container.runJSCommand(command);
    }

    public void setCurrentPosition(CurrentPosition position) {
        JSCommand command = JSCommands.SET_CURRENT_POSITION.instance();
        command.setParams(new Gson().toJson((Object) position));
        this.container.runJSCommand(command);
    }

    public void setDefaultPosition(DefaultPosition position) {
        JSCommand command = JSCommands.SET_DEFAULT_POSITION.instance();
        command.setParams(new Gson().toJson((Object) position));
        this.container.runJSCommand(command);
    }

    public void setResizeProperties(ResizeProperties props) {
        JSCommand command = JSCommands.SET_RESIZE_PROPERTIES.instance();
        command.setParams(new Gson().toJson((Object) props));
        this.container.runJSCommand(command);
    }

    public void setExpandProperties(ExpandProperties props) {
        JSCommand command = JSCommands.SET_EXPAND_PROPERTIES.instance();
        command.setParams(new Gson().toJson((Object) props));
        this.container.runJSCommand(command);
    }
}
