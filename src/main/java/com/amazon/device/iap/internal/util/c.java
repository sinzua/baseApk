package com.amazon.device.iap.internal.util;

/* compiled from: ReceiptVersion */
public enum c {
    LEGACY(0),
    V1(1),
    V2(2);
    
    private int d;

    public static c[] a() {
        return (c[]) e.clone();
    }

    private c(int i) {
        this.d = i;
    }
}
