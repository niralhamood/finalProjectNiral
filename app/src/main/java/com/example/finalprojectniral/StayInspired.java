package com.example.finalprojectniral;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.app.NotificationCompat;

import java.util.Random;

public class StayInspired extends AppCompatActivity {

    RadioGroup modeRadioGroup;
    RadioButton rbMotivational, rbGuiltDriven;
    Button btnStart;
    TextView tvStatus;

    boolean isRunning = false;

    Handler handler = new Handler();
    Runnable runnable;

    String[] motivationalMessages = {
            "You're doing great 💪",
            "Keep going, don't stop!",
            "Every step matters ✨",
            "Believe in yourself ❤️"
    };

    String[] guiltMessages = {
            "Why are you still not studying?",
            "Others are working harder than you!",
            "Your future depends on this...",
            "Stop wasting time. Start NOW."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay_inspired);

        // ربط العناصر
        modeRadioGroup = findViewById(R.id.modeRadioGroup);
        rbMotivational = findViewById(R.id.rbMotivational);
        rbGuiltDriven = findViewById(R.id.rbGuiltDriven);
        btnStart = findViewById(R.id.btnStart);
        tvStatus = findViewById(R.id.tvStatus);

        createNotificationChannel();

        btnStart.setOnClickListener(v -> {

            if (modeRadioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please choose a mode first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
                    return;
                }
            }

            if (!isRunning) {
                startNotifications();
                tvStatus.setText("Status: Active");
                btnStart.setText("Stop");
                isRunning = true;
            } else {
                stopNotifications();
                tvStatus.setText("Status: Inactive");
                btnStart.setText("Start");
                isRunning = false;
            }
        });
    }

    private void startNotifications() {

        runnable = new Runnable() {
            @Override
            public void run() {

                String message = getRandomMessage();
                sendNotification(message);

                // كل 15 ثانية (غيريها لو بدك)
                handler.postDelayed(this, 15000);
            }
        };

        handler.post(runnable);
    }

    private void stopNotifications() {
        handler.removeCallbacks(runnable);
    }

    private String getRandomMessage() {
        Random random = new Random();

        if (rbMotivational.isChecked()) {
            return motivationalMessages[random.nextInt(motivationalMessages.length)];
        } else {
            return guiltMessages[random.nextInt(guiltMessages.length)];
        }
    }

    @SuppressLint("NotificationPermission")
    private void sendNotification(String message) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Stay Inspired 💡")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(new Random().nextInt(), builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    "channel_id",
                    "Stay Inspired Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}