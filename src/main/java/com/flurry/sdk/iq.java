package com.flurry.sdk;

public enum iq {
    GET("GET", 0),
    PUT("PUT", 1),
    POST("POST", 2);
    
    String d;
    int e;

    private iq(String str, int i) {
        this.d = str;
        this.e = i;
    }

    public static iq a(int i) {
        switch (i) {
            case 0:
                return GET;
            case 1:
                return PUT;
            case 2:
                return POST;
            default:
                return null;
        }
    }

    public int a() {
        return this.e;
    }
}
