package com.nativex.common;

import java.util.HashMap;

public class StringConstants {
    public static final String ACTION_DEEPLINK = "com.nativex.action.DEEPLINK";
    public static final String EXTRA_VIDEO_URL = "VideoURL";
    public static final String MESSAGE_DIALOG_BUTTON_TEXT = "Ok";
    public static final String PACKAGE_NAME = "package_name";
    public static final String SCENE_NAME = "scene_name";
    public static final HashMap<String, String> stringsUsed = new HashMap();

    static {
        stringsUsed.put("complex_video_player_controls_hint", "Tap to display \n video controls");
        stringsUsed.put("complex_video_cannot_be_played", "This video cannot be played");
        stringsUsed.put("complex_video_server_error", "Server error.");
        stringsUsed.put("complex_video_unknown_error", "Unknown error.");
        stringsUsed.put("complex_video_media_player_error", "Media Player Error");
        stringsUsed.put("no_network_connectivity", "No Network Connectivity");
        stringsUsed.put("no_network_dialog_connect", "Connect");
        stringsUsed.put("progress_dialog_loading", "Loading, please wait...");
    }
}
