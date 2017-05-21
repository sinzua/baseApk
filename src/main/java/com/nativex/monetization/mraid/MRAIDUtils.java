package com.nativex.monetization.mraid;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.nativex.common.Log;
import com.nativex.common.StringConstants;
import com.nativex.monetization.mraid.objects.ObjectNames.ExpandProperties;
import com.nativex.monetization.theme.ThemeElementTypes;
import com.supersonicads.sdk.utils.Constants.ForceClosePosition;
import com.supersonicads.sdk.utils.Constants.JSMethods;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class MRAIDUtils {
    private static final String JS_QUOTE = "'";

    public enum ClosePosition {
        TOP_LEFT(ForceClosePosition.TOP_LEFT, ThemeElementTypes.MRAID_CLOSE_BUTTON_TOP_LEFT),
        TOP_RIGHT(ForceClosePosition.TOP_RIGHT, ThemeElementTypes.MRAID_CLOSE_BUTTON_TOP_RIGHT),
        TOP_CENTER("top-center", ThemeElementTypes.MRAID_CLOSE_BUTTON_TOP_CENTER),
        CENTER("center", ThemeElementTypes.MRAID_CLOSE_BUTTON_CENTER),
        BOTTOM_LEFT(ForceClosePosition.BOTTOM_LEFT, ThemeElementTypes.MRAID_CLOSE_BUTTON_BOTTOM_LEFT),
        BOTTOM_RIGHT(ForceClosePosition.BOTTOM_RIGHT, ThemeElementTypes.MRAID_CLOSE_BUTTON_BOTTOM_RIGHT),
        BOTTOM_CENTER("bottom-center", ThemeElementTypes.MRAID_CLOSE_BUTTON_BOTTOM_CENTER);
        
        private final String position;
        private final ThemeElementTypes themeElement;

        private ClosePosition(String s, ThemeElementTypes themeElement) {
            this.position = s;
            this.themeElement = themeElement;
        }

        public String getName() {
            return this.position;
        }

        public ThemeElementTypes getThemeElementType() {
            return this.themeElement;
        }

        public static ClosePosition getPosition(String s) {
            for (ClosePosition position : values()) {
                if (position.position.equals(s)) {
                    return position;
                }
            }
            return TOP_RIGHT;
        }
    }

    public enum Events {
        READY(ParametersKeys.READY),
        ERROR("error"),
        STATE_CHANGE("stateChange"),
        VIEWABLE_CHANGE(JSMethods.VIEWABLE_CHANGE),
        SIZE_CHANGE("sizeChange");
        
        private final String event;

        private Events(String e) {
            this.event = e;
        }

        public String getEvent() {
            return "\"" + this.event + "\"";
        }
    }

    public enum Features {
        SMS("sms"),
        TEL("tel"),
        CALENDAR("calendar"),
        STORE_PICTURE("storePicture"),
        INLINE_VIDEO("inlineVideo");
        
        private final String feature;
        private int supportLevel;
        private Boolean supported;

        private Features(String s) {
            this.supported = null;
            this.supportLevel = 0;
            this.feature = s;
        }

        public String getName() {
            return MRAIDUtils.wrapInJSQuotes(this.feature);
        }

        public boolean isSupported(Activity activity) {
            if (this.supported == null) {
                this.supported = MRAIDDeviceManager.checkFeatureSupport(activity, this);
                this.supportLevel = MRAIDDeviceManager.checkSupportLevel(activity, this);
            }
            return this.supported.booleanValue();
        }

        public int getSupportLevel() {
            return this.supportLevel;
        }
    }

    static class JSCommand {
        private final JSCommands command;
        private String jsObjectName;
        private String[] parameters;

        private JSCommand(JSCommands command) {
            this.jsObjectName = "mraid";
            this.command = command;
        }

        public JSCommands getCommand() {
            return this.command;
        }

        public void setParams(String... params) {
            this.parameters = params;
        }

        public String[] getParams() {
            return this.parameters;
        }

        public void setJsObjectName(String objectName) {
            this.jsObjectName = objectName;
        }

        public String getParamsAsString() {
            String params = "";
            if (this.parameters != null && this.parameters.length > 0) {
                params = this.parameters[0];
                for (int i = 1; i < this.parameters.length; i++) {
                    params = params + "," + this.parameters[i];
                }
            }
            return params;
        }

        public String getJSCall() {
            return "javascript:" + this.jsObjectName + "." + this.command.getCommand() + "(" + getParamsAsString() + ");";
        }
    }

    public enum JSCommands {
        SHOW_AD("showAd"),
        GET_VERSION("getVersion"),
        ADD_EVENT_LISTENER("addEventListener"),
        REMOVE_EVENT_LISTENER("removeEventListener"),
        FIRE_EVENT("fireEvent"),
        FIRE_CHANGE_EVENT("fireChangeEvent"),
        FIRE_ERROR_EVENT("fireErrorEvent"),
        GET_STATE("getState"),
        SET_STATE("setState"),
        SET_FEATURE_SUPPORT("setFeatureSupport"),
        SET_NATIVE_VIDEO_FEATURE_SUPPORT("setNativeVideoFeatureSupport"),
        SUPPORTS("supports"),
        GET_PLACEMENT_TYPE("getPlacementType"),
        SET_PLACEMENT_TYPE("setPlacementType"),
        GET_SCREEN_SIZE("getScreenSize"),
        SET_SCREEN_SIZE("setScreenSize"),
        GET_MAX_SIZE("getMaxSize"),
        SET_MAX_SIZE("setMaxSize"),
        USE_CUSTOM_CLOSE(ExpandProperties.USE_CUSTOM_CLOSE),
        OPEN("open"),
        CLOSE("close"),
        EXPAND("expand"),
        RESIZE("resize"),
        GET_ORIENTATION_PROPERTIES("getOrientationProperties"),
        SET_ORIENTATION_PROPERTIES("setOrientationProperties"),
        GET_EXPAND_PROPERTIES("getExpandProperties"),
        SET_EXPAND_PROPERTIES("setExpandProperties"),
        GET_RESIZE_PROPERTIES("getResizeProperties"),
        SET_RESIZE_PROPERTIES("setResizeProperties"),
        GET_DEFAULT_POSITION("getDefaultPosition"),
        SET_DEFAULT_POSITION("setDefaultPosition"),
        GET_CURRENT_POSITION("getCurrentPosition"),
        SET_CURRENT_POSITION("setCurrentPosition"),
        CREATE_CALENDAR_EVENT("createCalendarEvent"),
        PLAY_VIDEO("playVideo"),
        STORE_PICTURE("storePicture"),
        IS_VIEWABLE(ParametersKeys.IS_VIEWABLE),
        SET_IS_VIEWABLE("setIsViewable"),
        CALL_SDK("callSdk"),
        LOG("log"),
        SET_IS_DEBUG_MODE("setIsDebugMode"),
        GET_PAGE_SIZE("getPageSize"),
        CALL_RECEIVED("callReceived"),
        TRACK_VIDEO("video");
        
        private final String command;

        private JSCommands(String cmd) {
            this.command = cmd;
        }

        String getCommand() {
            return this.command;
        }

        public JSCommand instance() {
            return new JSCommand();
        }
    }

    public enum JSDialogAction {
        PROMPT,
        CONFIRM,
        ALERT,
        BEFORE_UNLOAD
    }

    public enum NativeXEvents {
        VIDEO_CANCELLED("videoCancelled");
        
        private final String event;

        private NativeXEvents(String e) {
            this.event = e;
        }

        public String getEvent() {
            return "\"" + this.event + "\"";
        }
    }

    public enum Orientation {
        PORTRAIT(ParametersKeys.ORIENTATION_PORTRAIT),
        LANDSCAPE(ParametersKeys.ORIENTATION_LANDSCAPE),
        NONE("none");
        
        private final String value;

        private Orientation(String s) {
            this.value = s;
        }

        public String getValue() {
            return this.value;
        }

        public static Orientation getOrientation(String s) {
            for (Orientation orientation : values()) {
                if (orientation.getValue().equals(s)) {
                    return orientation;
                }
            }
            return NONE;
        }
    }

    public enum PlacementType {
        INLINE("inline"),
        INTERSTITIAL("interstitial");
        
        private final String placement;

        private PlacementType(String s) {
            this.placement = s;
        }

        public String getValue() {
            return this.placement;
        }

        public String getName() {
            return MRAIDUtils.wrapInJSQuotes(this.placement);
        }

        public String toString() {
            return this.placement;
        }
    }

    enum States {
        LOADING("loading"),
        DEFAULT("default"),
        EXPANDED("expanded"),
        RESIZED("resized"),
        HIDDEN("hidden");
        
        private final String state;

        private States(String s) {
            this.state = s;
        }

        public String getName() {
            return MRAIDUtils.wrapInJSQuotes(this.state);
        }
    }

    enum UrlScheme {
        OPEN("mraid://open"),
        CLOSE("mraid://close"),
        PLAY_VIDEO("mraid://playVideo"),
        EXPAND("mraid://expand"),
        RESIZE("mraid://resize"),
        STORE_PICTURE("mraid://storePicture"),
        SET_RESIZE_PROPERTIES("mraid://setResizeProperties"),
        SET_EXPAND_PROPERTIES("mraid://setExpandProperties"),
        SET_ORIENTATION_PROPERTIES("mraid://setOrientationProperties"),
        LOADED("nativex://loaded"),
        LOG("nativex://log"),
        USE_CUSTOM_CLOSE("mraid://useCustomClose"),
        CREATE_CALENDAR_EVENT("mraid://createCalendarEvent/"),
        SIZE_SCRIPT_SET_PAGE_SIZE("nativeXSizeScript://setPageSize/"),
        NON_REWARD_CLOSE("webview:dismissView"),
        NON_REWARD_CLOSE_OLD("w3imaap:dismissView"),
        GOOGLE_MARKET("http://market.android.com/details"),
        GOOGLE_PLAY("https://play.google.com/"),
        MARKET_CUSTOM_SCHEME("market://"),
        AD_CONVERTED("nativex://adConverted/");
        
        private final String scheme;

        private UrlScheme(String scheme) {
            this.scheme = scheme;
        }

        public String getScheme() {
            return this.scheme;
        }

        public static UrlScheme checkForScheme(String url) {
            for (UrlScheme scheme : values()) {
                if (url.startsWith(scheme.getScheme())) {
                    return scheme;
                }
            }
            return null;
        }
    }

    public static String wrapInJSQuotes(String s) {
        return JS_QUOTE + s + JS_QUOTE;
    }

    static Map<String, String> decodeData(String data) {
        int i = 0;
        if (data == null || data.trim().length() == 0) {
            return new HashMap(0);
        }
        Map<String, String> decodedData = new HashMap();
        try {
            Map<String, String> decodedData2 = new HashMap();
            try {
                String[] params = data.split(RequestParameters.AMPERSAND);
                int length = params.length;
                while (i < length) {
                    String[] pair = params[i].split(RequestParameters.EQUAL);
                    if (pair.length == 2) {
                        decodedData2.put(URLDecoder.decode(pair[0]), URLDecoder.decode(pair[1]));
                    }
                    i++;
                }
                return decodedData2;
            } catch (Exception e) {
                return decodedData2;
            }
        } catch (Exception e2) {
            return decodedData;
        }
    }

    public static String getExceptionDescription(Throwable e) {
        if (e != null) {
            return e.getClass().getSimpleName() + ": " + e.getMessage();
        }
        return "";
    }

    public static void analyseUrl(Context context, String url) {
        try {
            if (checkForDeepLinking(url)) {
                launchApplication(context, url);
            } else {
                startMRAIDBrowser(context, url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkForDeepLinking(String url) {
        if (url.startsWith("package") || url.startsWith(ParametersKeys.ACTION) || url.startsWith("unity")) {
            return true;
        }
        return false;
    }

    private static void launchApplication(Context context, String url) {
        String applicationPackageName;
        if (url.startsWith("package")) {
            Uri packageUri = Uri.parse(url);
            applicationPackageName = packageUri.getHost();
            String activityName = packageUri.getLastPathSegment();
            if (applicationPackageName != null && activityName == null) {
                try {
                    Log.d("Android DeepLinking package:" + applicationPackageName);
                    context.startActivity(context.getPackageManager().getLaunchIntentForPackage(applicationPackageName));
                    return;
                } catch (ActivityNotFoundException e) {
                    Log.e("Error in Android DeepLinking using package name. ActivityNotFound exception thrown.");
                    return;
                }
            } else if (applicationPackageName != null) {
                try {
                    Log.d("Android DeepLinking package:" + applicationPackageName + " activity:" + activityName);
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(applicationPackageName, applicationPackageName + "." + activityName));
                    intent.setFlags(268435456);
                    context.startActivity(intent);
                    return;
                } catch (ActivityNotFoundException e2) {
                    Log.e("Error in Android DeepLinking same application using component name. ActivityNotFound exception thrown.");
                    return;
                }
            } else {
                Log.e("Error in Android DeepLinking 'package' URI format. Has to be package://packageName/activityName");
                return;
            }
        }
        if (url.startsWith(ParametersKeys.ACTION)) {
            String action = null;
            String dataScheme = null;
            String[] splitStrings = url.split(RequestParameters.AMPERSAND);
            if (splitStrings.length > 0) {
                String actionSplit = null;
                String dataSchemeSplit = null;
                if (splitStrings.length > 1) {
                    actionSplit = splitStrings[0];
                }
                if (splitStrings.length >= 2) {
                    dataSchemeSplit = splitStrings[1];
                }
                if (actionSplit != null) {
                    String[] actionSplitStrings = actionSplit.split(RequestParameters.EQUAL);
                    if (actionSplitStrings.length == 2 && actionSplitStrings[0].equals(ParametersKeys.ACTION)) {
                        action = actionSplitStrings[1];
                    }
                } else {
                    Log.e("Error in Android DeepLinking other application URI format. Has to be action=actionName");
                }
                if (dataSchemeSplit != null) {
                    String[] dataSchemeSplitStrings = dataSchemeSplit.split(RequestParameters.EQUAL);
                    if (dataSchemeSplitStrings.length == 2 && dataSchemeSplitStrings[0].equals("data")) {
                        dataScheme = dataSchemeSplitStrings[1];
                    }
                } else {
                    Log.d("Error in Android DeepLinking other application URI format. Has to be data=dataURI");
                }
            }
            if (action != null) {
                try {
                    Log.d("Android DeepLinking action:" + action + " dataScheme:" + dataScheme);
                    intent = new Intent();
                    intent.setAction(action);
                    if (dataScheme != null) {
                        intent.setData(Uri.parse(dataScheme));
                    }
                    intent.setFlags(268435456);
                    context.startActivity(intent);
                    return;
                } catch (ActivityNotFoundException e3) {
                    Log.e("Error in Android DeepLinking other application. ActivityNotFound exception thrown.");
                    return;
                }
            }
            Log.e("Error in Android DeepLinking other application. Action name is not defined.");
            return;
        }
        if (url.startsWith("unity")) {
            packageUri = Uri.parse(url);
            applicationPackageName = packageUri.getHost();
            String sceneName = packageUri.getLastPathSegment();
            if (applicationPackageName != null && sceneName == null) {
                Log.d("Unity DeepLinking package:" + applicationPackageName);
                context.startActivity(context.getPackageManager().getLaunchIntentForPackage(applicationPackageName));
                return;
            } else if (applicationPackageName != null) {
                Log.d("Unity DeepLinking package:" + applicationPackageName + " sceneName:" + sceneName);
                intent = new Intent(StringConstants.ACTION_DEEPLINK);
                intent.putExtra(StringConstants.PACKAGE_NAME, applicationPackageName);
                intent.putExtra(StringConstants.SCENE_NAME, sceneName);
                context.sendBroadcast(intent);
                return;
            } else {
                Log.e("Error in Unity DeepLinking URI format. Both package name and scene name couldn't be found.");
                return;
            }
        }
        Log.e("Error in DeepLinking URI format. None of the URI format was found.");
    }

    public static void startMRAIDBrowser(Context context, String url) {
        Intent browserIntent = new Intent("android.intent.action.VIEW");
        browserIntent.setData(Uri.parse(url));
        browserIntent.addFlags(268435456);
        context.startActivity(browserIntent);
    }
}
