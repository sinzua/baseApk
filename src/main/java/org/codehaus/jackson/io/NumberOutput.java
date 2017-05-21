package org.codehaus.jackson.io;

import com.nativex.common.JsonRequestConstants.UDIDs;
import com.supersonicads.sdk.utils.Constants.ControllerParameters;

public final class NumberOutput {
    private static int BILLION = 1000000000;
    static final char[] FULL_TRIPLETS = new char[4000];
    static final byte[] FULL_TRIPLETS_B = new byte[4000];
    static final char[] LEADING_TRIPLETS = new char[4000];
    private static long MAX_INT_AS_LONG = 2147483647L;
    private static int MILLION = 1000000;
    private static long MIN_INT_AS_LONG = -2147483648L;
    private static final char NULL_CHAR = '\u0000';
    static final String SMALLEST_LONG = String.valueOf(Long.MIN_VALUE);
    private static long TEN_BILLION_L = 10000000000L;
    private static long THOUSAND_L = 1000;
    static final String[] sSmallIntStrs = new String[]{"0", "1", "2", UDIDs.ANDROID_DEVICE_ID, UDIDs.ANDROID_ID, "5", "6", "7", "8", "9", "10"};
    static final String[] sSmallIntStrs2 = new String[]{"-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", "-9", "-10"};

    static {
        int ix = 0;
        for (int i1 = 0; i1 < 10; i1++) {
            char l1;
            char f1 = (char) (i1 + 48);
            if (i1 == 0) {
                l1 = '\u0000';
            } else {
                l1 = f1;
            }
            int i2 = 0;
            while (i2 < 10) {
                char l2;
                char f2 = (char) (i2 + 48);
                if (i1 == 0 && i2 == 0) {
                    l2 = '\u0000';
                } else {
                    l2 = f2;
                }
                for (int i3 = 0; i3 < 10; i3++) {
                    char f3 = (char) (i3 + 48);
                    LEADING_TRIPLETS[ix] = l1;
                    LEADING_TRIPLETS[ix + 1] = l2;
                    LEADING_TRIPLETS[ix + 2] = f3;
                    FULL_TRIPLETS[ix] = f1;
                    FULL_TRIPLETS[ix + 1] = f2;
                    FULL_TRIPLETS[ix + 2] = f3;
                    ix += 4;
                }
                i2++;
            }
        }
        for (int i = 0; i < 4000; i++) {
            FULL_TRIPLETS_B[i] = (byte) FULL_TRIPLETS[i];
        }
    }

    public static int outputInt(int value, char[] buffer, int offset) {
        if (value < 0) {
            if (value == Integer.MIN_VALUE) {
                return outputLong((long) value, buffer, offset);
            }
            int offset2 = offset + 1;
            buffer[offset] = '-';
            value = -value;
            offset = offset2;
        }
        if (value < MILLION) {
            if (value >= ControllerParameters.SECOND) {
                int thousands = value / ControllerParameters.SECOND;
                offset = outputFullTriplet(value - (thousands * ControllerParameters.SECOND), buffer, outputLeadingTriplet(thousands, buffer, offset));
            } else if (value < 10) {
                offset2 = offset + 1;
                buffer[offset] = (char) (value + 48);
                offset = offset2;
            } else {
                offset = outputLeadingTriplet(value, buffer, offset);
            }
            return offset;
        }
        boolean hasBillions = value >= BILLION;
        if (hasBillions) {
            value -= BILLION;
            if (value >= BILLION) {
                value -= BILLION;
                offset2 = offset + 1;
                buffer[offset] = '2';
                offset = offset2;
            } else {
                offset2 = offset + 1;
                buffer[offset] = '1';
                offset = offset2;
            }
        }
        int newValue = value / ControllerParameters.SECOND;
        int ones = value - (newValue * ControllerParameters.SECOND);
        value = newValue;
        newValue /= ControllerParameters.SECOND;
        thousands = value - (newValue * ControllerParameters.SECOND);
        if (hasBillions) {
            offset = outputFullTriplet(newValue, buffer, offset);
        } else {
            offset = outputLeadingTriplet(newValue, buffer, offset);
        }
        return outputFullTriplet(ones, buffer, outputFullTriplet(thousands, buffer, offset));
    }

