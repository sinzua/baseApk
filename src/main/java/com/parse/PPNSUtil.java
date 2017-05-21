package com.parse;

import android.app.Service;
import java.lang.reflect.InvocationTargetException;

class PPNSUtil {
    static String CLASS_PPNS_SERVICE = "com.parse.PPNSService";

    PPNSUtil() {
    }

    public static boolean isPPNSAvailable() {
        try {
            Class.forName(CLASS_PPNS_SERVICE);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static ProxyService newPPNSService(Service service) {
        try {
            return (ProxyService) Class.forName(CLASS_PPNS_SERVICE).getDeclaredConstructor(new Class[]{Service.class}).newInstance(new Object[]{service});
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e2) {
            throw new RuntimeException(e2);
        } catch (InvocationTargetException e3) {
            throw new RuntimeException(e3);
        } catch (InstantiationException e4) {
            throw new RuntimeException(e4);
        } catch (IllegalAccessException e5) {
            throw new RuntimeException(e5);
        }
    }
}
