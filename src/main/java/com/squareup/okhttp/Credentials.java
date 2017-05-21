package com.squareup.okhttp;

import com.lidroid.xutils.util.CharsetUtils;
import java.io.UnsupportedEncodingException;
import okio.ByteString;

public final class Credentials {
    private Credentials() {
    }

    public static String basic(String userName, String password) {
        try {
            return "Basic " + ByteString.of((userName + ":" + password).getBytes(CharsetUtils.DEFAULT_ENCODING_CHARSET)).base64();
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError();
        }
    }
}
