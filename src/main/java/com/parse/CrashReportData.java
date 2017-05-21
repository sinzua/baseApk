package com.parse;

import com.supersonicads.sdk.precache.DownloadManager;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.Map.Entry;

class CrashReportData extends EnumMap<ReportField, String> {
    private static final int CONTINUE = 3;
    private static final int IGNORE = 5;
    private static final int KEY_DONE = 4;
    private static final int NONE = 0;
    private static final String PROP_DTD_NAME = "http://java.sun.com/dtd/properties.dtd";
    private static final int SLASH = 1;
    private static final int UNICODE = 2;
    private static String lineSeparator = "\n";
    private static final long serialVersionUID = 4112578634029874840L;
    protected CrashReportData defaults;

    public CrashReportData() {
        super(ReportField.class);
    }

    public CrashReportData(CrashReportData properties) {
        super(ReportField.class);
        this.defaults = properties;
    }

    private void dumpString(Appendable sb, String string, boolean key) throws IOException {
        int i = 0;
        int length = string.length();
        if (!key && 0 < length && string.charAt(0) == ' ') {
            sb.append("\\ ");
            i = 0 + 1;
        }
        while (i < length) {
            char ch = string.charAt(i);
            switch (ch) {
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if ((key && ch == ' ') || ch == '\\' || ch == '#' || ch == '!' || ch == ':') {
                        sb.append('\\');
                    }
                    if (ch >= ' ' && ch <= '~') {
                        sb.append(ch);
                        break;
                    }
                    String hex = Integer.toHexString(ch);
                    sb.append("\\u");
                    for (int j = 0; j < 4 - hex.length(); j++) {
                        sb.append('0');
                    }
                    sb.append(hex);
                    break;
            }
            i++;
        }
    }

    public String getProperty(ReportField key) {
        String result = (String) super.get(key);
        if (result != null || this.defaults == null) {
            return result;
        }
        return this.defaults.getProperty(key);
    }

    public String getProperty(ReportField key, String defaultValue) {
        String property = (String) super.get(key);
        if (property == null && this.defaults != null) {
            property = this.defaults.getProperty(key);
        }
        return property == null ? defaultValue : property;
    }

    public void list(PrintStream out) {
        if (out == null) {
            throw new NullPointerException();
        }
        StringBuilder buffer = new StringBuilder(80);
        Enumeration<ReportField> keys = keys();
        while (keys.hasMoreElements()) {
            ReportField key = (ReportField) keys.nextElement();
            buffer.append(key);
            buffer.append('=');
            String property = (String) super.get(key);
            CrashReportData def = this.defaults;
            while (property == null) {
                property = (String) def.get(key);
                def = def.defaults;
            }
            if (property.length() > 40) {
                buffer.append(property.substring(0, 37));
                buffer.append("...");
            } else {
                buffer.append(property);
            }
            out.println(buffer.toString());
            buffer.setLength(0);
        }
    }

    public void list(PrintWriter writer) {
        if (writer == null) {
            throw new NullPointerException();
        }
        StringBuilder buffer = new StringBuilder(80);
        Enumeration<ReportField> keys = keys();
        while (keys.hasMoreElements()) {
            ReportField key = (ReportField) keys.nextElement();
            buffer.append(key);
            buffer.append('=');
            String property = (String) super.get(key);
            CrashReportData def = this.defaults;
            while (property == null) {
                property = (String) def.get(key);
                def = def.defaults;
            }
            if (property.length() > 40) {
                buffer.append(property.substring(0, 37));
                buffer.append("...");
            } else {
                buffer.append(property);
            }
            writer.println(buffer.toString());
            buffer.setLength(0);
        }
    }

    public synchronized void load(InputStream in) throws IOException {
        if (in == null) {
            throw new NullPointerException();
        }
        BufferedInputStream bis = new BufferedInputStream(in);
        bis.mark(Integer.MAX_VALUE);
        boolean isEbcdic = isEbcdic(bis);
        bis.reset();
        if (isEbcdic) {
            load(new InputStreamReader(bis));
        } else {
            load(new InputStreamReader(bis, "ISO8859-1"));
        }
    }

