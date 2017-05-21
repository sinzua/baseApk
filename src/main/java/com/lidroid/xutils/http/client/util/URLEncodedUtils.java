package com.lidroid.xutils.http.client.util;

import android.support.v4.media.TransportMediator;
import android.text.TextUtils;
import com.parse.ParseException;
import com.supersonicads.sdk.precache.DownloadManager;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;

public class URLEncodedUtils {
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final char[] DELIM = new char[]{'&'};
    private static final BitSet FRAGMENT = new BitSet(256);
    private static final String NAME_VALUE_SEPARATOR = "=";
    private static final String PARAMETER_SEPARATOR = "&";
    private static final BitSet PATHSAFE = new BitSet(256);
    private static final BitSet PUNCT = new BitSet(256);
    private static final int RADIX = 16;
    private static final BitSet RESERVED = new BitSet(256);
    private static final BitSet UNRESERVED = new BitSet(256);
    private static final BitSet URLENCODER = new BitSet(256);
    private static final BitSet USERINFO = new BitSet(256);

    public static boolean isEncoded(HttpEntity entity) {
        Header h = entity.getContentType();
        if (h == null) {
            return false;
        }
        HeaderElement[] elems = h.getElements();
        if (elems.length > 0) {
            return elems[0].getName().equalsIgnoreCase("application/x-www-form-urlencoded");
        }
        return false;
    }

    public static List<NameValuePair> parse(URI uri) {
        String query = uri.getRawQuery();
        if (TextUtils.isEmpty(query)) {
            return Collections.emptyList();
        }
        List<NameValuePair> result = new ArrayList();
        parse(result, new Scanner(query));
        return result;
    }

    public static void parse(List<NameValuePair> parameters, Scanner scanner) {
        scanner.useDelimiter("&");
        while (scanner.hasNext()) {
            String name;
            String value = null;
            String token = scanner.next();
            int i = token.indexOf("=");
            if (i != -1) {
                name = token.substring(0, i).trim();
                value = token.substring(i + 1).trim();
            } else {
                name = token.trim();
            }
            parameters.add(new BasicNameValuePair(name, value));
        }
    }

    static {
        int i;
        for (i = 97; i <= ParseException.INVALID_FILE_NAME; i++) {
            UNRESERVED.set(i);
        }
        for (i = 65; i <= 90; i++) {
            UNRESERVED.set(i);
        }
        for (i = 48; i <= 57; i++) {
            UNRESERVED.set(i);
        }
        UNRESERVED.set(95);
        UNRESERVED.set(45);
        UNRESERVED.set(46);
        UNRESERVED.set(42);
        URLENCODER.or(UNRESERVED);
        UNRESERVED.set(33);
        UNRESERVED.set(TransportMediator.KEYCODE_MEDIA_PLAY);
        UNRESERVED.set(39);
        UNRESERVED.set(40);
        UNRESERVED.set(41);
        PUNCT.set(44);
        PUNCT.set(59);
        PUNCT.set(58);
        PUNCT.set(36);
        PUNCT.set(38);
        PUNCT.set(43);
        PUNCT.set(61);
        USERINFO.or(UNRESERVED);
        USERINFO.or(PUNCT);
        PATHSAFE.or(UNRESERVED);
        PATHSAFE.set(47);
        PATHSAFE.set(59);
        PATHSAFE.set(58);
        PATHSAFE.set(64);
        PATHSAFE.set(38);
        PATHSAFE.set(61);
        PATHSAFE.set(43);
        PATHSAFE.set(36);
        PATHSAFE.set(44);
        RESERVED.set(59);
        RESERVED.set(47);
        RESERVED.set(63);
        RESERVED.set(58);
        RESERVED.set(64);
        RESERVED.set(38);
        RESERVED.set(61);
        RESERVED.set(43);
        RESERVED.set(36);
        RESERVED.set(44);
        RESERVED.set(91);
        RESERVED.set(93);
        FRAGMENT.or(RESERVED);
        FRAGMENT.or(UNRESERVED);
    }

    public static List<NameValuePair> parse(String s) {
        if (s == null) {
            return Collections.emptyList();
        }
        BasicHeaderValueParser parser = BasicHeaderValueParser.DEFAULT;
        CharArrayBuffer buffer = new CharArrayBuffer(s.length());
        buffer.append(s);
        ParserCursor cursor = new ParserCursor(0, buffer.length());
        List<NameValuePair> list = new ArrayList();
        while (!cursor.atEnd()) {
            NameValuePair nvp = parser.parseNameValuePair(buffer, cursor, DELIM);
            if (nvp.getName().length() > 0) {
                list.add(new BasicNameValuePair(nvp.getName(), nvp.getValue()));
            }
        }
        return list;
    }

