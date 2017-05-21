package com.nativex.monetization.mraid.objects;

public interface ObjectNames {

    public interface CalendarEntryData {
        public static final String DESCRIPTION = "description";
        public static final String END = "end";
        public static final String ID = "id";
        public static final String LOCATION = "location";
        public static final String RECURRENCE = "recurrence";
        public static final String REMINDER = "reminder";
        public static final String START = "start";
        public static final String STATUS = "status";
        public static final String SUMMARY = "summary";
        public static final String TRANSPARENCY = "transparency";
    }

    public interface CurrentPosition {
        public static final String HEIGHT = "height";
        public static final String WIDTH = "width";
        public static final String X = "x";
        public static final String Y = "y";
    }

    public interface DefaultPosition {
        public static final String HEIGHT = "height";
        public static final String WIDTH = "width";
        public static final String X = "x";
        public static final String Y = "y";
    }

    public interface ExpandProperties {
        public static final String HEIGHT = "height";
        public static final String IS_MODAL = "isModal";
        public static final String USE_CUSTOM_CLOSE = "useCustomClose";
        public static final String WIDTH = "width";
    }

    public interface MaxSize {
        public static final String HEIGHT = "height";
        public static final String WIDTH = "width";
    }

    public interface OrientationProperties {
        public static final String ALLOW_ORIENTATION_CHANGE = "allowOrientationChange";
        public static final String FORCE_ORIENTATION = "forceOrientation";
    }

    public interface ResizeProperties {
        public static final String ALLOWS_OFFSCREEN = "allowOffscreen";
        public static final String CUSTOM_CLOSE_POSITION = "customClosePosition";
        public static final String HEIGHT = "height";
        public static final String OFFSET_X = "offsetX";
        public static final String OFFSET_Y = "offsetY";
        public static final String WIDTH = "width";
    }

    public interface ScreenSize {
        public static final String HEIGHT = "height";
        public static final String WIDTH = "width";
    }
}