    private boolean isEbcdic(BufferedInputStream in) throws IOException {
        byte b;
        do {
            b = (byte) in.read();
            if (b == (byte) -1 || b == (byte) 35 || b == (byte) 10 || b == (byte) 61) {
                return false;
            }
        } while (b != (byte) 21);
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void load(java.io.Reader r22) throws java.io.IOException {
        /*
        r21 = this;
        monitor-enter(r21);
        r11 = 0;
        r17 = 0;
        r5 = 0;
        r19 = 40;
        r0 = r19;
        r4 = new char[r0];	 Catch:{ all -> 0x0036 }
        r14 = 0;
        r10 = -1;
        r7 = 1;
        r3 = new java.io.BufferedReader;	 Catch:{ all -> 0x0036 }
        r0 = r22;
        r3.<init>(r0);	 Catch:{ all -> 0x0036 }
        r15 = r14;
    L_0x0016:
        r8 = r3.read();	 Catch:{ all -> 0x0036 }
        r19 = -1;
        r0 = r19;
        if (r8 == r0) goto L_0x0022;
    L_0x0020:
        if (r8 != 0) goto L_0x0039;
    L_0x0022:
        r19 = 2;
        r0 = r19;
        if (r11 != r0) goto L_0x017c;
    L_0x0028:
        r19 = 4;
        r0 = r19;
        if (r5 > r0) goto L_0x017c;
    L_0x002e:
        r19 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x0036 }
        r20 = "luni.08";
        r19.<init>(r20);	 Catch:{ all -> 0x0036 }
        throw r19;	 Catch:{ all -> 0x0036 }
    L_0x0036:
        r19 = move-exception;
        monitor-exit(r21);
        throw r19;
    L_0x0039:
        r13 = (char) r8;
        r0 = r4.length;	 Catch:{ all -> 0x0036 }
        r19 = r0;
        r0 = r19;
        if (r15 != r0) goto L_0x0056;
    L_0x0041:
        r0 = r4.length;	 Catch:{ all -> 0x0036 }
        r19 = r0;
        r19 = r19 * 2;
        r0 = r19;
        r12 = new char[r0];	 Catch:{ all -> 0x0036 }
        r19 = 0;
        r20 = 0;
        r0 = r19;
        r1 = r20;
        java.lang.System.arraycopy(r4, r0, r12, r1, r15);	 Catch:{ all -> 0x0036 }
        r4 = r12;
    L_0x0056:
        r19 = 2;
        r0 = r19;
        if (r11 != r0) goto L_0x0099;
    L_0x005c:
        r19 = 16;
        r0 = r19;
        r6 = java.lang.Character.digit(r13, r0);	 Catch:{ all -> 0x0036 }
        if (r6 < 0) goto L_0x008a;
    L_0x0066:
        r19 = r17 << 4;
        r17 = r19 + r6;
        r5 = r5 + 1;
        r19 = 4;
        r0 = r19;
        if (r5 < r0) goto L_0x0016;
    L_0x0072:
        r11 = 0;
        r14 = r15 + 1;
        r0 = r17;
        r0 = (char) r0;	 Catch:{ all -> 0x0036 }
        r19 = r0;
        r4[r15] = r19;	 Catch:{ all -> 0x0036 }
        r19 = 10;
        r0 = r19;
        if (r13 == r0) goto L_0x0098;
    L_0x0082:
        r19 = 133; // 0x85 float:1.86E-43 double:6.57E-322;
        r0 = r19;
        if (r13 == r0) goto L_0x0098;
    L_0x0088:
        r15 = r14;
        goto L_0x0016;
    L_0x008a:
        r19 = 4;
        r0 = r19;
        if (r5 > r0) goto L_0x0072;
    L_0x0090:
        r19 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x0036 }
        r20 = "luni.09";
        r19.<init>(r20);	 Catch:{ all -> 0x0036 }
        throw r19;	 Catch:{ all -> 0x0036 }
    L_0x0098:
        r15 = r14;
    L_0x0099:
        r19 = 1;
        r0 = r19;
        if (r11 != r0) goto L_0x00ce;
    L_0x009f:
        r11 = 0;
        switch(r13) {
            case 10: goto L_0x00b6;
            case 13: goto L_0x00b3;
            case 98: goto L_0x00b9;
            case 102: goto L_0x00bc;
            case 110: goto L_0x00bf;
            case 114: goto L_0x00c2;
            case 116: goto L_0x00c5;
            case 117: goto L_0x00c8;
            case 133: goto L_0x00b6;
            default: goto L_0x00a3;
        };	 Catch:{ all -> 0x0036 }
    L_0x00a3:
        r7 = 0;
        r19 = 4;
        r0 = r19;
        if (r11 != r0) goto L_0x00ac;
    L_0x00aa:
        r10 = r15;
        r11 = 0;
    L_0x00ac:
        r14 = r15 + 1;
        r4[r15] = r13;	 Catch:{ all -> 0x0036 }
        r15 = r14;
        goto L_0x0016;
    L_0x00b3:
        r11 = 3;
        goto L_0x0016;
    L_0x00b6:
        r11 = 5;
        goto L_0x0016;
    L_0x00b9:
        r13 = 8;
        goto L_0x00a3;
    L_0x00bc:
        r13 = 12;
        goto L_0x00a3;
    L_0x00bf:
        r13 = 10;
        goto L_0x00a3;
    L_0x00c2:
        r13 = 13;
        goto L_0x00a3;
    L_0x00c5:
        r13 = 9;
        goto L_0x00a3;
    L_0x00c8:
        r11 = 2;
        r5 = 0;
        r17 = r5;
        goto L_0x0016;
    L_0x00ce:
        switch(r13) {
            case 10: goto L_0x0112;
            case 13: goto L_0x011b;
            case 33: goto L_0x00f1;
            case 35: goto L_0x00f1;
            case 58: goto L_0x0163;
            case 61: goto L_0x0163;
            case 92: goto L_0x0159;
            case 133: goto L_0x011b;
            default: goto L_0x00d1;
        };	 Catch:{ all -> 0x0036 }
    L_0x00d1:
        r19 = java.lang.Character.isWhitespace(r13);	 Catch:{ all -> 0x0036 }
        if (r19 == 0) goto L_0x016d;
    L_0x00d7:
        r19 = 3;
        r0 = r19;
        if (r11 != r0) goto L_0x00de;
    L_0x00dd:
        r11 = 5;
    L_0x00de:
        if (r15 == 0) goto L_0x0016;
    L_0x00e0:
        if (r15 == r10) goto L_0x0016;
    L_0x00e2:
        r19 = 5;
        r0 = r19;
        if (r11 == r0) goto L_0x0016;
    L_0x00e8:
        r19 = -1;
        r0 = r19;
        if (r10 != r0) goto L_0x016d;
    L_0x00ee:
        r11 = 4;
        goto L_0x0016;
    L_0x00f1:
        if (r7 == 0) goto L_0x00d1;
    L_0x00f3:
        r8 = r3.read();	 Catch:{ all -> 0x0036 }
        r19 = -1;
        r0 = r19;
        if (r8 == r0) goto L_0x0016;
    L_0x00fd:
        r13 = (char) r8;	 Catch:{ all -> 0x0036 }
        r19 = 13;
        r0 = r19;
        if (r13 == r0) goto L_0x0016;
    L_0x0104:
        r19 = 10;
        r0 = r19;
        if (r13 == r0) goto L_0x0016;
    L_0x010a:
        r19 = 133; // 0x85 float:1.86E-43 double:6.57E-322;
        r0 = r19;
        if (r13 != r0) goto L_0x00f3;
    L_0x0110:
        goto L_0x0016;
    L_0x0112:
        r19 = 3;
        r0 = r19;
        if (r11 != r0) goto L_0x011b;
    L_0x0118:
        r11 = 5;
        goto L_0x0016;
    L_0x011b:
        r11 = 0;
        r7 = 1;
        if (r15 > 0) goto L_0x0123;
    L_0x011f:
        if (r15 != 0) goto L_0x0154;
    L_0x0121:
        if (r10 != 0) goto L_0x0154;
    L_0x0123:
        r19 = -1;
        r0 = r19;
        if (r10 != r0) goto L_0x012a;
    L_0x0129:
        r10 = r15;
    L_0x012a:
        r16 = new java.lang.String;	 Catch:{ all -> 0x0036 }
        r19 = 0;
        r0 = r16;
        r1 = r19;
        r0.<init>(r4, r1, r15);	 Catch:{ all -> 0x0036 }
        r19 = com.parse.ReportField.class;
        r20 = 0;
        r0 = r16;
        r1 = r20;
        r20 = r0.substring(r1, r10);	 Catch:{ all -> 0x0036 }
        r19 = java.lang.Enum.valueOf(r19, r20);	 Catch:{ all -> 0x0036 }
        r0 = r16;
        r20 = r0.substring(r10);	 Catch:{ all -> 0x0036 }
        r0 = r21;
        r1 = r19;
        r2 = r20;
        r0.put(r1, r2);	 Catch:{ all -> 0x0036 }
    L_0x0154:
        r10 = -1;
        r14 = 0;
        r15 = r14;
        goto L_0x0016;
    L_0x0159:
        r19 = 4;
        r0 = r19;
        if (r11 != r0) goto L_0x0160;
    L_0x015f:
        r10 = r15;
    L_0x0160:
        r11 = 1;
        goto L_0x0016;
    L_0x0163:
        r19 = -1;
        r0 = r19;
        if (r10 != r0) goto L_0x00d1;
    L_0x0169:
        r11 = 0;
        r10 = r15;
        goto L_0x0016;
    L_0x016d:
        r19 = 5;
        r0 = r19;
        if (r11 == r0) goto L_0x0179;
    L_0x0173:
        r19 = 3;
        r0 = r19;
        if (r11 != r0) goto L_0x00a3;
    L_0x0179:
        r11 = 0;
        goto L_0x00a3;
    L_0x017c:
        r19 = -1;
        r0 = r19;
        if (r10 != r0) goto L_0x0185;
    L_0x0182:
        if (r15 <= 0) goto L_0x0185;
    L_0x0184:
        r10 = r15;
    L_0x0185:
        if (r10 < 0) goto L_0x01ce;
    L_0x0187:
        r16 = new java.lang.String;	 Catch:{ all -> 0x0036 }
        r19 = 0;
        r0 = r16;
        r1 = r19;
        r0.<init>(r4, r1, r15);	 Catch:{ all -> 0x0036 }
        r19 = com.parse.ReportField.class;
        r20 = 0;
        r0 = r16;
        r1 = r20;
        r20 = r0.substring(r1, r10);	 Catch:{ all -> 0x0036 }
        r9 = java.lang.Enum.valueOf(r19, r20);	 Catch:{ all -> 0x0036 }
        r9 = (com.parse.ReportField) r9;	 Catch:{ all -> 0x0036 }
        r0 = r16;
        r18 = r0.substring(r10);	 Catch:{ all -> 0x0036 }
        r19 = 1;
        r0 = r19;
        if (r11 != r0) goto L_0x01c7;
    L_0x01b0:
        r19 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0036 }
        r19.<init>();	 Catch:{ all -> 0x0036 }
        r0 = r19;
        r1 = r18;
        r19 = r0.append(r1);	 Catch:{ all -> 0x0036 }
        r20 = "\u0000";
        r19 = r19.append(r20);	 Catch:{ all -> 0x0036 }
        r18 = r19.toString();	 Catch:{ all -> 0x0036 }
    L_0x01c7:
        r0 = r21;
        r1 = r18;
        r0.put(r9, r1);	 Catch:{ all -> 0x0036 }
    L_0x01ce:
        monitor-exit(r21);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parse.CrashReportData.load(java.io.Reader):void");
    }

    public String put(ReportField key, String value, Writer writer) throws IOException {
        String result = (String) put(key, value);
        if (writer != null) {
            storeKeyValuePair(writer, key, value);
        }
        return result;
    }

    private Enumeration<ReportField> keys() {
        return Collections.enumeration(keySet());
    }

    @Deprecated
    public void save(OutputStream out, String comment) {
        try {
            store(out, comment);
        } catch (IOException e) {
        }
    }

    public Object setProperty(ReportField key, String value) {
        return put(key, value);
    }

    public synchronized void store(OutputStream out, String comment) throws IOException {
        store(getWriter(out), comment);
    }

    public static Writer getWriter(OutputStream out) {
        try {
            return new OutputStreamWriter(out, "ISO8859_1");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public synchronized void store(Writer writer, String comment) throws IOException {
        if (comment != null) {
            storeComment(writer, comment);
        }
        for (Entry<ReportField, String> entry : entrySet()) {
            storeKeyValuePair(writer, (ReportField) entry.getKey(), (String) entry.getValue());
        }
        writer.flush();
    }

    public synchronized void storeComment(Writer writer, String comment) throws IOException {
        writer.write("#");
        writer.write(comment);
        writer.write(lineSeparator);
    }

    public synchronized void storeKeyValuePair(Writer writer, ReportField key, String value) throws IOException {
        String valueString;
        String keyString = key.toString();
        if (value == null) {
            valueString = "";
        } else {
            valueString = value;
        }
        int totalLength = (keyString.length() + valueString.length()) + 1;
        StringBuilder sb = new StringBuilder((totalLength / 5) + totalLength);
        dumpString(sb, keyString, true);
        sb.append('=');
        dumpString(sb, valueString, false);
        sb.append(lineSeparator);
        writer.write(sb.toString());
        writer.flush();
    }

    public void storeToXML(OutputStream os, String comment) throws IOException {
        storeToXML(os, comment, DownloadManager.UTF8_CHARSET);
    }

    public synchronized void storeToXML(OutputStream os, String comment, String encoding) throws IOException {
        if (os == null || encoding == null) {
            throw new NullPointerException();
        }
        String encodingCanonicalName;
        try {
            encodingCanonicalName = Charset.forName(encoding).name();
        } catch (IllegalCharsetNameException e) {
            System.out.println("Warning: encoding name " + encoding + " is illegal, using UTF-8 as default encoding");
            encodingCanonicalName = DownloadManager.UTF8_CHARSET;
        } catch (UnsupportedCharsetException e2) {
            System.out.println("Warning: encoding " + encoding + " is not supported, using UTF-8 as default encoding");
            encodingCanonicalName = DownloadManager.UTF8_CHARSET;
        }
        PrintStream printStream = new PrintStream(os, false, encodingCanonicalName);
        printStream.print("<?xml version=\"1.0\" encoding=\"");
        printStream.print(encodingCanonicalName);
        printStream.println("\"?>");
        printStream.print("<!DOCTYPE properties SYSTEM \"");
        printStream.print(PROP_DTD_NAME);
        printStream.println("\">");
        printStream.println("<properties>");
        if (comment != null) {
            printStream.print("<comment>");
            printStream.print(substitutePredefinedEntries(comment));
            printStream.println("</comment>");
        }
        for (Entry<ReportField, String> entry : entrySet()) {
            String keyValue = ((ReportField) entry.getKey()).toString();
            String entryValue = (String) entry.getValue();
            printStream.print("<entry key=\"");
            printStream.print(substitutePredefinedEntries(keyValue));
            printStream.print("\">");
            printStream.print(substitutePredefinedEntries(entryValue));
            printStream.println("</entry>");
        }
        printStream.println("</properties>");
        printStream.flush();
    }

    private String substitutePredefinedEntries(String s) {
        return s.replaceAll(RequestParameters.AMPERSAND, "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("'", "&apos;").replaceAll("\"", "&quot;");
    }
}
