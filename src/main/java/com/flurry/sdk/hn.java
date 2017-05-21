package com.flurry.sdk;

import com.nativex.common.JsonRequestConstants.AndroidMarketInputs;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class hn {
    private static final String a = hn.class.getSimpleName();

    public Map<String, List<String>> a(String str) {
        kg.a(3, a, "Parsing referrer map");
        if (str == null) {
            return Collections.emptyMap();
        }
        Map<String, List<String>> hashMap = new HashMap();
        String[] split = str.split(RequestParameters.AMPERSAND);
        int length = split.length;
        for (String str2 : split) {
            String str22;
            String[] split2 = str22.split(RequestParameters.EQUAL);
            if (split2.length != 2) {
                kg.a(5, a, "Invalid referrer Element: " + str22 + " in referrer tag " + str);
            } else {
                str22 = URLDecoder.decode(split2[0]);
                String decode = URLDecoder.decode(split2[1]);
                if (hashMap.get(str22) == null) {
                    hashMap.put(str22, new ArrayList());
                }
                ((List) hashMap.get(str22)).add(decode);
            }
        }
        for (Entry entry : hashMap.entrySet()) {
            kg.a(3, a, "entry: " + ((String) entry.getKey()) + RequestParameters.EQUAL + entry.getValue());
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (hashMap.get(AndroidMarketInputs.SOURCE) == null) {
            stringBuilder.append("Campaign Source is missing.\n");
        }
        if (hashMap.get(AndroidMarketInputs.MEDIUM) == null) {
            stringBuilder.append("Campaign Medium is missing.\n");
        }
        if (hashMap.get(AndroidMarketInputs.CAMPAIGN) == null) {
            stringBuilder.append("Campaign Name is missing.\n");
        }
        if (stringBuilder.length() > 0) {
            kg.a(5, a, "Detected missing referrer keys : " + stringBuilder.toString());
        }
        return hashMap;
    }
}
