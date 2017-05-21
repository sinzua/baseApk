package com.forwardwin.base.widgets;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.RemoteViews;
import com.ty.instaview.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationUtil {
    public static Notification buildNotification(Context context, String title, String description, PendingIntent contentIntent, PendingIntent deleteIntent, boolean clearable) {
        Builder builder = new Builder(context).setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setDefaults(0).setSound(null).setContentIntent(contentIntent).setDeleteIntent(deleteIntent).setAutoCancel(true);
        if (!clearable) {
            builder.setOngoing(true);
        }
        Notification notification = builder.build();
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_content);
        contentView.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
        contentView.setTextViewText(R.id.title, title);
        contentView.setTextViewText(R.id.time, time);
        contentView.setTextViewText(R.id.description, description);
        notification.contentView = contentView;
        return notification;
    }
}
