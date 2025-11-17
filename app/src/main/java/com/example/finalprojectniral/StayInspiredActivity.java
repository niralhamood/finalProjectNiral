package com.example.finalprojectniral;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;

import java.util.concurrent.TimeUnit;

public class StayInspiredActivity extends AppCompatActivity {

    LinearLayout optionFocus, optionPositive, optionStress;
    Button btnStart;
    TextView tvStatus;

    String selectedMode = "";
    boolean isActive = false;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay_inspired);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        optionFocus = findViewById(R.id.boost_focus_layout);
        optionPositive = findViewById(R.id.stay_positive_layout);
        optionStress = findViewById(R.id.reduce_stress_layout);
        btnStart = findViewById(R.id.btnStart);
        tvStatus = findViewById(R.id.tvStatus);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // تحميل الوضع القديم
        loadSavedData();

        optionFocus.setOnClickListener(v -> selectMode("Boost Focus"));
        optionPositive.setOnClickListener(v -> selectMode("Stay Positive"));
        optionStress.setOnClickListener(v -> selectMode("Reduce Stress"));

        btnStart.setOnClickListener(v -> toggleActivation());
    }

    private void selectMode(String mode) {
        selectedMode = mode;
        resetBackgrounds();

        if (mode.equals("Boost Focus")) optionFocus.setBackgroundResource(R.drawable.rounded_button_selected);
        if (mode.equals("Stay Positive")) optionPositive.setBackgroundResource(R.drawable.rounded_button_selected);
        if (mode.equals("Reduce Stress")) optionStress.setBackgroundResource(R.drawable.rounded_button_selected);

        saveData();
    }

    private void resetBackgrounds() {
        optionFocus.setBackgroundResource(R.drawable.rounded_button_bg);
        optionPositive.setBackgroundResource(R.drawable.rounded_button_bg);
        optionStress.setBackgroundResource(R.drawable.rounded_button_bg);
    }

    private void toggleActivation() {

        if (selectedMode.isEmpty()) {
            tvStatus.setText("Please select a mode first");
            return;
        }

        isActive = !isActive;

        if (isActive) {
            tvStatus.setText("Status: Active (" + selectedMode + ")");
            btnStart.setText("Stop");
            btnStart.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_red_light));

            // تشغيل الإشعارات كل 30 دقيقة
            PeriodicWorkRequest request =
                    new PeriodicWorkRequest.Builder(NotificationWorker.class, 30, TimeUnit.MINUTES)
                            .build();
            WorkManager.getInstance(this).enqueue(request);

        } else {
            tvStatus.setText("Status: Inactive");
            btnStart.setText("Start");
            btnStart.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.pink_300));

            WorkManager.getInstance(this).cancelAllWork();
        }

        saveData();
    }

    private void saveData() {
        prefs.edit()
                .putString("mode", selectedMode)
                .putBoolean("active", isActive)
                .apply();
    }

    private void loadSavedData() {
        selectedMode = prefs.getString("mode", "");
        isActive = prefs.getBoolean("active", false);

        if (!selectedMode.isEmpty()) selectMode(selectedMode);

        if (isActive) {
            tvStatus.setText("Status: Active (" + selectedMode + ")");
            btnStart.setText("Stop");
            btnStart.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_red_light));
        }
    }
}
