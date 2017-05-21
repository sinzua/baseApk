package com.nativex.monetization.enums;

public enum StringResources {
    VIDEO_PLAYER_CONTROLS_HINT("complex_video_player_controls_hint"),
    VIDEO_CANNOT_BE_PLAYED("complex_video_cannot_be_played"),
    VIDEO_SERVER_ERROR("complex_video_server_error"),
    VIDEO_UNKNOWN_ERROR("complex_video_unknown_error"),
    VIDEO_MEDIA_PLAYER_ERROR("complex_video_media_player_error"),
    NO_NETWORK_CONNECTIVITY("no_network_connectivity"),
    PROGRESS_DIALOG_LOADING("progress_dialog_loading");
    
    private final String name;

    private StringResources(String xmlName) {
        this.name = xmlName;
    }

    public String toString() {
        return this.name;
    }
}
