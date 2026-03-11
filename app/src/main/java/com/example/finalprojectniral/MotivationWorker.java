package com.example.finalprojectniral;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MotivationWorker extends Worker {

    public MotivationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        // جلب المود المختار من SharedPreferences
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        String mode = prefs.getString("mode", "motivational");

        // نص الرسالة حسب المود
        String message;

        if (mode.equals("motivational")) {
            message = "Keep going! You're doing amazing 🌸";
        } else {
            message = "You skipped your goals again... Don't let yourself down. 💭";
        }

        // إرسال الإشعار
        NotificationHelper.showNotification(getApplicationContext(), "Stay Inspired", message);

        // نجاح العملية
        return Result.success();
    }
}