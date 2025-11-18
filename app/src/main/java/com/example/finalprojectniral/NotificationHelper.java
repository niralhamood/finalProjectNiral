package com.example.finalprojectniral;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    private static final String CHANNEL_ID = "motivation_channel";

    @SuppressLint("NotificationPermission")
    public static void showNotification(Context context, String title, String message) {

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // إنشاء قناة الإشعار (ضروري من Android 8.0 وطالع)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Motivation Messages",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription("Delivers periodic motivational notifications");
            manager.createNotificationChannel(channel);
        }

        // لما المستخدم يضغط على الإشعار → يرجع للتطبيق
        Intent intent = new Intent(context, StayInspiredActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        // شكل الإشعار
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground) // حطي أي أيقونة عندك
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        // إرسال الإشعار
        manager.notify(100, builder.build());
    }
}
