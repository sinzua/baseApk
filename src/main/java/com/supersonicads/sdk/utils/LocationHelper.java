package com.supersonicads.sdk.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import com.nativex.monetization.mraid.objects.ObjectNames.CalendarEntryData;

public class LocationHelper {
    public static Location getLastLocation(Context context) {
        Location bestLocation = null;
        if (!locationPermissionGranted(context)) {
            return null;
        }
        LocationManager locationManager = (LocationManager) context.getApplicationContext().getSystemService(CalendarEntryData.LOCATION);
        for (String provider : locationManager.getAllProviders()) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null && location.getTime() > Long.MIN_VALUE) {
                bestLocation = location;
            }
        }
        return bestLocation;
    }

    private static boolean locationPermissionGranted(Context context) {
        return context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0;
    }
}
