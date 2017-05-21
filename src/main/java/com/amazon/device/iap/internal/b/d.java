package com.amazon.device.iap.internal.b;

/* compiled from: ReceiptVerificationException */
public class d extends RuntimeException {
    private final String a;
    private final String b;
    private final String c;

    public d(String str, String str2, String str3) {
        this.a = str;
        this.b = str2;
        this.c = str3;
    }

    public String a() {
        return this.a;
    }
}
