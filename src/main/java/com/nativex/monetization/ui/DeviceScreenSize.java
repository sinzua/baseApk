package com.nativex.monetization.ui;

public class DeviceScreenSize {
    private static SCREEN_SIZE deviceScreenSize = SCREEN_SIZE.UNKNOWN;
    private float density;
    private float diagonalInches;
    private float heightInches;
    private int heightPixels;
    private SCREEN_SIZE screenSize = SCREEN_SIZE.SMALL;
    private float widthInches;
    private int widthPixels;

    public enum SCREEN_SIZE {
        UNKNOWN(0.0f),
        SMALL(3.5f),
        NORMAL(5.5f),
        LARGE(7.1f),
        EXTRA_LARGE(10.1f);
        
        private final float screenSize;

        private SCREEN_SIZE(float value) {
            this.screenSize = value;
        }

        public float getSize() {
            return this.screenSize;
        }
    }

    public int getWidthPixels() {
        return this.widthPixels;
    }

    public int getHeightPixels() {
        return this.heightPixels;
    }

    public float getWidthInches() {
        return this.widthInches;
    }

    public float getHeightInches() {
        return this.heightInches;
    }

    public float getDiagonalInches() {
        return this.diagonalInches;
    }

    public float getDensity() {
        return this.density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public void setWidthPixels(int widthPixels) {
        this.widthPixels = widthPixels;
    }

    public void setHeightPixels(int heightPixels) {
        this.heightPixels = heightPixels;
    }

    public void setWidthInches(float widthInches) {
        this.widthInches = widthInches;
    }

    public void setHeightInches(float heightInches) {
        this.heightInches = heightInches;
    }

    public void setDiagonalInches(float diagonalInches) {
        this.diagonalInches = diagonalInches;
        if (diagonalInches < SCREEN_SIZE.SMALL.getSize()) {
            this.screenSize = SCREEN_SIZE.SMALL;
        } else if (diagonalInches < SCREEN_SIZE.NORMAL.getSize()) {
            this.screenSize = SCREEN_SIZE.NORMAL;
        } else if (diagonalInches < SCREEN_SIZE.LARGE.getSize()) {
            this.screenSize = SCREEN_SIZE.LARGE;
        } else {
            this.screenSize = SCREEN_SIZE.EXTRA_LARGE;
        }
        deviceScreenSize = this.screenSize;
    }

    public int getHeightInPixels(float inches) {
        return (int) (((float) this.heightPixels) * (inches / this.heightInches));
    }

    public int getWidthInPixels(float inches) {
        return (int) (((float) this.widthPixels) * (inches / this.widthInches));
    }

    public SCREEN_SIZE getScreenSize() {
        return this.screenSize;
    }

    public int setDensity(int size) {
        return (int) (((float) size) * this.density);
    }

    public static SCREEN_SIZE getDeviceScreenSize() {
        return deviceScreenSize;
    }
}