    public static int outputInt(int value, byte[] buffer, int offset) {
        if (value < 0) {
            if (value == Integer.MIN_VALUE) {
                return outputLong((long) value, buffer, offset);
            }
            int offset2 = offset + 1;
            buffer[offset] = (byte) 45;
            value = -value;
            offset = offset2;
        }
        if (value < MILLION) {
            if (value >= ControllerParameters.SECOND) {
                int thousands = value / ControllerParameters.SECOND;
                offset = outputFullTriplet(value - (thousands * ControllerParameters.SECOND), buffer, outputLeadingTriplet(thousands, buffer, offset));
            } else if (value < 10) {
                offset2 = offset + 1;
                buffer[offset] = (byte) (value + 48);
                offset = offset2;
            } else {
                offset = outputLeadingTriplet(value, buffer, offset);
            }
            return offset;
        }
        boolean hasBillions = value >= BILLION;
        if (hasBillions) {
            value -= BILLION;
            if (value >= BILLION) {
                value -= BILLION;
                offset2 = offset + 1;
                buffer[offset] = (byte) 50;
                offset = offset2;
            } else {
                offset2 = offset + 1;
                buffer[offset] = (byte) 49;
                offset = offset2;
            }
        }
        int newValue = value / ControllerParameters.SECOND;
        int ones = value - (newValue * ControllerParameters.SECOND);
        value = newValue;
        newValue /= ControllerParameters.SECOND;
        thousands = value - (newValue * ControllerParameters.SECOND);
        if (hasBillions) {
            offset = outputFullTriplet(newValue, buffer, offset);
        } else {
            offset = outputLeadingTriplet(newValue, buffer, offset);
        }
        return outputFullTriplet(ones, buffer, outputFullTriplet(thousands, buffer, offset));
    }

    public static int outputLong(long value, char[] buffer, int offset) {
        if (value < 0) {
            if (value > MIN_INT_AS_LONG) {
                return outputInt((int) value, buffer, offset);
            }
            if (value == Long.MIN_VALUE) {
                int len = SMALLEST_LONG.length();
                SMALLEST_LONG.getChars(0, len, buffer, offset);
                return offset + len;
            }
            int offset2 = offset + 1;
            buffer[offset] = '-';
            value = -value;
            offset = offset2;
        } else if (value <= MAX_INT_AS_LONG) {
            return outputInt((int) value, buffer, offset);
        }
        int origOffset = offset;
        offset += calcLongStrLength(value);
        int ptr = offset;
        while (value > MAX_INT_AS_LONG) {
            ptr -= 3;
            long newValue = value / THOUSAND_L;
            outputFullTriplet((int) (value - (THOUSAND_L * newValue)), buffer, ptr);
            value = newValue;
        }
        int ivalue = (int) value;
        while (ivalue >= ControllerParameters.SECOND) {
            ptr -= 3;
            int newValue2 = ivalue / ControllerParameters.SECOND;
            outputFullTriplet(ivalue - (newValue2 * ControllerParameters.SECOND), buffer, ptr);
            ivalue = newValue2;
        }
        outputLeadingTriplet(ivalue, buffer, origOffset);
        return offset;
    }

    public static int outputLong(long value, byte[] buffer, int offset) {
        if (value < 0) {
            if (value > MIN_INT_AS_LONG) {
                return outputInt((int) value, buffer, offset);
            }
            int offset2;
            if (value == Long.MIN_VALUE) {
                int len = SMALLEST_LONG.length();
                int i = 0;
                offset2 = offset;
                while (i < len) {
                    offset = offset2 + 1;
                    buffer[offset2] = (byte) SMALLEST_LONG.charAt(i);
                    i++;
                    offset2 = offset;
                }
                offset = offset2;
                return offset2;
            }
            offset2 = offset + 1;
            buffer[offset] = (byte) 45;
            value = -value;
            offset = offset2;
        } else if (value <= MAX_INT_AS_LONG) {
            return outputInt((int) value, buffer, offset);
        }
        int origOffset = offset;
        offset += calcLongStrLength(value);
        int ptr = offset;
        while (value > MAX_INT_AS_LONG) {
            ptr -= 3;
            long newValue = value / THOUSAND_L;
            outputFullTriplet((int) (value - (THOUSAND_L * newValue)), buffer, ptr);
            value = newValue;
        }
        int ivalue = (int) value;
        while (ivalue >= ControllerParameters.SECOND) {
            ptr -= 3;
            int newValue2 = ivalue / ControllerParameters.SECOND;
            outputFullTriplet(ivalue - (newValue2 * ControllerParameters.SECOND), buffer, ptr);
            ivalue = newValue2;
        }
        outputLeadingTriplet(ivalue, buffer, origOffset);
        return offset;
    }

