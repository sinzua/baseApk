package com.parse;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.BigTextStyle;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;

class NotificationCompat {
    public static final int FLAG_HIGH_PRIORITY = 128;
    private static final NotificationCompatImpl IMPL;
    public static final int PRIORITY_DEFAULT = 0;

    public static class Builder {
        private static final int MAX_CHARSEQUENCE_LENGTH = 5120;
        PendingIntent mContentIntent;
        CharSequence mContentText;
        CharSequence mContentTitle;
        Context mContext;
        Bitmap mLargeIcon;
        Notification mNotification = new Notification();
        int mPriority;
        Style mStyle;

        public static abstract class Style {
            CharSequence mBigContentTitle;
            Builder mBuilder;
            CharSequence mSummaryText;
            boolean mSummaryTextSet = false;

            public void setBuilder(Builder builder) {
                if (this.mBuilder != builder) {
                    this.mBuilder = builder;
                    if (this.mBuilder != null) {
                        this.mBuilder.setStyle(this);
                    }
                }
            }

            public Notification build() {
                if (this.mBuilder != null) {
                    return this.mBuilder.build();
                }
                return null;
            }
        }

        public static class BigTextStyle extends Style {
            CharSequence mBigText;

            public BigTextStyle(Builder builder) {
                setBuilder(builder);
            }

            public BigTextStyle setBigContentTitle(CharSequence title) {
                this.mBigContentTitle = title;
                return this;
            }

            public BigTextStyle setSummaryText(CharSequence cs) {
                this.mSummaryText = cs;
                this.mSummaryTextSet = true;
                return this;
            }

            public BigTextStyle bigText(CharSequence cs) {
                this.mBigText = cs;
                return this;
            }
        }

        public Builder(Context context) {
            this.mContext = context;
            this.mNotification.when = System.currentTimeMillis();
            this.mNotification.audioStreamType = -1;
            this.mPriority = 0;
        }

        public Builder setWhen(long when) {
            this.mNotification.when = when;
            return this;
        }

        public Builder setSmallIcon(int icon) {
            this.mNotification.icon = icon;
            return this;
        }

        public Builder setSmallIcon(int icon, int level) {
            this.mNotification.icon = icon;
            this.mNotification.iconLevel = level;
            return this;
        }

        public Builder setContentTitle(CharSequence title) {
            this.mContentTitle = limitCharSequenceLength(title);
            return this;
        }

        public Builder setContentText(CharSequence text) {
            this.mContentText = limitCharSequenceLength(text);
            return this;
        }

        public Builder setContentIntent(PendingIntent intent) {
            this.mContentIntent = intent;
            return this;
        }

        public Builder setDeleteIntent(PendingIntent intent) {
            this.mNotification.deleteIntent = intent;
            return this;
        }

        public Builder setTicker(CharSequence tickerText) {
            this.mNotification.tickerText = limitCharSequenceLength(tickerText);
            return this;
        }

        public Builder setLargeIcon(Bitmap icon) {
            this.mLargeIcon = icon;
            return this;
        }

        public Builder setAutoCancel(boolean autoCancel) {
            setFlag(16, autoCancel);
            return this;
        }

        public Builder setDefaults(int defaults) {
            this.mNotification.defaults = defaults;
            if ((defaults & 4) != 0) {
                Notification notification = this.mNotification;
                notification.flags |= 1;
            }
            return this;
        }

        private void setFlag(int mask, boolean value) {
            if (value) {
                Notification notification = this.mNotification;
                notification.flags |= mask;
                return;
            }
            notification = this.mNotification;
            notification.flags &= mask ^ -1;
        }

        public Builder setPriority(int pri) {
            this.mPriority = pri;
            return this;
        }

        public Builder setStyle(Style style) {
            if (this.mStyle != style) {
                this.mStyle = style;
                if (this.mStyle != null) {
                    this.mStyle.setBuilder(this);
                }
            }
            return this;
        }

        @Deprecated
        public Notification getNotification() {
            return NotificationCompat.IMPL.build(this);
        }

        public Notification build() {
            return NotificationCompat.IMPL.build(this);
        }

        protected static CharSequence limitCharSequenceLength(CharSequence cs) {
            if (cs != null && cs.length() > MAX_CHARSEQUENCE_LENGTH) {
                return cs.subSequence(0, MAX_CHARSEQUENCE_LENGTH);
            }
            return cs;
        }
    }

    interface NotificationCompatImpl {
        Notification build(Builder builder);
    }

    static class NotificationCompatImplBase implements NotificationCompatImpl {
        NotificationCompatImplBase() {
        }

        public Notification build(Builder b) {
            Notification result = b.mNotification;
            result.setLatestEventInfo(b.mContext, b.mContentTitle, b.mContentText, b.mContentIntent);
            if (b.mPriority > 0) {
                result.flags |= 128;
            }
            return result;
        }
    }

    @TargetApi(16)
    static class NotificationCompatPostJellyBean implements NotificationCompatImpl {
        private android.app.Notification.Builder postJellyBeanBuilder;

        NotificationCompatPostJellyBean() {
        }

        public Notification build(Builder b) {
            this.postJellyBeanBuilder = new android.app.Notification.Builder(b.mContext);
            this.postJellyBeanBuilder.setContentTitle(b.mContentTitle).setContentText(b.mContentText).setTicker(b.mNotification.tickerText).setSmallIcon(b.mNotification.icon, b.mNotification.iconLevel).setContentIntent(b.mContentIntent).setDeleteIntent(b.mNotification.deleteIntent).setAutoCancel((b.mNotification.flags & 16) != 0).setLargeIcon(b.mLargeIcon).setDefaults(b.mNotification.defaults);
            if (b.mStyle != null && (b.mStyle instanceof BigTextStyle)) {
                BigTextStyle staticStyle = b.mStyle;
                BigTextStyle style = new BigTextStyle(this.postJellyBeanBuilder).setBigContentTitle(staticStyle.mBigContentTitle).bigText(staticStyle.mBigText);
                if (staticStyle.mSummaryTextSet) {
                    style.setSummaryText(staticStyle.mSummaryText);
                }
            }
            return this.postJellyBeanBuilder.build();
        }
    }

    NotificationCompat() {
    }

    static {
        if (VERSION.SDK_INT >= 16) {
            IMPL = new NotificationCompatPostJellyBean();
        } else {
            IMPL = new NotificationCompatImplBase();
        }
    }
}
