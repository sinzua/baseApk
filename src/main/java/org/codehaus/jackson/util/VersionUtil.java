package org.codehaus.jackson.util;

import com.supersonicads.sdk.precache.DownloadManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import org.codehaus.jackson.Version;

public class VersionUtil {
    public static final String VERSION_FILE = "VERSION.txt";
    private static final Pattern VERSION_SEPARATOR = Pattern.compile("[-_./;:]");

    public static Version versionFor(Class<?> cls) {
        Version version = null;
        InputStream in;
        try {
            in = cls.getResourceAsStream(VERSION_FILE);
            if (in != null) {
                version = parseVersion(new BufferedReader(new InputStreamReader(in, DownloadManager.UTF8_CHARSET)).readLine());
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        } catch (IOException e3) {
        } catch (Throwable th) {
            in.close();
        }
        return version == null ? Version.unknownVersion() : version;
    }

    public static Version parseVersion(String versionStr) {
        int patch = 0;
        String snapshot = null;
        if (versionStr == null) {
            return null;
        }
        versionStr = versionStr.trim();
        if (versionStr.length() == 0) {
            return null;
        }
        String[] parts = VERSION_SEPARATOR.split(versionStr);
        if (parts.length < 2) {
            return null;
        }
        int major = parseVersionPart(parts[0]);
        int minor = parseVersionPart(parts[1]);
        if (parts.length > 2) {
            patch = parseVersionPart(parts[2]);
        }
        if (parts.length > 3) {
            snapshot = parts[3];
        }
        return new Version(major, minor, patch, snapshot);
    }

    protected static int parseVersionPart(String partStr) {
        partStr = partStr.toString();
        int len = partStr.length();
        int number = 0;
        for (int i = 0; i < len; i++) {
            char c = partStr.charAt(i);
            if (c > '9' || c < '0') {
                break;
            }
            number = (number * 10) + (c - 48);
        }
        return number;
    }
}
