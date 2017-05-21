package com.flurry.sdk;

import bolts.MeasurementEvent;
import java.util.Map;

public class if extends kl {
    private static final String a = if.class.getSimpleName();

    public String a(String str, Map<String, String> map) {
        String a = a(str);
        while (a != null) {
            str = a(str, a, map);
            a = a(str);
        }
        return str;
    }

    private String a(String str, String str2, Map<String, String> map) {
        String valueOf;
        if (a("timestamp_epoch_millis", str2)) {
            valueOf = String.valueOf(System.currentTimeMillis());
            kg.a(3, a, "Replacing param timestamp_epoch_millis with: " + valueOf);
            return str.replace(str2, lt.c(valueOf));
        } else if (a("session_duration_millis", str2)) {
            valueOf = Long.toString(je.a().f());
            kg.a(3, a, "Replacing param session_duration_millis with: " + valueOf);
            return str.replace(str2, lt.c(valueOf));
        } else if (a("fg_timespent_millis", str2)) {
            valueOf = Long.toString(je.a().f());
            kg.a(3, a, "Replacing param fg_timespent_millis with: " + valueOf);
            return str.replace(str2, lt.c(valueOf));
        } else if (a("install_referrer", str2)) {
            valueOf = new hm().b();
            if (valueOf == null) {
                valueOf = "";
            }
            kg.a(3, a, "Replacing param install_referrer with: " + valueOf);
            return str.replace(str2, lt.c(valueOf));
        } else if (a("geo_latitude", str2)) {
            r1 = jj.a().e();
            valueOf = "";
            if (r1 != null) {
                valueOf = valueOf + lt.a(r1.getLatitude(), 3);
            }
            kg.a(3, a, "Replacing param geo_latitude with: " + valueOf);
            return str.replace(str2, lt.c(valueOf));
        } else if (a("geo_longitude", str2)) {
            r1 = jj.a().e();
            valueOf = "";
            if (r1 != null) {
                valueOf = valueOf + lt.a(r1.getLongitude(), 3);
            }
            kg.a(3, a, "Replacing param geo_longitude with: " + valueOf);
            return str.replace(str2, lt.c(valueOf));
        } else if (a("publisher_user_id", str2)) {
            valueOf = (String) lk.a().a("UserId");
            kg.a(3, a, "Replacing param publisher_user_id with: " + valueOf);
            return str.replace(str2, lt.c(valueOf));
        } else if (a(MeasurementEvent.MEASUREMENT_EVENT_NAME_KEY, str2)) {
            if (map == null || !map.containsKey(MeasurementEvent.MEASUREMENT_EVENT_NAME_KEY)) {
                kg.a(3, a, "Replacing param event_name with empty string");
                return str.replace(str2, "");
            }
            kg.a(3, a, "Replacing param event_name with: " + ((String) map.get(MeasurementEvent.MEASUREMENT_EVENT_NAME_KEY)));
            return str.replace(str2, lt.c((String) map.get(MeasurementEvent.MEASUREMENT_EVENT_NAME_KEY)));
        } else if (!a("event_time_millis", str2)) {
            kg.a(3, a, "Unknown param: " + str2);
            return str.replace(str2, "");
        } else if (map == null || !map.containsKey("event_time_millis")) {
            kg.a(3, a, "Replacing param event_time_millis with empty string");
            return str.replace(str2, "");
        } else {
            kg.a(3, a, "Replacing param event_time_millis with: " + ((String) map.get("event_time_millis")));
            return str.replace(str2, lt.c((String) map.get("event_time_millis")));
        }
    }
}
