package com.flurry.sdk;

import android.text.TextUtils;
import java.util.Arrays;

public class jd {
    private static String a = jd.class.getName();

    public static String a(String str) {
        String str2 = "a=" + js.a().d();
        if (TextUtils.isEmpty(str)) {
            return str2;
        }
        String str3 = "cid=" + b(str);
        return String.format("%s&%s", new Object[]{str2, str3});
    }

    private static String b(String str) {
        byte[] bArr = null;
        if (str != null && str.trim().length() > 0) {
            try {
                byte[] f = lt.f(str);
                if (f == null || f.length != 20) {
                    kg.a(6, a, "sha1 is not " + 20 + " bytes long: " + Arrays.toString(f));
                    f = null;
                } else {
                    try {
                        kg.a(5, a, "syndication hashedId is:" + new String(f));
                    } catch (Exception e) {
                        bArr = f;
                        kg.a(6, a, "Exception in getHashedSyndicationIdString()");
                        return lt.a(bArr);
                    }
                }
                bArr = f;
            } catch (Exception e2) {
                kg.a(6, a, "Exception in getHashedSyndicationIdString()");
                return lt.a(bArr);
            }
        }
        return lt.a(bArr);
    }
}
