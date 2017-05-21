package com.parse;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import bolts.Capture;
import bolts.Task;
import bolts.Task.TaskCompletionSource;
import com.nativex.monetization.mraid.objects.ObjectNames.CalendarEntryData;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class LocationNotifier {
    private static Location fakeLocation = null;

    LocationNotifier() {
    }

    static Task<Location> getCurrentLocationAsync(Context context, long timeout, Criteria criteria) {
        final TaskCompletionSource tcs = Task.create();
        final Capture<ScheduledFuture<?>> timeoutFuture = new Capture();
        final LocationManager manager = (LocationManager) context.getSystemService(CalendarEntryData.LOCATION);
        final LocationListener listener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (location != null) {
                    ((ScheduledFuture) timeoutFuture.get()).cancel(true);
                    tcs.trySetResult(location);
                    manager.removeUpdates(this);
                }
            }

            public void onProviderDisabled(String provider) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };
        timeoutFuture.set(ParseExecutors.scheduled().schedule(new Runnable() {
            public void run() {
                tcs.trySetError(new ParseException(ParseException.TIMEOUT, "Location fetch timed out."));
                manager.removeUpdates(listener);
            }
        }, timeout, TimeUnit.MILLISECONDS));
        String provider = manager.getBestProvider(criteria, true);
        if (provider != null) {
            manager.requestLocationUpdates(provider, 0, 0.0f, listener);
        }
        if (fakeLocation != null) {
            listener.onLocationChanged(fakeLocation);
        }
        return tcs.getTask();
    }

    static void setFakeLocation(Location location) {
        fakeLocation = location;
    }
}