    public static String toString(int value) {
        if (value < sSmallIntStrs.length) {
            if (value >= 0) {
                return sSmallIntStrs[value];
            }
            int v2 = (-value) - 1;
            if (v2 < sSmallIntStrs2.length) {
                return sSmallIntStrs2[v2];
            }
        }
        return Integer.toString(value);
    }

    public static String toString(long value) {
        if (value > 2147483647L || value < -2147483648L) {
            return Long.toString(value);
        }
        return toString((int) value);
    }

    public static String toString(double value) {
        return Double.toString(value);
    }

    private static int outputLeadingTriplet(int triplet, char[] buffer, int offset) {
        int offset2;
        int digitOffset = triplet << 2;
        int digitOffset2 = digitOffset + 1;
        char c = LEADING_TRIPLETS[digitOffset];
        if (c != '\u0000') {
            offset2 = offset + 1;
            buffer[offset] = c;
            offset = offset2;
        }
        digitOffset = digitOffset2 + 1;
        c = LEADING_TRIPLETS[digitOffset2];
        if (c != '\u0000') {
            offset2 = offset + 1;
            buffer[offset] = c;
            offset = offset2;
        }
        offset2 = offset + 1;
        buffer[offset] = LEADING_TRIPLETS[digitOffset];
        return offset2;
    }

    private static int outputLeadingTriplet(int triplet, byte[] buffer, int offset) {
        int offset2;
        int digitOffset = triplet << 2;
        int digitOffset2 = digitOffset + 1;
        char c = LEADING_TRIPLETS[digitOffset];
        if (c != '\u0000') {
            offset2 = offset + 1;
            buffer[offset] = (byte) c;
            offset = offset2;
        }
        digitOffset = digitOffset2 + 1;
        c = LEADING_TRIPLETS[digitOffset2];
        if (c != '\u0000') {
            offset2 = offset + 1;
            buffer[offset] = (byte) c;
            offset = offset2;
        }
        offset2 = offset + 1;
        buffer[offset] = (byte) LEADING_TRIPLETS[digitOffset];
        return offset2;
    }

    private static int outputFullTriplet(int triplet, char[] buffer, int offset) {
        int digitOffset = triplet << 2;
        int i = offset + 1;
        int digitOffset2 = digitOffset + 1;
        buffer[offset] = FULL_TRIPLETS[digitOffset];
        offset = i + 1;
        digitOffset = digitOffset2 + 1;
        buffer[i] = FULL_TRIPLETS[digitOffset2];
        i = offset + 1;
        buffer[offset] = FULL_TRIPLETS[digitOffset];
        return i;
    }

    private static int outputFullTriplet(int triplet, byte[] buffer, int offset) {
        int digitOffset = triplet << 2;
        int i = offset + 1;
        int digitOffset2 = digitOffset + 1;
        buffer[offset] = FULL_TRIPLETS_B[digitOffset];
        offset = i + 1;
        digitOffset = digitOffset2 + 1;
        buffer[i] = FULL_TRIPLETS_B[digitOffset2];
        i = offset + 1;
        buffer[offset] = FULL_TRIPLETS_B[digitOffset];
        return i;
    }

    private static int calcLongStrLength(long posValue) {
        int len = 10;
        for (long comp = TEN_BILLION_L; posValue >= comp && len != 19; comp = (comp << 3) + (comp << 1)) {
            len++;
        }
        return len;
    }
}
