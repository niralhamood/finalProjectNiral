package com.example.finalprojectniral;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {

    public NotificationWorker(Context context, WorkerParameters params) {
        super(context, params);
    }

    @SuppressLint("NotificationPermission")
    @Override
    public Result doWork() {

        String channelId = "motivation_channel";
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Motivation",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle("Stay Inspired 🌟")
                .setContentText("A little boost of motivation to brighten your day!")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true);

        manager.notify((int) System.currentTimeMillis(), builder.build());

        return Result.success();
    }
}
