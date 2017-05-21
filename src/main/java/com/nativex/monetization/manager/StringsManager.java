package com.nativex.monetization.manager;

import com.nativex.common.StringConstants;
import com.nativex.monetization.enums.StringResources;
import java.util.HashMap;
import java.util.Map.Entry;

public class StringsManager {
    private static HashMap<String, String> stringResourcesMap;

    public static void initialize() {
        stringResourcesMap = new HashMap();
        stringResourcesMap.clear();
        for (Entry<String, String> entry : StringConstants.stringsUsed.entrySet()) {
            stringResourcesMap.put(entry.getKey(), entry.getValue());
        }
    }

    public static String getString(StringResources resourceName) {
        if (stringResourcesMap == null) {
            initialize();
        }
        String stringFromXml = (String) stringResourcesMap.get(resourceName.toString());
        return stringFromXml != null ? stringFromXml : "";
    }
}
