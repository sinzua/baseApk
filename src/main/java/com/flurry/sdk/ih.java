package com.flurry.sdk;

public enum ih {
    HW_MACHINE(0),
    MODEL(1),
    BRAND(2),
    ID(3),
    DEVICE(4),
    PRODUCT(5),
    VERSION_RELEASE(6);
    
    private final int h;

    private ih(int i) {
        this.h = i;
    }

    public int a() {
        return this.h;
    }
}
