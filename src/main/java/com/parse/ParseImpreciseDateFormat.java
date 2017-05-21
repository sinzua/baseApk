package com.parse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

class ParseImpreciseDateFormat {
    private static final ParseImpreciseDateFormat INSTANCE = new ParseImpreciseDateFormat();
    private static final String TAG = "ParseDateFormat";
    private final DateFormat dateFormat;
    private final Object lock = new Object();

    public static ParseImpreciseDateFormat getInstance() {
        return INSTANCE;
    }

    private ParseImpreciseDateFormat() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        format.setTimeZone(new SimpleTimeZone(0, "GMT"));
        this.dateFormat = format;
    }

    Date parse(String dateString) {
        Date parse;
        synchronized (this.lock) {
            try {
                parse = this.dateFormat.parse(dateString);
            } catch (ParseException e) {
                PLog.e(TAG, "could not parse date: " + dateString, e);
                parse = null;
            }
        }
        return parse;
    }

    String format(Date date) {
        String format;
        synchronized (this.lock) {
            format = this.dateFormat.format(date);
        }
        return format;
    }
}
