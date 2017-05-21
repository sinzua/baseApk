package com.amazon.device.iap.internal.b;

/* compiled from: ReceiptParsingException */
public class a extends RuntimeException {
    private final String a;
    private final String b;

    public a(String str, String str2, Throwable th) {
        super(th);
        this.a = str;
        this.b = str2;
    }

    public String a() {
        return this.a;
    }
}
