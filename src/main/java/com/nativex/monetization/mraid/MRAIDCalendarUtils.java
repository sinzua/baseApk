package com.nativex.monetization.mraid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.widget.Toast;
import com.nativex.common.Utilities;
import com.nativex.monetization.dialogs.custom.AddCalendarEntryDialog.Calendar;
import com.nativex.monetization.mraid.MRAIDUtils.Features;
import com.nativex.monetization.mraid.objects.CalendarEntryData;
import com.nativex.monetization.mraid.objects.ObjectNames;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@SuppressLint({"InlinedApi"})
class MRAIDCalendarUtils {
    private static final String CALENDAR_ID = "_id";
    private static final String CALENDAR_NAME;

    private static class NewCalendarEntryCreator {
        private final CalendarEntryData data;
        private Date endDate;
        private Date startDate;

        public NewCalendarEntryCreator(CalendarEntryData data) {
            this.data = data;
        }

        @SuppressLint({"NewApi"})
        public void add(Context context, Calendar calendar) throws Exception {
            this.startDate = Utilities.parseHtmlDate(this.data.getStart());
            this.endDate = Utilities.parseHtmlDate(this.data.getEnd());
            if (this.startDate == null) {
                throw new Exception("Invalid start time.");
            } else if (this.endDate == null) {
                throw new Exception("Invalid end time.");
            } else {
                String timeZone;
                MRAIDCalendarUtils.validateCalendarData(this.data);
                ContentResolver cr = context.getContentResolver();
                ContentValues values = new ContentValues();
                values.put("dtstart", Long.valueOf(this.startDate.getTime()));
                values.put("dtend", Long.valueOf(this.endDate.getTime()));
                values.put("title", this.data.getDescription());
                values.put("description", this.data.getSummary());
                values.put("eventLocation", this.data.getLocation());
                values.put("availability", Integer.valueOf(parseTransparency()));
                String[] timeZoneIds = TimeZone.getAvailableIDs(this.startDate.getTimezoneOffset());
                if (timeZoneIds == null || timeZoneIds.length <= 0) {
                    timeZone = TimeZone.getDefault().getDisplayName();
                } else {
                    timeZone = TimeZone.getTimeZone(timeZoneIds[0]).getDisplayName();
                }
                MRAIDLogger.d("Time Zone " + timeZone);
                values.put("eventTimezone", timeZone);
                values.put("calendar_id", Integer.valueOf(calendar.getId()));
                int status = parseStatus();
                if (status >= 0) {
                    values.put("eventStatus", Integer.valueOf(status));
                }
                long eventId = Long.parseLong(cr.insert(Events.CONTENT_URI, values).getLastPathSegment());
                if (!Utilities.stringIsEmpty(this.data.getReminder())) {
                    addReminder(context, eventId, this.data.getReminder());
                }
            }
        }

        private int parseStatus() {
            String status = this.data.getStatus();
            if (Utilities.stringIsEmpty(status)) {
                return 0;
            }
            int i = -1;
            switch (status.hashCode()) {
                case -1320822226:
                    if (status.equals("tentative")) {
                        i = 0;
                        break;
                    }
                    break;
                case -804109473:
                    if (status.equals("confirmed")) {
                        i = 1;
                        break;
                    }
                    break;
                case 476588369:
                    if (status.equals("cancelled")) {
                        i = 2;
                        break;
                    }
                    break;
            }
            switch (i) {
                case 1:
                    return 1;
                case 2:
                    return 2;
                default:
                    return 0;
            }
        }

        private int parseTransparency() {
            String transparency = this.data.getTransparency();
            if (Utilities.stringIsEmpty(transparency) || !transparency.equals(ParametersKeys.TRANSPARENT)) {
                return 0;
            }
            return 1;
        }

