package com.nativex.monetization.ui;

public class ScreenDependentSize {
    private final int LARGE;
    private final int NORMAL;
    private final int SMALL;
    private final int XLARGE;
    public int size;

    public ScreenDependentSize(int size) {
        this(size, size, size, size);
    }

    public ScreenDependentSize(int small, int normal, int large, int xlarge) {
        this.SMALL = small;
        this.NORMAL = normal;
        this.LARGE = large;
        this.XLARGE = xlarge;
    }

    public static void setScreenSizes(DeviceScreenSize screenSize, ScreenDependentSize... sizes) {
        if (sizes.length > 0) {
            for (ScreenDependentSize screenDependentSize : sizes) {
                switch (screenSize.getScreenSize()) {
                    case EXTRA_LARGE:
                        screenDependentSize.size = screenDependentSize.XLARGE;
                        break;
                    case LARGE:
                        screenDependentSize.size = screenDependentSize.LARGE;
                        break;
                    case SMALL:
                        screenDependentSize.size = screenDependentSize.SMALL;
                        break;
                    default:
                        screenDependentSize.size = screenDependentSize.NORMAL;
                        break;
                }
                screenDependentSize.size = (int) ((((float) screenDependentSize.size) * screenSize.getDensity()) + 0.5f);
            }
        }
    }
}
