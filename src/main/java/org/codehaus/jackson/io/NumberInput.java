package org.codehaus.jackson.io;

public final class NumberInput {
    static final long L_BILLION = 1000000000;
    static final String MAX_LONG_STR = String.valueOf(Long.MAX_VALUE);
    static final String MIN_LONG_STR_NO_SIGN = String.valueOf(Long.MIN_VALUE).substring(1);
    public static final String NASTY_SMALL_DOUBLE = "2.2250738585072012e-308";

    public static final int parseInt(char[] digitChars, int offset, int len) {
        int num = digitChars[offset] - 48;
        len += offset;
        offset++;
        if (offset >= len) {
            return num;
        }
        num = (num * 10) + (digitChars[offset] - 48);
        offset++;
        if (offset >= len) {
            return num;
        }
        num = (num * 10) + (digitChars[offset] - 48);
        offset++;
        if (offset >= len) {
            return num;
        }
        num = (num * 10) + (digitChars[offset] - 48);
        offset++;
        if (offset >= len) {
            return num;
        }
        num = (num * 10) + (digitChars[offset] - 48);
        offset++;
        if (offset >= len) {
            return num;
        }
        num = (num * 10) + (digitChars[offset] - 48);
        offset++;
        if (offset >= len) {
            return num;
        }
        num = (num * 10) + (digitChars[offset] - 48);
        offset++;
        if (offset >= len) {
            return num;
        }
        num = (num * 10) + (digitChars[offset] - 48);
        offset++;
        if (offset < len) {
            return (num * 10) + (digitChars[offset] - 48);
        }
        return num;
    }

    public static final int parseInt(String str) {
        int offset;
        boolean negative = false;
        char c = str.charAt(0);
        int length = str.length();
        if (c == '-') {
            negative = true;
        }
        if (negative) {
            if (length == 1 || length > 10) {
                return Integer.parseInt(str);
            }
            offset = 1 + 1;
            c = str.charAt(1);
        } else if (length > 9) {
            return Integer.parseInt(str);
        } else {
            offset = 1;
        }
        if (c > '9' || c < '0') {
            int i = offset;
            return Integer.parseInt(str);
        }
        int num = c - 48;
        if (offset < length) {
            i = offset + 1;
            c = str.charAt(offset);
            if (c > '9' || c < '0') {
                return Integer.parseInt(str);
            }
            num = (num * 10) + (c - 48);
            if (i < length) {
                offset = i + 1;
                c = str.charAt(i);
                if (c > '9' || c < '0') {
                    i = offset;
                    return Integer.parseInt(str);
                }
                num = (num * 10) + (c - 48);
                if (offset < length) {
                    do {
                        i = offset;
                        offset = i + 1;
                        c = str.charAt(i);
                        if (c > '9' || c < '0') {
                            i = offset;
                            return Integer.parseInt(str);
                        }
                        num = (num * 10) + (c - 48);
                    } while (offset < length);
                }
            }
            return negative ? -num : num;
        }
        i = offset;
        if (negative) {
        }
    }

    public static final long parseLong(char[] digitChars, int offset, int len) {
        int len1 = len - 9;
        return ((long) parseInt(digitChars, offset + len1, 9)) + (((long) parseInt(digitChars, offset, len1)) * L_BILLION);
    }

    public static final long parseLong(String str) {
        if (str.length() <= 9) {
            return (long) parseInt(str);
        }
        return Long.parseLong(str);
    }

    public static final boolean inLongRange(char[] digitChars, int offset, int len, boolean negative) {
        String cmpStr = negative ? MIN_LONG_STR_NO_SIGN : MAX_LONG_STR;
        int cmpLen = cmpStr.length();
        if (len < cmpLen) {
            return true;
        }
        if (len > cmpLen) {
            return false;
        }
        int i = 0;
        while (i < cmpLen) {
            int diff = digitChars[offset + i] - cmpStr.charAt(i);
            if (diff == 0) {
                i++;
            } else if (diff >= 0) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public static final boolean inLongRange(String numberStr, boolean negative) {
        String cmpStr = negative ? MIN_LONG_STR_NO_SIGN : MAX_LONG_STR;
        int cmpLen = cmpStr.length();
        int actualLen = numberStr.length();
        if (actualLen < cmpLen) {
            return true;
        }
        if (actualLen > cmpLen) {
            return false;
        }
        int i = 0;
        while (i < cmpLen) {
            int diff = numberStr.charAt(i) - cmpStr.charAt(i);
            if (diff == 0) {
                i++;
            } else if (diff >= 0) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public static int parseAsInt(String input, int defaultValue) {
        if (input == null) {
            return defaultValue;
        }
        input = input.trim();
        int len = input.length();
        if (len == 0) {
            return defaultValue;
        }
        char c;
        int i = 0;
        if (0 < len) {
            c = input.charAt(0);
            if (c == '+') {
                input = input.substring(1);
                len = input.length();
            } else if (c == '-') {
                i = 0 + 1;
            }
        }
        while (i < len) {
            c = input.charAt(i);
            if (c > '9' || c < '0') {
                try {
                    return (int) parseDouble(input);
                } catch (NumberFormatException e) {
                    return defaultValue;
                }
            }
            i++;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e2) {
            return defaultValue;
        }
    }

    public static long parseAsLong(String input, long defaultValue) {
        if (input == null) {
            return defaultValue;
        }
        input = input.trim();
        int len = input.length();
        if (len == 0) {
            return defaultValue;
        }
        char c;
        int i = 0;
        if (0 < len) {
            c = input.charAt(0);
            if (c == '+') {
                input = input.substring(1);
                len = input.length();
            } else if (c == '-') {
                i = 0 + 1;
            }
        }
        while (i < len) {
            c = input.charAt(i);
            if (c > '9' || c < '0') {
                try {
                    return (long) parseDouble(input);
                } catch (NumberFormatException e) {
                    return defaultValue;
                }
            }
            i++;
        }
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e2) {
            return defaultValue;
        }
    }

    public static double parseAsDouble(String input, double defaultValue) {
        if (input != null) {
            input = input.trim();
            if (input.length() != 0) {
                try {
                    defaultValue = parseDouble(input);
                } catch (NumberFormatException e) {
                }
            }
        }
        return defaultValue;
    }

    public static final double parseDouble(String numStr) throws NumberFormatException {
        if (NASTY_SMALL_DOUBLE.equals(numStr)) {
            return Double.MIN_NORMAL;
        }
        return Double.parseDouble(numStr);
    }
}
