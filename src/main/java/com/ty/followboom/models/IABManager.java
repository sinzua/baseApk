package com.ty.followboom.models;

public class IABManager {
    private static IABManager sIABManager;

    public static IABManager getSingleton() {
        if (sIABManager == null) {
            synchronized (IABManager.class) {
                if (sIABManager == null) {
                    sIABManager = new IABManager();
                }
            }
        }
        return sIABManager;
    }
}
