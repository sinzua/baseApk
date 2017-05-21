package org.codehaus.jackson;

import com.supersonicads.sdk.precache.DownloadManager;

public enum JsonEncoding {
    UTF8(DownloadManager.UTF8_CHARSET, false),
    UTF16_BE("UTF-16BE", true),
    UTF16_LE("UTF-16LE", false),
    UTF32_BE("UTF-32BE", true),
    UTF32_LE("UTF-32LE", false);
    
    protected final boolean _bigEndian;
    protected final String _javaName;

    private JsonEncoding(String javaName, boolean bigEndian) {
        this._javaName = javaName;
        this._bigEndian = bigEndian;
    }

    public String getJavaName() {
        return this._javaName;
    }

    public boolean isBigEndian() {
        return this._bigEndian;
    }
}