        @SuppressLint({"NewApi"})
        private void addReminder(Context context, long eventId, String reminder) {
            long minutes;
            if (reminder.startsWith("-")) {
                minutes = Utilities.millisecondsToMinutes(Math.abs(Long.parseLong(reminder)));
            } else {
                Date reminderDate = Utilities.parseHtmlDate(reminder);
                if (reminderDate != null) {
                    long difference = this.startDate.getTime() - reminderDate.getTime();
                    if (difference >= 0) {
                        minutes = Utilities.millisecondsToMinutes(Math.abs(difference));
                    } else {
                        return;
                    }
                }
                return;
            }
            ContentResolver cr = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put("minutes", Integer.valueOf((int) minutes));
            values.put("event_id", Long.valueOf(eventId));
            values.put(ParametersKeys.METHOD, Integer.valueOf(1));
            cr.insert(Reminders.CONTENT_URI, values);
        }
    }

    private static class OldCalendarEntryCreator {
        private final CalendarEntryData data;
        private Date endDate;
        private Date startDate;
        private boolean test = false;
        private Uri uri;

        public OldCalendarEntryCreator(CalendarEntryData data) {
            this.data = data;
        }

        public OldCalendarEntryCreator(CalendarEntryData data, boolean test) {
            this.data = data;
            this.test = test;
        }

        private void add(Activity context, Calendar calendar) throws Exception {
            this.startDate = Utilities.parseHtmlDate(this.data.getStart());
            this.endDate = Utilities.parseHtmlDate(this.data.getEnd());
            if (this.startDate == null) {
                throw new Exception("Invalid start time.");
            } else if (this.endDate == null) {
                throw new Exception("Invalid end time.");
            } else {
                MRAIDCalendarUtils.validateCalendarData(this.data);
                if (calendar == null || !(this.test || Features.CALENDAR.getSupportLevel() == 1)) {
                    addUsingIntent(context);
                } else {
                    addUsingProvider(context, calendar);
                }
            }
        }

        private void addUsingProvider(Activity context, Calendar calendar) {
            int i = 0;
            boolean hasReminder = false;
            if (!Utilities.stringIsEmpty(this.data.getReminder())) {
                hasReminder = true;
            }
            ContentValues event = new ContentValues();
            event.put("calendar_id", Integer.valueOf(calendar.getId()));
            event.put("title", this.data.getDescription());
            event.put("description", this.data.getSummary());
            event.put("eventLocation", this.data.getLocation());
            long startTime = this.startDate.getTime();
            long endTime = this.endDate.getTime();
            event.put("dtstart", Long.valueOf(startTime));
            event.put("dtend", Long.valueOf(endTime));
            event.put("allDay", Integer.valueOf(0));
            event.put("eventStatus", Integer.valueOf(parseStatus()));
            event.put("visibility", Integer.valueOf(0));
            event.put(ObjectNames.CalendarEntryData.TRANSPARENCY, Integer.valueOf(parseTransparency()));
            String str = "hasAlarm";
            if (hasReminder) {
                i = 1;
            }
            event.put(str, Integer.valueOf(i));
            this.uri = context.getContentResolver().insert(Uri.parse(getCalendarUriBase(context) + EventEntry.TABLE_NAME), event);
            if (hasReminder) {
                addReminder(context, Long.parseLong(this.uri.getLastPathSegment()), this.data.getReminder());
            }
        }

        private void addReminder(Activity context, long eventId, String reminder) {
            long minutes;
            if (reminder.startsWith("-")) {
                minutes = Utilities.millisecondsToMinutes(Math.abs(Long.parseLong(reminder)));
            } else {
                Date reminderDate = Utilities.parseHtmlDate(reminder);
                if (reminderDate != null) {
                    long difference = this.startDate.getTime() - reminderDate.getTime();
                    if (difference >= 0) {
                        minutes = Utilities.millisecondsToMinutes(Math.abs(difference));
                    } else {
                        return;
                    }
                }
                return;
            }
            Uri REMINDERS_URI = Uri.parse(getCalendarUriBase(context) + "reminders");
            ContentValues values = new ContentValues();
            values.put("event_id", Long.valueOf(eventId));
            values.put(ParametersKeys.METHOD, Integer.valueOf(1));
            values.put("minutes", Integer.valueOf((int) minutes));
            context.getContentResolver().insert(REMINDERS_URI, values);
        }

        public void delete(Activity activity) {
            if (this.uri != null) {
                activity.getContentResolver().delete(this.uri, null, null);
            }
        }

