package com.forwardwin.base.widgets;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ExpandableListView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Tools {
    public static final String TAG = Tools.class.getName();

    public static int getAppInfo(Context context, String data) {
        int result = 0;
        try {
            String[] t1 = data.split("\\|");
            for (int i = 0; i < t1.length; i++) {
                String[] t2 = t1[i].split("\\,");
                boolean isContain = false;
                int j = 0;
                while (j < t2.length) {
                    try {
                        context.getPackageManager().getPackageInfo(t2[j], 0);
                        isContain = true;
                        break;
                    } catch (NameNotFoundException e) {
                        j++;
                    }
                }
                if (isContain) {
                    result |= 1 << i;
                }
            }
        } catch (Exception e2) {
        }
        return result;
    }

    public static boolean isInteger(float number) {
        return ((float) Math.round(number)) == number;
    }

    public static void expandAllGroup(ExpandableListView listView) {
        if (listView != null) {
            int n = listView.getExpandableListAdapter().getGroupCount();
            for (int i = 0; i < n; i++) {
                listView.expandGroup(i);
            }
        }
    }

    public static boolean checkInstalledApp(Context context, String appName) {
        if (TextUtils.isEmpty(appName)) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(appName, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo != null) {
            return true;
        }
        return false;
    }

    public static int parseInt(String src, int defaultValue) {
        if (!TextUtils.isEmpty(src)) {
            int index = src.indexOf(".");
            if (index > 0) {
                src = src.substring(0, index);
            }
            try {
                defaultValue = Integer.parseInt(src);
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public static byte[] inputStreamToBytes(InputStream stream) {
        if (stream == null) {
            return null;
        }
        ByteArrayOutputStream bytesStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        long size = 0;
        while (true) {
            try {
                int count = stream.read(buffer, 0, 512);
                if (count <= 0) {
                    break;
                }
                bytesStream.write(buffer, 0, count);
                size += (long) count;
            } catch (IOException e) {
                Log.d(TAG, e.getMessage(), e);
                if (size == 0) {
                    return null;
                }
            }
        }
        return bytesStream.toByteArray();
    }

    public static boolean isEmpty(CharSequence string) {
        return string == null || string.length() == 0;
    }

    public static long parseLong(String src, long defaultValue) {
        if (!TextUtils.isEmpty(src)) {
            try {
                defaultValue = Long.parseLong(src);
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String str = Integer.toHexString(b & 255);
            while (str.length() < 2) {
                str = "0" + str;
            }
            hexString.append(str);
        }
        return hexString.toString();
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean z = true;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivity == null || connectivity.getActiveNetworkInfo() == null) {
            return false;
        }
        if (connectivity.getNetworkInfo(0).getState() == State.CONNECTED) {
            return true;
        }
        if (connectivity.getNetworkInfo(1).getState() != State.CONNECTED) {
            z = false;
        }
        return z;
    }

    public static int getPixelByDip(Context context, int dip) {
        return (int) ((context.getResources().getDisplayMetrics().density * ((float) dip)) + 0.5f);
    }

    public static int getTextSpByPixel(Context context, int pixel) {
        return (int) (((float) pixel) / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static String timeDiffStringFromTimeInterval(long tInterval) {
        if (tInterval < 0) {
            return "";
        }
        long ti = (System.currentTimeMillis() / 1000) - tInterval;
        if (ti < 0) {
            return "";
        }
        if (ti < 60) {
            return ti + " seconds ago";
        }
        long diff;
        if (ti < 3600) {
            diff = (long) Math.round((float) (ti / 60));
            if (diff <= 1) {
                return diff + " minute ago";
            }
            return diff + " minutes ago";
        } else if (ti < 86400) {
            diff = (long) Math.round((float) ((ti / 60) / 60));
            if (diff <= 1) {
                return diff + " hour ago";
            }
            return diff + " hours ago";
        } else if (ti < 2629743) {
            diff = (long) Math.round((float) (((ti / 60) / 60) / 24));
            if (diff <= 1) {
                return diff + " day ago";
            }
            return diff + " days ago";
        } else if (ti < 18408201) {
            diff = (long) Math.round((float) ((((ti / 60) / 60) / 24) / 7));
            if (diff <= 1) {
                return diff + " week ago";
            }
            return diff + " weeks ago";
        } else if (ti >= 78892290) {
            return "";
        } else {
            diff = (long) Math.round((float) ((((ti / 60) / 60) / 24) / 30));
            if (diff <= 1) {
                return diff + " month ago";
            }
            return diff + " months ago";
        }
    }
}
