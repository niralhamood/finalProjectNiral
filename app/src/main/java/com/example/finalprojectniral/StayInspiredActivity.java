package com.example.finalprojectniral;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class StayInspiredActivity extends AppCompatActivity {

    RadioGroup modeRadioGroup;
    RadioButton rbMotivational, rbGuiltDriven;
    Button btnStart;
    TextView tvStatus;

    SharedPreferences preferences;

    boolean isActive = false;  // حالة التفعيل

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay_inspired);

        modeRadioGroup = findViewById(R.id.modeRadioGroup);
        rbMotivational = findViewById(R.id.rbMotivational);
        rbGuiltDriven = findViewById(R.id.rbGuiltDriven);
        btnStart = findViewById(R.id.btnStart);
        tvStatus = findViewById(R.id.tvStatus);

        preferences = getSharedPreferences("settings", MODE_PRIVATE);

        // إذا سبق وشغل المستخدم المود
        isActive = preferences.getBoolean("active", false);
        updateStatusUI();

        btnStart.setOnClickListener(v -> {

            // يجب اختيار مود
            if (modeRadioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please choose a message mode first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isActive) {
                startMotivationWorker();
            } else {
                stopMotivationWorker();
            }

            isActive = !isActive;
            preferences.edit().putBoolean("active", isActive).apply();
            updateStatusUI();
        });
    }

    private void startMotivationWorker() {

        // حفظ المود المختار في SharedPreferences
        String mode;

        if (rbMotivational.isChecked()) {
            mode = "motivational";
        } else {
            mode = "guilt";
        }

        preferences.edit().putString("mode", mode).apply();

        // WorkManager: كل 6 ساعات (يمكن تغييره)
        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(MotivationWorker.class, 6, TimeUnit.HOURS)
                        .build();

        WorkManager.getInstance(this).enqueue(request);

        Toast.makeText(this, "Notifications Activated", Toast.LENGTH_SHORT).show();
    }

    private void stopMotivationWorker() {
        WorkManager.getInstance(this).cancelAllWork();
        Toast.makeText(this, "Notifications Stopped", Toast.LENGTH_SHORT).show();
    }

    private void updateStatusUI() {
        if (isActive) {
            tvStatus.setText("Status: Active");
            btnStart.setText("Stop");
        } else {
            tvStatus.setText("Status: Inactive");
            btnStart.setText("Start");
        }
    }
}