        private int parseStatus() {
            String status = this.data.getStatus();
            if (Utilities.stringIsEmpty(status)) {
                return 0;
            }
            int i = -1;
            switch (status.hashCode()) {
                case -1320822226:
                    if (status.equals("tentative")) {
                        i = 0;
                        break;
                    }
                    break;
                case -804109473:
                    if (status.equals("confirmed")) {
                        i = 1;
                        break;
                    }
                    break;
                case 476588369:
                    if (status.equals("cancelled")) {
                        i = 2;
                        break;
                    }
                    break;
            }
            switch (i) {
                case 1:
                    return 1;
                case 2:
                    return 2;
                default:
                    return 0;
            }
        }

        private int parseTransparency() {
            String transparency = this.data.getTransparency();
            if (Utilities.stringIsEmpty(transparency) || !transparency.equals(ParametersKeys.TRANSPARENT)) {
                return 0;
            }
            return 1;
        }

        private String getCalendarUriBase(Activity activity) {
            Cursor managedCursor = null;
            try {
                managedCursor = activity.managedQuery(Uri.parse("content://calendar/calendars"), null, null, null, null);
            } catch (Exception e) {
            }
            if (managedCursor != null) {
                return "content://calendar/";
            }
            try {
                managedCursor = activity.managedQuery(Uri.parse("content://com.android.calendar/calendars"), null, null, null, null);
            } catch (Exception e2) {
            }
            if (managedCursor != null) {
                return "content://com.android.calendar/";
            }
            return null;
        }

        private void addUsingIntent(Activity context) {
            Intent intent = new Intent("android.intent.action.EDIT");
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", this.startDate.getTime());
            intent.putExtra("endTime", this.endDate.getTime());
            intent.putExtra("title", this.data.getDescription());
            context.startActivity(intent);
        }
    }

    static {
        if (VERSION.SDK_INT < 14) {
            CALENDAR_NAME = "name";
        } else {
            CALENDAR_NAME = "calendar_displayName";
        }
    }

    private MRAIDCalendarUtils() {
    }

    public static void createCalendarEntry(Activity context, Calendar calendar, CalendarEntryData data) throws Exception {
        if (VERSION.SDK_INT < 14 || calendar == null) {
            new OldCalendarEntryCreator(data).add(context, calendar);
        } else {
            new NewCalendarEntryCreator(data).add(context, calendar);
        }
        Toast.makeText(context, "Calendar entry added", 0).show();
    }

    public static boolean deviceSupportsCalendarProvider(Activity context) {
        try {
            Calendar calendar = new Calendar("Test", 0);
            CalendarEntryData data = new CalendarEntryData();
            data.setDescription("Test");
            data.setStart("1950-06-20T20:50+0200");
            data.setEnd("1950-06-20T20:55+0200");
            data.setReminder("-60000");
            data.setId("0");
            data.setLocation("Test");
            data.setStatus("tentative");
            data.setSummary("Test");
            data.setTransparency("opaque");
            OldCalendarEntryCreator entry = new OldCalendarEntryCreator(data, true);
            entry.add(context, calendar);
            entry.delete(context);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<Calendar> queryCalendarsList(Context context) {
        String[] projection = new String[]{CALENDAR_ID, CALENDAR_NAME};
        Uri calendarUri = Uri.parse("content://com.android.calendar/calendars");
        List<Calendar> calendars = new ArrayList();
        Cursor managedCursor = context.getContentResolver().query(calendarUri, projection, null, null, null);
        if (managedCursor.moveToFirst()) {
            int idCol = managedCursor.getColumnIndex(projection[0]);
            int nameCol = managedCursor.getColumnIndex(projection[1]);
            do {
                try {
                    calendars.add(new Calendar(managedCursor.getString(nameCol), Integer.parseInt(managedCursor.getString(idCol))));
                } catch (Exception e) {
                }
            } while (managedCursor.moveToNext());
            managedCursor.close();
        }
        return calendars;
    }

    private static void validateCalendarData(CalendarEntryData data) {
        if (Utilities.stringIsEmpty(data.getDescription())) {
            data.setDescription("<No Data>");
        }
        if (Utilities.stringIsEmpty(data.getLocation())) {
            data.setLocation("<No Data>");
        }
    }
}
