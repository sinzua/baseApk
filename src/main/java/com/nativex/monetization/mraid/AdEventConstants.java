package com.nativex.monetization.mraid;

class AdEventConstants {
    static final String EVENT_MESSAGE_ALREADY_FETCHED = "Ad is already fetched and ready to display";
    static final String EVENT_MESSAGE_ALREADY_SHOWN = "Ad is already shown. Dismiss it in order to show another one";
    static final String EVENT_MESSAGE_BEFORE_DISPLAY = "Before Ad is displayed";
    static final String EVENT_MESSAGE_COLLAPSED = "The ad was returned to default state from being expanded or resized";
    static final String EVENT_MESSAGE_DISMISSED = "Ad is dismissed";
    static final String EVENT_MESSAGE_DISPLAYED = "Ad displayed";
    static final String EVENT_MESSAGE_DOWNLOADING = "Ad is being downloaded at the moment";
    static final String EVENT_MESSAGE_ERROR_ATTACH_FAIL = "Failed to attach the ad to the view hierarchy";
    static final String EVENT_MESSAGE_ERROR_CREATING = "Error while creating/updating the ad";
    static final String EVENT_MESSAGE_ERROR_DISMISS_FAIL = "The ad failed to dismiss.";
    static final String EVENT_MESSAGE_ERROR_DOWNLOADING = "Error while downloading the ad";
    static final String EVENT_MESSAGE_ERROR_NO_AD = "There was no ad to show.";
    static final String EVENT_MESSAGE_ERROR_NO_SESSION = "No session. Unable to fetch an ad.";
    static final String EVENT_MESSAGE_ERROR_RELEASED = "Ad was released before the content was loaded.";
    static final String EVENT_MESSAGE_EXPANDED = "The ad was expanded";
    static final String EVENT_MESSAGE_EXPIRED = "The ad has expired";
    static final String EVENT_MESSAGE_FETCHED = "Ad finished downloading";
    static final String EVENT_MESSAGE_RESIZED = "The ad was resized";
    static final String EVENT_MESSAGE_USER_NAVIGATES_OUT_OF_APP = "The user clicked on a link in the ad and is navigating out of the app";
    static final String EVENT_MESSAGE_USER_TOUCH = "The user touched the banner";
    static final String EVENT_MESSAGE_VIDEO_75_PERCENT_COMPLETED = "Video ad has reached 75% completion";
    static final String EVENT_MESSAGE_VIDEO_COMPLETED = "Video ad has completed";

    AdEventConstants() {
    }
}
