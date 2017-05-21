package com.lidroid.xutils.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

public class PreferencesCookieStore implements CookieStore {
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    private static final String COOKIE_NAME_STORE = "names";
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private final SharedPreferences cookiePrefs;
    private final ConcurrentHashMap<String, Cookie> cookies = new ConcurrentHashMap();

    public class SerializableCookie implements Serializable {
        private static final long serialVersionUID = 6374381828722046732L;
        private transient BasicClientCookie clientCookie;
        private final transient Cookie cookie;

        public SerializableCookie(Cookie cookie) {
            this.cookie = cookie;
        }

        public Cookie getCookie() {
            Cookie bestCookie = this.cookie;
            if (this.clientCookie != null) {
                return this.clientCookie;
            }
            return bestCookie;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(this.cookie.getName());
            out.writeObject(this.cookie.getValue());
            out.writeObject(this.cookie.getComment());
            out.writeObject(this.cookie.getDomain());
            out.writeObject(this.cookie.getExpiryDate());
            out.writeObject(this.cookie.getPath());
            out.writeInt(this.cookie.getVersion());
            out.writeBoolean(this.cookie.isSecure());
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            this.clientCookie = new BasicClientCookie((String) in.readObject(), (String) in.readObject());
            this.clientCookie.setComment((String) in.readObject());
            this.clientCookie.setDomain((String) in.readObject());
            this.clientCookie.setExpiryDate((Date) in.readObject());
            this.clientCookie.setPath((String) in.readObject());
            this.clientCookie.setVersion(in.readInt());
            this.clientCookie.setSecure(in.readBoolean());
        }
    }

    public PreferencesCookieStore(Context context) {
        int i = 0;
        this.cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        String storedCookieNames = this.cookiePrefs.getString(COOKIE_NAME_STORE, null);
        if (storedCookieNames != null) {
            String[] cookieNames = TextUtils.split(storedCookieNames, ",");
            int length = cookieNames.length;
            while (i < length) {
                String name = cookieNames[i];
                String encodedCookie = this.cookiePrefs.getString(new StringBuilder(COOKIE_NAME_PREFIX).append(name).toString(), null);
                if (encodedCookie != null) {
                    Cookie decodedCookie = decodeCookie(encodedCookie);
                    if (decodedCookie != null) {
                        this.cookies.put(name, decodedCookie);
                    }
                }
                i++;
            }
            clearExpired(new Date());
        }
    }

    public void addCookie(Cookie cookie) {
        String name = cookie.getName();
        if (cookie.isExpired(new Date())) {
            this.cookies.remove(name);
        } else {
            this.cookies.put(name, cookie);
        }
        Editor editor = this.cookiePrefs.edit();
        editor.putString(COOKIE_NAME_STORE, TextUtils.join(",", this.cookies.keySet()));
        editor.putString(new StringBuilder(COOKIE_NAME_PREFIX).append(name).toString(), encodeCookie(new SerializableCookie(cookie)));
        editor.commit();
    }

    public void clear() {
        Editor editor = this.cookiePrefs.edit();
        for (String name : this.cookies.keySet()) {
            editor.remove(new StringBuilder(COOKIE_NAME_PREFIX).append(name).toString());
        }
        editor.remove(COOKIE_NAME_STORE);
        editor.commit();
        this.cookies.clear();
    }

    public boolean clearExpired(Date date) {
        boolean clearedAny = false;
        Editor editor = this.cookiePrefs.edit();
        for (Entry<String, Cookie> entry : this.cookies.entrySet()) {
            String name = (String) entry.getKey();
            Cookie cookie = (Cookie) entry.getValue();
            if (cookie.getExpiryDate() == null || cookie.isExpired(date)) {
                this.cookies.remove(name);
                editor.remove(new StringBuilder(COOKIE_NAME_PREFIX).append(name).toString());
                clearedAny = true;
            }
        }
        if (clearedAny) {
            editor.putString(COOKIE_NAME_STORE, TextUtils.join(",", this.cookies.keySet()));
        }
        editor.commit();
        return clearedAny;
    }

    public List<Cookie> getCookies() {
        return new ArrayList(this.cookies.values());
    }

    public Cookie getCookie(String name) {
        return (Cookie) this.cookies.get(name);
    }

    protected String encodeCookie(SerializableCookie cookie) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(os).writeObject(cookie);
            return byteArrayToHexString(os.toByteArray());
        } catch (Throwable th) {
            return null;
        }
    }

    protected Cookie decodeCookie(String cookieStr) {
        Cookie cookie = null;
        try {
            cookie = ((SerializableCookie) new ObjectInputStream(new ByteArrayInputStream(hexStringToByteArray(cookieStr))).readObject()).getCookie();
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
        return cookie;
    }

    protected String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (byte element : b) {
            int v = element & 255;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    protected byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[(len / 2)];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
