package com.parse;

class ParseTextUtils {
    static String join(CharSequence delimiter, Iterable tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object item : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    public static boolean isEmpty(CharSequence text) {
        return text == null || text.length() == 0;
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        return a == b || (a != null && a.equals(b));
    }

    private ParseTextUtils() {
    }
}
