package com.google.android.gms.ads.formats;

public final class NativeAdOptions {
    public static final int ORIENTATION_ANY = 0;
    public static final int ORIENTATION_LANDSCAPE = 2;
    public static final int ORIENTATION_PORTRAIT = 1;
    private final boolean zzoN;
    private final int zzoO;
    private final boolean zzoP;

    public static final class Builder {
        private boolean zzoN = false;
        private int zzoO = 0;
        private boolean zzoP = false;

        public NativeAdOptions build() {
            return new NativeAdOptions();
        }

        public Builder setImageOrientation(int orientation) {
            this.zzoO = orientation;
            return this;
        }

        public Builder setRequestMultipleImages(boolean shouldRequestMultipleImages) {
            this.zzoP = shouldRequestMultipleImages;
            return this;
        }

        public Builder setReturnUrlsForImageAssets(boolean shouldReturnUrls) {
            this.zzoN = shouldReturnUrls;
            return this;
        }
    }

    private NativeAdOptions(Builder builder) {
        this.zzoN = builder.zzoN;
        this.zzoO = builder.zzoO;
        this.zzoP = builder.zzoP;
    }

    public int getImageOrientation() {
        return this.zzoO;
    }

    public boolean shouldRequestMultipleImages() {
        return this.zzoP;
    }

    public boolean shouldReturnUrlsForImageAssets() {
        return this.zzoN;
    }
}
