package com.nativex.monetization.theme;

public enum ThemeElementTypes {
    LOGO,
    MESSAGE_DIALOG_BACKGROUND,
    MESSAGE_DIALOG_CLOSE_BUTTON_BACKGROUND,
    MESSAGE_DIALOG_OK_BUTTON_BACKGROUND_PRESSED,
    MESSAGE_DIALOG_OK_BUTTON_BACKGROUND_NORMAL,
    MESSAGE_DIALOG_OK_BUTTON_TEXT_COLOR,
    MESSAGE_DIALOG_BODY_BACKGROUND,
    MESSAGE_DIALOG_BODY_TEXT_COLOR,
    MESSAGE_DIALOG_TITLE_TEXT_COLOR,
    VIDEO_PLAYER_CLOSE_BUTTON_BACKGROUND,
    VIDEO_PLAYER_PROGRESS_BACKGROUND_EMPTY,
    VIDEO_PLAYER_PROGRESS_BACKGROUND_BUFFERED,
    VIDEO_PLAYER_PROGRESS_BACKGROUND_WATCHED,
    VIDEO_PLAYER_PROGRESS_ELAPSED_TEXT_COLOR,
    VIDEO_PLAYER_PROGRESS_DURATION_TEXT_COLOR,
    VIDEO_PLAYER_CONTROLS_HINT_BACKGROUND,
    VIDEO_PLAYER_CONTROLS_HINT_TEXT_COLOR,
    VIDEO_PLAYER_CONTROLS_SHOW_ANIMATION,
    VIDEO_PLAYER_CONTROLS_HIDE_ANIMATION,
    VIDEO_PLAYER_CONTROLS_HINT_SHOW_ANIMATION,
    VIDEO_PLAYER_CONTROLS_HINT_HIDE_ANIMATION,
    VIDEO_PLAYER_CONTROLS_CLOSE_SHOW_ANIMATION,
    VIDEO_PLAYER_CONTROLS_CLOSE_HIDE_ANIMATION,
    VIDEO_PLAYER_CONTROLS_PLAYER_SHOW_ANIMATION,
    VIDEO_PLAYER_CONTROLS_PLAYER_HIDE_ANIMATION,
    VIDEO_PLAYER_CONTROLS_PLAY_BUTTON_BACKGROUND,
    VIDEO_PLAYER_CONTROLS_PAUSE_BUTTON_BACKGROUND,
    VIDEO_ACTION_DIALOG_BACKGROUND,
    VIDEO_ACTION_DIALOG_CLOSE_BUTTON_BACKGROUND,
    VIDEO_ACTION_DIALOG_OFFER_NAME_TEXT_COLOR,
    VIDEO_ACTION_DIALOG_OFFER_DESCRIPTION_TEXT_COLOR,
    VIDEO_ACTION_DIALOG_OFFER_BACKGROUND,
    VIDEO_ACTION_DIALOG_SCROLLBAR_EMPTY_BACKGROUND,
    VIDEO_ACTION_DIALOG_SCROLLBAR_SELECTED_BACKGROUND,
    VIDEO_ACTION_DIALOG_SCROLLBAR_BACKGROUND,
    VIDEO_ACTION_DIALOG_TITLE_CONGRATULATIONS_TEXT_COLOR,
    VIDEO_ACTION_DIALOG_TITLE_REWARD_TEXT_COLOR,
    VIDEO_ACTION_DIALOG_TITLE_BACKGROUND,
    NATIVE_VIDEO_PLAYER_CLOSE_BUTTON,
    NATIVE_VIDEO_PLAYER_MUTE_BUTTON,
    NATIVE_VIDEO_PLAYER_UNMUTE_BUTTON,
    NATIVE_VIDEO_PLAYER_PRIVACY_BUTTON,
    MRAID_CLOSE_BUTTON_DEFAULT,
    MRAID_CLOSE_BUTTON_TOP_RIGHT,
    MRAID_CLOSE_BUTTON_TOP_LEFT,
    MRAID_CLOSE_BUTTON_TOP_CENTER,
    MRAID_CLOSE_BUTTON_BOTTOM_RIGHT,
    MRAID_CLOSE_BUTTON_BOTTOM_LEFT,
    MRAID_CLOSE_BUTTON_BOTTOM_CENTER,
    MRAID_CLOSE_BUTTON_CENTER;
    
    private static int id;
    private Integer key;

    static {
        id = 1;
    }

    public int getKey() {
        if (this.key == null) {
            this.key = Integer.valueOf(id);
            id++;
        }
        return this.key.intValue();
    }
}
