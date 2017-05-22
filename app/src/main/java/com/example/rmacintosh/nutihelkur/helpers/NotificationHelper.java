package com.example.rmacintosh.nutihelkur.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.example.rmacintosh.nutihelkur.R;

/**
 *
 *                    created by Rauno Pügi
 *
 *       significant help for learning and understanding processes:
 *       - https://github.com/AltBeacon/android-beacon-library-reference
 *       - https://github.com/uriio/beacons-android
 *       - https://github.com/beaconinside/awesome-beacon
 *       - https://github.com/BoydHogerheijde/Beacon-Scanner
 *       - https://github.com/Bridouille/android-beacon-scanner
 *       - https://github.com/justinodwyer/Beacon-Scanner-and-Logger
 *
 *        thanks to Radius Networks for providing a great beacon library,
 *        support and information
 */

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

    public static void sendSoundNotification(Context context, String notificationTitle, String notificationText) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationText)
                        .setSmallIcon(R.mipmap.ic_launcher2)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setDefaults(Notification.DEFAULT_VIBRATE);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}