    public static String format(List<? extends NameValuePair> parameters, String charset) {
        StringBuilder result = new StringBuilder();
        for (NameValuePair parameter : parameters) {
            String encodedName = encodeFormFields(parameter.getName(), charset);
            String encodedValue = encodeFormFields(parameter.getValue(), charset);
            if (result.length() > 0) {
                result.append("&");
            }
            result.append(encodedName);
            if (encodedValue != null) {
                result.append("=");
                result.append(encodedValue);
            }
        }
        return result.toString();
    }

    public static String format(Iterable<? extends NameValuePair> parameters, Charset charset) {
        StringBuilder result = new StringBuilder();
        for (NameValuePair parameter : parameters) {
            String encodedName = encodeFormFields(parameter.getName(), charset);
            String encodedValue = encodeFormFields(parameter.getValue(), charset);
            if (result.length() > 0) {
                result.append("&");
            }
            result.append(encodedName);
            if (encodedValue != null) {
                result.append("=");
                result.append(encodedValue);
            }
        }
        return result.toString();
    }

    private static String urlencode(String content, Charset charset, BitSet safechars, boolean blankAsPlus) {
        if (content == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        ByteBuffer bb = charset.encode(content);
        while (bb.hasRemaining()) {
            int b = bb.get() & 255;
            if (safechars.get(b)) {
                buf.append((char) b);
            } else if (blankAsPlus && b == 32) {
                buf.append('+');
            } else {
                buf.append("%");
                char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 15, 16));
                char hex2 = Character.toUpperCase(Character.forDigit(b & 15, 16));
                buf.append(hex1);
                buf.append(hex2);
            }
        }
        return buf.toString();
    }

    private static String urldecode(String content, Charset charset, boolean plusAsBlank) {
        if (content == null) {
            return null;
        }
        ByteBuffer bb = ByteBuffer.allocate(content.length());
        CharBuffer cb = CharBuffer.wrap(content);
        while (cb.hasRemaining()) {
            char c = cb.get();
            if (c == '%' && cb.remaining() >= 2) {
                char uc = cb.get();
                char lc = cb.get();
                int u = Character.digit(uc, 16);
                int l = Character.digit(lc, 16);
                if (u == -1 || l == -1) {
                    bb.put((byte) 37);
                    bb.put((byte) uc);
                    bb.put((byte) lc);
                } else {
                    bb.put((byte) ((u << 4) + l));
                }
            } else if (plusAsBlank && c == '+') {
                bb.put((byte) 32);
            } else {
                bb.put((byte) c);
            }
        }
        bb.flip();
        return charset.decode(bb).toString();
    }

    private static String decodeFormFields(String content, String charset) {
        if (content == null) {
            return null;
        }
        return urldecode(content, charset != null ? Charset.forName(charset) : Charset.forName(DownloadManager.UTF8_CHARSET), true);
    }

    private static String decodeFormFields(String content, Charset charset) {
        if (content == null) {
            return null;
        }
        if (charset == null) {
            charset = Charset.forName(DownloadManager.UTF8_CHARSET);
        }
        return urldecode(content, charset, true);
    }

    private static String encodeFormFields(String content, String charset) {
        if (content == null) {
            return null;
        }
        Charset forName;
        if (charset != null) {
            forName = Charset.forName(charset);
        } else {
            forName = Charset.forName(DownloadManager.UTF8_CHARSET);
        }
        return urlencode(content, forName, URLENCODER, true);
    }

    private static String encodeFormFields(String content, Charset charset) {
        if (content == null) {
            return null;
        }
        if (charset == null) {
            charset = Charset.forName(DownloadManager.UTF8_CHARSET);
        }
        return urlencode(content, charset, URLENCODER, true);
    }

    static String encUserInfo(String content, Charset charset) {
        return urlencode(content, charset, USERINFO, false);
    }

    static String encFragment(String content, Charset charset) {
        return urlencode(content, charset, FRAGMENT, false);
    }

    static String encPath(String content, Charset charset) {
        return urlencode(content, charset, PATHSAFE, false);
    }
}
