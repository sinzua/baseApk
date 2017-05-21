package com.google.android.gms.internal;

import com.lidroid.xutils.util.CharsetUtils;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class zzmz {
    private static final Pattern zzaof = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
    private static final Pattern zzaog = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
    private static final Pattern zzaoh = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    private static String decode(String content, String encoding) {
        if (encoding == null) {
            encoding = CharsetUtils.DEFAULT_ENCODING_CHARSET;
        }
        try {
            return URLDecoder.decode(content, encoding);
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Map<String, String> zza(URI uri, String str) {
        Map<String, String> emptyMap = Collections.emptyMap();
        String rawQuery = uri.getRawQuery();
        if (rawQuery == null || rawQuery.length() <= 0) {
            return emptyMap;
        }
        Map<String, String> hashMap = new HashMap();
        Scanner scanner = new Scanner(rawQuery);
        scanner.useDelimiter(RequestParameters.AMPERSAND);
        while (scanner.hasNext()) {
            String[] split = scanner.next().split(RequestParameters.EQUAL);
            if (split.length == 0 || split.length > 2) {
                throw new IllegalArgumentException("bad parameter");
            }
            String decode = decode(split[0], str);
            Object obj = null;
            if (split.length == 2) {
                obj = decode(split[1], str);
            }
            hashMap.put(decode, obj);
        }
        return hashMap;
    }
}
