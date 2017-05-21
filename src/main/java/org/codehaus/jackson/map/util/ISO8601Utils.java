package org.codehaus.jackson.map.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class ISO8601Utils {
    private static final String GMT_ID = "GMT";
    private static final TimeZone TIMEZONE_GMT = TimeZone.getTimeZone(GMT_ID);

    public static String format(Date date) {
        return format(date, false, TIMEZONE_GMT);
    }

    public static String format(Date date, boolean millis) {
        return format(date, millis, TIMEZONE_GMT);
    }

    public static String format(Date date, boolean millis, TimeZone tz) {
        int length;
        Calendar calendar = new GregorianCalendar(tz, Locale.US);
        calendar.setTime(date);
        int capacity = "yyyy-MM-ddThh:mm:ss".length() + (millis ? ".sss".length() : 0);
        if (tz.getRawOffset() == 0) {
            length = "Z".length();
        } else {
            length = "+hh:mm".length();
        }
        StringBuilder formatted = new StringBuilder(capacity + length);
        padInt(formatted, calendar.get(1), "yyyy".length());
        formatted.append('-');
        padInt(formatted, calendar.get(2) + 1, "MM".length());
        formatted.append('-');
        padInt(formatted, calendar.get(5), "dd".length());
        formatted.append('T');
        padInt(formatted, calendar.get(11), "hh".length());
        formatted.append(':');
        padInt(formatted, calendar.get(12), "mm".length());
        formatted.append(':');
        padInt(formatted, calendar.get(13), "ss".length());
        if (millis) {
            formatted.append('.');
            padInt(formatted, calendar.get(14), "sss".length());
        }
        int offset = tz.getOffset(calendar.getTimeInMillis());
        if (offset != 0) {
            int hours = Math.abs((offset / 60000) / 60);
            int minutes = Math.abs((offset / 60000) % 60);
            formatted.append(offset < 0 ? '-' : '+');
            padInt(formatted, hours, "hh".length());
            formatted.append(':');
            padInt(formatted, minutes, "mm".length());
        } else {
            formatted.append('Z');
        }
        return formatted.toString();
    }

    public static Date parse(String date) {
        IndexOutOfBoundsException e;
        NumberFormatException e2;
        IllegalArgumentException e3;
        int offset = 0 + 4;
        int i;
        try {
            int year = parseInt(date, 0, offset);
            checkOffset(date, offset, '-');
            i = offset + 1;
            offset = i + 2;
            int month = parseInt(date, i, offset);
            checkOffset(date, offset, '-');
            i = offset + 1;
            offset = i + 2;
            int day = parseInt(date, i, offset);
            checkOffset(date, offset, 'T');
            i = offset + 1;
            offset = i + 2;
            int hour = parseInt(date, i, offset);
            checkOffset(date, offset, ':');
            i = offset + 1;
            offset = i + 2;
            int minutes = parseInt(date, i, offset);
            checkOffset(date, offset, ':');
            i = offset + 1;
            offset = i + 2;
            int seconds = parseInt(date, i, offset);
            int milliseconds = 0;
            if (date.charAt(offset) == '.') {
                checkOffset(date, offset, '.');
                i = offset + 1;
                offset = i + 3;
                milliseconds = parseInt(date, i, offset);
                i = offset;
            } else {
                i = offset;
            }
            try {
                String timezoneId;
                char timezoneIndicator = date.charAt(i);
                if (timezoneIndicator == '+' || timezoneIndicator == '-') {
                    timezoneId = GMT_ID + date.substring(i);
                } else if (timezoneIndicator == 'Z') {
                    timezoneId = GMT_ID;
                } else {
                    throw new IndexOutOfBoundsException("Invalid time zone indicator " + timezoneIndicator);
                }
                TimeZone timezone = TimeZone.getTimeZone(timezoneId);
                if (timezone.getID().equals(timezoneId)) {
                    Calendar calendar = new GregorianCalendar(timezone);
                    calendar.setLenient(false);
                    calendar.set(1, year);
                    calendar.set(2, month - 1);
                    calendar.set(5, day);
                    calendar.set(11, hour);
                    calendar.set(12, minutes);
                    calendar.set(13, seconds);
                    calendar.set(14, milliseconds);
                    return calendar.getTime();
                }
                throw new IndexOutOfBoundsException();
            } catch (IndexOutOfBoundsException e4) {
                e = e4;
                throw new IllegalArgumentException("Failed to parse date " + date, e);
            } catch (NumberFormatException e5) {
                e2 = e5;
                throw new IllegalArgumentException("Failed to parse date " + date, e2);
            } catch (IllegalArgumentException e6) {
                e3 = e6;
                throw new IllegalArgumentException("Failed to parse date " + date, e3);
            }
        } catch (IndexOutOfBoundsException e7) {
            e = e7;
            i = offset;
            throw new IllegalArgumentException("Failed to parse date " + date, e);
        } catch (NumberFormatException e8) {
            e2 = e8;
            i = offset;
            throw new IllegalArgumentException("Failed to parse date " + date, e2);
        } catch (IllegalArgumentException e9) {
            e3 = e9;
            i = offset;
            throw new IllegalArgumentException("Failed to parse date " + date, e3);
        }
    }

    private static void checkOffset(String value, int offset, char expected) throws IndexOutOfBoundsException {
        char found = value.charAt(offset);
        if (found != expected) {
            throw new IndexOutOfBoundsException("Expected '" + expected + "' character but found '" + found + "'");
        }
    }

    private static int parseInt(String value, int beginIndex, int endIndex) throws NumberFormatException {
        if (beginIndex < 0 || endIndex > value.length() || beginIndex > endIndex) {
            throw new NumberFormatException(value);
        }
        int i;
        int digit;
        int i2 = beginIndex;
        int result = 0;
        if (i2 < endIndex) {
            i = i2 + 1;
            digit = Character.digit(value.charAt(i2), 10);
            if (digit < 0) {
                throw new NumberFormatException("Invalid number: " + value);
            }
            result = -digit;
        } else {
            i = i2;
        }
        while (i < endIndex) {
            i2 = i + 1;
            digit = Character.digit(value.charAt(i), 10);
            if (digit < 0) {
                throw new NumberFormatException("Invalid number: " + value);
            }
            result = (result * 10) - digit;
            i = i2;
        }
        return -result;
    }

    private static void padInt(StringBuilder buffer, int value, int length) {
        String strValue = Integer.toString(value);
        for (int i = length - strValue.length(); i > 0; i--) {
            buffer.append('0');
        }
        buffer.append(strValue);
    }
}
