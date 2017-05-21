package com.lidroid.xutils.bitmap.core;

public class BitmapSize {
    public static final BitmapSize ZERO = new BitmapSize(0, 0);
    private final int height;
    private final int width;

    public BitmapSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public BitmapSize scaleDown(int sampleSize) {
        return new BitmapSize(this.width / sampleSize, this.height / sampleSize);
    }

    public BitmapSize scale(float scale) {
        return new BitmapSize((int) (((float) this.width) * scale), (int) (((float) this.height) * scale));
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String toString() {
        return "_" + this.width + "_" + this.height;
    }
}
