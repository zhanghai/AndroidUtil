/*
 * Copyright (c) 2014 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.androidutil.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.Collection;

public class NotificationUtils {

    public static NotificationCompat.Builder makeBaseBuilder(int iconRes, CharSequence title,
                                                             CharSequence text, Context context) {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(iconRes)
                .setContentTitle(title)
                .setContentText(text);
    }

    public static NotificationCompat.Builder makeBaseBuilder(int iconRes, int titleRes,
                                                             CharSequence text, Context context) {
        return makeBaseBuilder(iconRes, context.getString(titleRes), text, context);
    }

    public static NotificationCompat.Builder makeBaseBuilder(int iconRes, int titleRes, int textRes,
                                                             Context context) {
        return makeBaseBuilder(iconRes, context.getString(titleRes), context.getString(textRes),
                context);
    }

    public static NotificationCompat.Builder makeActivityBuilder(int iconRes, CharSequence title,
                                                                 CharSequence text, Intent intent,
                                                                 Context context) {
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(intent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        return makeBaseBuilder(iconRes, title, text, context).setContentIntent(pendingIntent);
    }

    public static NotificationCompat.Builder makeActivityBuilder(int iconRes, CharSequence title,
                                                                 CharSequence text,
                                                                 Class<?> activityClass,
                                                                 Context context) {
        return makeActivityBuilder(iconRes, title, text, new Intent(context, activityClass),
                context);
    }

    public static NotificationCompat.Builder makeActivityBuilder(int iconRes, int titleRes,
                                                                 CharSequence text,
                                                                 Class<?> activityClass,
                                                                 Context context) {
        return makeActivityBuilder(iconRes, context.getString(titleRes), text, activityClass,
                context);
    }

    public static NotificationCompat.Builder makeActivityBuilder(int iconRes, int titleRes,
                                                                 int textRes,
                                                                 Class<?> activityClass,
                                                                 Context context) {
        return makeActivityBuilder(iconRes, context.getString(titleRes), context.getString(textRes),
                activityClass, context);
    }

    public static NotificationCompat.BigTextStyle makeBigTextStyle(CharSequence title,
                                                                   CharSequence text) {
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .setBigContentTitle(title);
        bigTextStyle.bigText(text);
        return bigTextStyle;
    }

    public static NotificationCompat.BigTextStyle makeBigTextStyle(int titleRes, CharSequence text,
                                                                   Context context) {
        return makeBigTextStyle(context.getString(titleRes), text);
    }

    public static NotificationCompat.InboxStyle makeInboxStyle(CharSequence title,
                                                               Collection<? extends CharSequence> lines) {
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                .setBigContentTitle(title);
        for (CharSequence line : lines) {
            inboxStyle.addLine(line);
        }
        return inboxStyle;
    }

    public static NotificationCompat.InboxStyle makeInboxStyle(int titleRes,
                                                               Collection<? extends CharSequence> lines,
                                                               Context context) {
        return makeInboxStyle(context.getString(titleRes), lines);
    }

    public static void show(int id, Notification notification, Context context) {
        NotificationManagerCompat.from(context).notify(id, notification);
    }

    public static void cancel(int id, Context context) {
        NotificationManagerCompat.from(context).cancel(id);
    }


    private NotificationUtils() {}
}
