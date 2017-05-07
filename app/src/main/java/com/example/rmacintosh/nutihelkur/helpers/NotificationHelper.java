package com.example.rmacintosh.nutihelkur.helpers;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.example.rmacintosh.nutihelkur.R;


public class NotificationHelper {

    public static void sendNotification(Context context, String notificationTitle, String notificationText) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationText)
                        .setSmallIcon(R.mipmap.ic_launcher2);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

    }

}
