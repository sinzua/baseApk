package com.supersonic.mediationsdk.sdk;

import com.supersonic.mediationsdk.SupersonicObject;

public class SupersonicFactory {
    private static Supersonic mInsatnce;

    public static synchronized Supersonic getInstance() {
        Supersonic supersonic;
        synchronized (SupersonicFactory.class) {
            if (mInsatnce == null) {
                mInsatnce = new SupersonicObject();
            }
            supersonic = mInsatnce;
        }
        return supersonic;
    }
}
