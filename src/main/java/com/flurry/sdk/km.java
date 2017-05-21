package com.flurry.sdk;

import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class km {
    private final HashMap<String, String> a = new HashMap();

    public void a(String str, String str2) {
        if (str != null) {
            this.a.put(str, str2);
        }
    }

    public String a() {
        StringBuilder stringBuilder = new StringBuilder();
        Set<Entry> entrySet = this.a.entrySet();
        if (entrySet.size() > 0) {
            for (Entry entry : entrySet) {
                stringBuilder.append(lt.c((String) entry.getKey()));
                stringBuilder.append(RequestParameters.EQUAL);
                stringBuilder.append(lt.c((String) entry.getValue()));
                stringBuilder.append(RequestParameters.AMPERSAND);
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        km kmVar = (km) obj;
        if (this.a != null) {
            if (this.a.equals(kmVar.a)) {
                return true;
            }
        } else if (kmVar.a == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.a != null ? this.a.hashCode() : 0;
    }
}
