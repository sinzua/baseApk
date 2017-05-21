package com.parse;

import android.os.Process;
import android.util.Log;
import com.supersonicads.sdk.precache.DownloadManager;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

class ProcFileReader {
    public static final int CANNOT_DETERMINE_OPEN_FDS = -1;
    public static final int SECURITY_EXCEPTION = -2;
    private static final Class<?> TAG = ProcFileReader.class;

    public static class OpenFDLimits {
        public final String hardLimit;
        public final String softLimit;

        public OpenFDLimits(String softLimit, String hardLimit) {
            this.softLimit = softLimit;
            this.hardLimit = hardLimit;
        }
    }

    ProcFileReader() {
    }

    public static int getOpenFDCount() {
        try {
            FD_DIRS = new String[3];
            FD_DIRS[0] = String.format("/proc/%s/fd", new Object[]{Integer.valueOf(Process.myPid())});
            FD_DIRS[1] = "/proc/self/fd";
            FD_DIRS[2] = String.format("/proc/%s/fd", new Object[]{Integer.valueOf(Process.myTid())});
            for (String file : FD_DIRS) {
                String[] fdFiles = new File(file).list();
                if (fdFiles != null) {
                    return fdFiles.length;
                }
            }
            return -1;
        } catch (SecurityException e) {
            Log.e(TAG.toString(), e.getMessage());
            return -2;
        }
    }

    public static OpenFDLimits getOpenFDLimits() {
        Throwable th;
        Scanner s = null;
        try {
            Scanner s2 = new Scanner(new File("/proc/self/limits"));
            try {
                if (s2.findWithinHorizon("Max open files", DownloadManager.OPERATION_TIMEOUT) != null) {
                    OpenFDLimits openFDLimits = new OpenFDLimits(s2.next(), s2.next());
                    if (s2 != null) {
                        s2.close();
                    }
                    return openFDLimits;
                } else if (s2 == null) {
                    return null;
                } else {
                    s2.close();
                    return null;
                }
            } catch (IOException e) {
                s = s2;
                if (s != null) {
                    return null;
                }
                s.close();
                return null;
            } catch (NoSuchElementException e2) {
                s = s2;
                if (s != null) {
                    return null;
                }
                s.close();
                return null;
            } catch (Throwable th2) {
                th = th2;
                s = s2;
                if (s != null) {
                    s.close();
                }
                throw th;
            }
        } catch (IOException e3) {
            if (s != null) {
                return null;
            }
            s.close();
            return null;
        } catch (NoSuchElementException e4) {
            if (s != null) {
                return null;
            }
            s.close();
            return null;
        } catch (Throwable th3) {
            th = th3;
            if (s != null) {
                s.close();
            }
            throw th;
        }
    }
}
